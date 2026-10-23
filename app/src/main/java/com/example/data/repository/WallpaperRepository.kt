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
    // Exactly 10 Wallpapers of the Day (refreshes strictly every 24 hours based on day cycle)
    fun getWallpapersOfTheDay(dayOfYear: Int = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)): List<Wallpaper> {
        val allPicks = CuratedWallpaperDataset.wallpapersOfTheDay
        if (allPicks.isEmpty()) return emptyList()
        val offset = (dayOfYear * 3) % allPicks.size
        return (0 until minOf(10, allPicks.size)).map { i ->
            val idx = (offset + i) % allPicks.size
            allPicks[idx]
        }
    }

    suspend fun getTrendingWallpapers(sourceId: String? = null, page: Int = 1, shuffleResults: Boolean = false): List<Wallpaper> = withContext(Dispatchers.IO) {
        if (!sourceId.isNullOrEmpty() && sourceId != "all") {
            val source = SourceRegistry.getSourceById(sourceId)
            return@withContext source?.fetchTrending(page, 30) ?: emptyList()
        }

        // Aggregate across multiple popular sources including Space & Wikipedia directly in main wallpapers
        val results = mutableListOf<Wallpaper>()
        val primarySources = listOf("reddit_amoled", "space_photography", "wikipedia", "wallhaven", "unsplash", "reddit_wallpaper", "pexels", "reddit_vertical", "reddit_mobile")
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
        val distinct = results.distinctBy { it.id }
        if (shuffleResults) distinct.shuffled() else distinct
    }

    suspend fun searchWallpapers(query: String, sourceId: String? = null, page: Int = 1): List<Wallpaper> = withContext(Dispatchers.IO) {
        val cleanQuery = query.trim().removePrefix("#")
        if (cleanQuery.isBlank()) return@withContext emptyList()

        if (!sourceId.isNullOrEmpty() && sourceId != "all") {
            val source = SourceRegistry.getSourceById(sourceId)
            return@withContext source?.search(cleanQuery, page) ?: emptyList()
        }

        val results = mutableListOf<Wallpaper>()

        // 1. Instant match against Curated Wallpapers & WOTD
        val localMatches = CuratedWallpaperDataset.wallpapersOfTheDay.filter {
            it.title.contains(cleanQuery, ignoreCase = true) ||
            it.category.contains(cleanQuery, ignoreCase = true) ||
            it.tags.any { tag -> tag.contains(cleanQuery, ignoreCase = true) } ||
            it.author.contains(cleanQuery, ignoreCase = true) ||
            it.source.contains(cleanQuery, ignoreCase = true)
        }
        results.addAll(localMatches)

        // 2. Query registered sources (including Wikipedia, Reddit, Wallhaven)
        val sourcesToQuery = SourceRegistry.getAllSources().take(10)
        for (source in sourcesToQuery) {
            try {
                val sourceResults = source.search(cleanQuery, page, 6)
                results.addAll(sourceResults)
            } catch (e: Exception) {
                // Ignore individual source failure
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
