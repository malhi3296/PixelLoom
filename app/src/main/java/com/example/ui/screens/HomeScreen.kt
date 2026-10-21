package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Wallpaper
import com.example.data.sources.SourceRegistry
import com.example.ui.components.WallpaperCard
import com.example.ui.components.WallpapersOfTheDaySection
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.Obsidian
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodels.WallpaperViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WallpaperViewModel,
    onWallpaperClick: (Wallpaper) -> Unit,
    onNavigateFavorites: () -> Unit,
    onNavigateDownloads: () -> Unit,
    onNavigateSources: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val favoriteIds = remember(favorites) { favorites.map { it.id }.toSet() }

    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Show action snackbars (set wallpaper, download success)
    LaunchedEffect(uiState.actionMessage) {
        val msg = uiState.actionMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissActionMessage()
        }
    }

    val sources = remember { SourceRegistry.getAllSources() }

    val popularTags = listOf("Amoled", "Cyberpunk", "Anime", "Space", "Minimal", "Nature", "Urban", "Sci-Fi", "Abstract")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pixelloom_logo),
                            contentDescription = "PixelLoom Logo",
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "PixelLoom",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Curated High-Res Wallpapers",
                                fontSize = 11.sp,
                                color = NeonCyan
                            )
                        }
                    }
                },
                actions = {
                    // Favorites Button
                    IconButton(
                        onClick = onNavigateFavorites,
                        modifier = Modifier.testTag("nav_favorites_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = if (favorites.isNotEmpty()) NeonPink else TextSecondary
                        )
                    }

                    // Downloads Button
                    IconButton(
                        onClick = onNavigateDownloads,
                        modifier = Modifier.testTag("nav_downloads_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Downloads",
                            tint = TextSecondary
                        )
                    }

                    // Sources Directory & Notification Trigger
                    IconButton(
                        onClick = onNavigateSources,
                        modifier = Modifier.testTag("nav_sources_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Sources",
                            tint = NeonCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Obsidian
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 14.dp)
                .testTag("home_wallpapers_grid")
        ) {
            // 1. Search Bar & Tag Chips
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("Search wallpapers, tags, aesthetics...", color = TextSecondary) },
                        leadingIcon = {
                            Icon(Icons.Outlined.Search, contentDescription = null, tint = NeonCyan)
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.clearSearch() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSecondary)
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            viewModel.executeSearch(uiState.searchQuery)
                            focusManager.clearFocus()
                        }),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkSurface,
                            unfocusedContainerColor = DarkSurface,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_text_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Popular Aesthetic Tag Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        popularTags.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = DarkSurfaceElevated,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                                modifier = Modifier.clickable {
                                    viewModel.onSearchQueryChanged(tag)
                                    viewModel.executeSearch(tag)
                                }
                            ) {
                                Text(
                                    text = "#$tag",
                                    color = if (uiState.searchQuery.equals(tag, true)) NeonCyan else TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Wallpapers of the Day (10 Curated Daily Wallpapers)
            if (!uiState.isSearching && uiState.wallpapersOfTheDay.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    WallpapersOfTheDaySection(
                        wallpapers = uiState.wallpapersOfTheDay,
                        favoriteIds = favoriteIds,
                        onWallpaperClick = onWallpaperClick,
                        onFavoriteToggle = { viewModel.toggleFavorite(it) }
                    )
                }
            }

            // 3. Source Filter Row
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filter by Platform",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "View all 24+",
                            color = NeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onNavigateSources() }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // All Sources option
                        item {
                            FilterChip(
                                selected = uiState.selectedSourceId == "all",
                                onClick = { viewModel.selectSource("all") },
                                label = { Text("✨ All Sources", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonCyan,
                                    selectedLabelColor = Obsidian,
                                    containerColor = DarkSurfaceElevated,
                                    labelColor = TextSecondary
                                )
                            )
                        }

                        // Selected prominent sources
                        items(sources) { source ->
                            FilterChip(
                                selected = uiState.selectedSourceId == source.id,
                                onClick = { viewModel.selectSource(source.id) },
                                label = { Text("${source.iconEmoji} ${source.shortName}", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonCyan,
                                    selectedLabelColor = Obsidian,
                                    containerColor = DarkSurfaceElevated,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                }
            }

            // 4. Section Title: "Trending Wallpapers" or current filter
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val currentSourceName = if (uiState.selectedSourceId == "all") "Trending Across All Sources"
                        else SourceRegistry.getSourceById(uiState.selectedSourceId)?.name ?: "Wallpapers"
                    Text(
                        text = if (uiState.isSearching) "Search Results for \"${uiState.searchQuery}\"" else currentSourceName,
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${uiState.trendingWallpapers.size} items",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // 5. Loading State
            if (uiState.isLoading) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = NeonCyan, strokeWidth = 3.dp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Weaving wallpapers...", color = TextSecondary, fontSize = 13.sp)
                        }
                    }
                }
            }

            // 6. Wallpapers Grid Items
            items(uiState.trendingWallpapers, key = { it.id }) { item ->
                val isFav = favoriteIds.contains(item.id)
                WallpaperCard(
                    wallpaper = item,
                    isFavorite = isFav,
                    onClick = { onWallpaperClick(item) },
                    onFavoriteToggle = { viewModel.toggleFavorite(item) }
                )
            }
        }
    }
}
