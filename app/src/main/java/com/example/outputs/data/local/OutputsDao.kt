package com.example.outputs.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.outputs.data.model.GenreTheme
import com.example.outputs.data.model.MysteryStatus
import com.example.outputs.data.model.PostType
import com.example.outputs.data.model.PrivacyMode
import kotlinx.coroutines.flow.Flow

@Dao
interface OutputsDao {

    // Posts
    @Query("SELECT * FROM posts WHERE isArchived = 0 ORDER BY id DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: String): Flow<PostEntity?>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostByIdOnce(postId: String): PostEntity?

    @Query("SELECT * FROM posts WHERE isSaved = 1 AND isArchived = 0 ORDER BY id DESC")
    fun getSavedPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE isArchived = 1 ORDER BY id DESC")
    fun getArchivedPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE type = :type AND isArchived = 0 ORDER BY id DESC")
    fun getPostsByType(type: PostType): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%') AND isArchived = 0")
    fun searchPosts(query: String): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("UPDATE posts SET isLiked = :isLiked, likesCount = likesCount + :delta WHERE id = :postId")
    suspend fun togglePostLike(postId: String, isLiked: Boolean, delta: Int)

    @Query("UPDATE posts SET isSaved = :isSaved, savesCount = savesCount + :delta WHERE id = :postId")
    suspend fun togglePostSaved(postId: String, isSaved: Boolean, delta: Int)

    @Query("UPDATE posts SET isArchived = :isArchived WHERE id = :postId")
    suspend fun setPostArchived(postId: String, isArchived: Boolean)

    @Query("UPDATE posts SET readingProgress = :progress WHERE id = :postId")
    suspend fun updateReadingProgress(postId: String, progress: Int)

    @Query("UPDATE posts SET mysteryStatus = :status WHERE id = :postId")
    suspend fun updateMysteryStatus(postId: String, status: MysteryStatus)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: String)

    // Comments
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY isAcceptedAnswer DESC, timestamp ASC")
    fun getCommentsForPost(postId: String): Flow<List<CommentEntity>>

    @Query("SELECT * FROM comments WHERE isSaved = 1 ORDER BY timestamp DESC")
    fun getSavedComments(): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Query("UPDATE comments SET text = :newText, isEdited = 1 WHERE id = :commentId")
    suspend fun updateCommentText(commentId: String, newText: String)

    @Query("UPDATE comments SET isLiked = :isLiked, likes = likes + :delta WHERE id = :commentId")
    suspend fun toggleCommentLike(commentId: String, isLiked: Boolean, delta: Int)

    @Query("UPDATE comments SET isSaved = :isSaved WHERE id = :commentId")
    suspend fun toggleCommentSaved(commentId: String, isSaved: Boolean)

    @Query("UPDATE comments SET replyCount = replyCount + 1 WHERE id = :commentId")
    suspend fun incrementCommentReplyCount(commentId: String)

    @Query("UPDATE comments SET isAcceptedAnswer = 1 WHERE id = :commentId")
    suspend fun markAcceptedAnswer(commentId: String)

    @Query("DELETE FROM comments WHERE id = :commentId")
    suspend fun deleteComment(commentId: String)

    // Evidence
    @Query("SELECT * FROM evidence WHERE postId = :postId ORDER BY upvotes DESC, confidenceLevel DESC")
    fun getEvidenceForPost(postId: String): Flow<List<EvidenceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidence(evidence: EvidenceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidenceList(evidenceList: List<EvidenceEntity>)

    @Query("UPDATE evidence SET upvotes = upvotes + 1 WHERE id = :evidenceId")
    suspend fun upvoteEvidence(evidenceId: String)

    // Theories
    @Query("SELECT * FROM theories WHERE postId = :postId ORDER BY isAccepted DESC, supportCount DESC")
    fun getTheoriesForPost(postId: String): Flow<List<TheoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheory(theory: TheoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheories(theories: List<TheoryEntity>)

    @Query("UPDATE theories SET supportCount = supportCount + 1 WHERE id = :theoryId")
    suspend fun supportTheory(theoryId: String)

    @Query("UPDATE theories SET challengeCount = challengeCount + 1 WHERE id = :theoryId")
    suspend fun challengeTheory(theoryId: String)

    @Query("UPDATE theories SET isAccepted = 1 WHERE id = :theoryId")
    suspend fun acceptTheory(theoryId: String)

    @Query("UPDATE theories SET isDebunked = 1 WHERE id = :theoryId")
    suspend fun debunkTheory(theoryId: String)

    // Timeline Events
    @Query("SELECT * FROM timeline_events WHERE postId = :postId ORDER BY id ASC")
    fun getTimelineForPost(postId: String): Flow<List<TimelineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimelineEvent(event: TimelineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimelineEvents(events: List<TimelineEntity>)

    // Groups
    @Query("SELECT * FROM groups ORDER BY memberCount DESC")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM groups WHERE id = :groupId")
    fun getGroupById(groupId: String): Flow<GroupEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<GroupEntity>)

    // Messages
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY id ASC")
    fun getMessages(conversationId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE isRequest = 1 ORDER BY id DESC")
    fun getMessageRequests(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    // User Session
    @Query("SELECT * FROM user_session LIMIT 1")
    fun getUserSession(): Flow<UserSessionEntity?>

    @Query("SELECT * FROM user_session LIMIT 1")
    suspend fun getUserSessionOnce(): UserSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserSession(user: UserSessionEntity)

    @Query("UPDATE user_session SET defaultPrivacyMode = :mode")
    suspend fun updateDefaultPrivacyMode(mode: PrivacyMode)

    @Query("UPDATE user_session SET selectedAppTheme = :theme")
    suspend fun updateAppTheme(theme: GenreTheme)

    @Query("UPDATE user_session SET blurSensitiveContent = :blur")
    suspend fun updateBlurSensitive(blur: Boolean)
}
