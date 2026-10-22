package com.example.data.sources

import com.example.data.model.SourceCategory
import com.example.data.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class SpaceWallpaperSource : WallpaperSource {
    override val id: String = "space_photography"
    override val name: String = "Space & NASA Astrophotography"
    override val shortName: String = "Space"
    override val category: SourceCategory = SourceCategory.PHOTOGRAPHY
    override val description: String = "James Webb, Hubble, NASA APOD, and Deep-Sky Cosmic Photography"
    override val websiteUrl: String = "https://apod.nasa.gov"
    override val iconEmoji: String = "🌌"

    // Curated high-resolution Deep Space, JWST, Hubble, and Astrophotography Masterpieces
    private val spaceWallpapers = listOf(
        Wallpaper(
            id = "space_jwst_carina",
            title = "Carina Nebula 'Cosmic Cliffs' (JWST)",
            thumbnailUrl = "https://images.unsplash.com/photo-1614728894747-a83421e2b9c9?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1614728894747-a83421e2b9c9?w=2400&auto=format&fit=crop&q=95",
            source = "NASA / JWST",
            author = "NASA, ESA, CSA, STScI",
            authorUrl = "https://webbtelescope.org",
            width = 2400,
            height = 3600,
            tags = listOf("Space", "JWST", "Nebula", "Carina", "Cosmic"),
            category = "Space",
            viewsCount = 112000,
            likesCount = 18400
        ),
        Wallpaper(
            id = "space_pillars_creation",
            title = "Pillars of Creation in Mid-Infrared (JWST)",
            thumbnailUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=2400&auto=format&fit=crop&q=95",
            source = "NASA / Hubble & Webb",
            author = "NASA, ESA, CSA, STScI",
            authorUrl = "https://nasa.gov",
            width = 2400,
            height = 3600,
            tags = listOf("Space", "PillarsOfCreation", "EagleNebula", "Stars"),
            category = "Space",
            viewsCount = 98000,
            likesCount = 15900
        ),
        Wallpaper(
            id = "space_andromeda_galaxy",
            title = "Andromeda Galaxy (M31) Spiral Arms",
            thumbnailUrl = "https://images.unsplash.com/photo-1502134249126-9f3755a50d78?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1502134249126-9f3755a50d78?w=2400&auto=format&fit=crop&q=95",
            source = "Astrophotography Archive",
            author = "Adam Evans (CC BY 2.0)",
            authorUrl = "https://apod.nasa.gov",
            width = 2160,
            height = 3840,
            tags = listOf("Space", "Andromeda", "Galaxy", "Stars", "Spiral"),
            category = "Space",
            viewsCount = 87000,
            likesCount = 14200
        ),
        Wallpaper(
            id = "space_saturn_cassini",
            title = "Saturn & Majestic Rings Backlit by the Sun",
            thumbnailUrl = "https://images.unsplash.com/photo-1614728423169-3f65fd722b7e?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1614728423169-3f65fd722b7e?w=2400&auto=format&fit=crop&q=95",
            source = "NASA / JPL-Caltech / SSI",
            author = "Cassini Imaging Team",
            authorUrl = "https://saturn.jpl.nasa.gov",
            width = 2400,
            height = 2400,
            tags = listOf("Space", "Saturn", "Cassini", "Planet", "Rings"),
            category = "Space",
            viewsCount = 91000,
            likesCount = 16800
        ),
        Wallpaper(
            id = "space_milkyway_atacama",
            title = "Milky Way Galaxy Arc Over Paranal Observatory",
            thumbnailUrl = "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=2400&auto=format&fit=crop&q=95",
            source = "European Southern Observatory",
            author = "Babak Tafreshi / ESO",
            authorUrl = "https://eso.org",
            width = 2160,
            height = 3840,
            tags = listOf("Space", "MilkyWay", "Atacama", "Astrophotography", "Stars"),
            category = "Space",
            viewsCount = 76000,
            likesCount = 11300
        ),
        Wallpaper(
            id = "space_orion_nebula",
            title = "The Great Orion Nebula (M42) Nursery",
            thumbnailUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=2400&auto=format&fit=crop&q=95",
            source = "NASA / STScI",
            author = "Hubble Space Telescope",
            authorUrl = "https://hubblesite.org",
            width = 2400,
            height = 3600,
            tags = listOf("Space", "Orion", "Nebula", "DeepSky", "Cosmos"),
            category = "Space",
            viewsCount = 83000,
            likesCount = 13500
        ),
        Wallpaper(
            id = "space_deep_field",
            title = "Webb's First Deep Field (SMACS 0723)",
            thumbnailUrl = "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=2400&auto=format&fit=crop&q=95",
            source = "NASA / ESA / CSA",
            author = "James Webb Space Telescope",
            authorUrl = "https://webbtelescope.org",
            width = 2400,
            height = 3200,
            tags = listOf("Space", "DeepField", "JWST", "Galaxies", "GravitationalLensing"),
            category = "Space",
            viewsCount = 104000,
            likesCount = 19200
        ),
        Wallpaper(
            id = "space_solar_eclipse",
            title = "Total Solar Eclipse Corona & Diamond Ring",
            thumbnailUrl = "https://images.unsplash.com/photo-1538370965046-79c0d6907d47?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1538370965046-79c0d6907d47?w=2400&auto=format&fit=crop&q=95",
            source = "NASA Eclipse Expedition",
            author = "Aubrey Gemignani / NASA",
            authorUrl = "https://nasa.gov",
            width = 2160,
            height = 3840,
            tags = listOf("Space", "Sun", "SolarEclipse", "Corona", "Astronomy"),
            category = "Space",
            viewsCount = 67000,
            likesCount = 9400
        ),
        Wallpaper(
            id = "space_jupiter_juno",
            title = "Jupiter Jovian Clouds & Swirls (JunoCam)",
            thumbnailUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=2400&auto=format&fit=crop&q=95",
            source = "NASA / JPL-Caltech / SwRI / MSSS",
            author = "Gerald Eichstädt & Seán Doran",
            authorUrl = "https://www.missionjuno.swri.edu",
            width = 2400,
            height = 3000,
            tags = listOf("Space", "Jupiter", "Juno", "Planet", "GasGiant"),
            category = "Space",
            viewsCount = 79000,
            likesCount = 12700
        ),
        Wallpaper(
            id = "space_helix_nebula",
            title = "The Helix Nebula 'Eye of God' (NGC 7293)",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=2400&auto=format&fit=crop&q=95",
            source = "NASA / JPL-Caltech / ESO",
            author = "C.R. O'Dell (Vanderbilt Univ.)",
            authorUrl = "https://hubblesite.org",
            width = 2400,
            height = 3600,
            tags = listOf("Space", "HelixNebula", "EyeOfGod", "PlanetaryNebula"),
            category = "Space",
            viewsCount = 89000,
            likesCount = 14800
        )
    )

    override suspend fun fetchTrending(page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        // Return curated space collection with rotation on pages
        val start = ((page - 1) * pageSize) % spaceWallpapers.size
        val items = mutableListOf<Wallpaper>()
        for (i in 0 until pageSize) {
            val idx = (start + i) % spaceWallpapers.size
            items.add(spaceWallpapers[idx])
        }
        items.distinctBy { it.id }
    }

    override suspend fun search(query: String, page: Int, pageSize: Int): List<Wallpaper> = withContext(Dispatchers.IO) {
        val clean = query.trim().lowercase()
        val matches = spaceWallpapers.filter {
            it.title.lowercase().contains(clean) ||
            it.tags.any { tag -> tag.lowercase().contains(clean) } ||
            it.author.lowercase().contains(clean) ||
            clean.contains("space") || clean.contains("nasa") || clean.contains("astronomy") || clean.contains("cosmic")
        }
        if (matches.isNotEmpty()) matches.take(pageSize) else emptyList()
    }
}
