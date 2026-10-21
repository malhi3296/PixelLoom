<p align="center">
  <img src="public/assets/aistudio/Gemini_Generated_Image_hhm390hhm390hhm3_1__ca463907.png" alt="PixelLoom Logo" width="180" />
</p>

<h1 align="center">PixelLoom</h1>

<p align="center">
  <strong>Curated High-Resolution Wallpapers Across the Web</strong><br>
  Built with Kotlin & Jetpack Compose • Material 3 • Freeform Cropping • 24+ Sources
</p>

<p align="center">
  <a href="#-apk-releases--downloads"><img src="https://img.shields.io/badge/Release-v1.0--preview-blue?style=for-the-badge&logo=android" alt="Download APK"></a>
  <img src="https://img.shields.io/badge/Platform-Android%207.0%2B%20(API%2024%2B)-brightgreen?style=for-the-badge" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin%202.2-purple?style=for-the-badge&logo=kotlin" alt="Language">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-blue?style=for-the-badge" alt="Compose">
</p>

---

## 📱 About PixelLoom

**PixelLoom** weaves together premier creative communities and digital art archives into a unified, high-performance Android wallpaper hub. Designed from the ground up with a clean **Material 3 Light Mode** aesthetic, smooth gesture-driven **Freeform Cropping**, and **custom dimension downloads**, PixelLoom gives your device screen a fresh coat of imagination every single day.

---

## 📦 APK Releases & Downloads

| Version | Status | Download Link | Build Type |
|---|---|---|---|
| **PixelLoom v1.0.0** | Latest Stable Release | [Download PixelLoom-v1.0-debug.apk](./PixelLoom-v1.0-debug.apk) | Debug APK (Signed) |
| **Direct Project Output** | Build Artifact | `app/build/outputs/apk/debug/app-debug.apk` | Universal APK |

> **How to install on device:**
> 1. Download `PixelLoom-v1.0-debug.apk`.
> 2. On your Android device, tap the downloaded APK.
> 3. Allow installation from unknown sources if prompted by your browser or file manager.
> 4. Launch **PixelLoom** and enjoy high-res wallpapers!

*In Google AI Studio, you can also download the latest APK directly or export the complete project ZIP via the top-right Settings/Export menu.*

---

## ✨ Key Features

### 🌐 24+ Unified Wallpaper Communities
Aggregates wallpapers across top creative platforms:
- **Reddit Communities:** `r/Amoledbackgrounds`, `r/wallpaper`, `r/Verticalwallpapers`, `r/MobileWallpaper`, `r/MinimalWallpaper` (Live Reddit JSON feeds)
- **Photography Powerhouses:** Unsplash, Pexels, Pixabay, StockSnap.io
- **Design, Vectors & Minimal:** Freepik, Simple Desktops, Designspiration, Pinterest
- **Digital Art & Anime:** Wallhaven (Live API), Wallpaper Alpha Coders, Anime Wallpaper, DeviantArt, Zedge, Wallpaper Cave, WallpaperSafari, Wallpapers.com, WallpapersWide, InterfaceLIFT, Mobiles Wall

### ✂️ Interactive Freeform Crop & Positioning
- **Single "Set Wallpaper" Action:** Framing and cropping are integrated directly into wallpaper application.
- **Freeform Resizing:** Touch and drag any of the 4 corner handles to define arbitrary rectangular crop regions.
- **Aspect Ratio Presets:** Switch between **Freeform**, **Phone (9:20)**, **Portrait (9:16)**, **Square (1:1)**, and **Wide (16:9)**.
- **Pinch-to-Zoom & Pan:** 1x to 5x multi-touch scaling with rule-of-thirds composition grid.
- **Rotate & Full Image:** 90° clockwise rotation button, reset button, and one-tap **"Full Image"** application.

### 🖼️ Wallpapers of the Day (10 Curated Picks)
- Dedicated carousel on the Home Screen featuring 10 daily curated wallpapers across various aesthetic genres (Amoled, Cyberpunk, Nature, Space, Anime, Urban).

