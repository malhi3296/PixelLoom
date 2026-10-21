package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {
    // Favorites
    @Query("SELECT * FROM favorites ORDER BY savedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    // Downloads
    @Query("SELECT * FROM download_history ORDER BY downloadedAt DESC")
    fun getAllDownloads(): Flow<List<DownloadHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadHistoryEntity)

    @Query("DELETE FROM download_history WHERE localId = :id")
    suspend fun deleteDownload(id: Long)

    @Query("DELETE FROM download_history")
    suspend fun clearDownloads()
}
