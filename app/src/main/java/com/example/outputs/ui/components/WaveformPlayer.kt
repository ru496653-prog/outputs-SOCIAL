package com.example.outputs.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandPurpleLight
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun WaveformPlayer(
    durationSeconds: Int,
    transcript: String = "",
    title: String = "Voice Evidence / Audio Log",
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var playbackProgress by remember { mutableFloatStateOf(0f) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var showTranscript by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "wave_anim")
    val wavePulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Simulate progress when playing
    LaunchedEffect(isPlaying, playbackSpeed) {
        if (isPlaying) {
            while (playbackProgress < 1.0f) {
                delay((100 / playbackSpeed).toLong())
                playbackProgress = (playbackProgress + (0.1f / durationSeconds.coerceAtLeast(1))).coerceAtMost(1f)
                if (playbackProgress >= 1.0f) {
                    isPlaying = false
                    playbackProgress = 0f
                    break
                }
            }
        }
    }

    val bars = remember {
        val random = Random(42)
        List(28) { random.nextFloat().coerceIn(0.2f, 0.95f) }
    }

    val formattedCurrentTime = formatSeconds((playbackProgress * durationSeconds).toInt())
    val formattedTotalTime = formatSeconds(durationSeconds)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = "Audio note",
                    tint = BrandCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (transcript.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showTranscript = !showTranscript }
                        .background(if (showTranscript) BrandCyan.copy(alpha = 0.2f) else Color.Transparent)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Subtitles,
                        contentDescription = "Transcript",
                        modifier = Modifier.size(13.dp),
                        tint = if (showTranscript) BrandCyan else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (showTranscript) "Hide Text" else "Transcript",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (showTranscript) BrandCyan else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Play/Pause Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(BrandPurpleLight)
                    .clickable { isPlaying = !isPlaying }
                    .testTag("play_pause_button")
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Waveform Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
                    .clickable {
                        // Quick scrub
                        playbackProgress = (playbackProgress + 0.25f) % 1.0f
                    }
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().height(32.dp)) {
                    val barWidth = 4.dp.toPx()
                    val totalBars = bars.size
                    val spacing = (size.width - (totalBars * barWidth)) / (totalBars - 1).coerceAtLeast(1)

                    bars.forEachIndexed { index, heightFraction ->
                        val x = index * (barWidth + spacing)
                        val animatedFraction = if (isPlaying) {
                            (heightFraction * wavePulse).coerceIn(0.15f, 1f)
                        } else {
                            heightFraction
                        }
                        val barHeight = size.height * animatedFraction
                        val y = (size.height - barHeight) / 2

                        val progressX = size.width * playbackProgress
                        val isPassed = x <= progressX

                        val barColor = when {
                            isPassed -> BrandCyan
                            isPlaying -> BrandAmber.copy(alpha = 0.8f)
                            else -> Color(0xFF6B728E)
                        }

                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Speed cycle button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                    .clickable {
                        playbackSpeed = when (playbackSpeed) {
                            1.0f -> 1.5f
                            1.5f -> 2.0f
                            else -> 1.0f
                        }
                    }
                    .padding(horizontal = 6.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${playbackSpeed}x",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Time indicators
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = formattedCurrentTime,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formattedTotalTime,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Auto-Transcription Display
        AnimatedVisibility(visible = showTranscript) {
            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, BrandCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = "AUTO-TRANSCRIPTION (AI PROCESSED)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 9.sp
                    ),
                    color = BrandCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"$transcript\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

private fun formatSeconds(totalSecs: Int): String {
    val mins = totalSecs / 60
    val secs = totalSecs % 60
    return String.format("%02d:%02d", mins, secs)
}
