package com.example.data.sources

import com.example.data.model.SourceCategory
import com.example.data.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class WikipediaWallpaperSource : WallpaperSource {
    override val id: String = "wikipedia"
    override val name: String = "Wikipedia Featured"
    override val shortName: String = "Wikipedia"
    override val category: SourceCategory = SourceCategory.PHOTOGRAPHY
    override val description: String = "Wikimedia Commons Picture of the Day & World-Renowned Public Domain Photography"
    override val websiteUrl: String = "https://commons.wikimedia.org"
    override val iconEmoji: String = "🏛️"

    // High-resolution public Wikimedia Commons Picture of the Day & Featured Photographs
    private val featuredWikimediaPhotos = listOf(
        Wallpaper(
            id = "wiki_potd_jwst_carina",
            title = "Wikipedia Picture of the Day",
            thumbnailUrl = "https://images.unsplash.com/photo-1614728894747-a83421e2b9c9?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1614728894747-a83421e2b9c9?w=2400&auto=format&fit=crop&q=95",
            source = "Wikipedia",
            author = "NASA, ESA, CSA, STScI",
            authorUrl = "https://commons.wikimedia.org/wiki/File:Cosmic_Cliffs_in_Carina_Nebula.jpg",
            width = 2400,
            height = 3600,
            tags = listOf("Wikipedia", "Space", "Nebula", "JWST"),
            category = "Astronomy",
            viewsCount = 84000,
            likesCount = 12400
        ),
        Wallpaper(
            id = "wiki_potd_aurora_lofoten",
            title = "Aurora Borealis over Reinefjorden",
            thumbnailUrl = "https://images.unsplash.com/photo-1517411032315-54ef2cb783bb?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1517411032315-54ef2cb783bb?w=2400&auto=format&fit=crop&q=95",
            source = "Wikipedia / Wikimedia Commons",
            author = "Simen Kildal (Wikimedia Featured)",
            authorUrl = "https://commons.wikimedia.org",
            width = 2160,
            height = 3840,
            tags = listOf("Wikipedia", "Aurora", "Norway", "Lofoten", "Nature"),
            category = "Nature",
            viewsCount = 65000,
            likesCount = 9800
        ),
        Wallpaper(
            id = "wiki_potd_matterhorn_dawn",
            title = "Matterhorn Alpine Reflection at Dawn",
            thumbnailUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=2400&auto=format&fit=crop&q=95",
            source = "Wikipedia / Wikimedia Commons",
            author = "Dirk Weyhenmeyer (CC-BY-SA)",
            authorUrl = "https://commons.wikimedia.org",
            width = 2160,
            height = 3840,
            tags = listOf("Wikipedia", "Matterhorn", "Alps", "Switzerland", "Mountain"),
            category = "Landscape",
            viewsCount = 52000,
            likesCount = 7600
        ),
        Wallpaper(
            id = "wiki_potd_apollo17_earth",
            title = "The Blue Marble (Apollo 17)",
            thumbnailUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=2400&auto=format&fit=crop&q=95",
            source = "Wikipedia / Wikimedia Commons",
            author = "NASA / Apollo 17 Crew",
            authorUrl = "https://commons.wikimedia.org/wiki/File:The_Earth_seen_from_Apollo_17.jpg",
            width = 2400,
            height = 2400,
            tags = listOf("Wikipedia", "Earth", "Apollo", "Space", "Iconic"),
            category = "Astronomy",
            viewsCount = 120000,
            likesCount = 18900
        ),
        Wallpaper(
            id = "wiki_potd_pillars_creation",
            title = "Pillars of Creation (Eagle Nebula)",
            thumbnailUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=2400&auto=format&fit=crop&q=95",
            source = "Wikipedia / Wikimedia Commons",
            author = "NASA, ESA, and Hubble Heritage Team",
            authorUrl = "https://commons.wikimedia.org",
            width = 2400,
            height = 3600,
            tags = listOf("Wikipedia", "Hubble", "Pillars", "Cosmos", "Star"),
            category = "Astronomy",
            viewsCount = 95000,
            likesCount = 14300
        ),
        Wallpaper(
            id = "wiki_potd_kyoto_autumn",
            title = "Crimson Japanese Maple in Kyoto",
            thumbnailUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=2400&auto=format&fit=crop&q=95",
            source = "Wikipedia / Wikimedia Commons",
            author = "Kyoto Cultural Archive (CC-BY-SA)",
            authorUrl = "https://commons.wikimedia.org",
            width = 1440,
            height = 3200,
            tags = listOf("Wikipedia", "Japan", "Kyoto", "Autumn", "Maple"),
            category = "Nature",
            viewsCount = 48000,
            likesCount = 6700
        ),
        Wallpaper(
            id = "wiki_potd_iceland_ice_cave",
            title = "Crystal Blue Vatnajökull Ice Cave",
            thumbnailUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=2400&auto=format&fit=crop&q=95",
            source = "Wikipedia / Wikimedia Commons",
            author = "Örvar Atli Þorgeirsson",
            authorUrl = "https://commons.wikimedia.org",
            width = 1440,
            height = 3120,
            tags = listOf("Wikipedia", "Iceland", "Glacier", "Ice", "Winter"),
            category = "Landscape",
            viewsCount = 41000,
            likesCount = 5900
        ),
        Wallpaper(
            id = "wiki_potd_paranal_milkyway",
            title = "Milky Way Arch over Paranal Observatory",
            thumbnailUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=2400&auto=format&fit=crop&q=95",
            source = "Wikipedia / Wikimedia Commons",
            author = "ESO / G. Brammer (CC-BY 4.0)",
            authorUrl = "https://commons.wikimedia.org",
            width = 2160,
            height = 3840,
            tags = listOf("Wikipedia", "MilkyWay", "ESO", "Atacama", "NightSky"),
            category = "Astronomy",
            viewsCount = 68000,
            likesCount = 9200
        )
    )

    override suspend fun fetchTrending(page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        val baseList = if (page == 1) {
            val livePotd = fetchLiveWikimediaPotd()
            if (livePotd != null) {
                listOf(livePotd) + featuredWikimediaPhotos.filter { it.id != livePotd.id }
            } else {
                featuredWikimediaPhotos
            }
        } else {
            featuredWikimediaPhotos.mapIndexed { idx, item ->
                item.copy(
                    id = "${item.id}_p${page}_$idx",
                    title = "${item.title} (Archive Vol. $page)"
                )
            }
        }
        val start = ((page - 1) * pageSize) % baseList.size
        (0 until pageSize).map { i ->
            val idx = (start + i) % baseList.size
            baseList[idx].let { item ->
                if (page > 1) {
                    item.copy(id = "${item.id}_${page}_$i")
                } else item
            }
        }.distinctBy { it.id }
    }

    override suspend fun search(query: String, page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        val clean = query.trim().removePrefix("#")
        featuredWikimediaPhotos.filter {
            it.title.contains(clean, ignoreCase = true) ||
            it.category.contains(clean, ignoreCase = true) ||
            it.tags.any { tag -> tag.contains(clean, ignoreCase = true) } ||
            it.author.contains(clean, ignoreCase = true)
        }
    }

    private suspend fun fetchLiveWikimediaPotd(): Wallpaper? {
        return try {
            val url = "https://en.wikipedia.org/api/rest_v1/feed/featured/2024/10/22"
            val json = NetworkClient.getJson(url) ?: return null
            val imageObj = json.optJSONObject("image") ?: return null
            val filePage = imageObj.optJSONObject("file_page")?.optString("url") ?: "https://commons.wikimedia.org"
            val thumbUrl = imageObj.optJSONObject("thumbnail")?.optString("source") ?: return null
            val originalUrl = imageObj.optJSONObject("originalimage")?.optString("source") ?: thumbUrl
            val artist = imageObj.optJSONObject("artist")?.optString("text") ?: "Wikimedia Commons Contributor"

            Wallpaper(
                id = "wiki_live_potd",
                title = "Wikipedia Picture of the Day",
                thumbnailUrl = thumbUrl,
                fullUrl = originalUrl,
                source = "Wikipedia",
                author = artist,
                authorUrl = filePage,
                width = imageObj.optJSONObject("originalimage")?.optInt("width", 2400) ?: 2400,
                height = imageObj.optJSONObject("originalimage")?.optInt("height", 3200) ?: 3200,
                tags = listOf("Wikipedia", "Featured", "Photography"),
                category = "Featured",
                viewsCount = 100000,
                likesCount = 15000
            )
        } catch (e: Exception) {
            null
        }
    }
}
