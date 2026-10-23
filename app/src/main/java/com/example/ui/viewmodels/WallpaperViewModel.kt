package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.PixelLoomApp
import com.example.data.local.DownloadHistoryEntity
import com.example.data.model.Wallpaper
import com.example.data.model.WallpaperDimension
import com.example.data.repository.WallpaperActionManager
import com.example.data.repository.WallpaperRepository
import com.example.data.repository.WallpaperTarget
import com.example.data.sources.SourceRegistry
import com.example.worker.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WallpaperUiState(
    val wallpapersOfTheDay: List<Wallpaper> = emptyList(),
    val wikipediaWallpapers: List<Wallpaper> = emptyList(),
    val spaceWallpapers: List<Wallpaper> = emptyList(),
    val trendingWallpapers: List<Wallpaper> = emptyList(),
    val activeWallpapers: List<Wallpaper> = emptyList(),
    val selectedWallpaperIndex: Int = 0,
    val selectedSourceId: String = "all",
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val errorMessage: String? = null,
    val selectedWallpaper: Wallpaper? = null,
    val isSettingWallpaper: Boolean = false,
    val actionMessage: String? = null,
    val isDownloading: Boolean = false
)

class WallpaperViewModel(
    private val repository: WallpaperRepository,
    private val actionManager: WallpaperActionManager
) : ViewModel() {

    companion object {
        private const val PREFS_NAME = "pixelloom_app_prefs"
        private const val KEY_LAST_WOTD_REFRESH = "last_wotd_refresh_time"
        private const val WOTD_24_HOURS_MS = 24 * 60 * 60 * 1000L
    }

    private val _uiState = MutableStateFlow(WallpaperUiState())
    val uiState: StateFlow<WallpaperUiState> = _uiState.asStateFlow()

    private var searchJob: kotlinx.coroutines.Job? = null
    private var lastAppOpenRefreshTime = 0L
    private var currentPage = 1

    val favorites: StateFlow<List<Wallpaper>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val downloads: StateFlow<List<DownloadHistoryEntity>> = repository.getDownloads()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        onAppOpened(forceRefreshAll = true)
    }

    /**
     * Refreshes all wallpapers on every app opening, EXCEPT Wallpapers of the Day
     * which are preserved and refreshed strictly every 24 hours.
     */
    fun onAppOpened(forceRefreshAll: Boolean = false) {
        val now = System.currentTimeMillis()
        if (!forceRefreshAll && now - lastAppOpenRefreshTime < 1500L) {
            return
        }
        lastAppOpenRefreshTime = now

        viewModelScope.launch {
            try {
                // 1. Wallpapers of the Day: Refresh ONLY if 24 hours have elapsed
                val prefs = PixelLoomApp.instance.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
                val lastWotdTime = prefs.getLong(KEY_LAST_WOTD_REFRESH, 0L)
                val isWotdExpired = (now - lastWotdTime) >= WOTD_24_HOURS_MS || _uiState.value.wallpapersOfTheDay.isEmpty()

                if (isWotdExpired) {
                    val freshWotd = repository.getWallpapersOfTheDay()
                    _uiState.update { it.copy(wallpapersOfTheDay = freshWotd) }
                    prefs.edit().putLong(KEY_LAST_WOTD_REFRESH, now).apply()
                } else if (_uiState.value.wallpapersOfTheDay.isEmpty()) {
                    val currentWotd = repository.getWallpapersOfTheDay()
                    _uiState.update { it.copy(wallpapersOfTheDay = currentWotd) }
                }

                // 2. All other wallpapers: ALWAYS refreshed every time the app opens
                val wikiSource = SourceRegistry.getSourceById("wikipedia")
                val wikiPhotos = wikiSource?.fetchTrending(page = (1..3).random(), pageSize = 12) ?: emptyList()

                val spaceSource = SourceRegistry.getSourceById("space_photography")
                val spacePhotos = spaceSource?.fetchTrending(page = (1..3).random(), pageSize = 12) ?: emptyList()

                val freshTrending = repository.getTrendingWallpapers(
                    sourceId = _uiState.value.selectedSourceId,
                    page = (1..2).random(),
                    shuffleResults = true
                )

                _uiState.update {
                    it.copy(
                        wikipediaWallpapers = wikiPhotos,
                        spaceWallpapers = spacePhotos,
                        trendingWallpapers = freshTrending,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load wallpapers") }
            }
        }
    }

    fun selectSource(sourceId: String) {
        searchJob?.cancel()
        currentPage = 1
        _uiState.update { it.copy(selectedSourceId = sourceId, isLoading = true, searchQuery = "", isSearching = false, canLoadMore = true) }
        viewModelScope.launch {
            try {
                val wallpapers = repository.getTrendingWallpapers(sourceId = sourceId, page = 1)
                _uiState.update { it.copy(trendingWallpapers = wallpapers, isLoading = false, canLoadMore = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Could not load source") }
            }
        }
    }

    fun selectCategory(categoryLabel: String) {
        val clean = categoryLabel.trim().substringAfter(" ").removePrefix("#")
        if (categoryLabel.contains("Trending", ignoreCase = true) || clean.equals("All", ignoreCase = true)) {
            clearSearch()
            return
        }
        searchJob?.cancel()
        currentPage = 1
        _uiState.update { it.copy(searchQuery = clean, isSearching = true, isLoading = true, canLoadMore = true) }
        viewModelScope.launch {
            try {
                val results = repository.searchWallpapers(clean, null, 1)
                _uiState.update { it.copy(trendingWallpapers = results, isLoading = false, canLoadMore = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Category search failed") }
            }
        }
    }

    fun loadMoreWallpapers() {
        val state = _uiState.value
        if (state.isLoadingMore || !state.canLoadMore) return

        _uiState.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            try {
                val nextPage = currentPage + 1
                val newItems = if (state.isSearching) {
                    repository.searchWallpapers(state.searchQuery, state.selectedSourceId, nextPage)
                } else {
                    repository.getTrendingWallpapers(
                        sourceId = state.selectedSourceId,
                        page = nextPage,
                        shuffleResults = false
                    )
                }
                if (newItems.isNotEmpty()) {
                    currentPage = nextPage
                    _uiState.update {
                        val combined = (it.trendingWallpapers + newItems).distinctBy { w -> w.id }
                        it.copy(
                            trendingWallpapers = combined,
                            isLoadingMore = false,
                            canLoadMore = true // Infinite scroll continues!
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoadingMore = false, canLoadMore = true) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingMore = false) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.update { it.copy(isSearching = false) }
            selectSource(_uiState.value.selectedSourceId)
            return
        }
        searchJob = viewModelScope.launch {
            kotlinx.coroutines.delay(280)
            executeSearch(query)
        }
    }

    fun executeSearch(query: String) {
        val clean = query.trim()
        if (clean.isBlank()) {
            _uiState.update { it.copy(isSearching = false) }
            selectSource(_uiState.value.selectedSourceId)
            return
        }
        _uiState.update { it.copy(isSearching = true, isLoading = true) }
        viewModelScope.launch {
            try {
                val results = repository.searchWallpapers(clean, _uiState.value.selectedSourceId)
                _uiState.update { it.copy(trendingWallpapers = results, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Search failed") }
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _uiState.update { it.copy(searchQuery = "", isSearching = false) }
        selectSource(_uiState.value.selectedSourceId)
    }

    fun selectWallpaper(wallpaper: Wallpaper) {
        val list = _uiState.value.trendingWallpapers
        val idx = list.indexOfFirst { it.id == wallpaper.id }
        val activeList = if (idx >= 0) list else listOf(wallpaper)
        val finalIdx = if (idx >= 0) idx else 0
        _uiState.update {
            it.copy(
                selectedWallpaper = wallpaper,
                activeWallpapers = activeList,
                selectedWallpaperIndex = finalIdx
            )
        }
    }

    fun selectWallpaperWithList(wallpaper: Wallpaper, list: List<Wallpaper>) {
        val idx = list.indexOfFirst { it.id == wallpaper.id }
        val activeList = if (idx >= 0) list else (listOf(wallpaper) + list)
        val finalIdx = if (idx >= 0) idx else 0
        _uiState.update {
            it.copy(
                selectedWallpaper = wallpaper,
                activeWallpapers = activeList,
                selectedWallpaperIndex = finalIdx
            )
        }
    }

    fun updateSelectedWallpaperIndex(index: Int) {
        val list = _uiState.value.activeWallpapers
        if (index in list.indices) {
            _uiState.update {
                it.copy(
                    selectedWallpaperIndex = index,
                    selectedWallpaper = list[index]
                )
            }
        }
    }

    fun selectWallpaperById(id: String) {
        viewModelScope.launch {
            val found = repository.findWallpaperById(id)
            if (found != null) {
                selectWallpaper(found)
            }
        }
    }

    fun toggleFavorite(wallpaper: Wallpaper) {
        val isFav = favorites.value.any { it.id == wallpaper.id }
        viewModelScope.launch {
            repository.toggleFavorite(wallpaper, isFav)
        }
    }

    fun setWallpaper(target: WallpaperTarget) {
        val wallpaper = _uiState.value.selectedWallpaper ?: return
        _uiState.update { it.copy(isSettingWallpaper = true, actionMessage = null) }
        viewModelScope.launch {
            val result = actionManager.setWallpaper(wallpaper.fullUrl, target)
            if (result.isSuccess) {
                val targetName = when (target) {
                    WallpaperTarget.HOME -> "Home screen"
                    WallpaperTarget.LOCK -> "Lock screen"
                    WallpaperTarget.BOTH -> "Home & Lock screens"
                    WallpaperTarget.EXTERNAL -> "Gallery app"
                }
                val msg = if (target == WallpaperTarget.EXTERNAL) "Opened in wallpaper chooser!" else "Successfully set as $targetName wallpaper!"
                _uiState.update { it.copy(isSettingWallpaper = false, actionMessage = msg) }
            } else {
                _uiState.update { it.copy(isSettingWallpaper = false, actionMessage = "Error setting wallpaper: ${result.exceptionOrNull()?.localizedMessage}") }
            }
        }
    }

    fun setCroppedWallpaper(croppedBitmap: android.graphics.Bitmap, target: WallpaperTarget) {
        _uiState.update { it.copy(isSettingWallpaper = true, actionMessage = null) }
        viewModelScope.launch {
            val result = actionManager.setCroppedWallpaper(croppedBitmap, target)
            if (result.isSuccess) {
                val targetName = when (target) {
                    WallpaperTarget.HOME -> "Home screen"
                    WallpaperTarget.LOCK -> "Lock screen"
                    WallpaperTarget.BOTH -> "Home & Lock screens"
                    WallpaperTarget.EXTERNAL -> "Gallery app"
                }
                val msg = if (target == WallpaperTarget.EXTERNAL) "Opened cropped wallpaper in chooser!" else "Cropped wallpaper applied to $targetName!"
                _uiState.update { it.copy(isSettingWallpaper = false, actionMessage = msg) }
            } else {
                _uiState.update { it.copy(isSettingWallpaper = false, actionMessage = "Error setting cropped wallpaper: ${result.exceptionOrNull()?.localizedMessage}") }
            }
        }
    }

    suspend fun loadBitmapForCropping(url: String): android.graphics.Bitmap? {
        return actionManager.fetchBitmap(url)
    }

    fun downloadWithDimensions(targetWidth: Int?, targetHeight: Int?) {
        val wallpaper = _uiState.value.selectedWallpaper ?: return
        _uiState.update { it.copy(isDownloading = true, actionMessage = null) }
        viewModelScope.launch {
            val result = actionManager.downloadWallpaper(wallpaper, targetWidth, targetHeight)
            if (result.isSuccess) {
                val resLabel = if (targetWidth != null && targetHeight != null) "${targetWidth}x${targetHeight}" else "Original Resolution"
                _uiState.update { it.copy(isDownloading = false, actionMessage = "Saved to Pictures/PixelLoom ($resLabel)") }
            } else {
                _uiState.update { it.copy(isDownloading = false, actionMessage = "Download failed: ${result.exceptionOrNull()?.localizedMessage}") }
            }
        }
    }

    fun triggerTestNotification() {
        NotificationScheduler.triggerImmediateTest(PixelLoomApp.instance)
        _uiState.update { it.copy(actionMessage = "Daily Inspiration notification dispatched!") }
    }

    fun dismissActionMessage() {
        _uiState.update { it.copy(actionMessage = null) }
    }

    class Factory(
        private val repository: WallpaperRepository,
        private val actionManager: WallpaperActionManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WallpaperViewModel(repository, actionManager) as T
        }
    }
}
