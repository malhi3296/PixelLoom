<p align="center">
  <img src="docs/screenshots/app_icon.png" alt="PixelLoom App Icon" width="128" style="border-radius: 28px;" />
</p>

<h1 align="center">PixelLoom</h1>

<p align="center">
  <strong>Curated High-Resolution Wallpapers Across the Web</strong><br>
  Built with Kotlin & Jetpack Compose • Material 3 • Freeform Cropping • 24+ Sources
</p>

<p align="center">
  <a href="#-direct-apk-download--installation"><img src="https://img.shields.io/badge/Release-v1.0--Direct%20APK-7C3AED?style=for-the-badge&logo=android" alt="Download APK"></a>
  <img src="https://img.shields.io/badge/Stability-Crash--Free%20Universal-success?style=for-the-badge" alt="Stability">
  <img src="https://img.shields.io/badge/Platform-Android%207.0%2B%20(API%2024%2B)-brightgreen?style=for-the-badge" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin%202.2-purple?style=for-the-badge&logo=kotlin" alt="Language">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-blue?style=for-the-badge" alt="Compose">
</p>

---

## 📱 About PixelLoom

**PixelLoom** weaves together premier creative communities and digital art archives into a unified, high-performance Android wallpaper hub. Engineered for complete reliability across all physical Android devices (Android 7.0 / API 24 through Android 15 / API 36), PixelLoom features a signature **Purple & White** brand identity, adaptive launcher icon, interactive **Freeform Cropping & Positioning**, and custom resolution downloads.

---

## 📥 Direct APK Download & Installation

The signed debug APK is ready for immediate installation on any physical Android device or emulator:

| Artifact | Direct Link | File Size | Compatibility |
|---|---|---|---|
| 🚀 **PixelLoom v1.0 Universal APK** | **[Download PixelLoom-v1.0-debug.apk](./PixelLoom-v1.0-debug.apk)** | ~23 MB | Android 7.0+ (API 24 - 36) |
| 📦 **Gradle Build Output** | `app/build/outputs/apk/debug/app-debug.apk` | ~23 MB | Universal |

### Quick Installation Guide
1. **Download:** Click the direct link above to download **[`PixelLoom-v1.0-debug.apk`](./PixelLoom-v1.0-debug.apk)** directly to your Android device.
2. **Install:** Tap the downloaded file in your browser or File Manager.
3. **Allow Source:** When prompted, toggle "Allow from this source" in system settings.
4. **Launch:** Open **PixelLoom** from your app drawer and enjoy ultra-fast, crash-free browsing!

---

## 📸 App Screenshots

<p align="center">
  <img src="docs/screenshots/screenshot_home.jpg" width="46%" alt="PixelLoom Home Screen Feed with Dark Mode Toggle & Wikipedia Section" />
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="docs/screenshots/screenshot_crop.jpg" width="46%" alt="PixelLoom Draggable Crop Box & Centered Viewport" />
</p>

---

## 🎨 Brand Identity & Logo Colorways

PixelLoom features a distinctive woven pixel icon motif. The app icon pairs a royal purple backdrop (`#6B38FB`) with a crisp white cutout logo in front, complemented by dynamic multi-color variants across the UI:

<p align="center">
  <img src="docs/screenshots/logo_palette.png" width="90%" alt="PixelLoom Logo Colorways Palette" />
</p>

- **Launcher Icon:** Royal Purple background (`#6B38FB`) with pure white logo cutout (`#FFFFFF`) in front, fully adaptive across Android 8.0+ squircle/round masking.
- **Top App Header:** Clean brand badge with an instant **Dark / Light Mode Toggle Button** replacing cluttered text headers.
- **Splash Screen:** Beautiful dual light and dark mode splash screen with pulsing ambient aura, squircle badge, and animated typography.
- **Multi-Color Spectrum:** Dynamic color accents including **Royal Purple**, **Cyber Cyan**, **Sunset Amber**, and **Emerald Mint**.

---

