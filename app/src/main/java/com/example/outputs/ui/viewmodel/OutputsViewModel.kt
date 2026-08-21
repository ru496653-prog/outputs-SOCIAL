package com.example.outputs.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.outputs.data.local.OutputsDatabase
import com.example.outputs.data.model.AnonymousGroup
import com.example.outputs.data.model.ChatMessage
import com.example.outputs.data.model.EvidenceItem
import com.example.outputs.data.model.EvidenceType
import com.example.outputs.data.model.GenreTheme
import com.example.outputs.data.model.MysteryStatus
import com.example.outputs.data.model.PostComment
import com.example.outputs.data.model.PostItem
import com.example.outputs.data.model.PostType
import com.example.outputs.data.model.PrivacyMode
import com.example.outputs.data.model.Theory
import com.example.outputs.data.model.TimelineEvent
import com.example.outputs.data.model.UserSession
import com.example.outputs.data.repository.OutputsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class FeedTab(val label: String, val icon: String) {
    FOR_YOU("For You", "✨"),
    MYSTERIES("Mysteries", "🔍"),
    REAL_INCIDENTS("Real Incidents", "⚠️"),
    VOICE("Voice", "🎙️"),
    STORIES("Stories", "📖"),
    OPINIONS("Opinions", "💭"),
    QUESTIONS("Questions", "❓"),
    FOLLOWING("Following", "📌")
}

enum class FeedSortOrder(val label: String, val icon: String) {
    NEWEST("Newest", "⏱️"),
    MOST_SUPPORTED("Most Supported", "🔥"),
    MOST_DISCUSSED("Most Discussed", "💬")
}

data class ReadingModeState(
    val post: PostItem? = null,
    val fontSizeSp: Float = 18f,
    val lineSpacingMultiplier: Float = 1.4f,
    val themeMode: String = "Dark", // "Dark", "Archive", "Minimal", "Cyber"
    val isAudioPlaying: Boolean = false,
    val audioProgressPercent: Float = 0f,
    val audioSpeed: Float = 1.0f
)

class OutputsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OutputsRepository

    init {
        val db = OutputsDatabase.getDatabase(application, viewModelScope)
        repository = OutputsRepository(db.outputsDao())
    }

    val userSession: StateFlow<UserSession> = repository.userSession.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserSession()
    )

    private val _selectedFeedTab = MutableStateFlow(FeedTab.FOR_YOU)
    val selectedFeedTab = _selectedFeedTab.asStateFlow()

    private val _selectedGenreFilter = MutableStateFlow<GenreTheme?>(null)
    val selectedGenreFilter = _selectedGenreFilter.asStateFlow()

    private val _selectedSortOrder = MutableStateFlow(FeedSortOrder.NEWEST)
    val selectedSortOrder = _selectedSortOrder.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val rawPosts: StateFlow<List<PostItem>> = repository.allPosts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val feedPosts: StateFlow<List<PostItem>> = combine(
        rawPosts,
        _selectedFeedTab,
        _selectedGenreFilter,
        _selectedSortOrder
    ) { posts, tab, genre, sortOrder ->
        var filtered = when (tab) {
            FeedTab.FOR_YOU -> posts
            FeedTab.MYSTERIES -> posts.filter { it.type == PostType.MYSTERY || it.type == PostType.INVESTIGATION }
            FeedTab.REAL_INCIDENTS -> posts.filter { it.type == PostType.REAL_INCIDENT }
            FeedTab.VOICE -> posts.filter { it.voiceAudioDurationSeconds > 0 }
            FeedTab.STORIES -> posts.filter { it.type == PostType.STORY || it.type == PostType.EXPERIENCE }
            FeedTab.OPINIONS -> posts.filter { it.type == PostType.OPINION }
            FeedTab.QUESTIONS -> posts.filter { it.type == PostType.QUESTION }
            FeedTab.FOLLOWING -> posts.filter { it.verifiedBadge || it.isLiked || it.isSaved }
        }
        if (genre != null) {
            filtered = filtered.filter { it.genreTheme == genre }
        }
        when (sortOrder) {
            FeedSortOrder.NEWEST -> filtered.sortedByDescending { it.id }
            FeedSortOrder.MOST_SUPPORTED -> filtered.sortedByDescending { (it.likesCount * 3) + it.savesCount }
            FeedSortOrder.MOST_DISCUSSED -> filtered.sortedByDescending { it.commentsCount + it.theoriesCount + it.evidenceCount }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val savedPosts: StateFlow<List<PostItem>> = repository.savedPosts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val savedComments: StateFlow<List<PostComment>> = repository.savedComments.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val archivedPosts: StateFlow<List<PostItem>> = repository.archivedPosts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val groups: StateFlow<List<AnonymousGroup>> = repository.allGroups.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Active detail & case states
    private val _selectedPostId = MutableStateFlow<String?>("post_1")
    val selectedPostId = _selectedPostId.asStateFlow()

    private val _readingModeState = MutableStateFlow(ReadingModeState())
    val readingModeState = _readingModeState.asStateFlow()

    // Selected post flows
    private val _activeComments = MutableStateFlow<List<PostComment>>(emptyList())
    val activeComments = _activeComments.asStateFlow()

    private val _activeEvidence = MutableStateFlow<List<EvidenceItem>>(emptyList())
    val activeEvidence = _activeEvidence.asStateFlow()

    private val _activeTheories = MutableStateFlow<List<Theory>>(emptyList())
    val activeTheories = _activeTheories.asStateFlow()

    private val _activeTimeline = MutableStateFlow<List<TimelineEvent>>(emptyList())
    val activeTimeline = _activeTimeline.asStateFlow()

    // Messaging flows
    private val _activeConversationId = MutableStateFlow("conv_ridge")
    val activeConversationId = _activeConversationId.asStateFlow()

    val currentConversationMessages: StateFlow<List<ChatMessage>> = _activeConversationId.combine(rawPosts) { convId, _ ->
        convId
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "conv_ridge").let {
        repository.getMessages("conv_ridge").stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    val messageRequests: StateFlow<List<ChatMessage>> = repository.getMessageRequests().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        selectPost("post_1")
    }

    fun selectFeedTab(tab: FeedTab) {
        _selectedFeedTab.value = tab
    }

    fun selectSortOrder(order: FeedSortOrder) {
        _selectedSortOrder.value = order
    }

    fun setGenreFilter(genre: GenreTheme?) {
        _selectedGenreFilter.value = genre
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectPost(postId: String) {
        _selectedPostId.value = postId
        viewModelScope.launch {
            repository.getCommentsForPost(postId).collect { _activeComments.value = it }
        }
        viewModelScope.launch {
            repository.getEvidenceForPost(postId).collect { _activeEvidence.value = it }
        }
        viewModelScope.launch {
            repository.getTheoriesForPost(postId).collect { _activeTheories.value = it }
        }
        viewModelScope.launch {
            repository.getTimelineForPost(postId).collect { _activeTimeline.value = it }
        }
    }

    fun openReadingMode(post: PostItem) {
        _readingModeState.value = _readingModeState.value.copy(
            post = post,
            isAudioPlaying = false,
            audioProgressPercent = 0f
        )
    }

    fun closeReadingMode() {
        _readingModeState.value = _readingModeState.value.copy(post = null)
    }

    fun updateReadingFontSize(delta: Float) {
        val newSize = (_readingModeState.value.fontSizeSp + delta).coerceIn(14f, 28f)
        _readingModeState.value = _readingModeState.value.copy(fontSizeSp = newSize)
    }

    fun updateReadingTheme(theme: String) {
        _readingModeState.value = _readingModeState.value.copy(themeMode = theme)
    }

    fun toggleAudioPlayback() {
        val current = _readingModeState.value.isAudioPlaying
        _readingModeState.value = _readingModeState.value.copy(isAudioPlaying = !current)
    }

    fun cycleAudioSpeed() {
        val current = _readingModeState.value.audioSpeed
        val next = when (current) {
            1.0f -> 1.25f
            1.25f -> 1.5f
            1.5f -> 2.0f
            else -> 1.0f
        }
        _readingModeState.value = _readingModeState.value.copy(audioSpeed = next)
    }

    fun updateReadingProgress(postId: String, progress: Int) {
        viewModelScope.launch {
            repository.updateReadingProgress(postId, progress)
        }
    }

    fun toggleLike(post: PostItem) {
        viewModelScope.launch {
            repository.toggleLike(post.id, post.isLiked)
        }
    }

    fun toggleSave(post: PostItem) {
        viewModelScope.launch {
            repository.toggleSave(post.id, post.isSaved)
        }
    }

    fun toggleArchive(post: PostItem) {
        viewModelScope.launch {
            repository.toggleArchive(post.id, post.isArchived)
        }
    }

    fun updateMysteryStatus(postId: String, status: MysteryStatus) {
        viewModelScope.launch {
            repository.updateMysteryStatus(postId, status)
        }
    }

    // Comments & Replies
    fun addComment(
        postId: String,
        text: String,
        isTheory: Boolean = false,
        isEvidence: Boolean = false,
        identityMode: PrivacyMode? = null
    ) {
        if (text.isBlank()) return
        val currentSession = userSession.value
        val mode = identityMode ?: currentSession.defaultPrivacyMode
        val authorName = when (mode) {
            PrivacyMode.REAL_PROFILE -> currentSession.publicUsername
            PrivacyMode.PSEUDONYM -> currentSession.pseudonym
            PrivacyMode.ANONYMOUS_USERNAME -> "AnonymousInvestigator"
            PrivacyMode.ANONYMOUS_NUMBER -> "Anonymous ${currentSession.anonymousNumberCode}"
            PrivacyMode.COMPLETELY_ANONYMOUS -> "Anonymous"
        }
        val newComment = PostComment(
            id = "c_${UUID.randomUUID()}",
            postId = postId,
            author = authorName,
            identityMode = mode,
            text = text.trim(),
            timestamp = "Just now",
            likes = 0,
            isLiked = false,
            isAcceptedAnswer = false,
            isTheory = isTheory,
            isEvidenceSubmitted = isEvidence,
            audioDurationSeconds = 0,
            replyCount = 0,
            parentCommentId = null,
            replyToAuthor = null,
            isSaved = false,
            isEdited = false
        )
        viewModelScope.launch {
            repository.addComment(newComment)
        }
    }

    fun replyToComment(
        parentCommentId: String,
        postId: String,
        replyToAuthor: String,
        text: String,
        identityMode: PrivacyMode? = null
    ) {
        if (text.isBlank()) return
        val currentSession = userSession.value
        val mode = identityMode ?: currentSession.defaultPrivacyMode
        val authorName = when (mode) {
            PrivacyMode.REAL_PROFILE -> currentSession.publicUsername
            PrivacyMode.PSEUDONYM -> currentSession.pseudonym
            PrivacyMode.ANONYMOUS_USERNAME -> "AnonymousInvestigator"
            PrivacyMode.ANONYMOUS_NUMBER -> "Anonymous ${currentSession.anonymousNumberCode}"
            PrivacyMode.COMPLETELY_ANONYMOUS -> "Anonymous"
        }
        val replyComment = PostComment(
            id = "c_${UUID.randomUUID()}",
            postId = postId,
            author = authorName,
            identityMode = mode,
            text = text.trim(),
            timestamp = "Just now",
            likes = 0,
            isLiked = false,
            isAcceptedAnswer = false,
            isTheory = false,
            isEvidenceSubmitted = false,
            audioDurationSeconds = 0,
            replyCount = 0,
            parentCommentId = parentCommentId,
            replyToAuthor = replyToAuthor,
            isSaved = false,
            isEdited = false
        )
        viewModelScope.launch {
            repository.addComment(replyComment)
        }
    }

    fun editComment(commentId: String, newText: String) {
        if (newText.isBlank()) return
        viewModelScope.launch {
            repository.editComment(commentId, newText.trim())
        }
    }

    fun toggleLikeComment(commentId: String, currentLiked: Boolean) {
        viewModelScope.launch {
            repository.toggleCommentLike(commentId, currentLiked)
        }
    }

    fun toggleSaveComment(commentId: String, currentSaved: Boolean) {
        viewModelScope.launch {
            repository.toggleCommentSave(commentId, currentSaved)
        }
    }

    fun likeComment(commentId: String) {
        viewModelScope.launch {
            repository.toggleCommentLike(commentId, false)
        }
    }

    fun markAcceptedAnswer(commentId: String) {
        viewModelScope.launch {
            repository.markAcceptedAnswer(commentId)
        }
    }

    // Evidence
    fun submitEvidence(
        postId: String,
        title: String,
        description: String,
        type: EvidenceType,
        confidenceLevel: Int
    ) {
        if (title.isBlank()) return
        val session = userSession.value
        val contributor = when (session.defaultPrivacyMode) {
            PrivacyMode.REAL_PROFILE -> session.publicUsername
            PrivacyMode.PSEUDONYM -> session.pseudonym
            PrivacyMode.ANONYMOUS_USERNAME -> "FieldAgent"
            PrivacyMode.ANONYMOUS_NUMBER -> "Anonymous ${session.anonymousNumberCode}"
            PrivacyMode.COMPLETELY_ANONYMOUS -> "Anonymous"
        }
        val evidence = EvidenceItem(
            id = "ev_${UUID.randomUUID()}",
            title = title,
            description = description,
            type = type,
            mediaResName = "archive_dossier_art",
            contributor = contributor,
            contributorIdentity = session.defaultPrivacyMode,
            dateAdded = "Today",
            confidenceLevel = confidenceLevel,
            upvotes = 1,
            verifiedByCreator = false
        )
        viewModelScope.launch {
            repository.addEvidence(evidence, postId)
        }
    }

    fun upvoteEvidence(evidenceId: String) {
        viewModelScope.launch {
            repository.upvoteEvidence(evidenceId)
        }
    }

    // Theories
    fun proposeTheory(postId: String, title: String, content: String) {
        if (title.isBlank() || content.isBlank()) return
        val session = userSession.value
        val author = when (session.defaultPrivacyMode) {
            PrivacyMode.REAL_PROFILE -> session.publicUsername
            PrivacyMode.PSEUDONYM -> session.pseudonym
            PrivacyMode.ANONYMOUS_USERNAME -> "TheoristAnon"
            PrivacyMode.ANONYMOUS_NUMBER -> "Anonymous ${session.anonymousNumberCode}"
            PrivacyMode.COMPLETELY_ANONYMOUS -> "Anonymous"
        }
        val theory = Theory(
            id = "th_${UUID.randomUUID()}",
            author = author,
            authorIdentity = session.defaultPrivacyMode,
            title = title,
            content = content,
            supportCount = 1,
            challengeCount = 0,
            isAccepted = false,
            isDebunked = false,
            evidenceIds = emptyList(),
            timestamp = "Just now"
        )
        viewModelScope.launch {
            repository.addTheory(theory, postId)
        }
    }

    fun supportTheory(theoryId: String) {
        viewModelScope.launch {
            repository.supportTheory(theoryId)
        }
    }

    fun challengeTheory(theoryId: String) {
        viewModelScope.launch {
            repository.challengeTheory(theoryId)
        }
    }

    fun acceptTheory(theoryId: String, postId: String) {
        viewModelScope.launch {
            repository.acceptTheory(theoryId)
            repository.updateMysteryStatus(postId, MysteryStatus.SOLVED)
        }
    }

    fun debunkTheory(theoryId: String) {
        viewModelScope.launch {
            repository.debunkTheory(theoryId)
        }
    }

    // Timeline Events
    fun addTimelineEvent(postId: String, timeLabel: String, title: String, description: String) {
        if (title.isBlank() || timeLabel.isBlank()) return
        val event = TimelineEvent(
            id = "tl_${UUID.randomUUID()}",
            timeLabel = timeLabel,
            title = title,
            description = description,
            isVerified = false,
            suggestedBy = "Anonymous ${userSession.value.anonymousNumberCode}"
        )
        viewModelScope.launch {
            repository.addTimelineEvent(event, postId)
        }
    }

    // Create New Output
    fun createOutput(
        title: String,
        content: String,
        type: PostType,
        category: String,
        privacyMode: PrivacyMode,
        genreTheme: GenreTheme,
        knownFacts: List<String>,
        unknownFacts: List<String>,
        questionToCommunity: String,
        tags: List<String>,
        isSensitive: Boolean,
        sensitiveWarning: String,
        voiceDurationSeconds: Int = 0,
        voiceTranscript: String = ""
    ) {
        val session = userSession.value
        val authorName = when (privacyMode) {
            PrivacyMode.REAL_PROFILE -> session.publicUsername
            PrivacyMode.PSEUDONYM -> session.pseudonym
            PrivacyMode.ANONYMOUS_USERNAME -> "AnonymousScribe"
            PrivacyMode.ANONYMOUS_NUMBER -> "Anonymous ${session.anonymousNumberCode}"
            PrivacyMode.COMPLETELY_ANONYMOUS -> "Anonymous"
        }
        val authorHandle = when (privacyMode) {
            PrivacyMode.REAL_PROFILE -> "@${session.publicUsername.lowercase()}"
            PrivacyMode.PSEUDONYM -> "@${session.pseudonym.lowercase()}"
            else -> "@anon"
        }
        val readTime = (content.split("\\s+".toRegex()).size / 150).coerceAtLeast(1)

        val newPost = PostItem(
            id = "post_${UUID.randomUUID()}",
            title = title,
            content = content,
            type = type,
            category = category.ifBlank { "General" },
            authorName = authorName,
            authorHandle = authorHandle,
            identityMode = privacyMode,
            anonymousNumberCode = session.anonymousNumberCode,
            timestamp = "Just now",
            readTimeMinutes = readTime,
            readingProgress = 0,
            coverImageRes = if (type == PostType.MYSTERY) "mystery_case_banner" else if (type == PostType.REAL_INCIDENT) "incident_investigation_bg" else "archive_dossier_art",
            voiceAudioDurationSeconds = voiceDurationSeconds,
            voiceTranscript = voiceTranscript,
            knownFacts = knownFacts,
            unknownFacts = unknownFacts,
            questionToCommunity = questionToCommunity,
            mysteryStatus = if (type == PostType.MYSTERY) MysteryStatus.OPEN else MysteryStatus.OPEN,
            tags = tags.ifEmpty { listOf("#${category.replace(" ", "")}") },
            likesCount = 1,
            commentsCount = 0,
            savesCount = 0,
            sharesCount = 0,
            isLiked = true,
            isSaved = false,
            isArchived = false,
            isSensitive = isSensitive,
            sensitiveWarning = sensitiveWarning,
            genreTheme = genreTheme,
            collaborationAllowed = true,
            collaborators = emptyList(),
            evidenceCount = 0,
            theoriesCount = 0,
            timelineCount = 0,
            verifiedBadge = false
        )
        viewModelScope.launch {
            repository.insertPost(newPost)
        }
    }

    // Direct Messaging
    fun sendMessage(conversationId: String, text: String) {
        if (text.isBlank()) return
        val session = userSession.value
        val msg = ChatMessage(
            id = "msg_${UUID.randomUUID()}",
            conversationId = conversationId,
            senderName = "Anonymous ${session.anonymousNumberCode}",
            senderIdentity = session.defaultPrivacyMode,
            isMe = true,
            text = text,
            timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
            mediaType = null,
            isRequest = false,
            audioDurationSeconds = 0
        )
        viewModelScope.launch {
            repository.sendMessage(msg)
        }
    }

    // 18+ Age Gate & Onboarding
    fun verifyAgeAndCompleteOnboarding(
        day: Int,
        month: Int,
        year: Int,
        citizenshipCountry: String,
        defaultPrivacyMode: PrivacyMode,
        publicUsername: String,
        pseudonym: String,
        interests: List<String>
    ): Boolean {
        // Calculate age
        val birthDate = Calendar.getInstance().apply {
            set(year, month - 1, day)
        }
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        if (age < 18) {
            return false // Must be 18+
        }

        val updated = userSession.value.copy(
            isAgeVerified = true,
            dob = String.format("%04d-%02d-%02d", year, month, day),
            citizenshipCountry = citizenshipCountry,
            defaultPrivacyMode = defaultPrivacyMode,
            publicUsername = publicUsername.ifBlank { "ShadowWriter" },
            pseudonym = pseudonym.ifBlank { "TheNightArchivist" },
            followedTags = interests
        )

        viewModelScope.launch {
            repository.updateUserSession(updated)
        }
        return true
    }

    fun updateDefaultPrivacyMode(mode: PrivacyMode) {
        viewModelScope.launch {
            repository.updateDefaultPrivacyMode(mode)
        }
    }

    fun updateAppTheme(theme: GenreTheme) {
        viewModelScope.launch {
            repository.updateAppTheme(theme)
        }
    }

    fun toggleSensitiveBlur(blur: Boolean) {
        viewModelScope.launch {
            repository.updateBlurSensitive(blur)
        }
    }

    fun logout() {
        viewModelScope.launch {
            val loggedOutSession = userSession.value.copy(
                isAgeVerified = false
            )
            repository.updateUserSession(loggedOutSession)
            _readingModeState.value = ReadingModeState()
            _selectedFeedTab.value = FeedTab.FOR_YOU
            _selectedSortOrder.value = FeedSortOrder.NEWEST
        }
    }
}
