package com.example.outputs.ui.screens.investigation

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.EvidenceItem
import com.example.outputs.data.model.EvidenceType
import com.example.outputs.data.model.MysteryStatus
import com.example.outputs.data.model.PostItem
import com.example.outputs.data.model.Theory
import com.example.outputs.data.model.TimelineEvent
import com.example.outputs.ui.components.AddTimelineDialog
import com.example.outputs.ui.components.EvidenceCard
import com.example.outputs.ui.components.IdentityBadge
import com.example.outputs.ui.components.ProposeTheoryDialog
import com.example.outputs.ui.components.SubmitEvidenceDialog
import com.example.outputs.ui.components.TheoryCard
import com.example.outputs.ui.components.TimelineView
import com.example.outputs.ui.components.WaveformPlayer
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurpleAccent
import com.example.ui.theme.BrandRed

enum class CaseTab(val label: String, val icon: String) {
    OVERVIEW("Overview", "🗂️"),
    TIMELINE("Timeline", "⏱️"),
    EVIDENCE("Evidence Board", "🔬"),
    THEORIES("Theories", "🧩"),
    COLLABORATORS("Collaborators", "👥")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseInvestigationScreen(
    post: PostItem,
    evidenceList: List<EvidenceItem>,
    theoriesList: List<Theory>,
    timelineList: List<TimelineEvent>,
    onBack: () -> Unit,
    onSubmitEvidence: (title: String, desc: String, type: EvidenceType, confidence: Int) -> Unit,
    onUpvoteEvidence: (EvidenceItem) -> Unit,
    onProposeTheory: (title: String, content: String) -> Unit,
    onSupportTheory: (Theory) -> Unit,
    onChallengeTheory: (Theory) -> Unit,
    onAcceptTheory: (Theory) -> Unit,
    onDebunkTheory: (Theory) -> Unit,
    onAddTimelineEvent: (time: String, title: String, desc: String) -> Unit,
    onOpenReadMode: (PostItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(CaseTab.OVERVIEW) }
    var showSubmitEvidenceDialog by remember { mutableStateOf(false) }
    var showProposeTheoryDialog by remember { mutableStateOf(false) }
    var showAddTimelineDialog by remember { mutableStateOf(false) }

    val statusColor = Color(post.mysteryStatus.colorHex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CASE #${post.id.takeLast(4).uppercase()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                ),
                                color = BrandCyan
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(statusColor.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = post.mysteryStatus.label.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 8.sp
                                    ),
                                    color = statusColor
                                )
                            }
                        }
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("investigation_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    IconButton(onClick = { onOpenReadMode(post) }) {
                        Icon(Icons.Default.MenuBook, contentDescription = "Reading Mode", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Case Navigation Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                edgePadding = 12.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = BrandCyan,
                divider = {}
            ) {
                CaseTab.values().forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = {
                            Text(
                                text = "${tab.icon} ${tab.label}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (selectedTab == tab) BrandCyan else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }

                when (selectedTab) {
                    CaseTab.OVERVIEW -> {
                        item {
                            // Cover Image Banner
                            if (post.coverImageRes.isNotBlank()) {
                                val imgId = context.resources.getIdentifier(post.coverImageRes, "drawable", context.packageName)
                                if (imgId != 0) {
                                    Image(
                                        painter = painterResource(id = imgId),
                                        contentDescription = "Case visual dossier",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.dp)
                                            .clip(RoundedCornerShape(14.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }

                            // Incident Description Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
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
                                            text = "Lead Investigator",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "INCIDENT BRIEF & OBSERVATIONS",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = BrandPurpleAccent
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = post.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Audio Telemetry / Voice Log if present
                        if (post.voiceAudioDurationSeconds > 0) {
                            item {
                                WaveformPlayer(
                                    durationSeconds = post.voiceAudioDurationSeconds,
                                    transcript = post.voiceTranscript,
                                    title = "Incident Audio Log / Demodulated Stream"
                                )
                            }
                        }

                        // Fact Dossier
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "DOCUMENTED FACTS (WHAT WE KNOW)",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = BrandGreen
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    if (post.knownFacts.isEmpty()) {
                                        Text("No verified facts recorded yet.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    } else {
                                        post.knownFacts.forEach { fact ->
                                            Text(text = "• $fact", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                            Spacer(modifier = Modifier.height(4.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text(
                                        text = "UNEXPLAINED ANOMALIES (WHAT WE DON'T KNOW)",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = BrandAmber
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    if (post.unknownFacts.isEmpty()) {
                                        Text("No unknown anomalies cataloged.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    } else {
                                        post.unknownFacts.forEach { anomaly ->
                                            Text(text = "• $anomaly", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                            Spacer(modifier = Modifier.height(4.dp))
                                        }
                                    }

                                    if (post.questionToCommunity.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(14.dp))
                                        Text(
                                            text = "INVESTIGATION CALL TO ACTION:",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            ),
                                            color = BrandCyan
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "\"${post.questionToCommunity}\"",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    CaseTab.TIMELINE -> {
                        item {
                            TimelineView(
                                timeline = timelineList,
                                onAddEvent = { showAddTimelineDialog = true }
                            )
                        }
                    }

                    CaseTab.EVIDENCE -> {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "EVIDENCE DEPOSITORY (${evidenceList.size})",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = BrandCyan
                                )

                                FilledTonalButton(
                                    onClick = { showSubmitEvidenceDialog = true },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = BrandCyan,
                                        contentColor = Color.Black
                                    ),
                                    modifier = Modifier.testTag("submit_evidence_button")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Submit Evidence", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (evidenceList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No evidence submitted yet. Be the first contributor!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        } else {
                            items(evidenceList, key = { it.id }) { ev ->
                                EvidenceCard(
                                    evidence = ev,
                                    onUpvote = onUpvoteEvidence
                                )
                            }
                        }
                    }

                    CaseTab.THEORIES -> {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "COMMUNITY THEORIES (${theoriesList.size})",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = BrandPurpleAccent
                                )

                                FilledTonalButton(
                                    onClick = { showProposeTheoryDialog = true },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = BrandPurpleAccent,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("propose_theory_button")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Propose Theory", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (theoriesList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No theories proposed yet. Post your hypothesis!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        } else {
                            items(theoriesList, key = { it.id }) { th ->
                                TheoryCard(
                                    theory = th,
                                    onSupport = onSupportTheory,
                                    onChallenge = onChallengeTheory,
                                    onAccept = onAcceptTheory,
                                    onDebunk = onDebunkTheory
                                )
                            }
                        }
                    }

                    CaseTab.COLLABORATORS -> {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "CASE INVESTIGATION SYNDICATE",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = BrandCyan
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))

                                    val collabs = if (post.collaborators.isNotEmpty()) post.collaborators else listOf("RidgeResearcher (Lead)", "Anonymous #4827", "OpticsPro", "Anonymous #1102")
                                    collabs.forEach { collab ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(CircleShape)
                                                    .background(BrandPurpleAccent.copy(alpha = 0.2f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.People,
                                                    contentDescription = null,
                                                    tint = BrandPurpleAccent,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(text = collab, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                                                Text(text = "Contributor • Evidence & Theory Access", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }

        // Dialogs
        if (showSubmitEvidenceDialog) {
            SubmitEvidenceDialog(
                onDismiss = { showSubmitEvidenceDialog = false },
                onSubmit = onSubmitEvidence
            )
        }

        if (showProposeTheoryDialog) {
            ProposeTheoryDialog(
                onDismiss = { showProposeTheoryDialog = false },
                onSubmit = onProposeTheory
            )
        }

        if (showAddTimelineDialog) {
            AddTimelineDialog(
                onDismiss = { showAddTimelineDialog = false },
                onSubmit = onAddTimelineEvent
            )
        }
    }
}
