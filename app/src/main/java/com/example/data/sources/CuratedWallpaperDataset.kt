package com.example.data.sources

import com.example.data.model.Wallpaper

object CuratedWallpaperDataset {

    // 10 Curated Wallpapers of the Day (high resolution, diverse styles: nature, cyberpunk, space, amoled, architecture, abstract, anime, minimal)
    val wallpapersOfTheDay: List<Wallpaper> = listOf(
        Wallpaper(
            id = "wotd_1",
            title = "Ethereal Neon Peaks",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=2400&auto=format&fit=crop&q=95",
            source = "Unsplash",
            author = "Alexandre Debiève",
            authorUrl = "https://unsplash.com/@alexandre_debieve",
            width = 1440,
            height = 3200,
            tags = listOf("Neon", "Mountains", "Cyberpunk", "Landscape"),
            category = "Cyberpunk",
            viewsCount = 14200,
            likesCount = 3840,
            isDailyPick = true
        ),
        Wallpaper(
            id = "wotd_2",
            title = "Deep Space Nebula Genesis",
            thumbnailUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=2400&auto=format&fit=crop&q=95",
            source = "r/Amoledbackgrounds",
            author = "u/StellarVoyager",
            authorUrl = "https://reddit.com/r/Amoledbackgrounds",
            width = 1440,
            height = 3120,
            tags = listOf("Space", "Nebula", "Amoled", "Cosmic"),
            category = "Amoled",
            viewsCount = 28900,
            likesCount = 5900,
            isDailyPick = true
        ),
        Wallpaper(
            id = "wotd_3",
            title = "Emerald Aurora Over Nordic Fjords",
            thumbnailUrl = "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=2400&auto=format&fit=crop&q=95",
            source = "InterfaceLIFT",
            author = "Sverre Haugland",
            authorUrl = "https://interfacelift.com",
            width = 2160,
            height = 3840,
            tags = listOf("Aurora", "Nature", "Norway", "Fjords"),
            category = "Nature",
            viewsCount = 19400,
            likesCount = 4200,
            isDailyPick = true
        ),
        Wallpaper(
            id = "wotd_4",
            title = "Tokyo Rain Reflections",
            thumbnailUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=2400&auto=format&fit=crop&q=95",
            source = "Wallhaven",
            author = "Kenjiro Takahashi",
            authorUrl = "https://wallhaven.cc",
            width = 1080,
            height = 2400,
            tags = listOf("Tokyo", "Cityscape", "Rain", "Aesthetic"),
            category = "Urban",
            viewsCount = 33100,
            likesCount = 8900,
            isDailyPick = true
        ),
        Wallpaper(
            id = "wotd_5",
            title = "Golden Dunes of Rub' al Khali",
            thumbnailUrl = "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?w=2400&auto=format&fit=crop&q=95",
            source = "Pexels",
            author = "Karim El-Sayed",
            authorUrl = "https://pexels.com",
            width = 1440,
            height = 2960,
            tags = listOf("Desert", "Minimal", "Gold", "Warm"),
            category = "Minimal",
            viewsCount = 12500,
            likesCount = 3100,
            isDailyPick = true
        ),
        Wallpaper(
            id = "wotd_6",
            title = "Geometric Prismatic Prism",
            thumbnailUrl = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?w=2400&auto=format&fit=crop&q=95",
            source = "Simple Desktops",
            author = "Tom Anders",
            authorUrl = "http://simpledesktops.com",
            width = 1440,
            height = 3200,
            tags = listOf("Abstract", "Gradient", "Clean", "Geometric"),
            category = "Abstract",
            viewsCount = 15800,
            likesCount = 4750,
            isDailyPick = true
        ),
        Wallpaper(
            id = "wotd_7",
            title = "Misty Forest of Ancient Cedars",
            thumbnailUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?w=2400&auto=format&fit=crop&q=95",
            source = "StockSnap.io",
            author = "Elena Rostova",
            authorUrl = "https://stocksnap.io",
            width = 1080,
            height = 2400,
            tags = listOf("Forest", "Fog", "Moody", "Evergreen"),
            category = "Nature",
            viewsCount = 22100,
            likesCount = 6120,
            isDailyPick = true
        ),
        Wallpaper(
            id = "wotd_8",
            title = "Futuristic Orbital Habitat",
            thumbnailUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=2400&auto=format&fit=crop&q=95",
            source = "Wallpaper Alpha Coders",
            author = "Arthur C. Clarke Society",
            authorUrl = "https://alphacoders.com",
            width = 2160,
            height = 3840,
            tags = listOf("Sci-Fi", "Earth", "Orbit", "Space"),
            category = "Sci-Fi",
            viewsCount = 37000,
            likesCount = 9800,
            isDailyPick = true
        ),
        Wallpaper(
            id = "wotd_9",
            title = "Brutalist Concrete & Azure Sky",
            thumbnailUrl = "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=2400&auto=format&fit=crop&q=95",
            source = "Designspiration",
            author = "Maya Lin Studio",
            authorUrl = "https://designspiration.com",
            width = 1080,
            height = 2340,
            tags = listOf("Architecture", "Minimal", "Sky", "Structure"),
            category = "Architecture",
            viewsCount = 11400,
            likesCount = 2890,
            isDailyPick = true
        ),
        Wallpaper(
            id = "wotd_10",
            title = "Cherry Blossom Shrine Sunset",
            thumbnailUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=800&auto=format&fit=crop&q=80",
            fullUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=2400&auto=format&fit=crop&q=95",
            source = "Anime Wallpaper",
            author = "Kyoto Dreamworks",
            authorUrl = "https://pixelloom.app/anime",
            width = 1440,
            height = 3200,
            tags = listOf("Anime", "Japan", "Sakura", "Sunset"),
            category = "Anime",
            viewsCount = 45200,
            likesCount = 12400,
            isDailyPick = true
        )
    )

