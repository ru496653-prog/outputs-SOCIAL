package com.example.outputs.data.model

enum class PostType(val label: String, val icon: String) {
    MYSTERY("Mystery", "🔍"),
    REAL_INCIDENT("Real Incident", "⚠️"),
    STORY("Story", "📖"),
    OPINION("Opinion", "💭"),
    QUESTION("Question", "❓"),
    INVESTIGATION("Investigation", "🗂️"),
    EXPERIENCE("Experience", "✨"),
    THEORY("Theory", "🧩"),
    CUSTOM("Custom", "📝")
}

enum class PrivacyMode(val label: String, val description: String) {
    REAL_PROFILE("Real Profile", "Shows your username, avatar and bio"),
    PSEUDONYM("Pseudonym", "Shows a chosen pen name (e.g. ShadowWriter)"),
    ANONYMOUS_USERNAME("Anonymous Username", "Shows a randomized alias (e.g. AnonymousFox)"),
    ANONYMOUS_NUMBER("Anonymous Number", "Shows a persistent session code (e.g. Anonymous #4827)"),
    COMPLETELY_ANONYMOUS("Completely Anonymous", "Shows only 'Anonymous' on the post")
}

enum class MysteryStatus(val label: String, val colorHex: Long) {
    OPEN("Open Investigation", 0xFF2196F3),
    INVESTIGATING("Active Research", 0xFFFF9800),
    STRONG_THEORY("Strong Theory", 0xFF9C27B0),
    SOLVED("Solved & Accepted", 0xFF4CAF50),
    DEBUNKED("Debunked", 0xFFE91E63),
    UNRESOLVED("Unresolved Archive", 0xFF9E9E9E)
}

enum class GenreTheme(val label: String, val primaryColorHex: Long, val surfaceColorHex: Long, val fontStyle: String) {
    MYSTERY("Mystery", 0xFF9D4EDD, 0xFF120E1E, "Moody"),
    HORROR("Horror", 0xFFD00000, 0xFF140808, "Distressed"),
    ACTION("Action", 0xFFFF6B35, 0xFF14120C, "Industrial"),
    NOIR("Noir", 0xFFE0E1DD, 0xFF0D0D0D, "FilmGrain"),
    CYBER("Cyber", 0xFF00F5D4, 0xFF08141E, "Matrix"),
    ARCHIVE("Archive", 0xFFD4A373, 0xFF1C1712, "Typewriter"),
    MINIMAL("Minimal", 0xFF90E0EF, 0xFF111418, "Clean"),
    DARK("Dark", 0xFFA0AAB2, 0xFF0F1117, "Classic");

    val icon: String
        get() = when (this) {
            MYSTERY -> "🔮"
            HORROR -> "🕯️"
            ACTION -> "⚡"
            NOIR -> "🕵️"
            CYBER -> "💾"
            ARCHIVE -> "📜"
            MINIMAL -> "✨"
            DARK -> "🌑"
        }
}

enum class EvidenceType(val label: String, val icon: String) {
    PHOTO("Photograph", "📷"),
    VIDEO("Video Footage", "📹"),
    AUDIO("Audio Recording", "🎙️"),
    DOCUMENT("Classified Document", "📄"),
    SCREENSHOT("Digital Screenshot", "📱"),
    LINK("External Archive Link", "🔗"),
    TIMELINE_EVENT("Timeline Timestamp", "⏱️"),
    TESTIMONY("Witness Testimony", "🗣️")
}

data class EvidenceItem(
    val id: String,
    val title: String,
    val description: String,
    val type: EvidenceType,
    val mediaResName: String = "",
    val contributor: String,
    val contributorIdentity: PrivacyMode = PrivacyMode.ANONYMOUS_NUMBER,
    val dateAdded: String,
    val confidenceLevel: Int = 85, // 0-100%
    val upvotes: Int = 0,
    val verifiedByCreator: Boolean = false
)

data class TimelineEvent(
    val id: String,
    val timeLabel: String,
    val title: String,
    val description: String,
    val isVerified: Boolean = false,
    val suggestedBy: String = "Anonymous #4827"
)

