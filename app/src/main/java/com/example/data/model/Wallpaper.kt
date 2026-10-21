package com.example.data.model

data class Wallpaper(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val fullUrl: String,
    val source: String,
    val author: String,
    val authorUrl: String? = null,
    val width: Int = 1080,
    val height: Int = 2400,
    val tags: List<String> = emptyList(),
    val colorHex: String? = null,
    val category: String = "General",
    val viewsCount: Int = 0,
    val likesCount: Int = 0,
    val isFavorite: Boolean = false,
    val isDailyPick: Boolean = false,
    val aspectRatio: Float = 0.5625f // default 9:16 portrait
)

enum class SourceCategory(val label: String) {
    PHOTOGRAPHY("Photography"),
    ANIME_ILLUSTRATION("Anime & Art"),
    AMOLED_MINIMAL("Amoled & Minimal"),
    COMMUNITY("Reddit & Forums"),
    VECTOR_GRAPHICS("Design & Vectors")
}

data class WallpaperDimension(
    val label: String,
    val width: Int,
    val height: Int,
    val isPreset: Boolean = true
) {
    val resolutionLabel: String
        get() = "${width} x ${height}"
}
