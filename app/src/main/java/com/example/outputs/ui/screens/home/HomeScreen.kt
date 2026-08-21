package com.example.outputs.ui.screens.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.GenreTheme
import com.example.outputs.data.model.PostItem
import com.example.outputs.ui.components.PostCard
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import com.example.outputs.ui.viewmodel.FeedSortOrder
import com.example.outputs.ui.viewmodel.FeedTab
import com.example.ui.theme.BrandSage
import com.example.ui.theme.BrandTerracotta
import com.example.ui.theme.BrandWarmAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: List<PostItem>,
    selectedTab: FeedTab,
    selectedGenre: GenreTheme?,
    selectedSortOrder: FeedSortOrder = FeedSortOrder.NEWEST,
    onSelectTab: (FeedTab) -> Unit,
    onSelectGenre: (GenreTheme?) -> Unit,
    onSelectSortOrder: (FeedSortOrder) -> Unit = {},
    onPostClick: (PostItem) -> Unit,
    onInvestigateClick: (PostItem) -> Unit,
    onReadModeClick: (PostItem) -> Unit,
    onLikeClick: (PostItem) -> Unit,
    onSaveClick: (PostItem) -> Unit,
    onShareClick: (PostItem) -> Unit,
    onCreatePostClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showThemeMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(BrandTerracotta.copy(alpha = 0.18f))
                                    .border(1.2.dp, BrandTerracotta, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "O",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif,
                                        color = BrandTerracotta
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "OUTPUTS",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif,
                                        letterSpacing = 2.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Say it. Share it. Solve it.",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = onSearchClick, modifier = Modifier.testTag("home_search_btn")) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurface)
                        }
                        IconButton(onClick = { showThemeMenu = !showThemeMenu }, modifier = Modifier.testTag("theme_selector_btn")) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Genre Atmosphere",
                                tint = if (selectedGenre != null) BrandTerracotta else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Box {
                            IconButton(onClick = { showSortMenu = true }, modifier = Modifier.testTag("feed_sort_button")) {
                                Icon(
                                    imageVector = Icons.Default.Sort,
                                    contentDescription = "Sort Feed",
                                    tint = if (selectedSortOrder != FeedSortOrder.NEWEST) BrandTerracotta else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false }
                            ) {
                                FeedSortOrder.values().forEach { order ->
                                    val isSelected = selectedSortOrder == order
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "${order.icon} ${order.label}",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                    ),
                                                    color = if (isSelected) BrandTerracotta else MaterialTheme.colorScheme.onSurface
                                                )
                                                if (isSelected) {
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Selected",
                                                        tint = BrandTerracotta,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        },
                                        onClick = {
                                            onSelectSortOrder(order)
                                            showSortMenu = false
                                        },
                                        modifier = Modifier.testTag("sort_option_${order.name.lowercase()}")
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )

                // Atmospheric Genre Switcher Bar
                AnimatedVisibility(visible = showThemeMenu) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (selectedGenre == null) BrandTerracotta else MaterialTheme.colorScheme.surface)
                                    .border(
                                        1.dp,
                                        if (selectedGenre == null) BrandTerracotta else MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onSelectGenre(null) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "All Atmospheres",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (selectedGenre == null) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        items(GenreTheme.values()) { genre ->
                            val isSelected = selectedGenre == genre
                            val genreColor = Color(genre.primaryColorHex)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) genreColor else MaterialTheme.colorScheme.surface)
                                    .border(
                                        1.dp,
                                        if (isSelected) genreColor else MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onSelectGenre(genre) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${genre.icon} ${genre.label}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Feed Tabs: For You, Mysteries, Real Incidents, Voice Logs, Stories, Opinions, Questions, Following
                ScrollableTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    edgePadding = 12.dp,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = BrandTerracotta,
                    indicator = { tabPositions ->
                        if (selectedTab.ordinal < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                                color = BrandTerracotta,
                                height = 3.dp
                            )
                        }
                    },
                    divider = {}
                ) {
                    FeedTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        Tab(
                            selected = isSelected,
                            onClick = { onSelectTab(tab) },
                            text = {
                                Text(
                                    text = "${tab.icon} ${tab.label}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (isSelected) BrandTerracotta else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier.testTag("feed_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreatePostClick,
                containerColor = BrandTerracotta,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("create_output_fab")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create Output")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "New Story",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (posts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(BrandTerracotta.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = BrandTerracotta,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Text(
                            text = "No outputs in this section yet",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Be the first to submit an anonymous mystery, real incident story, or voice audio transmission.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onCreatePostClick,
                            colors = ButtonDefaults.buttonColors(containerColor = BrandTerracotta),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("empty_state_create_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Post Anonymous Story", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .testTag("feed_stories_lazy_column"),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Editorial Feed Header Banner
                item(key = "feed_status_header") {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = BrandSage,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Encrypted Feed • ${posts.size} stories",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (selectedGenre != null) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(selectedGenre.primaryColorHex).copy(alpha = 0.2f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = selectedGenre.label,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            ),
                                            color = Color(selectedGenre.primaryColorHex)
                                        )
                                    }
                                }

                                var showHeaderSortMenu by remember { mutableStateOf(false) }
                                Box {
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(BrandTerracotta.copy(alpha = 0.12f))
                                            .border(1.dp, BrandTerracotta.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                                            .clickable { showHeaderSortMenu = true }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                            .testTag("feed_sort_chip"),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${selectedSortOrder.icon} ${selectedSortOrder.label}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            ),
                                            color = BrandTerracotta
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Sort,
                                            contentDescription = "Sort Feed",
                                            tint = BrandTerracotta,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showHeaderSortMenu,
                                        onDismissRequest = { showHeaderSortMenu = false }
                                    ) {
                                        FeedSortOrder.values().forEach { order ->
                                            val isSelected = selectedSortOrder == order
                                            DropdownMenuItem(
                                                text = {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Text(
                                                            text = "${order.icon} ${order.label}",
                                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                            ),
                                                            color = if (isSelected) BrandTerracotta else MaterialTheme.colorScheme.onSurface
                                                        )
                                                        if (isSelected) {
                                                            Spacer(modifier = Modifier.width(12.dp))
                                                            Icon(
                                                                imageVector = Icons.Default.Check,
                                                                contentDescription = "Selected",
                                                                tint = BrandTerracotta,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }
                                                    }
                                                },
                                                onClick = {
                                                    onSelectSortOrder(order)
                                                    showHeaderSortMenu = false
                                                },
                                                modifier = Modifier.testTag("header_sort_option_${order.name.lowercase()}")
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Anonymous User-Submitted Story Items
                items(posts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        onPostClick = onPostClick,
                        onInvestigateClick = onInvestigateClick,
                        onReadModeClick = onReadModeClick,
                        onLikeClick = onLikeClick,
                        onSaveClick = onSaveClick,
                        onShareClick = onShareClick
                    )
                }

                item(key = "feed_bottom_spacer") {
                    Spacer(modifier = Modifier.height(84.dp))
                }
            }
        }
    }
}

