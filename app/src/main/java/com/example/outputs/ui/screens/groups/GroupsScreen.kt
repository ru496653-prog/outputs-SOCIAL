package com.example.outputs.ui.screens.groups

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.AnonymousGroup
import com.example.outputs.data.model.ChatMessage
import com.example.outputs.data.model.PrivacyMode
import com.example.outputs.ui.components.IdentityBadge
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurpleAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(
    groups: List<AnonymousGroup>,
    onSelectGroup: (AnonymousGroup) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeGroupInChat by remember { mutableStateOf<AnonymousGroup?>(null) }
    var groupChatMessages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("g1", "grp_1", "TheNightArchivist", PrivacyMode.PSEUDONYM, false, "Welcome to the Midnight Investigators syndicate. We are looking into the Black Ridge frequency data.", "11:20 PM"),
                ChatMessage("g2", "grp_1", "Anonymous #4827", PrivacyMode.ANONYMOUS_NUMBER, false, "I checked the geological survey maps. There is an abandoned sodium quarry 4km away.", "11:25 PM"),
                ChatMessage("g3", "grp_1", "Anonymous #1102", PrivacyMode.ANONYMOUS_NUMBER, true, "I just uploaded the high-pass filter audio file to the case evidence board.", "11:32 PM")
            )
        )
    }
    var newGroupMsgText by remember { mutableStateOf("") }

    if (activeGroupInChat != null) {
        val group = activeGroupInChat!!
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = group.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                if (group.isPrivate) {
                                    Icon(Icons.Default.Lock, contentDescription = "Private syndicate", tint = BrandAmber, modifier = Modifier.size(14.dp))
                                }
                            }
                            Text(
                                text = "${group.membersCount} members • ${group.activeCasesCount} active cases",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { activeGroupInChat = null }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back to Groups", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            },
            bottomBar = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(10.dp)
                ) {
                    OutlinedTextField(
                        value = newGroupMsgText,
                        onValueChange = { newGroupMsgText = it },
                        placeholder = { Text("Message syndicate anonymously...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (newGroupMsgText.isNotBlank()) {
                                val msg = ChatMessage(
                                    id = "g_${System.currentTimeMillis()}",
                                    conversationId = group.id,
                                    senderName = "Anonymous #4827",
                                    senderIdentity = PrivacyMode.ANONYMOUS_NUMBER,
                                    isMe = true,
                                    text = newGroupMsgText,
                                    timestamp = "Just now"
                                )
                                groupChatMessages = groupChatMessages + msg
                                newGroupMsgText = ""
                            }
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(BrandPurpleAccent)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    // Pinned Case Notice
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = BrandPurpleAccent.copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = BrandPurpleAccent, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("PINNED CASE IN PROGRESS", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = BrandPurpleAccent)
                                Text(group.topic, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }

                items(groupChatMessages) { msg ->
                    Column(
                        horizontalAlignment = if (msg.isMe) Alignment.End else Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (!msg.isMe) {
                            IdentityBadge(
                                identityMode = msg.senderIdentity,
                                authorName = msg.senderName
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (msg.isMe) BrandPurpleAccent else MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (msg.isMe) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = msg.timestamp,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "ANONYMOUS GROUPS",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Syndicates & Community Hubs",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "EXPLORE ACTIVE SYNDICATES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = BrandCyan
                    )
                }

                items(groups, key = { it.id }) { grp ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                            .clickable { activeGroupInChat = grp }
                            .testTag("group_card_${grp.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(BrandCyan.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Group, contentDescription = null, tint = BrandCyan, modifier = Modifier.size(20.dp))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = grp.name,
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            if (grp.isPrivate) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(Icons.Default.Lock, contentDescription = "Private", tint = BrandAmber, modifier = Modifier.size(12.dp))
                                            }
                                        }
                                        Text(
                                            text = grp.topic,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = BrandPurpleAccent
                                        )
                                    }
                                }

                                FilledTonalButton(
                                    onClick = { activeGroupInChat = grp },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = BrandCyan.copy(alpha = 0.2f),
                                        contentColor = BrandCyan
                                    ),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(if (grp.isJoined) "Open Chat" else "Join", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = grp.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "👥 ${grp.membersCount} Investigators",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "🔍 ${grp.activeCasesCount} Active Cases",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BrandGreen
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
