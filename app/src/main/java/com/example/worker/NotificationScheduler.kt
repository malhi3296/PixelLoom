package com.example.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object NotificationScheduler {
    private const val PERIODIC_WORK_NAME = "pixelloom_daily_notification_work"

    /**
     * Schedules notifications to fire twice a day (every 12 hours) with random jitter
     */
    fun scheduleDailyInspirations(context: Context) {
        val workManager = WorkManager.getInstance(context)

        // Schedule every 12 hours with a flexible window
        val periodicRequest = PeriodicWorkRequestBuilder<DailyWallpaperWorker>(
            12, TimeUnit.HOURS,
            30, TimeUnit.MINUTES
        ).setInitialDelay(Random.nextLong(15, 180), TimeUnit.MINUTES)
         .build()

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }

    /**
     * Trigger immediate inspiration notification for testing
     */
    fun triggerImmediateTest(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val oneTimeRequest = OneTimeWorkRequestBuilder<DailyWallpaperWorker>().build()
        workManager.enqueue(oneTimeRequest)
    }
}
