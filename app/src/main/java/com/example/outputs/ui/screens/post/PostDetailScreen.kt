package com.example.outputs.ui.screens.post

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.PostComment
import com.example.outputs.data.model.PostItem
import com.example.outputs.data.model.PostType
import com.example.outputs.data.model.PrivacyMode
import com.example.outputs.ui.components.IdentityBadge
import com.example.outputs.ui.components.WaveformPlayer
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurpleAccent
import com.example.ui.theme.BrandRed

enum class CommentFilter {
    ALL,
    SAVED,
    THEORIES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    post: PostItem,
    comments: List<PostComment>,
    onBack: () -> Unit,
    onInvestigateClick: (PostItem) -> Unit,
    onReadModeClick: (PostItem) -> Unit,
    onLikeClick: (PostItem) -> Unit,
    onSaveClick: (PostItem) -> Unit,
    onAddComment: (postId: String, text: String, isTheory: Boolean, identityMode: PrivacyMode?) -> Unit = { _, _, _, _ -> },
    onReplyComment: (parentCommentId: String, postId: String, replyToAuthor: String, text: String, identityMode: PrivacyMode?) -> Unit = { _, _, _, _, _ -> },
    onEditComment: (commentId: String, newText: String) -> Unit = { _, _ -> },
    onSaveComment: (commentId: String, currentSaved: Boolean) -> Unit = { _, _ -> },
    onLikeComment: (commentId: String) -> Unit = {},
    onMarkAcceptedAnswer: (commentId: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var commentText by remember { mutableStateOf("") }
    var isSubmittingAsTheory by remember { mutableStateOf(false) }
    var selectedIdentityMode by remember { mutableStateOf(PrivacyMode.PSEUDONYM) }
    var replyingToComment by remember { mutableStateOf<PostComment?>(null) }
    var editingCommentDialog by remember { mutableStateOf<PostComment?>(null) }
    var selectedCommentFilter by remember { mutableStateOf(CommentFilter.ALL) }

    // Dialog for editing comment
    if (editingCommentDialog != null) {
        var editText by remember(editingCommentDialog) { mutableStateOf(editingCommentDialog!!.text) }
        AlertDialog(
            onDismissRequest = { editingCommentDialog = null },
            title = {
                Text(
                    text = "Edit Comment",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Author: ${editingCommentDialog!!.author}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editText,
                        onValueChange = { editText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_comment_input"),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 5,
                        placeholder = { Text("Update comment text...") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editText.isNotBlank()) {
                            onEditComment(editingCommentDialog!!.id, editText)
                            Toast.makeText(context, "Comment updated", Toast.LENGTH_SHORT).show()
                            editingCommentDialog = null
                        }
                    },
                    modifier = Modifier.testTag("save_edit_comment_confirm")
                ) {
                    Text("Save", fontWeight = FontWeight.Bold, color = BrandCyan)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingCommentDialog = null }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Filter comments based on selected filter
    val filteredComments = when (selectedCommentFilter) {
        CommentFilter.ALL -> comments
        CommentFilter.SAVED -> comments.filter { it.isSaved || it.replies.any { r -> r.isSaved } }
        CommentFilter.THEORIES -> comments.filter { it.isTheory }
    }

    val totalCommentsCount = comments.size + comments.sumOf { it.replies.size }
    val savedCommentsCount = comments.count { it.isSaved } + comments.sumOf { it.replies.count { r -> r.isSaved } }
    val theoriesCount = comments.count { it.isTheory }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = post.category,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("post_detail_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    IconButton(onClick = { onReadModeClick(post) }) {
                        Icon(Icons.Default.MenuBook, contentDescription = "Distraction-free Read Mode", tint = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(onClick = { onSaveClick(post) }) {
                        Icon(
                            imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (post.isSaved) BrandAmber else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            // Reply / Comment Input Bar
            SurfaceReplyBar(
                text = commentText,
                onTextChange = { commentText = it },
                isTheory = isSubmittingAsTheory,
                onToggleTheory = { isSubmittingAsTheory = !isSubmittingAsTheory },
                replyingTo = replyingToComment,
                onCancelReply = { replyingToComment = null },
                selectedIdentityMode = selectedIdentityMode,
                onSelectIdentityMode = { selectedIdentityMode = it },
                onSend = {
                    if (commentText.isNotBlank()) {
                        val replyTarget = replyingToComment
                        if (replyTarget != null) {
                            onReplyComment(
                                replyTarget.id,
                                post.id,
                                replyTarget.author,
                                commentText,
                                selectedIdentityMode
                            )
                            Toast.makeText(context, "Reply posted", Toast.LENGTH_SHORT).show()
                            replyingToComment = null
                        } else {
                            onAddComment(post.id, commentText, isSubmittingAsTheory, selectedIdentityMode)
                            Toast.makeText(context, "Comment posted", Toast.LENGTH_SHORT).show()
                            isSubmittingAsTheory = false
                        }
                        commentText = ""
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            // Header Identity & Category
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IdentityBadge(
                        identityMode = post.identityMode,
                        authorName = post.authorName,
                        anonymousNumberCode = post.anonymousNumberCode
                    )
                    Text(
                        text = "${post.readTimeMinutes} min read • ${post.timestamp}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Cover Image
            if (post.coverImageRes.isNotBlank()) {
                item {
                    val imgId = context.resources.getIdentifier(post.coverImageRes, "drawable", context.packageName)
                    if (imgId != 0) {
                        Image(
                            painter = painterResource(id = imgId),
                            contentDescription = "Post Cover",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Title
            item {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Voice audio player if present
            if (post.voiceAudioDurationSeconds > 0) {
                item {
                    WaveformPlayer(
                        durationSeconds = post.voiceAudioDurationSeconds,
                        transcript = post.voiceTranscript,
                        title = "🎙️ Voice Output / Audio Recording"
                    )
                }
            }

            // Full Content Text
            item {
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Investigation Room button for Mystery or Incident
            if (post.type == PostType.MYSTERY || post.type == PostType.REAL_INCIDENT || post.type == PostType.INVESTIGATION) {
                item {
                    FilledTonalButton(
                        onClick = { onInvestigateClick(post) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("open_case_room_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = BrandPurpleAccent.copy(alpha = 0.2f),
                            contentColor = BrandPurpleAccent
                        )
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Open Investigation Case Room (${post.evidenceCount} Evidences, ${post.theoriesCount} Theories)",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Engagement stats
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onLikeClick(post) }
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) BrandRed else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${post.likesCount} likes",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "$totalCommentsCount responses",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Comments Section Header & Filter Tabs
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "COMMUNITY RESPONSES & REPLIES",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = BrandCyan
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                selected = selectedCommentFilter == CommentFilter.ALL,
                                onClick = { selectedCommentFilter = CommentFilter.ALL },
                                label = { Text("All ($totalCommentsCount)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandCyan.copy(alpha = 0.2f),
                                    selectedLabelColor = BrandCyan
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedCommentFilter == CommentFilter.SAVED,
                                onClick = { selectedCommentFilter = CommentFilter.SAVED },
                                label = { Text("Saved ($savedCommentsCount)") },
                                leadingIcon = {
                                    Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(14.dp))
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandAmber.copy(alpha = 0.2f),
                                    selectedLabelColor = BrandAmber
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedCommentFilter == CommentFilter.THEORIES,
                                onClick = { selectedCommentFilter = CommentFilter.THEORIES },
                                label = { Text("Theories ($theoriesCount)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandPurpleAccent.copy(alpha = 0.2f),
                                    selectedLabelColor = BrandPurpleAccent
                                )
                            )
                        }
                    }
                }
            }

            // Comments List
            if (filteredComments.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (selectedCommentFilter == CommentFilter.SAVED) "No saved comments yet. Bookmark any comment to review later."
                                else "No responses yet. Share your thoughts, theory, or clue below!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredComments, key = { it.id }) { comment ->
                    CommentItemView(
                        comment = comment,
                        onLike = { onLikeComment(comment.id) },
                        onReply = {
                            replyingToComment = comment
                        },
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(comment.text))
                            Toast.makeText(context, "Comment copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        onEdit = {
                            editingCommentDialog = comment
                        },
                        onSave = {
                            onSaveComment(comment.id, comment.isSaved)
                            val status = if (!comment.isSaved) "saved" else "unsaved"
                            Toast.makeText(context, "Comment $status", Toast.LENGTH_SHORT).show()
                        },
                        onAccept = { onMarkAcceptedAnswer(comment.id) },
                        onReplyToNested = { nestedComment ->
                            replyingToComment = nestedComment
                        },
                        onCopyNested = { nestedComment ->
                            clipboardManager.setText(AnnotatedString(nestedComment.text))
                            Toast.makeText(context, "Reply copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        onEditNested = { nestedComment ->
                            editingCommentDialog = nestedComment
                        },
                        onSaveNested = { nestedComment ->
                            onSaveComment(nestedComment.id, nestedComment.isSaved)
                            val status = if (!nestedComment.isSaved) "saved" else "unsaved"
                            Toast.makeText(context, "Reply $status", Toast.LENGTH_SHORT).show()
                        },
                        onLikeNested = { nestedComment ->
                            onLikeComment(nestedComment.id)
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun CommentItemView(
    comment: PostComment,
    onLike: () -> Unit,
    onReply: () -> Unit,
    onCopy: () -> Unit,
    onEdit: () -> Unit,
    onSave: () -> Unit,
    onAccept: () -> Unit,
    onReplyToNested: (PostComment) -> Unit,
    onCopyNested: (PostComment) -> Unit,
    onEditNested: (PostComment) -> Unit,
    onSaveNested: (PostComment) -> Unit,
    onLikeNested: (PostComment) -> Unit,
    modifier: Modifier = Modifier
) {
    var showReplies by remember { mutableStateOf(true) }

    Column(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    1.dp,
                    if (comment.isAcceptedAnswer) BrandGreen
                    else if (comment.isSaved) BrandAmber.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(12.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (comment.isAcceptedAnswer) BrandGreen.copy(alpha = 0.08f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Header: Identity, badges, timestamp, and edit indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IdentityBadge(
                        identityMode = comment.identityMode,
                        authorName = comment.author
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (comment.isEdited) {
                            Text(
                                text = "edited",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }

                        if (comment.isAcceptedAnswer) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(BrandGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(11.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Accepted Answer", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold), color = BrandGreen)
                            }
                        } else if (comment.isTheory) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(BrandPurpleAccent.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Theory", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold), color = BrandPurpleAccent)
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = comment.timestamp, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // If this is a reply to another user
                if (comment.replyToAuthor != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "↳ replying to ${comment.replyToAuthor}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = BrandCyan
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Comment text
                Text(
                    text = comment.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Action Bar: Like, Reply, Copy, Edit, Save, Solution
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Like Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onLike() }
                                .testTag("like_comment_${comment.id}")
                        ) {
                            Icon(
                                imageVector = if (comment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like response",
                                tint = if (comment.isLiked) BrandRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${comment.likes}",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (comment.isLiked) BrandRed else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Reply Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onReply() }
                                .testTag("reply_comment_${comment.id}")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Reply,
                                contentDescription = "Reply to comment",
                                tint = BrandCyan,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Reply",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = BrandCyan
                            )
                        }

                        // Copy Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onCopy() }
                                .testTag("copy_comment_${comment.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy comment text",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Copy",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Edit Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onEdit() }
                                .testTag("edit_comment_${comment.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit comment",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Edit",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Save / Bookmark Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onSave() }
                                .testTag("save_comment_${comment.id}")
                        ) {
                            Icon(
                                imageVector = if (comment.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save comment",
                                tint = if (comment.isSaved) BrandAmber else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (comment.isSaved) "Saved" else "Save",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (comment.isSaved) BrandAmber else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Solution Toggle
                    if (!comment.isAcceptedAnswer) {
                        Text(
                            text = "Mark Solution",
                            style = MaterialTheme.typography.labelSmall.copy(color = BrandGreen, fontWeight = FontWeight.SemiBold),
                            modifier = Modifier
                                .clickable { onAccept() }
                                .testTag("accept_comment_${comment.id}")
                        )
                    }
                }
            }
        }

        // Nested Threaded Replies
        if (comment.replies.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))

            // Expand/Collapse replies toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { showReplies = !showReplies }
            ) {
                Icon(
                    imageVector = if (showReplies) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = BrandCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (showReplies) "Hide ${comment.replies.size} replies" else "View ${comment.replies.size} replies",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = BrandCyan
                )
            }

            AnimatedVisibility(visible = showReplies, enter = fadeIn(), exit = fadeOut()) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 6.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    comment.replies.forEach { reply ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Thread guide line
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .background(BrandCyan.copy(alpha = 0.3f))
                                    .padding(vertical = 4.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            // Reply card
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, if (reply.isSaved) BrandAmber.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(10.dp)),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        IdentityBadge(
                                            identityMode = reply.identityMode,
                                            authorName = reply.author
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (reply.isEdited) {
                                                Text(
                                                    text = "edited",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                                    modifier = Modifier.padding(end = 4.dp)
                                                )
                                            }
                                            Text(
                                                text = reply.timestamp,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    if (reply.replyToAuthor != null) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "↳ replying to ${reply.replyToAuthor}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                                            color = BrandCyan
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = reply.text,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Reply action row: Like, Reply, Copy, Edit, Save
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable { onLikeNested(reply) }
                                        ) {
                                            Icon(
                                                imageVector = if (reply.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                contentDescription = "Like reply",
                                                tint = if (reply.isLiked) BrandRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "${reply.likes}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = if (reply.isLiked) BrandRed else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable { onReplyToNested(reply) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.Reply,
                                                contentDescription = "Reply",
                                                tint = BrandCyan,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "Reply",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Medium),
                                                color = BrandCyan
                                            )
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable { onCopyNested(reply) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Copy reply",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "Copy",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable { onEditNested(reply) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit reply",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "Edit",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable { onSaveNested(reply) }
                                        ) {
                                            Icon(
                                                imageVector = if (reply.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                                contentDescription = "Save reply",
                                                tint = if (reply.isSaved) BrandAmber else MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = if (reply.isSaved) "Saved" else "Save",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = if (reply.isSaved) BrandAmber else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SurfaceReplyBar(
    text: String,
    onTextChange: (String) -> Unit,
    isTheory: Boolean,
    onToggleTheory: () -> Unit,
    replyingTo: PostComment?,
    onCancelReply: () -> Unit,
    selectedIdentityMode: PrivacyMode,
    onSelectIdentityMode: (PrivacyMode) -> Unit,
    onSend: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
            .padding(10.dp)
    ) {
        // Active Reply Banner
        if (replyingTo != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(BrandCyan.copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Reply,
                        contentDescription = null,
                        tint = BrandCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Replying to ${replyingTo.author}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = BrandCyan
                    )
                }
                IconButton(
                    onClick = onCancelReply,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel reply",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Post Mode & Identity Selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isTheory) BrandPurpleAccent else MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onToggleTheory() }
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (isTheory) "🧩 Posting as Theory" else "💬 Comment Mode",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isTheory) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            // Quick Privacy Identity Selector
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "As: ${selectedIdentityMode.name.take(7)}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(BrandCyan.copy(alpha = 0.15f))
                        .clickable {
                            val nextMode = when (selectedIdentityMode) {
                                PrivacyMode.PSEUDONYM -> PrivacyMode.ANONYMOUS_NUMBER
                                PrivacyMode.ANONYMOUS_NUMBER -> PrivacyMode.ANONYMOUS_USERNAME
                                PrivacyMode.ANONYMOUS_USERNAME -> PrivacyMode.COMPLETELY_ANONYMOUS
                                PrivacyMode.COMPLETELY_ANONYMOUS -> PrivacyMode.REAL_PROFILE
                                PrivacyMode.REAL_PROFILE -> PrivacyMode.PSEUDONYM
                            }
                            onSelectIdentityMode(nextMode)
                        }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Switch",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        color = BrandCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Text input field and send button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        if (replyingTo != null) "Write your reply to ${replyingTo.author}..."
                        else if (isTheory) "Propose your theory or hypothesis..."
                        else "Write your response..."
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("comment_input_field"),
                maxLines = 3,
                shape = RoundedCornerShape(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSend,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isTheory) BrandPurpleAccent else BrandCyan)
                    .testTag("send_comment_button")
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
