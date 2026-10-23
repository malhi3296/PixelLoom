package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Wallpaper
import com.example.ui.components.DownloadDimensionsDialog
import com.example.ui.components.SetWallpaperBottomSheet
import com.example.ui.components.WallpaperCropDialog
import com.example.ui.components.ZoomableImage
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodels.WallpaperViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    wallpaper: Wallpaper,
    viewModel: WallpaperViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    // 1. Sliding Wallpapers Pager
    val wallpapersList = remember(uiState.activeWallpapers, wallpaper) {
        if (uiState.activeWallpapers.isNotEmpty()) uiState.activeWallpapers else listOf(wallpaper)
    }
    val initialIndex = remember(wallpaper.id, wallpapersList) {
        val idx = wallpapersList.indexOfFirst { it.id == wallpaper.id }
        if (idx >= 0) idx else 0
    }
    val pagerState = rememberPagerState(initialPage = initialIndex) {
        wallpapersList.size
    }

    val currentWallpaper = wallpapersList.getOrNull(pagerState.currentPage) ?: wallpaper
    val isFavorite = remember(favorites, currentWallpaper.id) { favorites.any { it.id == currentWallpaper.id } }

    var showCropDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showSetWallpaperSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("detail_screen")
    ) {
        // High-res Pinch-to-Zoom Wallpaper inside HorizontalPager for smooth sliding
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val pageItem = wallpapersList.getOrNull(page) ?: currentWallpaper
            ZoomableImage(
                url = pageItem.fullUrl,
                contentDescription = pageItem.title,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Top Navigation & Action Bar Overlay with Status Bar Insets
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent)
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 900.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .testTag("detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentWallpaper.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${currentWallpaper.source} • by ${currentWallpaper.author}",
                        color = NeonCyan,
                        fontSize = 11.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Crop & Frame Button
                IconButton(
                    onClick = { showCropDialog = true },
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .testTag("detail_crop_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Crop,
                        contentDescription = "Crop & Frame",
                        tint = NeonCyan,
                        modifier = Modifier.size(17.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Info Dialog Button
                IconButton(
                    onClick = { showInfoDialog = true },
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .testTag("detail_info_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Smaller Favorite Button on Top Right (User requested smaller fav btn)
                IconButton(
                    onClick = { viewModel.toggleFavorite(currentWallpaper) },
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .testTag("detail_favorite_button")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) NeonPink else Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Share Button
                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Check out this breathtaking '${currentWallpaper.title}' wallpaper on PixelLoom: ${currentWallpaper.fullUrl}"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Wallpaper via"))
                    },
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .testTag("detail_share_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }

        // Bottom Primary Actions Floating Panel (Guaranteed strictly above navigation buttons)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.94f))
                    )
                )
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "Set Wallpaper" Button (Opens options: Home, Lock, Both, or Set with...)
                Button(
                    onClick = { showSetWallpaperSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(54.dp)
                        .testTag("set_wallpaper_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Wallpaper,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Set Wallpaper",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                // Download Button (Opens Custom Dimensions Dialog)
                FilledTonalButton(
                    onClick = { showDownloadDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color.White.copy(alpha = 0.22f),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .testTag("download_wallpaper_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Download",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        // Set Wallpaper Options: Home screen, Lockscreen, Both, or Set with... (Other gallery app)
        if (showSetWallpaperSheet) {
            SetWallpaperBottomSheet(
                onDismissRequest = { showSetWallpaperSheet = false },
                onOptionSelected = { target ->
                    showSetWallpaperSheet = false
                    viewModel.selectWallpaper(currentWallpaper)
                    viewModel.setWallpaper(target)
                },
                onCropRequested = {
                    showSetWallpaperSheet = false
                    showCropDialog = true
                }
            )
        }

        // Interactive Freeform Wallpaper Crop & Frame Dialog
        if (showCropDialog) {
            WallpaperCropDialog(
                wallpaper = currentWallpaper,
                viewModel = viewModel,
                onDismissRequest = { showCropDialog = false },
                onApplyCrop = { croppedBitmap, target ->
                    viewModel.setCroppedWallpaper(croppedBitmap, target)
                    showCropDialog = false
                },
                onApplyDirect = { target ->
                    viewModel.selectWallpaper(currentWallpaper)
                    viewModel.setWallpaper(target)
                    showCropDialog = false
                }
            )
        }

        // Download Dimensions Dialog
        if (showDownloadDialog) {
            DownloadDimensionsDialog(
                wallpaper = currentWallpaper,
                onDismissRequest = { showDownloadDialog = false },
                onDownloadOriginal = {
                    viewModel.downloadWithDimensions(null, null)
                    showDownloadDialog = false
                },
                onDownloadCustom = { width, height ->
                    viewModel.downloadWithDimensions(width, height)
                    showDownloadDialog = false
                },
                isDownloading = uiState.isDownloading
            )
        }

        // Wallpaper Details & Attribution Dialog
        if (showInfoDialog) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { showInfoDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = currentWallpaper.title,
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Created by ${currentWallpaper.author}",
                            color = NeonCyan,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        DetailMetaRow("Platform Source", currentWallpaper.source)
                        DetailMetaRow("Resolution", "${currentWallpaper.width} × ${currentWallpaper.height} px")
                        DetailMetaRow("Category", currentWallpaper.category)
                        if (currentWallpaper.tags.isNotEmpty()) {
                            DetailMetaRow("Tags", currentWallpaper.tags.joinToString(", "))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (currentWallpaper.authorUrl != null) {
                                androidx.compose.material3.TextButton(onClick = {
                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(currentWallpaper.authorUrl))
                                    context.startActivity(browserIntent)
                                }) {
                                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Visit Creator", color = NeonCyan)
                                }
                            }
                            androidx.compose.material3.TextButton(onClick = { showInfoDialog = false }) {
                                Text("Close", color = TextSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailMetaRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontSize = 13.sp)
        Text(text = value, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}
