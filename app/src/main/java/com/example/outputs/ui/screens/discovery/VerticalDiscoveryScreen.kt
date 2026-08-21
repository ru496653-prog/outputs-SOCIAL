package com.example.outputs.ui.screens.discovery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.outputs.data.model.PostItem
import com.example.outputs.data.model.PostType
import com.example.outputs.ui.components.IdentityBadge
import com.example.outputs.ui.components.WaveformPlayer
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurpleAccent
import com.example.ui.theme.BrandRed

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalDiscoveryScreen(
    posts: List<PostItem>,
    onInvestigateClick: (PostItem) -> Unit,
    onReadModeClick: (PostItem) -> Unit,
    onLikeClick: (PostItem) -> Unit,
    onSaveClick: (PostItem) -> Unit,
    onShareClick: (PostItem) -> Unit,
    onCommentClick: (PostItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { posts.size })

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (posts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No discovery outputs available.", color = Color.White)
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val post = posts[page]
                var isSensitiveRevealed by remember { mutableStateOf(!post.isSensitive) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0C0E14))
                ) {
                    // Full background cover image with gradient overlay
                    if (post.coverImageRes.isNotBlank()) {
                        val imgId = context.resources.getIdentifier(post.coverImageRes, "drawable", context.packageName)
                        if (imgId != 0) {
                            Image(
                                painter = painterResource(id = imgId),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Darkness Gradient Veil
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.5f),
                                        Color.Black.copy(alpha = 0.7f),
                                        Color.Black.copy(alpha = 0.95f)
                                    )
                                )
                            )
                    )

                    // Top Status / Type Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, start = 16.dp, end = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(BrandPurpleAccent)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${post.type.icon} ${post.type.label.uppercase()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = Color.White
                            )
                        }

                        Text(
                            text = "${post.readTimeMinutes} min read",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    // Content overlay at the bottom & Left
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, end = 80.dp, bottom = 84.dp)
                    ) {
                        IdentityBadge(
                            identityMode = post.identityMode,
                            authorName = post.authorName,
                            anonymousNumberCode = post.anonymousNumberCode
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (post.isSensitive && !isSensitiveRevealed) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandRed.copy(alpha = 0.3f))
                                    .clickable { isSensitiveRevealed = true }
                                    .padding(8.dp)
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = BrandRed, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Sensitive Content • Tap to reveal",
                                    style = MaterialTheme.typography.labelSmall.copy(color = BrandRed, fontWeight = FontWeight.Bold)
                                )
                            }
                        } else {
                            Text(
                                text = post.content,
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFD4D8E2)),
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Voice clip player preview
                        if (post.voiceAudioDurationSeconds > 0) {
                            Spacer(modifier = Modifier.height(10.dp))
                            WaveformPlayer(
                                durationSeconds = post.voiceAudioDurationSeconds,
                                transcript = post.voiceTranscript,
                                title = "🎙️ Voice Audio Stream"
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action Buttons: Read Full Story & Investigate
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(
                                onClick = { onReadModeClick(post) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Color.White.copy(alpha = 0.2f),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Read Mode", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            }

                            if (post.type == PostType.MYSTERY || post.type == PostType.REAL_INCIDENT || post.type == PostType.INVESTIGATION) {
                                FilledTonalButton(
                                    onClick = { onInvestigateClick(post) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = BrandCyan,
                                        contentColor = Color.Black
                                    ),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Investigate", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                }
                            }
                        }
                    }

                    // Floating Vertical Action Bar (Like, Comment, Save, Share) on Right Edge
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 12.dp, bottom = 90.dp)
                    ) {
                        // Like Button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .clickable { onLikeClick(post) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Like",
                                    tint = if (post.isLiked) BrandRed else Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                text = "${post.likesCount}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                            )
                        }

                        // Comment Button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .clickable { onCommentClick(post) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubbleOutline,
                                    contentDescription = "Comments",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Text(
                                text = "${post.commentsCount}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                            )
                        }

                        // Save Button
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickable { onSaveClick(post) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save",
                                tint = if (post.isSaved) BrandAmber else Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Share Button
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickable { onShareClick(post) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
