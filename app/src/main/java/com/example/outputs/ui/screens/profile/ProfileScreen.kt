package com.example.outputs.ui.screens.profile

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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.outputs.data.model.GenreTheme
import com.example.outputs.data.model.PostItem
import com.example.outputs.data.model.PrivacyMode
import com.example.outputs.data.model.UserSession
import com.example.outputs.ui.components.IdentityBadge
import com.example.outputs.ui.components.PostCard
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurpleAccent
import com.example.ui.theme.BrandRed
import com.example.ui.theme.BrandTerracotta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userSession: UserSession,
    savedPosts: List<PostItem>,
    archivedPosts: List<PostItem>,
    onSelectPost: (PostItem) -> Unit,
    onInvestigateClick: (PostItem) -> Unit,
    onReadModeClick: (PostItem) -> Unit,
    onLikeClick: (PostItem) -> Unit,
    onSaveClick: (PostItem) -> Unit,
    onShareClick: (PostItem) -> Unit,
    onUpdatePrivacyMode: (PrivacyMode) -> Unit,
    onUpdateTheme: (GenreTheme) -> Unit,
    onToggleSensitiveBlur: (Boolean) -> Unit,
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedProfileTab by remember { mutableIntStateOf(0) } // 0 = Solved Archive, 1 = Bookmarks, 2 = Privacy & Settings
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = BrandRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out of Session", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "Are you sure you want to log out? Your active anonymous investigator vault (${userSession.pseudonym} / ${userSession.anonymousNumberCode}) will be locked and you will return to the age verification gate.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = BrandRed.copy(alpha = 0.2f),
                        contentColor = BrandRed
                    ),
                    modifier = Modifier.testTag("confirm_logout_button")
                ) {
                    Text("Log Out", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                    modifier = Modifier.testTag("cancel_logout_button")
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "INVESTIGATOR PROFILE",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Outputs Case Archive & Privacy Vault",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier.testTag("profile_logout_topbar_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Log Out",
                            tint = BrandRed
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Card with Identity & 18+ Verification seal
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, BrandPurpleAccent.copy(alpha = 0.3f), RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(BrandPurpleAccent.copy(alpha = 0.2f))
                                        .border(1.dp, BrandPurpleAccent, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = "Verified Identity",
                                        tint = BrandPurpleAccent,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = userSession.publicUsername,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Pseudonym: ${userSession.pseudonym}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BrandPurpleAccent
                                    )
                                    Text(
                                        text = "Assigned: Anonymous #${userSession.anonymousNumberCode}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (userSession.isAgeVerified) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(BrandGreen.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "✓ 18+ VERIFIED",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        ),
                                        color = BrandGreen
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Achievements & Contributions Stats
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(vertical = 10.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${userSession.solvedMysteriesCount}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BrandGreen
                                )
                                Text(
                                    text = "Cases Solved",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${userSession.helpfulEvidenceCount}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BrandCyan
                                )
                                Text(
                                    text = "Helpful Evidences",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${userSession.theoriesAcceptedCount}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BrandAmber
                                )
                                Text(
                                    text = "Accepted Theories",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Profile Tabs: Solved Case Archive, Saved Bookmarks, Privacy & Settings
            item {
                TabRow(
                    selectedTabIndex = selectedProfileTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = BrandCyan
                ) {
                    Tab(
                        selected = selectedProfileTab == 0,
                        onClick = { selectedProfileTab = 0 },
                        text = { Text("Case Archive (${archivedPosts.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedProfileTab == 1,
                        onClick = { selectedProfileTab = 1 },
                        text = { Text("Saved (${savedPosts.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedProfileTab == 2,
                        onClick = { selectedProfileTab = 2 },
                        text = { Text("Privacy & Settings", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }
            }

            when (selectedProfileTab) {
                0 -> {
                    // Solved Archive
                    if (archivedPosts.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text("No archived cases yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        items(archivedPosts, key = { it.id }) { post ->
                            PostCard(
                                post = post,
                                onPostClick = onSelectPost,
                                onInvestigateClick = onInvestigateClick,
                                onReadModeClick = onReadModeClick,
                                onLikeClick = onLikeClick,
                                onSaveClick = onSaveClick,
                                onShareClick = onShareClick
                            )
                        }
                    }
                }

                1 -> {
                    // Saved Bookmarks
                    if (savedPosts.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text("No saved outputs bookmarked.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        items(savedPosts, key = { it.id }) { post ->
                            PostCard(
                                post = post,
                                onPostClick = onSelectPost,
                                onInvestigateClick = onInvestigateClick,
                                onReadModeClick = onReadModeClick,
                                onLikeClick = onLikeClick,
                                onSaveClick = onSaveClick,
                                onShareClick = onShareClick
                            )
                        }
                    }
                }

                2 -> {
                    // Privacy & App Settings
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "DEFAULT POSTING IDENTITY",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = BrandCyan
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                PrivacyMode.values().forEach { mode ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onUpdatePrivacyMode(mode) }
                                            .padding(vertical = 4.dp)
                                    ) {
                                        RadioButton(
                                            selected = userSession.defaultPrivacyMode == mode,
                                            onClick = { onUpdatePrivacyMode(mode) },
                                            colors = RadioButtonDefaults.colors(selectedColor = BrandPurpleAccent)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text(text = mode.label, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                                            Text(text = mode.description, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Content safety toggle
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Blur Sensitive Content by Default", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                                        Text("Hides potentially distressing incident descriptions until tapped", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Switch(
                                        checked = userSession.blurSensitiveContent,
                                        onCheckedChange = { onToggleSensitiveBlur(it) },
                                        colors = SwitchDefaults.colors(checkedThumbColor = BrandCyan)
                                    )
                                }
                            }
                        }
                    }

                    // Session Security & Logout Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, BrandRed.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Security,
                                            contentDescription = null,
                                            tint = BrandRed,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "SESSION & SECURITY",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            ),
                                            color = BrandRed
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(BrandGreen.copy(alpha = 0.15f))
                                            .border(1.dp, BrandGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "18+ VERIFIED",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp
                                            ),
                                            color = BrandGreen
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Active session linked to pseudonymous key ${userSession.anonymousNumberCode}. Logging out will clear encrypted keys and require age verification to regain access.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                FilledTonalButton(
                                    onClick = { showLogoutDialog = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("logout_button"),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = BrandRed.copy(alpha = 0.15f),
                                        contentColor = BrandRed
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Logout,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Log Out of Anonymous Vault",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}
