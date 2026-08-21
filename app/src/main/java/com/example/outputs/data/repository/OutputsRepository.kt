package com.example.outputs.data.repository

import com.example.outputs.data.local.CommentEntity
import com.example.outputs.data.local.EvidenceEntity
import com.example.outputs.data.local.OutputsDao
import com.example.outputs.data.local.PostEntity
import com.example.outputs.data.local.TheoryEntity
import com.example.outputs.data.local.TimelineEntity
import com.example.outputs.data.local.UserSessionEntity
import com.example.outputs.data.model.AnonymousGroup
import com.example.outputs.data.model.ChatMessage
import com.example.outputs.data.model.EvidenceItem
import com.example.outputs.data.model.GenreTheme
import com.example.outputs.data.model.MysteryStatus
import com.example.outputs.data.model.PostComment
import com.example.outputs.data.model.PostItem
import com.example.outputs.data.model.PostType
import com.example.outputs.data.model.PrivacyMode
import com.example.outputs.data.model.Theory
import com.example.outputs.data.model.TimelineEvent
import com.example.outputs.data.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OutputsRepository(private val dao: OutputsDao) {

    val allPosts: Flow<List<PostItem>> = dao.getAllPosts().map { list ->
        list.map { it.toModel() }
    }

    val savedPosts: Flow<List<PostItem>> = dao.getSavedPosts().map { list ->
        list.map { it.toModel() }
    }

    val archivedPosts: Flow<List<PostItem>> = dao.getArchivedPosts().map { list ->
        list.map { it.toModel() }
    }

    val allGroups: Flow<List<AnonymousGroup>> = dao.getAllGroups().map { list ->
        list.map {
            AnonymousGroup(
                id = it.id,
                name = it.name,
                description = it.description,
                category = it.category,
                memberCount = it.memberCount,
                isPrivate = it.isPrivate,
                isAnonymousOnly = it.isAnonymousOnly,
                myAnonymousAlias = it.myAnonymousAlias,
                unreadCount = it.unreadCount,
                pinnedNotice = it.pinnedNotice,
                activeCaseCount = it.activeCaseCount
            )
        }
    }

    val userSession: Flow<UserSession> = dao.getUserSession().map {
        it?.toModel() ?: UserSession()
    }

    fun getPostById(postId: String): Flow<PostItem?> = dao.getPostById(postId).map { it?.toModel() }

    fun getPostsByType(type: PostType): Flow<List<PostItem>> = dao.getPostsByType(type).map { list ->
        list.map { it.toModel() }
    }

    fun searchPosts(query: String): Flow<List<PostItem>> = dao.searchPosts(query).map { list ->
        list.map { it.toModel() }
    }

    val savedComments: Flow<List<PostComment>> = dao.getSavedComments().map { list ->
        list.map { it.toModel() }
    }

    fun getCommentsForPost(postId: String): Flow<List<PostComment>> = dao.getCommentsForPost(postId).map { list ->
        val models = list.map { it.toModel() }
        val parentMap = models.groupBy { it.parentCommentId }
        val topLevel = parentMap[null] ?: emptyList()
        topLevel.map { parent ->
            parent.copy(replies = parentMap[parent.id] ?: emptyList())
        }
    }

    fun getEvidenceForPost(postId: String): Flow<List<EvidenceItem>> = dao.getEvidenceForPost(postId).map { list ->
        list.map {
            EvidenceItem(
                id = it.id,
                title = it.title,
                description = it.description,
                type = it.type,
                mediaResName = it.mediaResName,
                contributor = it.contributor,
                contributorIdentity = it.contributorIdentity,
                dateAdded = it.dateAdded,
                confidenceLevel = it.confidenceLevel,
                upvotes = it.upvotes,
                verifiedByCreator = it.verifiedByCreator
            )
        }
    }

    fun getTheoriesForPost(postId: String): Flow<List<Theory>> = dao.getTheoriesForPost(postId).map { list ->
        list.map {
            Theory(
                id = it.id,
                author = it.author,
                authorIdentity = it.authorIdentity,
                title = it.title,
                content = it.content,
                supportCount = it.supportCount,
                challengeCount = it.challengeCount,
                isAccepted = it.isAccepted,
                isDebunked = it.isDebunked,
                evidenceIds = it.evidenceIds,
                timestamp = it.timestamp
            )
        }
    }

    fun getTimelineForPost(postId: String): Flow<List<TimelineEvent>> = dao.getTimelineForPost(postId).map { list ->
        list.map {
            TimelineEvent(
                id = it.id,
                timeLabel = it.timeLabel,
                title = it.title,
                description = it.description,
                isVerified = it.isVerified,
                suggestedBy = it.suggestedBy
            )
        }
    }

    fun getMessages(conversationId: String): Flow<List<ChatMessage>> = dao.getMessages(conversationId).map { list ->
        list.map {
            ChatMessage(
                id = it.id,
                conversationId = it.conversationId,
                senderName = it.senderName,
                senderIdentity = it.senderIdentity,
                isMe = it.isMe,
                text = it.text,
                timestamp = it.timestamp,
                mediaType = it.mediaType,
                isRequest = it.isRequest,
                audioDurationSeconds = it.audioDurationSeconds
            )
        }
    }

    fun getMessageRequests(): Flow<List<ChatMessage>> = dao.getMessageRequests().map { list ->
        list.map {
            ChatMessage(
                id = it.id,
                conversationId = it.conversationId,
                senderName = it.senderName,
                senderIdentity = it.senderIdentity,
                isMe = it.isMe,
                text = it.text,
                timestamp = it.timestamp,
                mediaType = it.mediaType,
                isRequest = it.isRequest,
                audioDurationSeconds = it.audioDurationSeconds
            )
        }
    }

    // Actions
    suspend fun insertPost(post: PostItem) = dao.insertPost(post.toEntity())

    suspend fun toggleLike(postId: String, currentLiked: Boolean) {
        val delta = if (currentLiked) -1 else 1
        dao.togglePostLike(postId, !currentLiked, delta)
    }

    suspend fun toggleSave(postId: String, currentSaved: Boolean) {
        val delta = if (currentSaved) -1 else 1
        dao.togglePostSaved(postId, !currentSaved, delta)
    }

    suspend fun toggleArchive(postId: String, currentArchived: Boolean) {
        dao.setPostArchived(postId, !currentArchived)
    }

    suspend fun updateReadingProgress(postId: String, progress: Int) {
        dao.updateReadingProgress(postId, progress)
    }

    suspend fun updateMysteryStatus(postId: String, status: MysteryStatus) {
        dao.updateMysteryStatus(postId, status)
    }

    suspend fun deletePost(postId: String) {
        dao.deletePost(postId)
    }

    suspend fun addComment(comment: PostComment) {
        dao.insertComment(comment.toEntity())
        if (comment.parentCommentId != null) {
            dao.incrementCommentReplyCount(comment.parentCommentId)
        }
    }

    suspend fun editComment(commentId: String, newText: String) {
        dao.updateCommentText(commentId, newText)
    }

    suspend fun toggleCommentLike(commentId: String, currentLiked: Boolean) {
        val delta = if (currentLiked) -1 else 1
        dao.toggleCommentLike(commentId, !currentLiked, delta)
    }

    suspend fun toggleCommentSave(commentId: String, currentSaved: Boolean) {
        dao.toggleCommentSaved(commentId, !currentSaved)
    }

    suspend fun markAcceptedAnswer(commentId: String) {
        dao.markAcceptedAnswer(commentId)
    }

    suspend fun deleteComment(commentId: String) {
        dao.deleteComment(commentId)
    }

    suspend fun addEvidence(evidence: EvidenceItem, postId: String) {
        dao.insertEvidence(
            EvidenceEntity(
                id = evidence.id,
                postId = postId,
                title = evidence.title,
                description = evidence.description,
                type = evidence.type,
                mediaResName = evidence.mediaResName,
                contributor = evidence.contributor,
                contributorIdentity = evidence.contributorIdentity,
                dateAdded = evidence.dateAdded,
                confidenceLevel = evidence.confidenceLevel,
                upvotes = evidence.upvotes,
                verifiedByCreator = evidence.verifiedByCreator
            )
        )
    }

    suspend fun upvoteEvidence(evidenceId: String) {
        dao.upvoteEvidence(evidenceId)
    }

    suspend fun addTheory(theory: Theory, postId: String) {
        dao.insertTheory(
            TheoryEntity(
                id = theory.id,
                postId = postId,
                author = theory.author,
                authorIdentity = theory.authorIdentity,
                title = theory.title,
                content = theory.content,
                supportCount = theory.supportCount,
                challengeCount = theory.challengeCount,
                isAccepted = theory.isAccepted,
                isDebunked = theory.isDebunked,
                evidenceIds = theory.evidenceIds,
                timestamp = theory.timestamp
            )
        )
    }

    suspend fun supportTheory(theoryId: String) {
        dao.supportTheory(theoryId)
    }

    suspend fun challengeTheory(theoryId: String) {
        dao.challengeTheory(theoryId)
    }

    suspend fun acceptTheory(theoryId: String) {
        dao.acceptTheory(theoryId)
    }

    suspend fun debunkTheory(theoryId: String) {
        dao.debunkTheory(theoryId)
    }

    suspend fun addTimelineEvent(event: TimelineEvent, postId: String) {
        dao.insertTimelineEvent(
            TimelineEntity(
                id = event.id,
                postId = postId,
                timeLabel = event.timeLabel,
                title = event.title,
                description = event.description,
                isVerified = event.isVerified,
                suggestedBy = event.suggestedBy
            )
        )
    }

    suspend fun sendMessage(message: ChatMessage) {
        dao.insertMessage(
            com.example.outputs.data.local.MessageEntity(
                id = message.id,
                conversationId = message.conversationId,
                senderName = message.senderName,
                senderIdentity = message.senderIdentity,
                isMe = message.isMe,
                text = message.text,
                timestamp = message.timestamp,
                mediaType = message.mediaType,
                isRequest = message.isRequest,
                audioDurationSeconds = message.audioDurationSeconds
            )
        )
    }

    suspend fun updateUserSession(session: UserSession) {
        dao.insertOrUpdateUserSession(session.toEntity())
    }

    suspend fun updateDefaultPrivacyMode(mode: PrivacyMode) {
        dao.updateDefaultPrivacyMode(mode)
    }

    suspend fun updateAppTheme(theme: GenreTheme) {
        dao.updateAppTheme(theme)
    }

    suspend fun updateBlurSensitive(blur: Boolean) {
        dao.updateBlurSensitive(blur)
    }

    private fun PostEntity.toModel() = PostItem(
        id = id,
        title = title,
        content = content,
        type = type,
        category = category,
        authorName = authorName,
        authorHandle = authorHandle,
        identityMode = identityMode,
        anonymousNumberCode = anonymousNumberCode,
        timestamp = timestamp,
        readTimeMinutes = readTimeMinutes,
        readingProgress = readingProgress,
        coverImageRes = coverImageRes,
        voiceAudioDurationSeconds = voiceAudioDurationSeconds,
        voiceTranscript = voiceTranscript,
        knownFacts = knownFacts,
        unknownFacts = unknownFacts,
        questionToCommunity = questionToCommunity,
        mysteryStatus = mysteryStatus,
        tags = tags,
        likesCount = likesCount,
        commentsCount = commentsCount,
        savesCount = savesCount,
        sharesCount = sharesCount,
        isLiked = isLiked,
        isSaved = isSaved,
        isArchived = isArchived,
        isSensitive = isSensitive,
        sensitiveWarning = sensitiveWarning,
        genreTheme = genreTheme,
        collaborationAllowed = collaborationAllowed,
        collaborators = collaborators,
        evidenceCount = evidenceCount,
        theoriesCount = theoriesCount,
        timelineCount = timelineCount,
        verifiedBadge = verifiedBadge
    )

    private fun PostItem.toEntity() = PostEntity(
        id = id,
        title = title,
        content = content,
        type = type,
        category = category,
        authorName = authorName,
        authorHandle = authorHandle,
        identityMode = identityMode,
        anonymousNumberCode = anonymousNumberCode,
        timestamp = timestamp,
        readTimeMinutes = readTimeMinutes,
        readingProgress = readingProgress,
        coverImageRes = coverImageRes,
        voiceAudioDurationSeconds = voiceAudioDurationSeconds,
        voiceTranscript = voiceTranscript,
        knownFacts = knownFacts,
        unknownFacts = unknownFacts,
        questionToCommunity = questionToCommunity,
        mysteryStatus = mysteryStatus,
        tags = tags,
        likesCount = likesCount,
        commentsCount = commentsCount,
        savesCount = savesCount,
        sharesCount = sharesCount,
        isLiked = isLiked,
        isSaved = isSaved,
        isArchived = isArchived,
        isSensitive = isSensitive,
        sensitiveWarning = sensitiveWarning,
        genreTheme = genreTheme,
        collaborationAllowed = collaborationAllowed,
        collaborators = collaborators,
        evidenceCount = evidenceCount,
        theoriesCount = theoriesCount,
        timelineCount = timelineCount,
        verifiedBadge = verifiedBadge
    )

    private fun UserSessionEntity.toModel() = UserSession(
        userId = userId,
        isAgeVerified = isAgeVerified,
        dob = dob,
        citizenshipCountry = citizenshipCountry,
        publicUsername = publicUsername,
        pseudonym = pseudonym,
        anonymousNumberCode = anonymousNumberCode,
        defaultPrivacyMode = defaultPrivacyMode,
        bio = bio,
        solvedMysteriesCount = solvedMysteriesCount,
        helpfulEvidenceCount = helpfulEvidenceCount,
        activeCollaborationsCount = activeCollaborationsCount,
        followedTags = followedTags,
        blurSensitiveContent = blurSensitiveContent,
        selectedAppTheme = selectedAppTheme
    )

    private fun UserSession.toEntity() = UserSessionEntity(
        userId = userId,
        isAgeVerified = isAgeVerified,
        dob = dob,
        citizenshipCountry = citizenshipCountry,
        publicUsername = publicUsername,
        pseudonym = pseudonym,
        anonymousNumberCode = anonymousNumberCode,
        defaultPrivacyMode = defaultPrivacyMode,
        bio = bio,
        solvedMysteriesCount = solvedMysteriesCount,
        helpfulEvidenceCount = helpfulEvidenceCount,
        activeCollaborationsCount = activeCollaborationsCount,
        followedTags = followedTags,
        blurSensitiveContent = blurSensitiveContent,
        selectedAppTheme = selectedAppTheme
    )

    private fun CommentEntity.toModel() = PostComment(
        id = id,
        postId = postId,
        author = author,
        identityMode = identityMode,
        text = text,
        timestamp = timestamp,
        likes = likes,
        isLiked = isLiked,
        isAcceptedAnswer = isAcceptedAnswer,
        isTheory = isTheory,
        isEvidenceSubmitted = isEvidenceSubmitted,
        audioDurationSeconds = audioDurationSeconds,
        replyCount = replyCount,
        parentCommentId = parentCommentId,
        replyToAuthor = replyToAuthor,
        isSaved = isSaved,
        isEdited = isEdited,
        replies = emptyList()
    )

    private fun PostComment.toEntity() = CommentEntity(
        id = id,
        postId = postId,
        author = author,
        identityMode = identityMode,
        text = text,
        timestamp = timestamp,
        likes = likes,
        isLiked = isLiked,
        isAcceptedAnswer = isAcceptedAnswer,
        isTheory = isTheory,
        isEvidenceSubmitted = isEvidenceSubmitted,
        audioDurationSeconds = audioDurationSeconds,
        replyCount = replyCount,
        parentCommentId = parentCommentId,
        replyToAuthor = replyToAuthor,
        isSaved = isSaved,
        isEdited = isEdited
    )
}
