package com.example.data.repository

import com.example.data.local.DownloadHistoryEntity
import com.example.data.local.FavoriteEntity
import com.example.data.local.WallpaperDao
import com.example.data.model.Wallpaper
import com.example.data.sources.CuratedWallpaperDataset
import com.example.data.sources.SourceRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WallpaperRepository(
    private val wallpaperDao: WallpaperDao
) {
    // Exactly 10 Wallpapers of the Day
    fun getWallpapersOfTheDay(): List<Wallpaper> {
        return CuratedWallpaperDataset.wallpapersOfTheDay
    }

    suspend fun getTrendingWallpapers(sourceId: String? = null, page: Int = 1): List<Wallpaper> = withContext(Dispatchers.IO) {
        if (!sourceId.isNullOrEmpty() && sourceId != "all") {
            val source = SourceRegistry.getSourceById(sourceId)
            return@withContext source?.fetchTrending(page) ?: emptyList()
        }

        // Aggregate across multiple popular sources
        val results = mutableListOf<Wallpaper>()
        val primarySources = listOf("reddit_amoled", "wallhaven", "unsplash", "reddit_wallpaper", "pexels")
        for (sid in primarySources) {
            val s = SourceRegistry.getSourceById(sid)
            if (s != null) {
                try {
                    results.addAll(s.fetchTrending(page, 10))
                } catch (e: Exception) {
                    // Ignore individual source failure
                }
            }
        }
        if (results.isEmpty()) {
            results.addAll(CuratedWallpaperDataset.wallpapersOfTheDay)
        }
        results.distinctBy { it.id }
    }

    suspend fun searchWallpapers(query: String, sourceId: String? = null, page: Int = 1): List<Wallpaper> = withContext(Dispatchers.IO) {
        if (!sourceId.isNullOrEmpty() && sourceId != "all") {
            val source = SourceRegistry.getSourceById(sourceId)
            return@withContext source?.search(query, page) ?: emptyList()
        }

        val results = mutableListOf<Wallpaper>()
        for (source in SourceRegistry.getAllSources().take(8)) {
            try {
                results.addAll(source.search(query, page, 6))
            } catch (e: Exception) {
                // Ignore
            }
        }
        results.distinctBy { it.id }
    }

    suspend fun findWallpaperById(id: String): Wallpaper? = withContext(Dispatchers.IO) {
        // Check WOTD first
        CuratedWallpaperDataset.wallpapersOfTheDay.find { it.id == id }?.let { return@withContext it }

        // Check sources
        for (source in SourceRegistry.getAllSources()) {
            val items = try { source.fetchTrending(1, 20) } catch (e: Exception) { emptyList() }
            items.find { it.id == id }?.let { return@withContext it }
        }
        null
    }

    // Favorites
    fun getFavorites(): Flow<List<Wallpaper>> {
        return wallpaperDao.getAllFavorites().map { list ->
            list.map { it.toWallpaper() }
        }
    }

    fun isFavorite(id: String): Flow<Boolean> {
        return wallpaperDao.isFavorite(id)
    }

    suspend fun toggleFavorite(wallpaper: Wallpaper, isCurrentlyFavorite: Boolean) = withContext(Dispatchers.IO) {
        if (isCurrentlyFavorite) {
            wallpaperDao.deleteFavoriteById(wallpaper.id)
        } else {
            wallpaperDao.insertFavorite(FavoriteEntity.fromWallpaper(wallpaper))
        }
    }

    // Downloads
    fun getDownloads(): Flow<List<DownloadHistoryEntity>> {
        return wallpaperDao.getAllDownloads()
    }

    suspend fun recordDownload(
        wallpaper: Wallpaper,
        savedUri: String,
        width: Int,
        height: Int
    ) = withContext(Dispatchers.IO) {
        wallpaperDao.insertDownload(
            DownloadHistoryEntity(
                wallpaperId = wallpaper.id,
                title = wallpaper.title,
                savedUri = savedUri,
                width = width,
                height = height,
                thumbnailUrl = wallpaper.thumbnailUrl,
                source = wallpaper.source
            )
        )
    }
}
