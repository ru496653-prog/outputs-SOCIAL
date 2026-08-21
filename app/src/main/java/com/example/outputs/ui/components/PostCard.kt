package com.example.outputs.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.GenreTheme
import com.example.outputs.data.model.MysteryStatus
import com.example.outputs.data.model.PostItem
import com.example.outputs.data.model.PostType
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurpleAccent
import com.example.ui.theme.BrandRed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostCard(
    post: PostItem,
    onPostClick: (PostItem) -> Unit,
    onInvestigateClick: (PostItem) -> Unit,
    onReadModeClick: (PostItem) -> Unit,
    onLikeClick: (PostItem) -> Unit,
    onSaveClick: (PostItem) -> Unit,
    onShareClick: (PostItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isSensitiveRevealed by remember { mutableStateOf(!post.isSensitive) }

    // Genre styling accent
    val genreColor = Color(post.genreTheme.primaryColorHex)
    val cardBorder = when (post.genreTheme) {
        GenreTheme.MYSTERY -> BrandPurpleAccent.copy(alpha = 0.35f)
        GenreTheme.HORROR -> BrandRed.copy(alpha = 0.35f)
        GenreTheme.CYBER -> BrandCyan.copy(alpha = 0.4f)
        GenreTheme.ARCHIVE -> BrandAmber.copy(alpha = 0.35f)
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, cardBorder, RoundedCornerShape(18.dp))
            .clickable { onPostClick(post) }
            .testTag("post_card_${post.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Category, Genre badge, Identity Badge, Read time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Type & Category Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(genreColor.copy(alpha = 0.18f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${post.type.icon} ${post.type.label.uppercase()}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            ),
                            color = genreColor
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "• ${post.category}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${post.readTimeMinutes} min read",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Author Identity Row
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (post.verifiedBadge) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(BrandGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified facts",
                                tint = BrandGreen,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Evidence Verified",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = BrandGreen
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Text(
                        text = post.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mystery Status Banner (if Mystery or Incident)
            if (post.type == PostType.MYSTERY || post.type == PostType.REAL_INCIDENT || post.type == PostType.INVESTIGATION) {
                val statusColor = Color(post.mysteryStatus.colorHex)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.12f))
                        .border(1.dp, statusColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "STATUS: ${post.mysteryStatus.label.uppercase()}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = statusColor
                        )
                    }

                    Text(
                        text = "${post.evidenceCount} Evidence • ${post.theoriesCount} Theories",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Cover Image Artwork (if present)
            if (post.coverImageRes.isNotBlank()) {
                val imageResId = context.resources.getIdentifier(
                    post.coverImageRes,
                    "drawable",
                    context.packageName
                )
                if (imageResId != 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = "Cover Image for ${post.title}",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        // Gradient shade
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                    )
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Post Title
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Post Content Preview with Sensitive Filter
            if (post.isSensitive && !isSensitiveRevealed) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(1.dp, BrandRed.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                        .clickable { isSensitiveRevealed = true }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Sensitive content",
                            tint = BrandRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Sensitive Incident Report",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = BrandRed
                            )
                            Text(
                                text = post.sensitiveWarning.ifBlank { "Tap to reveal potentially distressing details." },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Voice audio preview if attached
            if (post.voiceAudioDurationSeconds > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                WaveformPlayer(
                    durationSeconds = post.voiceAudioDurationSeconds,
                    transcript = post.voiceTranscript,
                    title = "🎙️ Voice Audio & Telemetry Recording"
                )
            }

            // Known vs Unknown Facts summary box
            if (post.knownFacts.isNotEmpty() || post.unknownFacts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                        .padding(10.dp)
                ) {
                    if (post.knownFacts.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "✓ WHAT WE KNOW:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = BrandGreen
                            )
                        }
                        Text(
                            text = "• ${post.knownFacts.first()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (post.unknownFacts.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "? UNRESOLVED FACTOR:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = BrandAmber
                            )
                        }
                        Text(
                            text = "• ${post.unknownFacts.first()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Reading Progress Indicator (if started)
            if (post.readingProgress > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${post.readingProgress}% read",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = BrandCyan
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                LinearProgressIndicator(
                    progress = { post.readingProgress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = BrandCyan,
                    trackColor = MaterialTheme.colorScheme.surface
                )
            }

            // Tags Row
            if (post.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    post.tags.take(3).forEach { tag ->
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = BrandPurpleAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Footer & Investigate Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Like, Comment, Save
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Like
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onLikeClick(post) }
                            .padding(4.dp)
                            .testTag("like_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) BrandRed else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.likesCount}",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (post.isLiked) BrandRed else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Comment
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onPostClick(post) }
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Comments",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.commentsCount}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Save
                    IconButton(
                        onClick = { onSaveClick(post) },
                        modifier = Modifier.size(28.dp).testTag("save_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (post.isSaved) BrandAmber else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Action Buttons: Investigate & Read Mode
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Read Mode button
                    IconButton(
                        onClick = { onReadModeClick(post) },
                        modifier = Modifier.size(34.dp).testTag("read_mode_btn_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Reading Mode",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Primary Investigate / Discuss button
                    if (post.type == PostType.MYSTERY || post.type == PostType.REAL_INCIDENT || post.type == PostType.INVESTIGATION) {
                        FilledTonalButton(
                            onClick = { onInvestigateClick(post) },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = BrandPurpleAccent.copy(alpha = 0.2f),
                                contentColor = BrandPurpleAccent
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(34.dp).testTag("investigate_button_${post.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Investigate",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}
