package com.example.outputs.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.PostItem
import com.example.outputs.ui.viewmodel.ReadingModeState
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandPurpleLight
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingModeScreen(
    state: ReadingModeState,
    onClose: () -> Unit,
    onFontSizeChange: (Float) -> Unit,
    onThemeChange: (String) -> Unit,
    onToggleAudio: () -> Unit,
    onToggleSave: (PostItem) -> Unit,
    onUpdateProgress: (postId: String, progress: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val post = state.post ?: return
    var showControlsSheet by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Calculate reading progress as user scrolls
    val maxScroll = scrollState.maxValue
    val currentScroll = scrollState.value
    val progressPercent = if (maxScroll > 0) ((currentScroll.toFloat() / maxScroll) * 100).toInt() else 0

    LaunchedEffect(progressPercent) {
        onUpdateProgress(post.id, progressPercent)
    }

    // Reading Themes
    val (bgColor, textColor, accentColor, fontFamily) = when (state.themeMode) {
        "Archive" -> ReadingThemeVisuals(
            Color(0xFF1E1A16), // Sepia / Archive Dark
            Color(0xFFEADBCE),
            BrandAmber,
            FontFamily.Serif
        )
        "Cyber" -> ReadingThemeVisuals(
            Color(0xFF070F18),
            Color(0xFFD8F3DC),
            BrandCyan,
            FontFamily.Monospace
        )
        "Minimal" -> ReadingThemeVisuals(
            Color(0xFF121418),
            Color(0xFFF1F3F5),
            Color(0xFF90E0EF),
            FontFamily.SansSerif
        )
        else -> ReadingThemeVisuals( // Dark Default
            Color(0xFF0A0C10),
            Color(0xFFE6E8EE),
            BrandPurpleLight,
            FontFamily.Default
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = textColor,
                            maxLines = 1
                        )
                        Text(
                            text = "${post.readTimeMinutes} min read • $progressPercent% read",
                            style = MaterialTheme.typography.labelSmall,
                            color = textColor.copy(alpha = 0.6f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onClose, modifier = Modifier.testTag("close_reading_mode")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Exit Reading Mode", tint = textColor)
                    }
                },
                actions = {
                    IconButton(onClick = { onToggleSave(post) }) {
                        Icon(
                            imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (post.isSaved) BrandAmber else textColor
                        )
                    }
                    IconButton(onClick = { showControlsSheet = !showControlsSheet }) {
                        Icon(Icons.Default.Tune, contentDescription = "Reading Settings", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor
                )
            )
        },
        containerColor = bgColor
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(bgColor)
        ) {
            // Reading Progress Bar
            LinearProgressIndicator(
                progress = { progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = accentColor,
                trackColor = bgColor
            )

            // Optional Floating Reading Settings Panel
            AnimatedVisibility(visible = showControlsSheet) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor.copy(alpha = 0.95f))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    // Font Size Adjuster
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Font Size", style = MaterialTheme.typography.labelMedium, color = textColor)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledTonalButton(
                                onClick = { onFontSizeChange(-2f) },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(36.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                            ) {
                                Text("A-", style = MaterialTheme.typography.labelMedium)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${state.fontSizeSp.toInt()}sp", style = MaterialTheme.typography.labelMedium, color = textColor)
                            Spacer(modifier = Modifier.width(8.dp))
                            FilledTonalButton(
                                onClick = { onFontSizeChange(2f) },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(36.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                            ) {
                                Text("A+", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Reading Theme Palette Switcher
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Atmosphere", style = MaterialTheme.typography.labelMedium, color = textColor)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Dark", "Archive", "Cyber", "Minimal").forEach { themeName ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (state.themeMode == themeName) accentColor.copy(alpha = 0.3f) else Color.Transparent)
                                        .clickable { onThemeChange(themeName) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = themeName,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (state.themeMode == themeName) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (state.themeMode == themeName) accentColor else textColor.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Distraction-free Content Body
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Identity & Tagline header
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
                        text = post.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp
                    ),
                    color = textColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Audio Narration bar (if present)
                if (post.voiceAudioDurationSeconds > 0) {
                    WaveformPlayer(
                        durationSeconds = post.voiceAudioDurationSeconds,
                        transcript = post.voiceTranscript,
                        title = "Audio Narrative Playback"
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Main Article Body
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = fontFamily,
                        fontSize = state.fontSizeSp.sp,
                        lineHeight = (state.fontSizeSp * state.lineSpacingMultiplier).sp
                    ),
                    color = textColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Known / Unknown Fact Dossier if present
                if (post.knownFacts.isNotEmpty() || post.unknownFacts.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor.copy(alpha = 0.6f))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "FACT DOSSIER & OPEN QUESTIONS",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = accentColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        post.knownFacts.forEach { fact ->
                            Text(
                                text = "✓ $fact",
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        if (post.questionToCommunity.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "COMMUNITY QUESTION:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = BrandAmber
                            )
                            Text(
                                text = post.questionToCommunity,
                                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                color = textColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "— End of Output —",
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor.copy(alpha = 0.5f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

private data class ReadingThemeVisuals<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
