package com.example.data.sources

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object NetworkClient {
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .followRedirects(true)
            .build()
    }

    suspend fun getJson(url: String, userAgent: String = "PixelLoom/1.0 (Android Wallpaper Client)"): JSONObject? {
        return try {
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", userAgent)
                .header("Accept", "application/json")
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) return null
            val bodyString = response.body?.string() ?: return null
            JSONObject(bodyString)
        } catch (e: Exception) {
            null
        }
    }
}
