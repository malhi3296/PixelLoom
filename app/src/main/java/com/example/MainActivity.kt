package com.example

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SourcesScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.PixelLoomTheme
import com.example.ui.viewmodels.WallpaperViewModel

enum class Screen {
    HOME,
    DETAIL,
    FAVORITES,
    DOWNLOADS,
    SOURCES
}

class MainActivity : ComponentActivity() {

    private val viewModel: WallpaperViewModel by viewModels {
        val app = application as PixelLoomApp
        WallpaperViewModel.Factory(app.repository, app.actionManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        handleIntent(intent)

        setContent {
            val systemDark = androidx.compose.foundation.isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf<Boolean?>(null) }
            val currentDark = isDarkMode ?: systemDark

            PixelLoomTheme(darkTheme = currentDark) {
                var isSplashFinished by remember { mutableStateOf(false) }

                AnimatedContent(
                    targetState = isSplashFinished,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(350)) togetherWith fadeOut(animationSpec = tween(350))
                    },
                    label = "SplashCrossfade",
                    modifier = Modifier.fillMaxSize()
                ) { finished ->
                    if (!finished) {
                        SplashScreen(
                            onSplashFinished = { isSplashFinished = true }
                        )
                    } else {
                        MainContent(
                            viewModel = viewModel,
                            isDark = currentDark,
                            onToggleTheme = { isDarkMode = !currentDark },
                            onHandleIntent = { handleIntent(intent) }
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        val deepLinkData = intent.data
        val wallpaperId = if (deepLinkData != null && deepLinkData.scheme == "pixelloom" && deepLinkData.host == "wallpaper") {
            deepLinkData.lastPathSegment
        } else {
            intent.getStringExtra("wallpaper_id")
        }

        if (!wallpaperId.isNullOrEmpty()) {
            viewModel.selectWallpaperById(wallpaperId)
        }
    }
}

@Composable
fun MainContent(
    viewModel: WallpaperViewModel,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    onHandleIntent: () -> Unit
) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    val uiState by viewModel.uiState.collectAsState()

    // Android 13+ Notification Permission request
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Switch to DETAIL screen if a wallpaper is selected from deep link or elsewhere
    LaunchedEffect(uiState.selectedWallpaper) {
        if (uiState.selectedWallpaper != null) {
            currentScreen = Screen.DETAIL
        }
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
        label = "ScreenTransition",
        modifier = Modifier.fillMaxSize()
    ) { screen ->
        when (screen) {
            Screen.HOME -> HomeScreen(
                viewModel = viewModel,
                onWallpaperClick = { wallpaper ->
                    viewModel.selectWallpaper(wallpaper)
                    currentScreen = Screen.DETAIL
                },
                onNavigateFavorites = { currentScreen = Screen.FAVORITES },
                onNavigateDownloads = { currentScreen = Screen.DOWNLOADS },
                onNavigateSources = { currentScreen = Screen.SOURCES },
                isDark = isDark,
                onToggleTheme = onToggleTheme
            )

            Screen.DETAIL -> {
                val selected = uiState.selectedWallpaper
                if (selected != null) {
                    DetailScreen(
                        wallpaper = selected,
                        viewModel = viewModel,
                        onBack = { currentScreen = Screen.HOME }
                    )
                } else {
                    currentScreen = Screen.HOME
                }
            }

            Screen.FAVORITES -> FavoritesScreen(
                viewModel = viewModel,
                onBack = { currentScreen = Screen.HOME },
                onWallpaperClick = { wallpaper ->
                    viewModel.selectWallpaper(wallpaper)
                    currentScreen = Screen.DETAIL
                }
            )

            Screen.DOWNLOADS -> DownloadsScreen(
                viewModel = viewModel,
                onBack = { currentScreen = Screen.HOME },
                onWallpaperClick = { wallpaper ->
                    viewModel.selectWallpaper(wallpaper)
                    currentScreen = Screen.DETAIL
                }
            )

            Screen.SOURCES -> SourcesScreen(
                viewModel = viewModel,
                onBack = { currentScreen = Screen.HOME },
                onSelectSource = { sourceId ->
                    viewModel.selectSource(sourceId)
                    currentScreen = Screen.HOME
                }
            )
        }
    }
}
