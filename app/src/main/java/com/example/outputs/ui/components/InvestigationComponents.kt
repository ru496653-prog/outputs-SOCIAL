package com.example.outputs.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.EvidenceItem
import com.example.outputs.data.model.EvidenceType
import com.example.outputs.data.model.Theory
import com.example.outputs.data.model.TimelineEvent
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurpleAccent
import com.example.ui.theme.BrandRed

@Composable
fun EvidenceCard(
    evidence: EvidenceItem,
    onUpvote: (EvidenceItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BrandCyan.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${evidence.type.icon} ${evidence.type.label}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = BrandCyan
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ID: #${evidence.id.takeLast(4)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (evidence.verifiedByCreator) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(BrandGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified by Case Creator",
                            tint = BrandGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Verified Evidence",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = BrandGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Evidence Title
            Text(
                text = evidence.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Evidence Description
            Text(
                text = evidence.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Optional Image Attachment
            if (evidence.mediaResName.isNotBlank()) {
                val imgId = context.resources.getIdentifier(evidence.mediaResName, "drawable", context.packageName)
                if (imgId != 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = imgId),
                        contentDescription = "Evidence Media",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Confidence Level Meter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "CONFIDENCE LEVEL: ${evidence.confidenceLevel}%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    ),
                    color = if (evidence.confidenceLevel >= 85) BrandGreen else BrandAmber
                )
                Text(
                    text = "Added by ${evidence.contributor} • ${evidence.dateAdded}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { evidence.confidenceLevel / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = if (evidence.confidenceLevel >= 85) BrandGreen else BrandAmber,
                trackColor = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Upvote & Endorse
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IdentityBadge(
                    identityMode = evidence.contributorIdentity,
                    authorName = evidence.contributor
                )

                FilledTonalButton(
                    onClick = { onUpvote(evidence) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = BrandCyan
                    ),
                    modifier = Modifier.height(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Upvote Evidence",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Endorse (${evidence.upvotes})",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun TheoryCard(
    theory: Theory,
    onSupport: (Theory) -> Unit,
    onChallenge: (Theory) -> Unit,
    onAccept: (Theory) -> Unit,
    onDebunk: (Theory) -> Unit,
    isCaseOwner: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (theory.isAccepted) BrandGreen else if (theory.isDebunked) BrandRed.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(14.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (theory.isAccepted) BrandGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Theory Status Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiObjects,
                        contentDescription = "Theory",
                        tint = BrandPurpleAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "THEORY PROPOSAL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = BrandPurpleAccent
                    )
                }

                if (theory.isAccepted) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BrandGreen)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "✓ ACCEPTED EXPLANATION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = Color.Black
                            )
                        )
                    }
                } else if (theory.isDebunked) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BrandRed.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "✕ DEBUNKED BY EVIDENCE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = BrandRed
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = theory.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = theory.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IdentityBadge(
                    identityMode = theory.authorIdentity,
                    authorName = theory.author
                )

                // Support / Challenge Actions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Support
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BrandGreen.copy(alpha = 0.12f))
                            .clickable { onSupport(theory) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Support Theory",
                            tint = BrandGreen,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${theory.supportCount} Support",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = BrandGreen
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Challenge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BrandRed.copy(alpha = 0.12f))
                            .clickable { onChallenge(theory) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "Challenge Theory",
                            tint = BrandRed,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${theory.challengeCount}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = BrandRed
                        )
                    }
                }
            }

            // Case owner Mark as Accepted / Debunked controls
            if (isCaseOwner && !theory.isAccepted && !theory.isDebunked) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = { onDebunk(theory) },
                        colors = ButtonDefaults.textButtonColors(contentColor = BrandRed)
                    ) {
                        Text("Mark Debunked", style = MaterialTheme.typography.labelSmall)
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    FilledTonalButton(
                        onClick = { onAccept(theory) },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = BrandGreen.copy(alpha = 0.2f),
                            contentColor = BrandGreen
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Accept Solution", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineView(
    timeline: List<TimelineEvent>,
    onAddEvent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "INVESTIGATION TIMELINE",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = BrandCyan
            )

            FilledTonalButton(
                onClick = onAddEvent,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = BrandCyan.copy(alpha = 0.15f),
                    contentColor = BrandCyan
                ),
                modifier = Modifier.height(28.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add timeline event", modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Timestamp", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        timeline.forEachIndexed { index, event ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                // Time Node column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(64.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (event.isVerified) BrandGreen.copy(alpha = 0.2f) else BrandAmber.copy(alpha = 0.2f))
                            .border(1.dp, if (event.isVerified) BrandGreen else BrandAmber, RoundedCornerShape(6.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = event.timeLabel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            ),
                            color = if (event.isVerified) BrandGreen else BrandAmber
                        )
                    }

                    if (index < timeline.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(40.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Event details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (event.isVerified) {
                            Text(
                                text = "VERIFIED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = BrandGreen
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Suggested by ${event.suggestedBy}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// Dialogs
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitEvidenceDialog(
    onDismiss: () -> Unit,
    onSubmit: (title: String, desc: String, type: EvidenceType, confidence: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(EvidenceType.DOCUMENT) }
    var confidence by remember { mutableFloatStateOf(85f) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Submit Evidence to Case", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Evidence Title") },
                    placeholder = { Text("e.g. Spectrum Waterfall Capture") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = "${selectedType.icon} ${selectedType.label}",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Evidence Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        EvidenceType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text("${type.icon} ${type.label}") },
                                onClick = {
                                    selectedType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & Methodology") },
                    placeholder = { Text("How was this collected? Equipment used?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Confidence Level: ${confidence.toInt()}%",
                    style = MaterialTheme.typography.labelMedium
                )
                Slider(
                    value = confidence,
                    onValueChange = { confidence = it },
                    valueRange = 10f..100f,
                    steps = 9
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSubmit(title, description, selectedType, confidence.toInt())
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandCyan, contentColor = Color.Black)
            ) {
                Text("Submit Evidence", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ProposeTheoryDialog(
    onDismiss: () -> Unit,
    onSubmit: (title: String, content: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Propose Investigation Theory", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                Text(
                    text = "Theories should be grounded in documented facts and reasonable deduction.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Theory Headline") },
                    placeholder = { Text("e.g. Atmospheric mirage from quarry sodium lamp") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Detailed Explanation & Deduction") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSubmit(title, content)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandPurpleAccent)
            ) {
                Text("Publish Theory", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddTimelineDialog(
    onDismiss: () -> Unit,
    onSubmit: (time: String, title: String, desc: String) -> Unit
) {
    var time by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Timeline Event", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column {
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Timestamp / Time Label") },
                    placeholder = { Text("e.g. 10:45 PM or Day 3") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Event Headline") },
                    placeholder = { Text("e.g. Signal Modulation Detected") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Event Details") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (time.isNotBlank() && title.isNotBlank()) {
                        onSubmit(time, title, desc)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandCyan, contentColor = Color.Black)
            ) {
                Text("Add Node", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
