package com.example.data.sources

import com.example.data.model.SourceCategory
import com.example.data.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RedditWallpaperSource(
    private val subreddit: String,
    override val id: String,
    override val name: String,
    override val shortName: String,
    override val description: String,
    override val iconEmoji: String,
    private val defaultTags: List<String>,
    private val fallbackWallpapers: List<Wallpaper>
) : WallpaperSource {
    override val category: SourceCategory = SourceCategory.COMMUNITY
    override val websiteUrl: String = "https://reddit.com/r/$subreddit"

    override suspend fun fetchTrending(page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        val limit = pageSize.coerceAtLeast(25)
        // Rotate through Reddit sorting endpoints across pages
        val url = when (page) {
            1 -> "https://www.reddit.com/r/$subreddit/hot.json?limit=$limit"
            2 -> "https://www.reddit.com/r/$subreddit/top.json?t=month&limit=$limit"
            3 -> "https://www.reddit.com/r/$subreddit/top.json?t=all&limit=$limit"
            else -> "https://www.reddit.com/r/$subreddit/new.json?limit=$limit"
        }
        val live = parseRedditJson(url)
        val pagedFallbacks = fallbackWallpapers.mapIndexed { idx, w ->
            if (page > 1) {
                w.copy(
                    id = "${w.id}_p${page}_$idx",
                    title = "${w.title} (Page $page)"
                )
            } else w
        }
        (live + pagedFallbacks).distinctBy { it.id }.take(pageSize)
    }

    override suspend fun search(query: String, page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        val cleanQuery = query.trim().removePrefix("#")
        val encodedQuery = java.net.URLEncoder.encode(cleanQuery, "UTF-8")
        val url = "https://www.reddit.com/r/$subreddit/search.json?q=${encodedQuery}&restrict_sr=1&limit=$pageSize"
        val live = parseRedditJson(url)
        val filteredFallbacks = fallbackWallpapers.filter {
            it.title.contains(cleanQuery, ignoreCase = true) ||
            it.tags.any { tag -> tag.contains(cleanQuery, ignoreCase = true) }
        }
        (live + filteredFallbacks).distinctBy { it.id }
    }

    private suspend fun parseRedditJson(url: String): List<Wallpaper> {
        val json = NetworkClient.getJson(url) ?: return emptyList()
        val results = mutableListOf<Wallpaper>()
        try {
            val data = json.getJSONObject("data")
            val children = data.getJSONArray("children")
            for (i in 0 until children.length()) {
                val child = children.getJSONObject(i).getJSONObject("data")
                val postUrl = child.optString("url", "")
                val title = child.optString("title", "Wallpaper")
                val author = "u/" + child.optString("author", "redditor")
                val ups = child.optInt("ups", 100)
                val id = child.optString("id", "${subreddit}_$i")
                val over18 = child.optBoolean("over_18", false)
                if (over18) continue // Filter NSFW

                val previewSource = child.optJSONObject("preview")
                    ?.getJSONArray("images")
                    ?.optJSONObject(0)
                    ?.getJSONObject("source")
                    ?.optString("url")
                    ?.replace("&amp;", "&")

                val isImage = postUrl.endsWith(".jpg", ignoreCase = true) ||
                              postUrl.endsWith(".png", ignoreCase = true) ||
                              postUrl.endsWith(".webp", ignoreCase = true) ||
                              postUrl.contains("i.redd.it") ||
                              !previewSource.isNullOrEmpty()

                if (isImage) {
                    val finalFullUrl = if (postUrl.contains("i.redd.it") || postUrl.endsWith(".jpg") || postUrl.endsWith(".png") || postUrl.endsWith(".webp")) {
                        postUrl
                    } else {
                        previewSource ?: postUrl
                    }
                    val previewUrl = previewSource ?: postUrl

                    // Extract resolution from title if present e.g. [1440x2560]
                    var width = 1080
                    var height = 2400
                    val resMatch = Regex("\\[?(\\d{3,4})\\s*[xX×]\\s*(\\d{3,4})\\]?").find(title)
                    if (resMatch != null) {
                        width = resMatch.groupValues[1].toIntOrNull() ?: 1080
                        height = resMatch.groupValues[2].toIntOrNull() ?: 2400
                    }

                    results.add(
                        Wallpaper(
                            id = "reddit_${subreddit}_$id",
                            title = title.replace(Regex("\\[.*?\\]"), "").trim().ifEmpty { "Community Pick" },
                            thumbnailUrl = previewUrl,
                            fullUrl = postUrl,
                            source = name,
                            author = author,
                            authorUrl = "https://reddit.com/$author",
                            width = width,
                            height = height,
                            tags = defaultTags + listOf(subreddit, "Reddit"),
                            category = if (subreddit.contains("Amoled", true)) "Amoled" else "Community",
                            likesCount = ups,
                            viewsCount = ups * 8
                        )
                    )
                }
            }
        } catch (e: Exception) {
            // Return any parsed results
        }
        return results
    }
}
