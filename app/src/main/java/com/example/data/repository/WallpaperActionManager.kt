package com.example.data.repository

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.R
import com.example.data.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

enum class WallpaperTarget {
    HOME,
    LOCK,
    BOTH
}

class WallpaperActionManager(
    private val context: Context,
    private val repository: WallpaperRepository
) {
    private val imageLoader = ImageLoader(context)

    companion object {
        const val DOWNLOAD_CHANNEL_ID = "pixelloom_wallpaper_downloads"
    }

    init {
        createDownloadNotificationChannel()
    }

    private fun createDownloadNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Wallpaper Downloads"
            val descriptionText = "Displays status and progress for downloaded wallpapers"
            val channel = NotificationChannel(
                DOWNLOAD_CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = descriptionText
                setShowBadge(false)
            }
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    suspend fun fetchBitmap(url: String): Bitmap? = withContext(Dispatchers.IO) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false) // Software bitmap for manipulation and WallpaperManager
            .build()
        val result = (imageLoader.execute(request) as? SuccessResult)?.drawable
        (result as? android.graphics.drawable.BitmapDrawable)?.bitmap
    }

    suspend fun setWallpaper(
        url: String,
        target: WallpaperTarget
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val bitmap = fetchBitmap(url) ?: return@withContext Result.failure(Exception("Failed to download wallpaper image"))
            val wallpaperManager = WallpaperManager.getInstance(context)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val whichFlag = when (target) {
                    WallpaperTarget.HOME -> WallpaperManager.FLAG_SYSTEM
                    WallpaperTarget.LOCK -> WallpaperManager.FLAG_LOCK
                    WallpaperTarget.BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                }
                wallpaperManager.setBitmap(bitmap, null, true, whichFlag)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setCroppedWallpaper(
        bitmap: Bitmap,
        target: WallpaperTarget
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val wallpaperManager = WallpaperManager.getInstance(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val whichFlag = when (target) {
                    WallpaperTarget.HOME -> WallpaperManager.FLAG_SYSTEM
                    WallpaperTarget.LOCK -> WallpaperManager.FLAG_LOCK
                    WallpaperTarget.BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                }
                wallpaperManager.setBitmap(bitmap, null, true, whichFlag)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadWallpaper(
        wallpaper: Wallpaper,
        targetWidth: Int?,
        targetHeight: Int?
    ): Result<Uri> = withContext(Dispatchers.IO) {
        val notificationId = (System.currentTimeMillis() % 100000).toInt() + 5000
        val nm = NotificationManagerCompat.from(context)

        // 1. Initial ongoing download notification
        try {
            val progressNotification = NotificationCompat.Builder(context, DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Downloading Wallpaper")
                .setContentText(wallpaper.title)
                .setProgress(100, 25, false)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
            nm.notify(notificationId, progressNotification)
        } catch (e: Throwable) {
            // Permission or notification exception
        }

        try {
            val originalBitmap = fetchBitmap(wallpaper.fullUrl)
            if (originalBitmap == null) {
                showDownloadFailedNotification(notificationId, wallpaper.title, "Failed to download image from source")
                return@withContext Result.failure(Exception("Failed to fetch image data"))
            }

            // Update to 70% progress (processing/resizing)
            try {
                val processingNotification = NotificationCompat.Builder(context, DOWNLOAD_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Processing Resolution...")
                    .setContentText(wallpaper.title)
                    .setProgress(100, 70, false)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build()
                nm.notify(notificationId, processingNotification)
            } catch (e: Throwable) {}

            val processedBitmap: Bitmap = if (targetWidth != null && targetHeight != null &&
                (targetWidth != originalBitmap.width || targetHeight != originalBitmap.height)
            ) {
                scaleAndCropBitmap(originalBitmap, targetWidth, targetHeight)
            } else {
                originalBitmap
            }

            val savedUri = saveBitmapToPictures(
                bitmap = processedBitmap,
                title = wallpaper.title.replace(Regex("[^a-zA-Z0-9_-]"), "_")
            )
            if (savedUri == null) {
                showDownloadFailedNotification(notificationId, wallpaper.title, "Could not write to device storage")
                return@withContext Result.failure(Exception("Failed to save image to device storage"))
            }

            // Log in download history
            repository.recordDownload(
                wallpaper = wallpaper,
                savedUri = savedUri.toString(),
                width = processedBitmap.width,
                height = processedBitmap.height
            )

            // 3. Completion Notification with View Intent
            try {
                val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(savedUri, "image/*")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    notificationId,
                    viewIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
                )

                val doneNotification = NotificationCompat.Builder(context, DOWNLOAD_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Wallpaper Downloaded")
                    .setContentText("${wallpaper.title} saved to Pictures/PixelLoom")
                    .setProgress(0, 0, false)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()
                nm.notify(notificationId, doneNotification)
            } catch (e: Throwable) {}

            Result.success(savedUri)
        } catch (e: Exception) {
            showDownloadFailedNotification(notificationId, wallpaper.title, e.message ?: "Download failed")
            Result.failure(e)
        }
    }

    private fun showDownloadFailedNotification(notificationId: Int, title: String, error: String) {
        try {
            val nm = NotificationManagerCompat.from(context)
            val failedNotification = NotificationCompat.Builder(context, DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Download Failed")
                .setContentText("$title: $error")
                .setProgress(0, 0, false)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            nm.notify(notificationId, failedNotification)
        } catch (e: Throwable) {}
    }

    private fun scaleAndCropBitmap(source: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val srcWidth = source.width
        val srcHeight = source.height

        val widthRatio = targetWidth.toFloat() / srcWidth
        val heightRatio = targetHeight.toFloat() / srcHeight
        val scale = maxOf(widthRatio, heightRatio)

        val scaledWidth = (scale * srcWidth).toInt()
        val scaledHeight = (scale * srcHeight).toInt()

        val scaled = Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true)

        val xOffset = (scaledWidth - targetWidth) / 2
        val yOffset = (scaledHeight - targetHeight) / 2

        val cropped = Bitmap.createBitmap(
            scaled,
            xOffset.coerceAtLeast(0),
            yOffset.coerceAtLeast(0),
            targetWidth.coerceAtMost(scaledWidth),
            targetHeight.coerceAtMost(scaledHeight)
        )
        return cropped
    }

    private fun saveBitmapToPictures(bitmap: Bitmap, title: String): Uri? {
        val fileName = "PixelLoom_${title}_${System.currentTimeMillis()}.jpg"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/PixelLoom")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: return null

            try {
                resolver.openOutputStream(uri)?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 96, stream)
                }
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
                return uri
            } catch (e: Exception) {
                resolver.delete(uri, null, null)
                return null
            }
        } else {
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val loomDir = File(picturesDir, "PixelLoom")
            if (!loomDir.exists()) loomDir.mkdirs()
            val file = File(loomDir, fileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 96, out)
            }
            return Uri.fromFile(file)
        }
    }
}
