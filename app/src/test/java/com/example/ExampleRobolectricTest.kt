package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.sources.CuratedWallpaperDataset
import com.example.data.sources.SourceRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("PixelLoom", appName)
    }

    @Test
    fun `wallpapers of the day returns exactly 10 picks`() {
        val wotd = CuratedWallpaperDataset.wallpapersOfTheDay
        assertEquals(10, wotd.size)
        wotd.forEach { wallpaper ->
            assertNotNull(wallpaper.id)
            assertTrue(wallpaper.title.isNotEmpty())
            assertTrue(wallpaper.thumbnailUrl.startsWith("http"))
            assertTrue(wallpaper.fullUrl.startsWith("http"))
        }
    }

    @Test
    fun `source registry aggregates all requested sources`() {
        val sources = SourceRegistry.getAllSources()
        assertTrue("Expected at least 20 sources, got ${sources.size}", sources.size >= 20)

        // Verify specific sources
        val expectedSourceIds = listOf(
            "reddit_amoled", "reddit_wallpaper", "reddit_vertical", "reddit_mobile", "reddit_minimal",
            "wallhaven", "unsplash", "pexels", "pixabay", "stocksnap", "freepik",
            "simple_desktops", "designspiration", "pinterest", "alphacoders", "anime_wallpaper",
            "deviantart", "zedge", "wallpaper_cave", "wallpapersafari", "wallpapers_com",
            "wallpaperswide", "interfacelift", "mobiles_wall"
        )

        for (id in expectedSourceIds) {
            val source = SourceRegistry.getSourceById(id)
            assertNotNull("Source $id should be registered", source)
        }
    }

    @Test
    fun `brand resources and logo assets exist and load`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val logoDrawable = context.getDrawable(R.drawable.ic_pixelloom_logo_white)
        assertNotNull("White logo cutout asset must exist", logoDrawable)

        val purpleDrawable = context.getDrawable(R.drawable.ic_pixelloom_logo_purple)
        assertNotNull("Purple logo asset must exist", purpleDrawable)
    }
}
