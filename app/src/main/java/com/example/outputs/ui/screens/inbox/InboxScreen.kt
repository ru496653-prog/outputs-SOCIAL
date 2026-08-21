package com.example.outputs.ui.screens.inbox

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.ChatMessage
import com.example.outputs.data.model.PrivacyMode
import com.example.outputs.ui.components.IdentityBadge
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurpleAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    onSendMessage: (conversationId: String, text: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedInboxTab by remember { mutableIntStateOf(0) } // 0 = Primary, 1 = Requests
    var activeConversationUser by remember { mutableStateOf<String?>(null) }
    var chatMessages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("m1", "c1", "RidgeResearcher", PrivacyMode.PSEUDONYM, false, "Hello! I saw your post regarding the Black Ridge recording. Have you tried cross-referencing the USGS seismic telemetry for 2024?", "10:14 PM"),
                ChatMessage("m2", "c1", "Anonymous #4827", PrivacyMode.ANONYMOUS_NUMBER, true, "I did! There were micro-tremors recorded at 14Hz right around that same time window.", "10:18 PM"),
                ChatMessage("m3", "c1", "RidgeResearcher", PrivacyMode.PSEUDONYM, false, "Fascinating. Let's link that to the main Case Investigation room.", "10:22 PM")
            )
        )
    }
    var currentInputText by remember { mutableStateOf("") }

    if (activeConversationUser != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = activeConversationUser!!,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(11.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("End-to-End Anonymized Channel", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = BrandGreen)
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { activeConversationUser = null }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
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
                        value = currentInputText,
                        onValueChange = { currentInputText = it },
                        placeholder = { Text("Write private message...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (currentInputText.isNotBlank()) {
                                val newMsg = ChatMessage(
                                    id = "msg_${System.currentTimeMillis()}",
                                    conversationId = "c1",
                                    senderName = "Anonymous #4827",
                                    senderIdentity = PrivacyMode.ANONYMOUS_NUMBER,
                                    isMe = true,
                                    text = currentInputText,
                                    timestamp = "Just now"
                                )
                                chatMessages = chatMessages + newMsg
                                onSendMessage("c1", currentInputText)
                                currentInputText = ""
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
                items(chatMessages) { msg ->
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
                                text = "ANONYMOUS INBOX",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Encrypted 1-on-1 Inquiries",
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
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                TabRow(
                    selectedTabIndex = selectedInboxTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = BrandCyan
                ) {
                    Tab(
                        selected = selectedInboxTab == 0,
                        onClick = { selectedInboxTab = 0 },
                        text = { Text("Direct Messages (1)", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedInboxTab == 1,
                        onClick = { selectedInboxTab = 1 },
                        text = { Text("Message Requests (1)", fontWeight = FontWeight.Bold) }
                    )
                }

                if (selectedInboxTab == 0) {
                    // Active Conversations
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                                    .clickable { activeConversationUser = "RidgeResearcher" },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(BrandPurpleAccent.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Mail, contentDescription = null, tint = BrandPurpleAccent)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("RidgeResearcher", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                                            Text("10:22 PM", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Text("Fascinating. Let's link that to the main Case...", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Message Requests
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
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
                                        IdentityBadge(identityMode = PrivacyMode.ANONYMOUS_NUMBER, authorName = "Anonymous #9914")
                                        Text("Yesterday", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("I have high-resolution photos of the sodium quarry gate from 2019 if you need comparison pictures.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        FilledTonalButton(
                                            onClick = { activeConversationUser = "Anonymous #9914" },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = BrandCyan, contentColor = Color.Black),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Text("Accept Inquiry", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
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