### 📐 Custom Dimensions & High-Res Downloads
- **Original Dimensions:** Downloads at max available resolution directly to `Pictures/PixelLoom`.
- **Preset Resolutions:** FHD+ (1080×2400), QHD+ (1440×3200), Full HD (1080×1920), 4K UHD (2160×3840), Super Retina (1170×2532).
- **Custom Dimensions:** Custom width/height input fields with center-scaled Bitmap Matrix processing.
- Compatible with Android Scoped Storage (`MediaStore.Images.Media`).

### 🔔 Daily Visual Inspirations (WorkManager)
- Background job triggered **2 times daily at random intervals**.
- Pairs inspirational lines about art and aesthetics with deep links (`pixelloom://wallpaper/{id}`) into trending wallpapers.
- Includes an in-app **"Test Notification"** button to preview instantly.

### 💾 Local Room Database
- **Favorites:** Offline storage of bookmarked wallpapers.
- **Download History:** Tracks all saved files, dimensions, and timestamps.

---

## 🛠️ Tech Stack & Architecture

- **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture
- **Language:** Kotlin 2.2
- **UI Framework:** Jetpack Compose with Material Design 3 (Light Mode First)
- **Image Pipeline:** [Coil](https://coil-kt.github.io/coil/) (Asynchronous image loading, caching, bitmap decoding)
- **Local Persistence:** [Android Room Database](https://developer.android.com/training/data-storage/room) with KSP
- **Background Processing:** [AndroidX WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- **Networking & Web Scraping:** OkHttp, Retrofit, [Jsoup](https://jsoup.org/)
- **State Management:** Kotlin Coroutines & `StateFlow`
- **Testing:** JUnit4 & Robolectric local JVM testing

---

## 📂 Project Structure

```
PixelLoom/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/
│   │   │   │   ├── PixelLoomApp.kt          # Application initialization & WorkManager
│   │   │   │   ├── MainActivity.kt          # Navigation, deep links, permission handling
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/               # Room Entities, DAO, Database
│   │   │   │   │   ├── model/               # Wallpaper, WallpaperDimension models
│   │   │   │   │   ├── repository/          # WallpaperRepository, ActionManager (Crop/Set/Save)
│   │   │   │   │   └── sources/             # Modular WallpaperSource interface & 24+ sources
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/          # WallpaperCard, Freeform CropDialog, WOTD Section
│   │   │   │   │   ├── screens/             # HomeScreen, DetailScreen, Favorites, Downloads
│   │   │   │   │   ├── theme/               # Color.kt (Light Mode), Theme.kt, Type.kt
│   │   │   │   │   └── viewmodels/          # WallpaperViewModel
│   │   │   │   └── worker/                  # DailyWallpaperWorker & NotificationScheduler
│   │   │   └── res/
│   │   │       ├── drawable/                # ic_pixelloom_logo.png, vector drawables
│   │   │       ├── mipmap-*/                # Adaptive launcher icons
│   │   │       └── values/                  # strings.xml, colors.xml
│   │   └── test/java/com/example/           # Robolectric unit tests
│   └── build.gradle.kts
├── PixelLoom-v1.0-debug.apk                 # Packaged release APK
├── settings.gradle.kts
└── README.md
```

---

## 🔒 Permissions Used

| Permission | Reason |
|---|---|
| `SET_WALLPAPER` | Setting Home and Lock screen wallpapers via `WallpaperManager` |
| `INTERNET` | Fetching high-resolution wallpapers and community feeds |
| `ACCESS_NETWORK_STATE` | Monitoring network connectivity for graceful offline states |
| `POST_NOTIFICATIONS` | Delivering daily visual inspirations (Android 13+) |

---

## 🚀 Building From Source

```bash
# Clone the repository
git clone https://github.com/your-username/PixelLoom.git
cd PixelLoom

# Build the Debug APK
gradle assembleDebug

# Run unit and Robolectric tests
gradle :app:testDebugUnitTest
```

The compiled APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
Wallpapers displayed are sourced from public APIs and Creative Commons communities for personal customization.
