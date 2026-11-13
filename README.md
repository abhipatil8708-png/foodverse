# FoodVerse – AI-Powered Food Donation & Discovery Platform

> **Tagline:** Turning Food Surplus into Community Smiles with Multimodal AI.

FoodVerse is a modern, native Android application built with Kotlin and Jetpack Compose. It aims to reduce urban food waste and address hunger by seamlessly connecting food donors (restaurants, caterers, banquet organizers, households) with NGOs, community shelters, and volunteers.

---

## 🌟 Highlights & Core Features

### 🍱 Feature 1: AI Food Recognition & Donation
1. **Camera Capture & Gallery Upload:** Donors can capture real-time food photos or select images from their device photo library.
2. **Gemini Multimodal AI Analysis:** Leverages Google's `gemini-1.5-flash` model to analyze the food image directly on device.
3. **Automated Dish & Category Extraction:** Detects the food name, classifies it into categories (`Cooked Meals`, `Bakery`, `Fruits & Veggies`, `Dairy`, `Groceries`, `Beverages`), and provides an appetizing description.
4. **Visual Freshness Observations:** Generates visual appearance notes (intact packaging, vibrant garnishing, storage state).
5. **Strict Food Safety Disclaimer:** Proactively displays unmissable warnings that AI observations are strictly visual and **never** certify microbial food safety.
6. **Smart Publishing Flow:** Auto-populates donation forms with quantity, preparation time, use-by time, and pickup address, persisting the donation in real time.

### 📍 Feature 2: Smart Nearby Food Discovery
1. **Location-Aware Proximity Sorting:** Discovers edible surplus food sorted by real-time distance using the Haversine geospatial formula.
2. **Interactive Filters & Live Search:** Filter by category chips (`Cooked Meals`, `Bakery`, etc.), search by dish name or street address, and toggle "Available only".
3. **Comprehensive Detail View:** High-resolution image preview, Gemini verification badge, donor contact info, and navigation links.
4. **One-Tap Actions:**
   - **Navigate:** Launch external turn-by-turn navigation via Google Maps intent (`geo:lat,lng`).
   - **Call Donor:** Direct phone dialer intent (`Intent.ACTION_DIAL`) to coordinate pickup.
   - **Reserve Food:** Claim food with instant status transition (`AVAILABLE` $\rightarrow$ `RESERVED` $\rightarrow$ `COLLECTED`).
5. **Graceful Permission Handling:** When location permission is denied or pending, automatically provides accurate downtown reference coordinates with a non-blocking prompt to enable GPS.

---

## 🏗️ Technical Architecture & Design System

The application strictly adheres to modern Android **MVVM (Model-View-ViewModel)** with Unidirectional Data Flow (UDF) and the Repository pattern:

```
com.example.foodverse/
├── data/
│   ├── model/
│   │   ├── FoodDonation.kt       // Core donation entity with GPS, time, and status
│   │   ├── FoodCategory.kt       // Category enum with display names & emojis
│   │   ├── DonationStatus.kt     // AVAILABLE, RESERVED, COLLECTED, CANCELLED
│   │   ├── AiAnalysisResult.kt   // Gemini AI extraction result & disclaimers
│   │   └── UserProfile.kt        // User profile, impact points & meals saved
│   ├── repository/
│   │   ├── DonationRepository.kt      // Reactive repository interface
│   │   └── LocalDonationRepository.kt // Reactive StateFlow repository with persistence
│   └── datasource/
│       ├── GeminiFoodService.kt  // Google Generative AI integration + assisted demo mode
│       └── SampleDataProvider.kt // Realistic pre-seeded donations with photos
├── presentation/
│   ├── components/               // FoodDonationCard, StatusChip, SafetyNoticeCard, Buttons, etc.
│   ├── navigation/               // Screen, NavGraph, FoodVerseBottomBar
│   ├── splash/                   // Animated splash screen with brand scaling
│   ├── onboarding/               // 3-slide interactive onboarding carousel
│   ├── auth/                     // Login, Register & 1-click Demo Guest mode
│   ├── home/                     // Dashboard with impact stats & hero AI banner
│   ├── donation/                 // AI Scanner & 2-step donation form
│   ├── discovery/                // Location-based discovery feed & detail view
│   ├── mydonations/              // Active vs. Completed donation manager
│   └── profile/                  // Impact scorecard, safety policy & role switcher
├── ui/theme/                     // Fresh green Material 3 theme & typography
└── utils/                        // LocationHelper (Haversine formula), BitmapUtils
```

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin 2.2.10 |
| **UI Toolkit** | Jetpack Compose (Compose BOM 2024.11.00 / 2026.02.01) |
| **Design System** | Material 3 (Clean White background with Emerald Green `#10B981` accents) |
| **Architecture** | MVVM with Unidirectional Data Flow |
| **State Management** | StateFlow & Coroutines Flow |
| **AI Integration** | Google Generative AI Android SDK (`gemini-1.5-flash`) |
| **Image Loading** | Coil Compose 2.7.0 with async crossfade |
| **Location** | Android Location Services & Haversine Distance Formula |
| **Navigation** | Navigation Compose with type-safe arguments |
| **Build System** | Gradle Kotlin DSL (`agp = 9.5.1`, `compileSdk = 37`, `minSdk = 24`) |

---

## 🚀 Getting Started & Build Instructions

### Prerequisites
1. Android Studio Ladybug / Meerkat (or newer)
2. JDK 17 or JDK 21 (configured in Android Studio)
3. Android SDK 37 (installed via SDK Manager)

### Step 1: Clone or Open the Project
Open the project directory `foodverse` in Android Studio.

### Step 2: (Optional) Configure Gemini AI API Key
FoodVerse includes an intelligent assisted demo mode that operates out of the box without any setup. If you wish to use live Gemini Multimodal API calls:
1. Obtain an API key from [Google AI Studio](https://aistudio.google.com/).
2. Add the following line to `local.properties`:
   ```properties
   GEMINI_API_KEY=AIzaSyYourActualGeminiApiKeyHere
   ```
3. Sync and build the project. The key is securely injected at compile time via `BuildConfig.GEMINI_API_KEY` and will **never** be checked into version control.

### Step 3: Run the Application
1. Connect a physical Android device via USB (with USB Debugging enabled) or start an Android Emulator (API 24 to 35+).
2. Run the Gradle assemble command:
   ```bash
   ./gradlew assembleDebug
   ```
3. The APK will be generated at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 🧪 Testing

The repository contains automated unit tests verifying business logic, geospatial distance calculations, and category classification:

```bash
./gradlew testDebugUnitTest
```

Result: **100% Passed (5/5 tests in `DonationRepositoryTest` + `ExampleUnitTest`)**.

---

## 🛡️ Responsible AI & Food Safety Policy

FoodVerse incorporates responsible AI practices:
* **Strict Non-Certification Rule:** Gemini AI is explicitly prevented from certifying food as safe or fit for human consumption.
* **Persistent Safety Banner:** Every AI observation screen displays a clear advisory reminding donors and claimers to inspect food visually, check expiration dates, and verify hygiene before distribution.
