package com.example.outputs.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.outputs.data.model.PostItem
import com.example.outputs.data.model.PostType
import com.example.outputs.ui.components.PostCard
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandPurpleAccent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    posts: List<PostItem>,
    onSelectPost: (PostItem) -> Unit,
    onInvestigateClick: (PostItem) -> Unit,
    onReadModeClick: (PostItem) -> Unit,
    onLikeClick: (PostItem) -> Unit,
    onSaveClick: (PostItem) -> Unit,
    onShareClick: (PostItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTypeFilter by remember { mutableStateOf<PostType?>(null) }

    val trendingTopics = listOf(
        "#BlackRidgeHum", "#SodiumQuarry2019", "#UnsolvedIncidents",
        "#AcousticAnomaly", "#MissingStation", "#KathmanduMystery",
        "#NightLogs", "#Signals94"
    )

    val filteredPosts = posts.filter { post ->
        val matchesQuery = searchQuery.isBlank() ||
                post.title.contains(searchQuery, ignoreCase = true) ||
                post.content.contains(searchQuery, ignoreCase = true) ||
                post.category.contains(searchQuery, ignoreCase = true) ||
                post.tags.any { it.contains(searchQuery, ignoreCase = true) }

        val matchesType = selectedTypeFilter == null || post.type == selectedTypeFilter

        matchesQuery && matchesType
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search mysteries, cases, stories...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = BrandCyan) },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("explore_search_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(26.dp)
                    )
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            // Filter by Type chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedTypeFilter == null) BrandCyan else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedTypeFilter = null }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "All Formats",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (selectedTypeFilter == null) Color.Black else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    PostType.values().forEach { type ->
                        val isSelected = selectedTypeFilter == type
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) BrandPurpleAccent else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { selectedTypeFilter = if (isSelected) null else type }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${type.icon} ${type.label}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Trending Tags Section (when not actively searching)
            if (searchQuery.isBlank()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = BrandAmber, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "TRENDING CASE DISCUSSIONS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = BrandAmber
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                trendingTopics.forEach { tag ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.surface)
                                            .clickable { searchQuery = tag.replace("#", "") }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = tag,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = BrandPurpleAccent,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Results Header
            item {
                Text(
                    text = "SEARCH & DOSSIER RESULTS (${filteredPosts.size})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = BrandCyan
                )
            }

            // Results List
            if (filteredPosts.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No matching outputs found for '$searchQuery'", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                items(filteredPosts, key = { it.id }) { post ->
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

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}
