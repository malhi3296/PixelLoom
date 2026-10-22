package com.example.data.sources

import com.example.data.model.SourceCategory
import com.example.data.model.Wallpaper

object SourceRegistry {

    private val allSourcesList: MutableList<WallpaperSource> = mutableListOf()

    init {
        // 1. Reddit Communities (Live Reddit JSON API + Subreddit Fallback)
        allSourcesList.add(
            RedditWallpaperSource(
                subreddit = "Amoledbackgrounds",
                id = "reddit_amoled",
                name = "r/Amoledbackgrounds",
                shortName = "Amoled",
                description = "Pure black (>50%) battery-saving wallpapers for OLED & AMOLED displays",
                iconEmoji = "🖤",
                defaultTags = listOf("Amoled", "OLED", "Dark", "Minimal"),
                fallbackWallpapers = CuratedWallpaperDataset.getSubredditFallbacks("amoledbackgrounds")
            )
        )
        allSourcesList.add(
            RedditWallpaperSource(
                subreddit = "wallpaper",
                id = "reddit_wallpaper",
                name = "r/wallpaper",
                shortName = "r/wallpaper",
                description = "Reddit's largest general wallpaper community with diverse resolutions",
                iconEmoji = "🖼️",
                defaultTags = listOf("Landscape", "Art", "Community"),
                fallbackWallpapers = CuratedWallpaperDataset.getSubredditFallbacks("wallpaper")
            )
        )
        allSourcesList.add(
            RedditWallpaperSource(
                subreddit = "Verticalwallpapers",
                id = "reddit_vertical",
                name = "r/Verticalwallpapers",
                shortName = "Vertical",
                description = "Tailored 9:16 and 9:19.5 wallpapers built specifically for smartphones",
                iconEmoji = "📱",
                defaultTags = listOf("Vertical", "Mobile", "Portrait"),
                fallbackWallpapers = CuratedWallpaperDataset.getSubredditFallbacks("verticalwallpapers")
            )
        )
        allSourcesList.add(
            RedditWallpaperSource(
                subreddit = "MobileWallpaper",
                id = "reddit_mobile",
                name = "r/MobileWallpaper",
                shortName = "Mobile",
                description = "Curated mobile phone backgrounds from creators worldwide",
                iconEmoji = "📲",
                defaultTags = listOf("Mobile", "Phone", "HD"),
                fallbackWallpapers = CuratedWallpaperDataset.getSubredditFallbacks("mobilewallpaper")
            )
        )
        allSourcesList.add(
            RedditWallpaperSource(
                subreddit = "MinimalWallpaper",
                id = "reddit_minimal",
                name = "r/MinimalWallpaper",
                shortName = "Minimal",
                description = "Clean, distraction-free aesthetic minimalism for serene setups",
                iconEmoji = "✨",
                defaultTags = listOf("Minimal", "Clean", "Zen"),
                fallbackWallpapers = CuratedWallpaperDataset.getSubredditFallbacks("minimalwallpaper")
            )
        )

        // 2. Wallhaven (Live API)
        allSourcesList.add(
            WallhavenWallpaperSource(
                fallbackWallpapers = listOf(
                    CuratedWallpaperDataset.wallpapersOfTheDay[3],
                    CuratedWallpaperDataset.wallpapersOfTheDay[9]
                )
            )
        )

        // Wikipedia / Wikimedia Commons Featured Picture of the Day
        allSourcesList.add(WikipediaWallpaperSource())

        // NASA, JWST & Deep Space Astrophotography
        allSourcesList.add(SpaceWallpaperSource())

        // 3. Photography Powerhouses
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "unsplash",
                name = "Unsplash",
                shortName = "Unsplash",
                category = SourceCategory.PHOTOGRAPHY,
                description = "Over 3 million free high-resolution photos contributed by world-class photographers",
                websiteUrl = "https://unsplash.com",
                iconEmoji = "📷",
                defaultTags = listOf("Photography", "Nature", "Urban"),
                seedWallpapers = listOf(
                    CuratedWallpaperDataset.wallpapersOfTheDay[0],
                    Wallpaper("uns_1", "Neon Cyberpunk Alley", "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=2400&auto=format&fit=crop&q=95", "Unsplash", "Florian Olivo", "https://unsplash.com", 1440, 3200, listOf("Neon", "Cyberpunk")),
                    Wallpaper("uns_2", "Icelandic Black Beach", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=2400&auto=format&fit=crop&q=95", "Unsplash", "Sean Oulashin", "https://unsplash.com", 1440, 3120, listOf("Ocean", "Beach", "Nature"))
                )
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "pexels",
                name = "Pexels",
                shortName = "Pexels",
                category = SourceCategory.PHOTOGRAPHY,
                description = "High quality stock photography hand-picked for vibrant phone backgrounds",
                websiteUrl = "https://pexels.com",
                iconEmoji = "🌿",
                defaultTags = listOf("Stock", "Nature", "Vibrant"),
                seedWallpapers = listOf(
                    CuratedWallpaperDataset.wallpapersOfTheDay[4],
                    Wallpaper("pex_1", "Emerald Moss Waterfall", "https://images.unsplash.com/photo-1432405972618-c60b0225b8f9?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1432405972618-c60b0225b8f9?w=2400&auto=format&fit=crop&q=95", "Pexels", "Lukas Krahn", null, 1080, 2400, listOf("Waterfall", "Green", "Fresh")),
                    Wallpaper("pex_2", "Autumn Aspen Grove", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=2400&auto=format&fit=crop&q=95", "Pexels", "Markus Spiske", null, 1440, 3200, listOf("Autumn", "Forest", "Gold"))
                )
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "pixabay",
                name = "Pixabay",
                shortName = "Pixabay",
                category = SourceCategory.PHOTOGRAPHY,
                description = "Vibrant creative commons community offering millions of stunning visuals",
                websiteUrl = "https://pixabay.com",
                iconEmoji = "🎨",
                defaultTags = listOf("Pixabay", "Art", "Nature"),
                seedWallpapers = listOf(
                    Wallpaper("pix_1", "Hummingbird in Flight", "https://images.unsplash.com/photo-1520808663317-647b476a81b9?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1520808663317-647b476a81b9?w=2400&auto=format&fit=crop&q=95", "Pixabay", "Wildlife Creator", null, 1080, 2400, listOf("Wildlife", "Bird", "Color")),
                    Wallpaper("pix_2", "Macro Water Droplet Prism", "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=2400&auto=format&fit=crop&q=95", "Pixabay", "MacroMaster", null, 1440, 3120, listOf("Macro", "Water", "Light"))
                )
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "stocksnap",
                name = "StockSnap.io",
                shortName = "StockSnap",
                category = SourceCategory.PHOTOGRAPHY,
                description = "Hundreds of high resolution photos added weekly, free from copyright restrictions",
                websiteUrl = "https://stocksnap.io",
                iconEmoji = "📸",
                defaultTags = listOf("StockSnap", "Free", "HD"),
                seedWallpapers = listOf(CuratedWallpaperDataset.wallpapersOfTheDay[6])
            )
        )

        // 4. Design, Vectors & Minimalism
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "freepik",
                name = "Freepik",
                shortName = "Freepik",
                category = SourceCategory.VECTOR_GRAPHICS,
                description = "Leading graphic resources, 3D renders, and vector illustrations",
                websiteUrl = "https://freepik.com",
                iconEmoji = "💎",
                defaultTags = listOf("Vector", "3D", "Illustration"),
                seedWallpapers = listOf(
                    Wallpaper("fp_1", "Liquid Holographic Waves", "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=2400&auto=format&fit=crop&q=95", "Freepik", "Freepik Vector Lab", null, 1440, 3200, listOf("Liquid", "3D", "Hologram")),
                    Wallpaper("fp_2", "Minimalist Pastel Shapes", "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=2400&auto=format&fit=crop&q=95", "Freepik", "Studio Minimal", null, 1080, 2400, listOf("Minimal", "Pastel", "Geometry"))
                )
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "simple_desktops",
                name = "Simple Desktops",
                shortName = "Simple",
                category = SourceCategory.AMOLED_MINIMAL,
                description = "Distraction-free desktop and mobile wallpapers with iconic vector minimalism",
                websiteUrl = "http://simpledesktops.com",
                iconEmoji = "⚪",
                defaultTags = listOf("Simple", "Vector", "Minimal"),
                seedWallpapers = listOf(CuratedWallpaperDataset.wallpapersOfTheDay[5])
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "designspiration",
                name = "Designspiration",
                shortName = "Designspire",
                category = SourceCategory.VECTOR_GRAPHICS,
                description = "Architecture, industrial design, and typographic wallpaper curation",
                websiteUrl = "https://designspiration.com",
                iconEmoji = "🏛️",
                defaultTags = listOf("Design", "Architecture", "Typography"),
                seedWallpapers = listOf(CuratedWallpaperDataset.wallpapersOfTheDay[8])
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "pinterest",
                name = "Pinterest",
                shortName = "Pinterest",
                category = SourceCategory.VECTOR_GRAPHICS,
                description = "Trending aesthetic moodboards and phone lock screen inspirations",
                websiteUrl = "https://pinterest.com",
                iconEmoji = "📌",
                defaultTags = listOf("Aesthetic", "Moodboard", "Trending"),
                seedWallpapers = listOf(
                    Wallpaper("pin_1", "Retro Synthwave Sunset Highway", "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=2400&auto=format&fit=crop&q=95", "Pinterest", "Aesthetic Curator", null, 1080, 2400, listOf("Synthwave", "Sunset", "Retro"))
                )
            )
        )

        // 5. Digital Art, Anime & Curated Archives
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "alphacoders",
                name = "Wallpaper Alpha Coders",
                shortName = "AlphaCoders",
                category = SourceCategory.ANIME_ILLUSTRATION,
                description = "Massive database of 4K anime, fantasy, game art, and sci-fi wallpapers",
                websiteUrl = "https://wall.alphacoders.com",
                iconEmoji = "⚔️",
                defaultTags = listOf("Sci-Fi", "Gaming", "Fantasy"),
                seedWallpapers = listOf(CuratedWallpaperDataset.wallpapersOfTheDay[7])
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "anime_wallpaper",
                name = "Anime Wallpaper",
                shortName = "Anime",
                category = SourceCategory.ANIME_ILLUSTRATION,
                description = "Ultra high definition anime landscapes, shonen heroes, and aesthetic lo-fi scenes",
                websiteUrl = "https://pixelloom.app/anime",
                iconEmoji = "🌸",
                defaultTags = listOf("Anime", "Manga", "Lo-Fi", "Japan"),
                seedWallpapers = listOf(CuratedWallpaperDataset.wallpapersOfTheDay[9])
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "deviantart",
                name = "DeviantArt",
                shortName = "DeviantArt",
                category = SourceCategory.ANIME_ILLUSTRATION,
                description = "Independent visual artists showcasing custom digital paintings and concept art",
                websiteUrl = "https://deviantart.com",
                iconEmoji = "🖌️",
                defaultTags = listOf("DigitalArt", "ConceptArt", "Fantasy"),
                seedWallpapers = listOf(
                    Wallpaper("da_1", "Cyber Dragon Sovereign", "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=2400&auto=format&fit=crop&q=95", "DeviantArt", "ArtStation Guild", null, 1440, 3200, listOf("Dragon", "Fantasy", "Cyber"))
                )
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "zedge",
                name = "Zedge",
                shortName = "Zedge",
                category = SourceCategory.COMMUNITY,
                description = "Global mobile customization platform with live & static phone wallpapers",
                websiteUrl = "https://zedge.net",
                iconEmoji = "⚡",
                defaultTags = listOf("Zedge", "Mobile", "Ringtones"),
                seedWallpapers = listOf(
                    Wallpaper("zd_1", "Neon Electric Lightning", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=2400&auto=format&fit=crop&q=95", "Zedge", "Volt Master", null, 1080, 2400, listOf("Lightning", "Electric", "Storm"))
                )
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "wallpaper_cave",
                name = "Wallpaper Cave",
                shortName = "Cave",
                category = SourceCategory.COMMUNITY,
                description = "User-uploaded community galleries for fandoms, movies, sports, and dark themes",
                websiteUrl = "https://wallpapercave.com",
                iconEmoji = "🦇",
                defaultTags = listOf("Cave", "Community", "Fandom"),
                seedWallpapers = listOf(
                    Wallpaper("wc_1", "Dark Knight Gotham Rooftop", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=2400&auto=format&fit=crop&q=95", "Wallpaper Cave", "CaveExplorer", null, 1440, 2960, listOf("Dark", "Night", "City"))
                )
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "wallpapersafari",
                name = "WallpaperSafari",
                shortName = "Safari",
                category = SourceCategory.COMMUNITY,
                description = "Comprehensive collection of discovered desktop and smartphone backgrounds",
                websiteUrl = "https://wallpapersafari.com",
                iconEmoji = "🧭",
                defaultTags = listOf("Safari", "Collection", "HD"),
                seedWallpapers = listOf(
                    Wallpaper("ws_1", "Savannah Sunset Silhouette", "https://images.unsplash.com/photo-1516426122078-c23e76319801?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1516426122078-c23e76319801?w=2400&auto=format&fit=crop&q=95", "WallpaperSafari", "WildPhotos", null, 1080, 2400, listOf("Africa", "Sunset", "Safari"))
                )
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "wallpapers_com",
                name = "Wallpapers.com",
                shortName = "Wallpapers.com",
                category = SourceCategory.PHOTOGRAPHY,
                description = "High resolution curated wallpapers optimized for all screen aspect ratios",
                websiteUrl = "https://wallpapers.com",
                iconEmoji = "🌐",
                defaultTags = listOf("Wallpapers", "UltraHD", "Curated"),
                seedWallpapers = listOf(
                    Wallpaper("wcom_1", "Alpine Azure Glacier Lake", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=2400&auto=format&fit=crop&q=95", "Wallpapers.com", "Alpine Crew", null, 2160, 3840, listOf("Mountains", "Glacier", "Azure"))
                )
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "wallpaperswide",
                name = "WallpapersWide",
                shortName = "Wide",
                category = SourceCategory.PHOTOGRAPHY,
                description = "Famous for widescreen, dual-monitor, and ultrawide high-resolution wallpapers",
                websiteUrl = "https://wallpaperswide.com",
                iconEmoji = "🖥️",
                defaultTags = listOf("Widescreen", "4K", "UltraWide"),
                seedWallpapers = listOf(
                    Wallpaper("ww_1", "Cosmic Stargate Panorama", "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=2400&auto=format&fit=crop&q=95", "WallpapersWide", "SpaceWide", null, 2160, 3840, listOf("Panorama", "Space", "Cosmic"))
                )
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "interfacelift",
                name = "InterfaceLIFT",
                shortName = "InterfaceLIFT",
                category = SourceCategory.PHOTOGRAPHY,
                description = "Pioneering high-end landscape photography with meticulous color grading",
                websiteUrl = "https://interfacelift.com",
                iconEmoji = "🏔️",
                defaultTags = listOf("FineArt", "Landscape", "ProPhoto"),
                seedWallpapers = listOf(CuratedWallpaperDataset.wallpapersOfTheDay[2])
            )
        )
        allSourcesList.add(
            GenericWebWallpaperSource(
                id = "mobiles_wall",
                name = "Mobiles Wall",
                shortName = "MobilesWall",
                category = SourceCategory.AMOLED_MINIMAL,
                description = "Exclusively smartphone vertical wallpapers in pure crisp resolutions",
                websiteUrl = "https://mobileswall.com",
                iconEmoji = "📱",
                defaultTags = listOf("MobileOnly", "Vertical", "LockScreen"),
                seedWallpapers = listOf(
                    Wallpaper("mw_only_1", "Golden Hour Solitude", "https://images.unsplash.com/photo-1495616811223-4d98c6e9c869?w=800&auto=format&fit=crop&q=80", "https://images.unsplash.com/photo-1495616811223-4d98c6e9c869?w=2400&auto=format&fit=crop&q=95", "Mobiles Wall", "Mobile Vision", null, 1080, 2400, listOf("Solitude", "GoldenHour", "Peace"))
                )
            )
        )
    }

    fun getAllSources(): List<WallpaperSource> = allSourcesList

    fun getSourceById(id: String): WallpaperSource? {
        return allSourcesList.find { it.id.equals(id, ignoreCase = true) }
    }
}
