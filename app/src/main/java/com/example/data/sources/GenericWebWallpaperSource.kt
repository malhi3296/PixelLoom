package com.example.data.sources

import com.example.data.model.SourceCategory
import com.example.data.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class GenericWebWallpaperSource(
    override val id: String,
    override val name: String,
    override val shortName: String,
    override val category: SourceCategory,
    override val description: String,
    override val websiteUrl: String,
    override val iconEmoji: String,
    private val defaultTags: List<String>,
    private val seedWallpapers: List<Wallpaper>,
    private val scrapeSelector: String? = null
) : WallpaperSource {

    override suspend fun fetchTrending(page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        if (scrapeSelector != null) {
            val scraped = attemptScrape(websiteUrl, scrapeSelector)
            if (scraped.isNotEmpty()) return@withContext scraped
        }
        seedWallpapers
    }

    override suspend fun search(query: String, page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        val filtered = seedWallpapers.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.tags.any { t -> t.contains(query, ignoreCase = true) } ||
            it.category.contains(query, ignoreCase = true)
        }
        if (filtered.isNotEmpty()) filtered else seedWallpapers
    }

    private fun attemptScrape(url: String, selector: String): List<Wallpaper> {
        return try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 Chrome/120.0 Mobile Safari/537.36")
                .timeout(6000)
                .get()

            val elements = doc.select(selector)
            val scraped = mutableListOf<Wallpaper>()
            elements.forEachIndexed { idx, element ->
                val imgSrc = element.attr("src").ifEmpty { element.attr("data-src") }
                val alt = element.attr("alt").ifEmpty { "$name Wallpaper #$idx" }
                if (imgSrc.startsWith("http")) {
                    scraped.add(
                        Wallpaper(
                            id = "${id}_scraped_$idx",
                            title = alt,
                            thumbnailUrl = imgSrc,
                            fullUrl = imgSrc,
                            source = name,
                            author = "$name Curator",
                            authorUrl = websiteUrl,
                            width = 1080,
                            height = 2400,
                            tags = defaultTags,
                            category = category.label
                        )
                    )
                }
            }
            scraped
        } catch (e: Exception) {
            emptyList()
        }
    }
}