    fun getSubredditFallbacks(sub: String): List<Wallpaper> {
        val s = sub.lowercase()
        return when {
            s.contains("amoled") -> listOf(
                Wallpaper("ab_1", "Obsidian Eclipse Ring", "https://images.unsplash.com/photo-1532274402911-5a369e4c4bb5?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1532274402911-5a369e4c4bb5?w=2400&auto=format&fit=crop&q=95", "r/Amoledbackgrounds", "u/VoidArchitect", null, 1440, 3120, listOf("Amoled", "Dark", "True Black"), category = "Amoled", likesCount = 4200),
                Wallpaper("ab_2", "OLED Geometric Core", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=2400&auto=format&fit=crop&q=95", "r/Amoledbackgrounds", "u/NeonSpectre", null, 1080, 2400, listOf("Amoled", "Minimal", "Neon"), category = "Amoled", likesCount = 3800),
                Wallpaper("ab_3", "Minimal Moon Phases", "https://images.unsplash.com/photo-1532693322450-2cb5c511067d?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1532693322450-2cb5c511067d?w=2400&auto=format&fit=crop&q=95", "r/Amoledbackgrounds", "u/LunarCrafter", null, 1440, 3200, listOf("Moon", "Amoled", "Night"), category = "Amoled", likesCount = 5100),
                Wallpaper("ab_4", "Deep Abyss Monolith", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=2400&auto=format&fit=crop&q=95", "r/Amoledbackgrounds", "u/DarkMatter", null, 1440, 3120, listOf("Dark", "Monolith", "Amoled"), category = "Amoled", likesCount = 2900),
                Wallpaper("ab_5", "Cyberpunk Black Horizon", "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=2400&auto=format&fit=crop&q=95", "r/Amoledbackgrounds", "u/GridRunner", null, 1080, 2400, listOf("Cyberpunk", "Amoled", "Neon"), category = "Amoled", likesCount = 3400),
                Wallpaper("ab_6", "Nebula Darkness", "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=2400&auto=format&fit=crop&q=95", "r/Amoledbackgrounds", "u/AstroDark", null, 1440, 3200, listOf("Space", "Amoled", "Stars"), category = "Amoled", likesCount = 4700),
                Wallpaper("ab_7", "Black Sand Solitude", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=2400&auto=format&fit=crop&q=95", "r/Amoledbackgrounds", "u/CoastQuiet", null, 1080, 2400, listOf("Beach", "Amoled", "Minimal"), category = "Amoled", likesCount = 3100),
                Wallpaper("ab_8", "Minimal Geometric Cube", "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?w=2400&auto=format&fit=crop&q=95", "r/Amoledbackgrounds", "u/CubeVector", null, 1440, 3120, listOf("Geometric", "Amoled", "Clean"), category = "Amoled", likesCount = 2600)
            )
            s.contains("vertical") -> listOf(
                Wallpaper("vw_1", "Neon City Vertigo", "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=2400&auto=format&fit=crop&q=95", "r/Verticalwallpapers", "u/CityDrifter", null, 1080, 2400, listOf("Vertical", "Urban", "Neon"), category = "Urban", likesCount = 4100),
                Wallpaper("vw_2", "Vertical Mountain Cascade", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=2400&auto=format&fit=crop&q=95", "r/Verticalwallpapers", "u/AlpineEchoes", null, 1440, 3200, listOf("Vertical", "Mountains", "Nature"), category = "Nature", likesCount = 3800),
                Wallpaper("vw_3", "Emerald Forest Canopy", "https://images.unsplash.com/photo-1448375240586-882707db888b?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1448375240586-882707db888b?w=2400&auto=format&fit=crop&q=95", "r/Verticalwallpapers", "u/ForestRanger", null, 1080, 2400, listOf("Vertical", "Forest", "Evergreen"), category = "Nature", likesCount = 4900),
                Wallpaper("vw_4", "Cyber Skyscraper Vista", "https://images.unsplash.com/photo-1477959858617-67f30bc75b82?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1477959858617-67f30bc75b82?w=2400&auto=format&fit=crop&q=95", "r/Verticalwallpapers", "u/SkylineHunter", null, 1440, 3120, listOf("Vertical", "City", "Skyscrapers"), category = "Urban", likesCount = 5200),
                Wallpaper("vw_5", "Purple Twilight Fjord", "https://images.unsplash.com/photo-1517411032315-54ef2cb783bb?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1517411032315-54ef2cb783bb?w=2400&auto=format&fit=crop&q=95", "r/Verticalwallpapers", "u/NordicLens", null, 1080, 2400, listOf("Vertical", "Aurora", "Norway"), category = "Nature", likesCount = 4400),
                Wallpaper("vw_6", "Tokyo Alleyway Glow", "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=2400&auto=format&fit=crop&q=95", "r/Verticalwallpapers", "u/NightTokyo", null, 1440, 3200, listOf("Vertical", "Tokyo", "Alley"), category = "Urban", likesCount = 6100),
                Wallpaper("vw_7", "Golden Desert Dunes", "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?w=2400&auto=format&fit=crop&q=95", "r/Verticalwallpapers", "u/SaharaEye", null, 1080, 2400, listOf("Vertical", "Desert", "Gold"), category = "Minimal", likesCount = 3700),
                Wallpaper("vw_8", "Starry Sky Pine Trees", "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=2400&auto=format&fit=crop&q=95", "r/Verticalwallpapers", "u/AstroPine", null, 1440, 3120, listOf("Vertical", "Night", "Stars"), category = "Nature", likesCount = 5800)
            )
            s.contains("minimal") -> listOf(
                Wallpaper("mw_1", "Single Monolith Dune", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=2400&auto=format&fit=crop&q=95", "r/MinimalWallpaper", "u/CleanLines", null, 1080, 2400, listOf("Minimal", "Clean", "Nature"), category = "Minimal", likesCount = 3600),
                Wallpaper("mw_2", "Subtle Pastel Gradients", "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=2400&auto=format&fit=crop&q=95", "r/MinimalWallpaper", "u/ColorHarmony", null, 1440, 3120, listOf("Pastel", "Minimal", "Art"), category = "Minimal", likesCount = 4200),
                Wallpaper("mw_3", "Pure Concrete Symmetry", "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=2400&auto=format&fit=crop&q=95", "r/MinimalWallpaper", "u/ArchLines", null, 1080, 2400, listOf("Minimal", "Architecture", "Clean"), category = "Architecture", likesCount = 2800),
                Wallpaper("mw_4", "Single Fern Leaf", "https://images.unsplash.com/photo-1453728013993-6d66e9c9123a?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1453728013993-6d66e9c9123a?w=2400&auto=format&fit=crop&q=95", "r/MinimalWallpaper", "u/Botanical", null, 1440, 3200, listOf("Minimal", "Plant", "Macro"), category = "Nature", likesCount = 3300),
                Wallpaper("mw_5", "Calm Mist Horizon", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=2400&auto=format&fit=crop&q=95", "r/MinimalWallpaper", "u/StillWater", null, 1080, 2400, listOf("Minimal", "Water", "Calm"), category = "Minimal", likesCount = 3900),
                Wallpaper("mw_6", "Linear Prism Waves", "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=2400&auto=format&fit=crop&q=95", "r/MinimalWallpaper", "u/PrismArt", null, 1440, 3120, listOf("Minimal", "Waves", "Cyan"), category = "Abstract", likesCount = 4500)
            )
            s.contains("mobile") -> listOf(
                Wallpaper("mob_1", "Crimson Maple Kyoto", "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=2400&auto=format&fit=crop&q=95", "r/MobileWallpaper", "u/KyotoLens", null, 1440, 3200, listOf("Mobile", "Autumn", "Japan"), category = "Nature", likesCount = 6700),
                Wallpaper("mob_2", "Neon Road Drive", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=2400&auto=format&fit=crop&q=95", "r/MobileWallpaper", "u/SpeedDrive", null, 1080, 2400, listOf("Mobile", "Neon", "Cyberpunk"), category = "Cyberpunk", likesCount = 5400),
                Wallpaper("mob_3", "Mountain Lake Reflection", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=2400&auto=format&fit=crop&q=95", "r/MobileWallpaper", "u/LakeEcho", null, 1440, 3200, listOf("Mobile", "Lake", "Nature"), category = "Nature", likesCount = 4800),
                Wallpaper("mob_4", "Deep Cosmos Nebula", "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=2400&auto=format&fit=crop&q=95", "r/MobileWallpaper", "u/CosmicDrift", null, 1080, 2400, listOf("Mobile", "Space", "Cosmic"), category = "Space", likesCount = 7200),
                Wallpaper("mob_5", "Urban Skyscraper Angle", "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=2400&auto=format&fit=crop&q=95", "r/MobileWallpaper", "u/ModernTower", null, 1440, 3120, listOf("Mobile", "Architecture", "Glass"), category = "Architecture", likesCount = 3900),
                Wallpaper("mob_6", "Pink Sakura Twilight", "https://images.unsplash.com/photo-1522383225653-ed111181a951?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1522383225653-ed111181a951?w=2400&auto=format&fit=crop&q=95", "r/MobileWallpaper", "u/SakuraBloom", null, 1080, 2400, listOf("Mobile", "Sakura", "Pink"), category = "Nature", likesCount = 6100)
            )
            else -> listOf(
                Wallpaper("rw_1", "Nordic Mountain Ridge", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=2400&auto=format&fit=crop&q=95", "r/$sub", "u/EarthLens", null, 1440, 2960, listOf(sub, "Landscape"), category = "Nature", likesCount = 4100),
                Wallpaper("rw_2", "Luminescence Ocean Waves", "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=2400&auto=format&fit=crop&q=95", "r/$sub", "u/OceanWaves", null, 1080, 2400, listOf(sub, "Ocean", "Blue"), category = "Nature", likesCount = 4900),
                Wallpaper("rw_3", "Alpine Sunrise Glow", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=2400&auto=format&fit=crop&q=95", "r/$sub", "u/SunrisePeak", null, 1440, 3200, listOf(sub, "Alpine", "Dawn"), category = "Nature", likesCount = 5300),
                Wallpaper("rw_4", "Cosmic Orion Nebula", "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=2400&auto=format&fit=crop&q=95", "r/$sub", "u/DeepStargazer", null, 1080, 2400, listOf(sub, "Space", "Orion"), category = "Space", likesCount = 7600),
                Wallpaper("rw_5", "Neon Highway Horizon", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=2400&auto=format&fit=crop&q=95", "r/$sub", "u/RetroSynth", null, 1440, 3120, listOf(sub, "Neon", "Synthwave"), category = "Cyberpunk", likesCount = 6200),
                Wallpaper("rw_6", "Golden Sand Monolith", "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?w=2400&auto=format&fit=crop&q=95", "r/$sub", "u/DesertWanderer", null, 1080, 2400, listOf(sub, "Dunes", "Minimal"), category = "Minimal", likesCount = 3800)
            )
        }
    }
}
