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
    val trendingWallpapers: List<Wallpaper> = emptyList(),
    val selectedSourceId: String = "all",
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
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

    private val _uiState = MutableStateFlow(WallpaperUiState())
    val uiState: StateFlow<WallpaperUiState> = _uiState.asStateFlow()

    private var searchJob: kotlinx.coroutines.Job? = null

    val favorites: StateFlow<List<Wallpaper>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val downloads: StateFlow<List<DownloadHistoryEntity>> = repository.getDownloads()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        val wotd = repository.getWallpapersOfTheDay()
        _uiState.update { it.copy(wallpapersOfTheDay = wotd, isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                // Fetch Wikipedia featured photography
                val wikiSource = SourceRegistry.getSourceById("wikipedia")
                val wikiPhotos = wikiSource?.fetchTrending(1, 12) ?: emptyList()
                _uiState.update { it.copy(wikipediaWallpapers = wikiPhotos) }

                val trending = repository.getTrendingWallpapers(sourceId = "all")
                _uiState.update { it.copy(trendingWallpapers = trending, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load wallpapers") }
            }
        }
    }

    fun selectSource(sourceId: String) {
        searchJob?.cancel()
        _uiState.update { it.copy(selectedSourceId = sourceId, isLoading = true, searchQuery = "", isSearching = false) }
        viewModelScope.launch {
            try {
                val wallpapers = repository.getTrendingWallpapers(sourceId = sourceId)
                _uiState.update { it.copy(trendingWallpapers = wallpapers, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Could not load source") }
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
        _uiState.update { it.copy(selectedWallpaper = wallpaper) }
    }

    fun selectWallpaperById(id: String) {
        viewModelScope.launch {
            val found = repository.findWallpaperById(id)
            if (found != null) {
                _uiState.update { it.copy(selectedWallpaper = found) }
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
                }
                _uiState.update { it.copy(isSettingWallpaper = false, actionMessage = "Successfully set as $targetName wallpaper!") }
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
                }
                _uiState.update { it.copy(isSettingWallpaper = false, actionMessage = "Cropped wallpaper applied to $targetName!") }
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