data class Theory(
    val id: String,
    val author: String,
    val authorIdentity: PrivacyMode,
    val title: String,
    val content: String,
    val supportCount: Int = 0,
    val challengeCount: Int = 0,
    val isAccepted: Boolean = false,
    val isDebunked: Boolean = false,
    val evidenceIds: List<String> = emptyList(),
    val timestamp: String = "Just now"
)

data class PostComment(
    val id: String,
    val postId: String,
    val author: String,
    val identityMode: PrivacyMode,
    val text: String,
    val timestamp: String,
    val likes: Int = 0,
    val isLiked: Boolean = false,
    val isAcceptedAnswer: Boolean = false,
    val isTheory: Boolean = false,
    val isEvidenceSubmitted: Boolean = false,
    val audioDurationSeconds: Int = 0,
    val replyCount: Int = 0,
    val parentCommentId: String? = null,
    val replyToAuthor: String? = null,
    val isSaved: Boolean = false,
    val isEdited: Boolean = false,
    val replies: List<PostComment> = emptyList()
)

data class PostItem(
    val id: String,
    val title: String,
    val content: String,
    val type: PostType,
    val category: String,
    val authorName: String,
    val authorHandle: String,
    val identityMode: PrivacyMode,
    val anonymousNumberCode: String = "#7294",
    val timestamp: String,
    val readTimeMinutes: Int = 3,
    val readingProgress: Int = 0, // 0 - 100%
    val coverImageRes: String = "",
    val voiceAudioDurationSeconds: Int = 0,
    val voiceTranscript: String = "",
    val knownFacts: List<String> = emptyList(),
    val unknownFacts: List<String> = emptyList(),
    val questionToCommunity: String = "",
    val mysteryStatus: MysteryStatus = MysteryStatus.OPEN,
    val tags: List<String> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val savesCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isArchived: Boolean = false,
    val isSensitive: Boolean = false,
    val sensitiveWarning: String = "",
    val genreTheme: GenreTheme = GenreTheme.DARK,
    val collaborationAllowed: Boolean = true,
    val collaborators: List<String> = emptyList(),
    val evidenceCount: Int = 0,
    val theoriesCount: Int = 0,
    val timelineCount: Int = 0,
    val verifiedBadge: Boolean = false
)

data class AnonymousGroup(
    val id: String,
    val name: String,
    val description: String,
    val category: String = "General",
    val memberCount: Int = 120,
    val isPrivate: Boolean = false,
    val isAnonymousOnly: Boolean = true,
    val myAnonymousAlias: String = "Member #38",
    val unreadCount: Int = 0,
    val pinnedNotice: String = "",
    val activeCaseCount: Int = 1,
    val isJoined: Boolean = true
) {
    val membersCount: Int get() = memberCount
    val activeCasesCount: Int get() = activeCaseCount
    val topic: String get() = category
}

data class ChatMessage(
    val id: String,
    val conversationId: String,
    val senderName: String,
    val senderIdentity: PrivacyMode,
    val isMe: Boolean,
    val text: String,
    val timestamp: String,
    val mediaType: String? = null,
    val isRequest: Boolean = false,
    val audioDurationSeconds: Int = 0
)

data class UserSession(
    val userId: String = "user_anon_849201",
    val isAgeVerified: Boolean = false,
    val dob: String = "",
    val citizenshipCountry: String = "",
    val publicUsername: String = "ShadowWriter",
    val pseudonym: String = "TheNightArchivist",
    val anonymousNumberCode: String = "#4827",
    val defaultPrivacyMode: PrivacyMode = PrivacyMode.ANONYMOUS_NUMBER,
    val bio: String = "Investigating unsolved phenomena, forgotten archives, and midnight stories.",
    val solvedMysteriesCount: Int = 3,
    val helpfulEvidenceCount: Int = 14,
    val activeCollaborationsCount: Int = 2,
    val followedTags: List<String> = listOf("#Mystery", "#RealIncident", "#Unexplained", "#History", "#Tech"),
    val blurSensitiveContent: Boolean = true,
    val selectedAppTheme: GenreTheme = GenreTheme.MYSTERY
) {
    val theoriesAcceptedCount: Int get() = solvedMysteriesCount
}
