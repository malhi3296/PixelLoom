package com.example

import android.app.Application
import com.example.data.local.PixelLoomDatabase
import com.example.data.repository.WallpaperActionManager
import com.example.data.repository.WallpaperRepository
import com.example.worker.NotificationScheduler

class PixelLoomApp : Application() {

    val database: PixelLoomDatabase by lazy {
        PixelLoomDatabase.getDatabase(this)
    }

    val repository: WallpaperRepository by lazy {
        WallpaperRepository(database.wallpaperDao())
    }

    val actionManager: WallpaperActionManager by lazy {
        WallpaperActionManager(this, repository)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        try {
            // Eagerly warm up singletons in a safe manner
            val _db = database
            val _repo = repository
            val _mgr = actionManager

            // Schedule daily notifications with fallback
            NotificationScheduler.scheduleDailyInspirations(this)
        } catch (t: Throwable) {
            android.util.Log.e("PixelLoomApp", "Non-fatal startup initialization issue: ${t.message}", t)
        }
    }

    companion object {
        lateinit var instance: PixelLoomApp
            private set
    }
}
