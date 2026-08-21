package com.example.outputs.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.outputs.data.model.EvidenceType
import com.example.outputs.data.model.GenreTheme
import com.example.outputs.data.model.MysteryStatus
import com.example.outputs.data.model.PostType
import com.example.outputs.data.model.PrivacyMode
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val type: PostType,
    val category: String,
    val authorName: String,
    val authorHandle: String,
    val identityMode: PrivacyMode,
    val anonymousNumberCode: String,
    val timestamp: String,
    val readTimeMinutes: Int,
    val readingProgress: Int,
    val coverImageRes: String,
    val voiceAudioDurationSeconds: Int,
    val voiceTranscript: String,
    val knownFacts: List<String>,
    val unknownFacts: List<String>,
    val questionToCommunity: String,
    val mysteryStatus: MysteryStatus,
    val tags: List<String>,
    val likesCount: Int,
    val commentsCount: Int,
    val savesCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val isArchived: Boolean,
    val isSensitive: Boolean,
    val sensitiveWarning: String,
    val genreTheme: GenreTheme,
    val collaborationAllowed: Boolean,
    val collaborators: List<String>,
    val evidenceCount: Int,
    val theoriesCount: Int,
    val timelineCount: Int,
    val verifiedBadge: Boolean
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,
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
    val isEdited: Boolean = false
)

@Entity(tableName = "evidence")
data class EvidenceEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val title: String,
    val description: String,
    val type: EvidenceType,
    val mediaResName: String,
    val contributor: String,
    val contributorIdentity: PrivacyMode,
    val dateAdded: String,
    val confidenceLevel: Int,
    val upvotes: Int,
    val verifiedByCreator: Boolean
)

@Entity(tableName = "theories")
data class TheoryEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val author: String,
    val authorIdentity: PrivacyMode,
    val title: String,
    val content: String,
    val supportCount: Int,
    val challengeCount: Int,
    val isAccepted: Boolean,
    val isDebunked: Boolean,
    val evidenceIds: List<String>,
    val timestamp: String
)

@Entity(tableName = "timeline_events")
data class TimelineEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val timeLabel: String,
    val title: String,
    val description: String,
    val isVerified: Boolean,
    val suggestedBy: String
)

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val category: String,
    val memberCount: Int,
    val isPrivate: Boolean,
    val isAnonymousOnly: Boolean,
    val myAnonymousAlias: String,
    val unreadCount: Int,
    val pinnedNotice: String,
    val activeCaseCount: Int
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderName: String,
    val senderIdentity: PrivacyMode,
    val isMe: Boolean,
    val text: String,
    val timestamp: String,
    val mediaType: String?,
    val isRequest: Boolean,
    val audioDurationSeconds: Int
)

@Entity(tableName = "user_session")
data class UserSessionEntity(
    @PrimaryKey val userId: String,
    val isAgeVerified: Boolean,
    val dob: String,
    val citizenshipCountry: String,
    val publicUsername: String,
    val pseudonym: String,
    val anonymousNumberCode: String,
    val defaultPrivacyMode: PrivacyMode,
    val bio: String,
    val solvedMysteriesCount: Int,
    val helpfulEvidenceCount: Int,
    val activeCollaborationsCount: Int,
    val followedTags: List<String>,
    val blurSensitiveContent: Boolean,
    val selectedAppTheme: GenreTheme
)

class Converters {
    private val moshi = Moshi.Builder().build()
    private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
    private val stringListAdapter = moshi.adapter<List<String>>(stringListType)

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return list?.let { stringListAdapter.toJson(it) } ?: "[]"
    }

    @TypeConverter
    fun toStringList(json: String?): List<String> {
        return json?.let { stringListAdapter.fromJson(it) } ?: emptyList()
    }

    @TypeConverter
    fun fromPostType(type: PostType): String = type.name

    @TypeConverter
    fun toPostType(value: String): PostType = runCatching { PostType.valueOf(value) }.getOrDefault(PostType.STORY)

    @TypeConverter
    fun fromPrivacyMode(mode: PrivacyMode): String = mode.name

    @TypeConverter
    fun toPrivacyMode(value: String): PrivacyMode = runCatching { PrivacyMode.valueOf(value) }.getOrDefault(PrivacyMode.ANONYMOUS_NUMBER)

    @TypeConverter
    fun fromMysteryStatus(status: MysteryStatus): String = status.name

    @TypeConverter
    fun toMysteryStatus(value: String): MysteryStatus = runCatching { MysteryStatus.valueOf(value) }.getOrDefault(MysteryStatus.OPEN)

    @TypeConverter
    fun fromGenreTheme(theme: GenreTheme): String = theme.name

    @TypeConverter
    fun toGenreTheme(value: String): GenreTheme = runCatching { GenreTheme.valueOf(value) }.getOrDefault(GenreTheme.MYSTERY)

    @TypeConverter
    fun fromEvidenceType(type: EvidenceType): String = type.name

    @TypeConverter
    fun toEvidenceType(value: String): EvidenceType = runCatching { EvidenceType.valueOf(value) }.getOrDefault(EvidenceType.PHOTO)
}
