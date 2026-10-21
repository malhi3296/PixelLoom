package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Wallpaper

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val thumbnailUrl: String,
    val fullUrl: String,
    val source: String,
    val author: String,
    val authorUrl: String? = null,
    val width: Int,
    val height: Int,
    val category: String,
    val savedAt: Long = System.currentTimeMillis()
) {
    fun toWallpaper(): Wallpaper = Wallpaper(
        id = id,
        title = title,
        thumbnailUrl = thumbnailUrl,
        fullUrl = fullUrl,
        source = source,
        author = author,
        authorUrl = authorUrl,
        width = width,
        height = height,
        category = category,
        isFavorite = true
    )

    companion object {
        fun fromWallpaper(w: Wallpaper): FavoriteEntity = FavoriteEntity(
            id = w.id,
            title = w.title,
            thumbnailUrl = w.thumbnailUrl,
            fullUrl = w.fullUrl,
            source = w.source,
            author = w.author,
            authorUrl = w.authorUrl,
            width = w.width,
            height = w.height,
            category = w.category
        )
    }
}

@Entity(tableName = "download_history")
data class DownloadHistoryEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val wallpaperId: String,
    val title: String,
    val savedUri: String,
    val width: Int,
    val height: Int,
    val downloadedAt: Long = System.currentTimeMillis(),
    val thumbnailUrl: String,
    val source: String
)
