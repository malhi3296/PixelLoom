package com.example

import android.app.Application
import com.example.data.local.PixelLoomDatabase
import com.example.data.repository.WallpaperActionManager
import com.example.data.repository.WallpaperRepository
import com.example.worker.NotificationScheduler

class PixelLoomApp : Application() {

    lateinit var database: PixelLoomDatabase
        private set

    lateinit var repository: WallpaperRepository
        private set

    lateinit var actionManager: WallpaperActionManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = PixelLoomDatabase.getDatabase(this)
        repository = WallpaperRepository(database.wallpaperDao())
        actionManager = WallpaperActionManager(this, repository)

        // Schedule daily notifications
        try {
            NotificationScheduler.scheduleDailyInspirations(this)
        } catch (e: Exception) {
            // Handled gracefully in testing/restricted environments
        }
    }

    companion object {
        lateinit var instance: PixelLoomApp
            private set
    }
}
