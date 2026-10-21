package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class, DownloadHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PixelLoomDatabase : RoomDatabase() {
    abstract fun wallpaperDao(): WallpaperDao

    companion object {
        @Volatile
        private var INSTANCE: PixelLoomDatabase? = null

        fun getDatabase(context: Context): PixelLoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PixelLoomDatabase::class.java,
                    "pixelloom_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
