package com.example.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.MainActivity
import com.example.R
import com.example.data.sources.CuratedWallpaperDataset
import kotlin.random.Random

class DailyWallpaperWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "pixelloom_daily_inspiration"
        const val NOTIFICATION_ID = 4040

        val CREATIVE_LINES = listOf(
            "Your screen is a canvas; give it a fresh coat of imagination today.",
            "Light bends, color sings — discover today's visual masterpiece.",
            "Every glance at your phone deserves a breath of high-res serenity.",
            "Weave today's mood with deep obsidian tones and glowing spectrums.",
            "Pixels woven with passion. Refresh your horizon today.",
            "Step into a new dimension of visual wonder on PixelLoom.",
            "A splash of cosmic beauty is waiting for your lock screen.",
            "Where artistry meets clarity — unlock your phone's true resolution.",
            "Craft your digital sanctuary with today's trending inspiration.",
            "Quiet the noise. Let your wallpaper tell an unforgettable story."
        )
    }

    override suspend fun doWork(): Result {
        try {
            createNotificationChannel()

            val wotdList = CuratedWallpaperDataset.wallpapersOfTheDay
            val pickedWallpaper = wotdList.randomOrNull() ?: return Result.failure()
            val creativeLine = CREATIVE_LINES.random()

            // Fetch preview bitmap for rich notification
            val bitmap = fetchNotificationBitmap(pickedWallpaper.thumbnailUrl)

            // Deep link intent
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("pixelloom://wallpaper/${pickedWallpaper.id}"), context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("wallpaper_id", pickedWallpaper.id)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                Random.nextInt(1000, 9999),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )

            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("🎨 Daily Inspiration: ${pickedWallpaper.title}")
                .setContentText(creativeLine)
                .setSubText("PixelLoom Pick")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            if (bitmap != null) {
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .setSummaryText(creativeLine)
                )
            } else {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle().bigText(creativeLine)
                )
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_ID, notificationBuilder.build())

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private suspend fun fetchNotificationBitmap(url: String): Bitmap? {
        return try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()
            val result = (loader.execute(request) as? SuccessResult)?.drawable
            (result as? BitmapDrawable)?.bitmap
        } catch (e: Exception) {
            null
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_desc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