## 🛡️ Robust Startup Architecture (Crash-Proof)

The latest build addresses all physical device startup edge-cases:
- **Clean Manifest & Zero Firebase Dependencies:** Removed unused Google Services providers that previously caused `IllegalStateException` crashes on launch when running without backend credentials.
- **Resilient WorkManager & Notification Channels:** Notification channels and background workers are isolated with defensive exception handling and standard monochromatic notification drawables (`ic_notification`).
- **Thread-Safe Lazy Initialization:** Application singletons (Database, Repository, ActionManager) use thread-safe Kotlin lazy delegates to eliminate race conditions during cold starts and activity recreation.
- **Scoped Storage & WallpaperManager Guarding:** Media storage and lock-screen wallpaper operations are guarded with OEM-specific API level checks.
- **Edge-to-Edge System Bar Insets:** Full `navigationBarsPadding()` across full-screen modals and crop dialogs ensures action buttons never sit behind Android 3-button or gesture bars.

---

## ✨ Key Features & Recent Enhancements

### 🌐 Curated Wallpaper Sources & Wikipedia Archive
- **Wikipedia / Wikimedia Commons:** Dedicated carousel featuring high-resolution Wikimedia "Picture of the Day" and historic curated open photography.
- **Reddit Feeds:** `r/Amoledbackgrounds`, `r/wallpaper`, `r/Verticalwallpapers`, `r/MobileWallpaper`, `r/MinimalWallpaper`
- **Design & Digital Art:** Wallhaven, Unsplash, Pexels, Pixabay, StockSnap.io, Freepik, Anime Wallpaper, Simple Desktops, and more.
- **Universal Search:** Instant debounced search with URL-safe query handling across tags like `#Amoled`, `#Wikipedia`, `#Minimal`, `#Anime`, and custom keywords.

### ✂️ Finger-Draggable Freeform Crop & Quick Set
- **Draggable Crop Box:** Place fingers inside the crop rectangle to slide the selection freely across the image, or drag corner handles to resize.
- **Balanced Viewport:** Centered layout eliminating top whitespace between the top bar and bottom controls.
- **Aspect Ratio Presets:** Switch between **Freeform**, **Phone (9:20)**, **Portrait (9:16)**, **Square (1:1)**, and **Wide (16:9)**.
- **Full Image Mode:** One-tap option to apply wallpapers in their native aspect ratio without cropping.

### 🔔 Notification Drawer Download Progress & Actions
- **Live Progress Bar:** Shows real-time indeterminate and tiered percentage progress in the Android notification bar while downloading.
- **Tap to Open:** Tapping completed download notifications directly opens the saved image in your favorite gallery viewer.

### 📂 Download History with Full Preview
- Tapping any wallpaper in the **Download History** tab opens it directly in the immersive full-screen detail view for re-applying or custom cropping.

### 💖 Real-Time Favorites
- Mark or unmark favorites directly inside the wallpaper detail view with immediate reactive UI updates.

### ⚙️ Background Inspiration & Notification Controls
- Inspiration wallpapers schedule automatically twice a day in the background.
- Manual triggers replaced with an **Open Notification Settings** shortcut allowing direct system-level alert customization.

---

## 🛠️ Tech Stack

- **Language:** Kotlin 2.2
- **UI Toolkit:** Jetpack Compose with Material 3 (Dual Light & Dark Theme)
- **Image Loading:** Coil 2.7
- **Database:** Android Room 2.7 with KSP
- **Background Tasks:** AndroidX WorkManager
- **Networking:** OkHttp 4.12, Retrofit 2.11, Jsoup
- **Testing:** Robolectric & JUnit 4 JVM testing

---

## 🚀 Building From Source

```bash
# Clone the repository
git clone https://github.com/your-username/PixelLoom.git
cd PixelLoom

# Build debug APK
gradle assembleDebug

# Run unit & Robolectric tests
gradle :app:testDebugUnitTest
```

---

## 📄 License

Licensed under the [MIT License](LICENSE).
