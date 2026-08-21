package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.outputs.data.model.PostItem
import com.example.outputs.ui.components.ReadingModeScreen
import com.example.outputs.ui.screens.create.CreateOutputScreen
import com.example.outputs.ui.screens.discovery.VerticalDiscoveryScreen
import com.example.outputs.ui.screens.explore.ExploreScreen
import com.example.outputs.ui.screens.groups.GroupsScreen
import com.example.outputs.ui.screens.home.HomeScreen
import com.example.outputs.ui.screens.inbox.InboxScreen
import com.example.outputs.ui.screens.investigation.CaseInvestigationScreen
import com.example.outputs.ui.screens.onboarding.AgeGateScreen
import com.example.outputs.ui.screens.post.PostDetailScreen
import com.example.outputs.ui.screens.profile.ProfileScreen
import com.example.outputs.ui.viewmodel.OutputsViewModel
import com.example.ui.theme.BrandTerracotta
import com.example.ui.theme.BrandWarmAmber
import com.example.ui.theme.OutputsTheme

enum class MainNavigationTab(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    DISCOVERY("Discovery", Icons.Default.Explore),
    CREATE("Create", Icons.Default.AddCircle),
    GROUPS("Syndicates", Icons.Default.Group),
    INBOX("Inbox", Icons.Default.Mail),
    PROFILE("Vault", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OutputsTheme {
                OutputsApp()
            }
        }
    }
}

