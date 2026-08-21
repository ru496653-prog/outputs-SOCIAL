package com.example.outputs.ui.screens.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.GenreTheme
import com.example.outputs.data.model.PostType
import com.example.outputs.data.model.PrivacyMode
import com.example.outputs.ui.components.IdentityBadge
import com.example.outputs.ui.components.WaveformPlayer
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurpleAccent
import com.example.ui.theme.BrandRed
import com.example.ui.theme.BrandSage
import com.example.ui.theme.BrandTerracotta
import com.example.ui.theme.BrandWarmAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOutputScreen(
    defaultPrivacyMode: PrivacyMode,
    onBack: () -> Unit,
    onPublish: (
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
        voiceDurationSeconds: Int,
        voiceTranscript: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(PostType.MYSTERY) }
    var category by remember { mutableStateOf("Unexplained Phenomenon") }
    var selectedPrivacyMode by remember { mutableStateOf(defaultPrivacyMode) }
    var selectedGenre by remember { mutableStateOf(GenreTheme.MYSTERY) }

    // Mystery & Case Specific fields
    var knownFactInput by remember { mutableStateOf("") }
    var knownFactsList by remember { mutableStateOf(listOf<String>()) }

    var unknownFactInput by remember { mutableStateOf("") }
    var unknownFactsList by remember { mutableStateOf(listOf<String>()) }

    var questionToCommunity by remember { mutableStateOf("") }

    // Voice simulation
    var isRecordingVoice by remember { mutableStateOf(false) }
    var recordedDurationSeconds by remember { mutableIntStateOf(0) }
    var voiceTranscript by remember { mutableStateOf("") }

    // Sensitive Warning
    var isSensitive by remember { mutableStateOf(false) }
    var sensitiveWarningText by remember { mutableStateOf("Contains distressing or unsolved real incident details.") }

    var tagsText by remember { mutableStateOf("#Investigation #Outputs") }
    var showAnonymousConfirmDialog by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val initiatePublish = {
        if (title.isBlank()) {
            validationError = "Please enter a title or case headline before publishing."
        } else if (content.isBlank()) {
            validationError = "Please write your story content before publishing."
        } else {
            validationError = null
            showAnonymousConfirmDialog = true
        }
    }

    val commitPublish = {
        val tagList = tagsText.split(" ", ",").map { it.trim() }.filter { it.isNotBlank() }
        onPublish(
            title,
            content,
            selectedType,
            category,
            selectedPrivacyMode,
            selectedGenre,
            knownFactsList,
            unknownFactsList,
            questionToCommunity,
            tagList,
            isSensitive,
            sensitiveWarningText,
            recordedDurationSeconds,
            voiceTranscript
        )
        showAnonymousConfirmDialog = false
        onBack()
    }

    // Custom Anonymous Story Confirmation Dialog
    if (showAnonymousConfirmDialog) {
        AnonymousStoryConfirmationDialog(
            title = title,
            type = selectedType,
            category = category,
            privacyMode = selectedPrivacyMode,
            genreTheme = selectedGenre,
            isSensitive = isSensitive,
            onConfirm = commitPublish,
            onDismiss = { showAnonymousConfirmDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create New Output",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("create_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = initiatePublish,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = BrandTerracotta,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("publish_output_btn")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Publish", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Validation Error Alert if any
            if (validationError != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = BrandRed.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = BrandRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = validationError ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = BrandRed
                        )
                    }
                }
            }
            // 1. Output Format Selector
            Text(
                text = "1. SELECT OUTPUT FORMAT",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = BrandCyan
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PostType.values().forEach { type ->
                    val isSelected = selectedType == type
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) BrandPurpleAccent else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedType = type }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "${type.icon} ${type.label}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // 2. Identity Mode Selector
            Text(
                text = "2. AUTHOR IDENTITY LEVEL",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = BrandCyan
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PrivacyMode.values().forEach { mode ->
                    val isSelected = selectedPrivacyMode == mode
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) BrandCyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, if (isSelected) BrandCyan else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                            .clickable { selectedPrivacyMode = mode }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = mode.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) BrandCyan else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }

            // 3. Title & Category
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Output Title / Case Headline") },
                placeholder = { Text("e.g. The Hum of Black Ridge: Frequency Anomaly") },
                modifier = Modifier.fillMaxWidth().testTag("output_title_input"),
                singleLine = true
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category / Domain") },
                placeholder = { Text("e.g. Unexplained Phenomenon, Acoustics, Crime, Story") },
                modifier = Modifier.fillMaxWidth()
            )

            // 4. Atmosphere / Genre Theme
            Text(
                text = "3. ATMOSPHERE / GENRE STYLING",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = BrandCyan
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GenreTheme.values().forEach { genre ->
                    val isSelected = selectedGenre == genre
                    val genreColor = Color(genre.primaryColorHex)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) genreColor else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedGenre = genre }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = genre.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else genreColor
                            )
                        )
                    }
                }
            }

            // 5. Main Content / Story Narrative
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Narrative, Case Report, or Output Content") },
                placeholder = { Text("Write your story, detail the observations, or share your opinion here...") },
                modifier = Modifier.fillMaxWidth().testTag("output_content_input"),
                minLines = 6
            )

            // 6. Voice Audio Recording Tool (Section 52 & 53)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.GraphicEq, contentDescription = null, tint = BrandCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "VOICE AUDIO LOG & TELEMETRY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = BrandCyan
                            )
                        }

                        if (recordedDurationSeconds > 0) {
                            Text(
                                text = "${recordedDurationSeconds}s Attached",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = BrandGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Attach an anonymous voice recording, dispatch log, or field telemetry.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!isRecordingVoice && recordedDurationSeconds == 0) {
                        Button(
                            onClick = {
                                isRecordingVoice = true
                                recordedDurationSeconds = 48
                                voiceTranscript = "Telemetry recorded live at 03:00. Audio frequency sweeps detected."
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPurpleAccent)
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Record Anonymous Audio")
                        }
                    } else if (isRecordingVoice) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(BrandRed)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Recording in progress (simulated audio capture)...", style = MaterialTheme.typography.labelSmall, color = BrandRed)
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = { isRecordingVoice = false },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandRed)
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Done")
                            }
                        }
                    } else {
                        WaveformPlayer(
                            durationSeconds = recordedDurationSeconds,
                            transcript = voiceTranscript,
                            title = "Attached Audio Note"
                        )
                    }
                }
            }

            // 7. Case-Specific Known / Unknown Facts & Community Question
            if (selectedType == PostType.MYSTERY || selectedType == PostType.REAL_INCIDENT || selectedType == PostType.INVESTIGATION) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "INVESTIGATION DOSSIER CREATOR",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = BrandAmber
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Known Facts
                        Text("Known Verified Facts:", style = MaterialTheme.typography.labelSmall, color = BrandGreen)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = knownFactInput,
                                onValueChange = { knownFactInput = it },
                                placeholder = { Text("Add known fact...") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = {
                                if (knownFactInput.isNotBlank()) {
                                    knownFactsList = knownFactsList + knownFactInput
                                    knownFactInput = ""
                                }
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Fact", tint = BrandGreen)
                            }
                        }
                        knownFactsList.forEach { fact ->
                            Text(text = "✓ $fact", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Unknown Anomalies
                        Text("Unknown Factors / Anomalies:", style = MaterialTheme.typography.labelSmall, color = BrandAmber)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = unknownFactInput,
                                onValueChange = { unknownFactInput = it },
                                placeholder = { Text("Add unexplained anomaly...") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = {
                                if (unknownFactInput.isNotBlank()) {
                                    unknownFactsList = unknownFactsList + unknownFactInput
                                    unknownFactInput = ""
                                }
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Anomaly", tint = BrandAmber)
                            }
                        }
                        unknownFactsList.forEach { unk ->
                            Text(text = "? $unk", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Question to Community
                        OutlinedTextField(
                            value = questionToCommunity,
                            onValueChange = { questionToCommunity = it },
                            label = { Text("Direct Question to Community") },
                            placeholder = { Text("e.g. Can anyone decode the harmonic modulation in this log?") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // 8. Sensitive Content Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(14.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mark as Sensitive Content",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Applies content blur & trigger warning shield",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isSensitive,
                    onCheckedChange = { isSensitive = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = BrandRed)
                )
            }

            // 9. Tags
            OutlinedTextField(
                value = tagsText,
                onValueChange = { tagsText = it },
                label = { Text("Tags & Keywords") },
                placeholder = { Text("#Mystery #Acoustics #Investigation") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Bottom Action Button
            Button(
                onClick = initiatePublish,
                colors = ButtonDefaults.buttonColors(containerColor = BrandTerracotta),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("bottom_publish_story_btn")
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedPrivacyMode == PrivacyMode.REAL_PROFILE) "Publish Output" else "Post Anonymous Story",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AnonymousStoryConfirmationDialog(
    title: String,
    type: PostType,
    category: String,
    privacyMode: PrivacyMode,
    genreTheme: GenreTheme,
    isSensitive: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val isAnonymous = privacyMode != PrivacyMode.REAL_PROFILE

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(BrandTerracotta.copy(alpha = 0.15f))
                    .border(1.5.dp, BrandTerracotta, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isAnonymous) Icons.Default.VisibilityOff else Icons.Default.Security,
                    contentDescription = "Confirmation Shield",
                    tint = BrandTerracotta,
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        title = {
            Text(
                text = if (isAnonymous) "Confirm Anonymous Story" else "Confirm Story Publication",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = if (isAnonymous) {
                        "You are about to commit an anonymous story to the platform repository. Please verify your anonymity protocols before publishing."
                    } else {
                        "You are about to publish this story to the platform repository. Please review your publication details before committing."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Story & Privacy Snapshot Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Title preview
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Format and category badges
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${type.icon} ${type.label}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = BrandWarmAmber
                            )
                            Text("•", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), thickness = 0.8.dp)

                        // Identity protocol
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "IDENTITY PROTOCOL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    fontSize = 9.sp
                                ),
                                color = BrandSage
                            )
                            IdentityBadge(
                                identityMode = privacyMode,
                                authorName = when (privacyMode) {
                                    PrivacyMode.PSEUDONYM -> "Pen Name Author"
                                    PrivacyMode.ANONYMOUS_USERNAME -> "Anonymous"
                                    PrivacyMode.ANONYMOUS_NUMBER -> "Anonymous #4827"
                                    PrivacyMode.COMPLETELY_ANONYMOUS -> "Anonymous"
                                    PrivacyMode.REAL_PROFILE -> "My Profile"
                                }
                            )
                            Text(
                                text = privacyMode.description,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Anonymity & Safeguards Checklist
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text("🛡️ ", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = if (isAnonymous) "Real name, email, and bio will remain completely hidden." else "Author profile will be visible on the public feed.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(verticalAlignment = Alignment.Top) {
                        Text("🔒 ", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "EXIF, location metadata, and device identifiers are stripped.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (isSensitive) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text("⚠️ ", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = "Sensitive trigger warning & blur filter applied.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = BrandRed
                            )
                        }
                    }
                }

                Text(
                    text = "Are you ready to commit this story to the Outputs platform?",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = BrandTerracotta),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_publish_dialog_btn")
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Confirm & Commit", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("cancel_publish_dialog_btn")
            ) {
                Text("Review & Edit")
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("anonymous_story_confirm_dialog")
    )
}

