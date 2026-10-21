package com.example.data.sources

import com.example.data.model.SourceCategory
import com.example.data.model.Wallpaper

interface WallpaperSource {
    val id: String
    val name: String
    val shortName: String
    val category: SourceCategory
    val description: String
    val websiteUrl: String
    val iconEmoji: String

    suspend fun fetchTrending(page: Int = 1, pageSize: Int = 20): List<Wallpaper>
    suspend fun search(query: String, page: Int = 1, pageSize: Int = 20): List<Wallpaper>
}