@Composable
fun OutputsApp(
    viewModel: OutputsViewModel = viewModel()
) {
    val session by viewModel.userSession.collectAsState()
    val feedPosts by viewModel.feedPosts.collectAsState()
    val rawPosts by viewModel.rawPosts.collectAsState()
    val savedPosts by viewModel.savedPosts.collectAsState()
    val archivedPosts by viewModel.archivedPosts.collectAsState()
    val groups by viewModel.groups.collectAsState()
    val selectedFeedTab by viewModel.selectedFeedTab.collectAsState()
    val selectedGenreFilter by viewModel.selectedGenreFilter.collectAsState()
    val selectedSortOrder by viewModel.selectedSortOrder.collectAsState()
    val readingModeState by viewModel.readingModeState.collectAsState()

    val comments by viewModel.activeComments.collectAsState()
    val evidenceList by viewModel.activeEvidence.collectAsState()
    val theoriesList by viewModel.activeTheories.collectAsState()
    val timelineList by viewModel.activeTimeline.collectAsState()

    var currentTab by remember { mutableStateOf(MainNavigationTab.HOME) }
    var viewingPostDetail by remember { mutableStateOf<PostItem?>(null) }
    var activeInvestigationCase by remember { mutableStateOf<PostItem?>(null) }
    var isSearchOpen by remember { mutableStateOf(false) }

    // 18+ Mandatory Age Gate Check
    if (!session.isAgeVerified) {
        AgeGateScreen(
            onVerificationSuccess = { day, month, year, country, mode, user, pseudo, tags ->
                viewModel.verifyAgeAndCompleteOnboarding(
                    day, month, year, country, mode, user, pseudo, tags
                )
            }
        )
        return
    }

    // Distraction-Free Reading Mode Overlay
    if (readingModeState.post != null) {
        ReadingModeScreen(
            state = readingModeState,
            onClose = { viewModel.closeReadingMode() },
            onFontSizeChange = { delta -> viewModel.updateReadingFontSize(delta) },
            onThemeChange = { theme -> viewModel.updateReadingTheme(theme) },
            onToggleAudio = { viewModel.toggleAudioPlayback() },
            onToggleSave = { post -> viewModel.toggleSave(post) },
            onUpdateProgress = { id, progress -> viewModel.updateReadingProgress(id, progress) }
        )
        return
    }

    // Case Investigation Room Overlay
    if (activeInvestigationCase != null) {
        CaseInvestigationScreen(
            post = activeInvestigationCase!!,
            evidenceList = evidenceList,
            theoriesList = theoriesList,
            timelineList = timelineList,
            onBack = { activeInvestigationCase = null },
            onSubmitEvidence = { title, desc, type, conf ->
                viewModel.submitEvidence(activeInvestigationCase!!.id, title, desc, type, conf)
            },
            onUpvoteEvidence = { ev -> viewModel.upvoteEvidence(ev.id) },
            onProposeTheory = { title, content ->
                viewModel.proposeTheory(activeInvestigationCase!!.id, title, content)
            },
            onSupportTheory = { th -> viewModel.supportTheory(th.id) },
            onChallengeTheory = { th -> viewModel.challengeTheory(th.id) },
            onAcceptTheory = { th -> viewModel.acceptTheory(th.id, activeInvestigationCase!!.id) },
            onDebunkTheory = { th -> viewModel.debunkTheory(th.id) },
            onAddTimelineEvent = { time, title, desc ->
                viewModel.addTimelineEvent(activeInvestigationCase!!.id, time, title, desc)
            },
            onOpenReadMode = { post -> viewModel.openReadingMode(post) }
        )
        return
    }

    // Post Detail Screen Overlay
    if (viewingPostDetail != null) {
        PostDetailScreen(
            post = viewingPostDetail!!,
            comments = comments,
            onBack = { viewingPostDetail = null },
            onInvestigateClick = { post ->
                viewModel.selectPost(post.id)
                activeInvestigationCase = post
            },
            onReadModeClick = { post -> viewModel.openReadingMode(post) },
            onLikeClick = { post -> viewModel.toggleLike(post) },
            onSaveClick = { post -> viewModel.toggleSave(post) },
            onAddComment = { postId, text, isTheory, identityMode ->
                viewModel.addComment(postId, text, isTheory = isTheory, identityMode = identityMode)
            },
            onReplyComment = { parentId, postId, replyToAuthor, text, identityMode ->
                viewModel.replyToComment(parentId, postId, replyToAuthor, text, identityMode)
            },
            onEditComment = { commentId, newText ->
                viewModel.editComment(commentId, newText)
            },
            onSaveComment = { commentId, currentSaved ->
                viewModel.toggleSaveComment(commentId, currentSaved)
            },
            onLikeComment = { id -> viewModel.likeComment(id) },
            onMarkAcceptedAnswer = { id -> viewModel.markAcceptedAnswer(id) }
        )
        return
    }

    // Search / Explore View Overlay
    if (isSearchOpen) {
        ExploreScreen(
            posts = rawPosts,
            onSelectPost = { post ->
                viewModel.selectPost(post.id)
                viewingPostDetail = post
            },
            onInvestigateClick = { post ->
                viewModel.selectPost(post.id)
                activeInvestigationCase = post
            },
            onReadModeClick = { post -> viewModel.openReadingMode(post) },
            onLikeClick = { post -> viewModel.toggleLike(post) },
            onSaveClick = { post -> viewModel.toggleSave(post) },
            onShareClick = {},
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                MainNavigationTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                                tint = if (isSelected) BrandTerracotta else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 10.sp
                                ),
                                color = if (isSelected) BrandTerracotta else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = BrandTerracotta.copy(alpha = 0.15f),
                            selectedIconColor = BrandTerracotta,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainNavigationTab.HOME -> {
                    HomeScreen(
                        posts = feedPosts,
                        selectedTab = selectedFeedTab,
                        selectedGenre = selectedGenreFilter,
                        selectedSortOrder = selectedSortOrder,
                        onSelectTab = { viewModel.selectFeedTab(it) },
                        onSelectGenre = { viewModel.setGenreFilter(it) },
                        onSelectSortOrder = { viewModel.selectSortOrder(it) },
                        onPostClick = { post ->
                            viewModel.selectPost(post.id)
                            viewingPostDetail = post
                        },
                        onInvestigateClick = { post ->
                            viewModel.selectPost(post.id)
                            activeInvestigationCase = post
                        },
                        onReadModeClick = { post -> viewModel.openReadingMode(post) },
                        onLikeClick = { post -> viewModel.toggleLike(post) },
                        onSaveClick = { post -> viewModel.toggleSave(post) },
                        onShareClick = {},
                        onCreatePostClick = { currentTab = MainNavigationTab.CREATE },
                        onSearchClick = { isSearchOpen = true }
                    )
                }

                MainNavigationTab.DISCOVERY -> {
                    VerticalDiscoveryScreen(
                        posts = rawPosts,
                        onInvestigateClick = { post ->
                            viewModel.selectPost(post.id)
                            activeInvestigationCase = post
                        },
                        onReadModeClick = { post -> viewModel.openReadingMode(post) },
                        onLikeClick = { post -> viewModel.toggleLike(post) },
                        onSaveClick = { post -> viewModel.toggleSave(post) },
                        onShareClick = {},
                        onCommentClick = { post ->
                            viewModel.selectPost(post.id)
                            viewingPostDetail = post
                        }
                    )
                }

                MainNavigationTab.CREATE -> {
                    CreateOutputScreen(
                        defaultPrivacyMode = session.defaultPrivacyMode,
                        onBack = { currentTab = MainNavigationTab.HOME },
                        onPublish = { title, content, type, category, privacyMode, genreTheme, knownFacts, unknownFacts, question, tags, isSens, sensWarn, voiceDur, voiceTrans ->
                            viewModel.createOutput(
                                title = title,
                                content = content,
                                type = type,
                                category = category,
                                privacyMode = privacyMode,
                                genreTheme = genreTheme,
                                knownFacts = knownFacts,
                                unknownFacts = unknownFacts,
                                questionToCommunity = question,
                                tags = tags,
                                isSensitive = isSens,
                                sensitiveWarning = sensWarn,
                                voiceDurationSeconds = voiceDur,
                                voiceTranscript = voiceTrans
                            )
                            currentTab = MainNavigationTab.HOME
                        }
                    )
                }

                MainNavigationTab.GROUPS -> {
                    GroupsScreen(
                        groups = groups,
                        onSelectGroup = {}
                    )
                }

                MainNavigationTab.INBOX -> {
                    InboxScreen(
                        onSendMessage = { convId, text -> viewModel.sendMessage(convId, text) }
                    )
                }

                MainNavigationTab.PROFILE -> {
                    ProfileScreen(
                        userSession = session,
                        savedPosts = savedPosts,
                        archivedPosts = archivedPosts,
                        onSelectPost = { post ->
                            viewModel.selectPost(post.id)
                            viewingPostDetail = post
                        },
                        onInvestigateClick = { post ->
                            viewModel.selectPost(post.id)
                            activeInvestigationCase = post
                        },
                        onReadModeClick = { post -> viewModel.openReadingMode(post) },
                        onLikeClick = { post -> viewModel.toggleLike(post) },
                        onSaveClick = { post -> viewModel.toggleSave(post) },
                        onShareClick = {},
                        onUpdatePrivacyMode = { mode -> viewModel.updateDefaultPrivacyMode(mode) },
                        onUpdateTheme = { theme -> viewModel.updateAppTheme(theme) },
                        onToggleSensitiveBlur = { blur -> viewModel.toggleSensitiveBlur(blur) },
                        onLogout = { viewModel.logout() }
                    )
                }
            }
        }
    }
}

