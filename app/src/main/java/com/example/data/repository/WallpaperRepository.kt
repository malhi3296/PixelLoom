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
            val items = source?.fetchTrending(page, 25) ?: emptyList()
            if (items.isNotEmpty()) {
                return@withContext items
            }
            return@withContext CuratedWallpaperDataset.getWallpapersForCategory(source?.category?.label ?: sourceId, page, 25)
        }

        // Aggregate across multiple popular sources including Space & Wikipedia directly in main wallpapers
        val results = mutableListOf<Wallpaper>()
        val primarySources = listOf("reddit_amoled", "space_photography", "wikipedia", "wallhaven", "unsplash", "reddit_wallpaper", "pexels", "reddit_vertical", "reddit_mobile")
        for (sid in primarySources) {
            val s = SourceRegistry.getSourceById(sid)
            if (s != null) {
                try {
                    results.addAll(s.fetchTrending(page, 8))
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

        // Pure matching wallpapers for this specific category or keyword
        val categoryResults = CuratedWallpaperDataset.getWallpapersForCategory(cleanQuery, page, 25)

        // Query only sources that belong or specialize in this category
        val sourceMatches = mutableListOf<Wallpaper>()
        for (source in SourceRegistry.getAllSources()) {
            if (source.name.contains(cleanQuery, ignoreCase = true) ||
                source.shortName.contains(cleanQuery, ignoreCase = true) ||
                source.category.label.contains(cleanQuery, ignoreCase = true)) {
                try {
                    val res = source.search(cleanQuery, page, 8)
                    sourceMatches.addAll(res.filter {
                        it.title.contains(cleanQuery, ignoreCase = true) ||
                        it.category.contains(cleanQuery, ignoreCase = true) ||
                        it.tags.any { t -> t.contains(cleanQuery, ignoreCase = true) }
                    })
                } catch (e: Exception) {
                    // Ignore individual source failure
                }
            }
        }

        (categoryResults + sourceMatches).distinctBy { it.id }
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
