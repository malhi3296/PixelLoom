package com.example.data.sources

import com.example.data.model.SourceCategory
import com.example.data.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WallhavenWallpaperSource(
    private val fallbackWallpapers: List<Wallpaper>
) : WallpaperSource {
    override val id: String = "wallhaven"
    override val name: String = "Wallhaven"
    override val shortName: String = "Wallhaven"
    override val category: SourceCategory = SourceCategory.ANIME_ILLUSTRATION
    override val description: String = "The premier anime, digital art, and scenic high-res wallpaper platform"
    override val websiteUrl: String = "https://wallhaven.cc"
    override val iconEmoji: String = "🌌"

    override suspend fun fetchTrending(page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        val url = "https://wallhaven.cc/api/v1/search?sorting=toplist&order=desc&page=$page"
        val live = parseWallhavenJson(url)
        if (live.isNotEmpty()) {
            live
        } else {
            fallbackWallpapers.mapIndexed { idx, it ->
                if (page > 1) it.copy(id = "${it.id}_p${page}_$idx", title = "${it.title} (Page $page)") else it
            }
        }
    }

    override suspend fun search(query: String, page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        val cleanQuery = query.trim().removePrefix("#")
        val encodedQuery = java.net.URLEncoder.encode(cleanQuery, "UTF-8")
        val url = "https://wallhaven.cc/api/v1/search?q=${encodedQuery}&page=$page"
        val live = parseWallhavenJson(url)
        if (live.isNotEmpty()) {
            live
        } else {
            fallbackWallpapers.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
            }
        }
    }

    private suspend fun parseWallhavenJson(url: String): List<Wallpaper> {
        val json = NetworkClient.getJson(url) ?: return emptyList()
        val results = mutableListOf<Wallpaper>()
        try {
            val data = json.getJSONArray("data")
            for (i in 0 until data.length()) {
                val item = data.getJSONObject(i)
                val id = item.optString("id", "$i")
                val path = item.optString("path", "")
                val thumbs = item.optJSONObject("thumbs")
                val thumbUrl = thumbs?.optString("large") ?: thumbs?.optString("original") ?: path
                val resX = item.optInt("dimension_x", 1920)
                val resY = item.optInt("dimension_y", 1080)
                val category = item.optString("category", "Anime")
                val views = item.optInt("views", 1200)
                val favorites = item.optInt("favorites", 250)
                val colorHex = item.optJSONArray("colors")?.optString(0)

                if (path.isNotEmpty()) {
                    results.add(
                        Wallpaper(
                            id = "wallhaven_$id",
                            title = "Artwork #$id",
                            thumbnailUrl = thumbUrl,
                            fullUrl = path,
                            source = name,
                            author = "Wallhaven Artist",
                            authorUrl = "https://wallhaven.cc/w/$id",
                            width = resX,
                            height = resY,
                            tags = listOf("Wallhaven", category, "HD Artwork"),
                            colorHex = colorHex,
                            category = "Digital Art",
                            viewsCount = views,
                            likesCount = favorites
                        )
                    )
                }
            }
        } catch (e: Exception) {
            // fallback
        }
        return results
    }
}
