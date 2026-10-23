package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.NeonPink
import com.example.ui.viewmodels.WallpaperViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WallpaperViewModel,
    onWallpaperClick: (Wallpaper) -> Unit,
    onNavigateFavorites: () -> Unit,
    onNavigateDownloads: () -> Unit,
    onNavigateSources: () -> Unit,
    isDark: Boolean = isSystemInDarkTheme(),
    onToggleTheme: () -> Unit = {}
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
    val trendingCategories = listOf(
        "🔥 Trending",
        "🖤 Amoled",
        "🌌 Cosmic Space",
        "🤖 Cyberpunk",
        "⛩️ Anime",
        "✨ Minimal",
        "🌿 Nature",
        "🏙️ Urban",
        "🏛️ Wikipedia",
        "🚀 Sci-Fi",
        "🎨 Abstract"
    )

    // Infinite scroll detection
    val gridState = rememberLazyGridState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = gridState.layoutInfo.totalItemsCount
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisible >= totalItems - 6
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !uiState.isLoading && !uiState.isLoadingMore && uiState.canLoadMore) {
            viewModel.loadMoreWallpapers()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // App Logo Icon Badge (Purple + White logo cutout)
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF6B38FB))
                                .padding(5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_pixelloom_logo_white),
                                contentDescription = "App Logo",
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Dark Mode Toggle Button (Replaces PixelLoom text & tagline)
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1)
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .clickable { onToggleTheme() }
                                .testTag("dark_mode_toggle_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = if (isDark) "Switch to Light Mode" else "Switch to Dark Mode",
                                    tint = if (isDark) BrandSecondary else Color(0xFFF59E0B),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isDark) "Dark" else "Light",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else Color(0xFF0F172A)
                                )
                            }
                        }
                    }
                },
                actions = {
                    // Favorites Button (Compact 34.dp as requested)
                    IconButton(
                        onClick = onNavigateFavorites,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("nav_favorites_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = if (favorites.isNotEmpty()) NeonPink else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Downloads Button
                    IconButton(
                        onClick = onNavigateDownloads,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("nav_downloads_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Downloads",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Sources Directory Button
                    IconButton(
                        onClick = onNavigateSources,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("nav_sources_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Sources",
                            tint = if (isDark) BrandSecondary else BrandPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 1200.dp)
                    .testTag("home_wallpapers_grid")
            ) {
                // 1. Search Bar & Tag Chips
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            placeholder = {
                                Text(
                                    "Search wallpapers, aesthetics...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Search,
                                    contentDescription = null,
                                    tint = if (isDark) BrandSecondary else BrandPrimary
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.clearSearch() }) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = {
                                viewModel.executeSearch(uiState.searchQuery)
                                focusManager.clearFocus()
                            }),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = if (isDark) BrandSecondary else BrandPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_text_field")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Dynamic Trending Categories Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            trendingCategories.forEach { categoryLabel ->
                                val cleanTag = categoryLabel.substringAfter(" ")
                                val isSelected = if (categoryLabel.contains("Trending")) {
                                    !uiState.isSearching && uiState.selectedSourceId == "all"
                                } else {
                                    uiState.searchQuery.equals(cleanTag, ignoreCase = true)
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected)
                                        (if (isDark) BrandSecondary.copy(alpha = 0.2f) else BrandPrimary.copy(alpha = 0.12f))
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) (if (isDark) BrandSecondary else BrandPrimary)
                                        else MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier.clickable {
                                        if (categoryLabel.contains("Trending")) {
                                            viewModel.clearSearch()
                                        } else {
                                            viewModel.onSearchQueryChanged(cleanTag)
                                            viewModel.executeSearch(cleanTag)
                                        }
                                    }
                                ) {
                                    Text(
                                        text = categoryLabel,
                                        color = if (isSelected)
                                            (if (isDark) BrandSecondary else BrandPrimary)
                                        else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Wallpapers of the Day (10 Curated Daily Wallpapers)
                // STRICT RULE: Only shown when "All Sources" is selected and not searching
                if (!uiState.isSearching && uiState.selectedSourceId == "all" && uiState.wallpapersOfTheDay.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        WallpapersOfTheDaySection(
                            wallpapers = uiState.wallpapersOfTheDay,
                            favoriteIds = favoriteIds,
                            onWallpaperClick = { wp ->
                                viewModel.selectWallpaperWithList(wp, uiState.wallpapersOfTheDay)
                                onWallpaperClick(wp)
                            },
                            onFavoriteToggle = { viewModel.toggleFavorite(it) }
                        )
                    }
                }

                // 3. Source Filter Row
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Filter by Platform",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "View all 24+ →",
                                color = if (isDark) BrandSecondary else BrandPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { onNavigateSources() }
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // All Sources option
                            item {
                                FilterChip(
                                    selected = uiState.selectedSourceId == "all",
                                    onClick = { viewModel.selectSource("all") },
                                    label = { Text("✨ All Sources", fontSize = 11.5.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BrandPrimary,
                                        selectedLabelColor = Color.White,
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = uiState.selectedSourceId == "all",
                                        borderColor = if (uiState.selectedSourceId == "all") BrandPrimary else MaterialTheme.colorScheme.outline
                                    )
                                )
                            }

                            // Selected prominent sources
                            items(sources) { source ->
                                FilterChip(
                                    selected = uiState.selectedSourceId == source.id,
                                    onClick = { viewModel.selectSource(source.id) },
                                    label = { Text("${source.iconEmoji} ${source.shortName}", fontSize = 11.5.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BrandPrimary,
                                        selectedLabelColor = Color.White,
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = uiState.selectedSourceId == source.id,
                                        borderColor = if (uiState.selectedSourceId == source.id) BrandPrimary else MaterialTheme.colorScheme.outline
                                    )
                                )
                            }
                        }
                    }
                }

                // 4. Section Title: "Trending Wallpapers" or current filter
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentSourceName = if (uiState.selectedSourceId == "all") "Trending Across All Sources"
                        else SourceRegistry.getSourceById(uiState.selectedSourceId)?.name ?: "Wallpapers"
                        Text(
                            text = if (uiState.isSearching) "Search Results for \"${uiState.searchQuery}\"" else currentSourceName,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${uiState.trendingWallpapers.size} items",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.5.sp
                        )
                    }
                }

                // 5. Loading State
                if (uiState.isLoading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = if (isDark) BrandSecondary else BrandPrimary,
                                    strokeWidth = 3.dp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Weaving wallpapers...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.5.sp
                                )
                            }
                        }
                    }
                }

                // 6. Responsive Wallpapers Grid Items
                items(uiState.trendingWallpapers, key = { it.id }) { item ->
                    val isFav = favoriteIds.contains(item.id)
                    WallpaperCard(
                        wallpaper = item,
                        isFavorite = isFav,
                        onClick = {
                            viewModel.selectWallpaperWithList(item, uiState.trendingWallpapers)
                            onWallpaperClick(item)
                        },
                        onFavoriteToggle = { viewModel.toggleFavorite(item) }
                    )
                }

                // 7. Infinite Scrolling Indicator
                if (uiState.isLoadingMore) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = if (isDark) BrandSecondary else BrandPrimary,
                                strokeWidth = 2.5.dp
                            )
                        }
                    }
                }
            }
        }
    }
}
