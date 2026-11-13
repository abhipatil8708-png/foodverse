# FoodVerse – AI Usage & Transparency Log

This document outlines the responsible use of Artificial Intelligence during the conception, design, architecture, implementation, and verification of the **FoodVerse** Android application.

---

## 1. Role of AI Assistant (Antigravity)

During this project, Antigravity acted as a **Senior Android Architect, UI/UX Designer, Lead Kotlin Developer, QA Engineer, and Technical Mentor**. The student provided the core idea, problem statement, feature constraints, and domain goals. Antigravity converted this vision into a production-quality native Android application.

---

## 2. AI-Assisted Architecture & System Design
* **MVVM Layer Separation:** Designed clean boundaries between `data`, `presentation`, `navigation`, `components`, and `utils`.
* **Resilient Dual-Mode Repository Pattern:** Developed the `LocalDonationRepository` with reactive StateFlow so the application runs seamlessly out-of-the-box with pre-seeded sample data, while remaining completely ready for Firebase Firestore backend attachment.
* **Responsible AI & Food Safety Disclaimers:** Designed system prompts and strict UI disclaimers guaranteeing that Gemini never claims to certify microbial food safety.

---

## 3. Multimodal Gemini AI Integration
* Integrated `com.google.ai.client.generativeai:generativeai:0.9.0` with the `gemini-1.5-flash` multimodal model.
* Engineered a structured JSON prompt extracting `foodName`, `category`, `description`, `freshnessObservation`, `estimatedServings`, and `storageRecommendation`.
* Implemented a zero-crash fallback and assisted demo mode for offline testing or environments without an API key.
* Secured the API key via Gradle `buildConfigField` reading from `local.properties` rather than hardcoding credentials into source code.

---

## 4. Debugging & QA Assistance
* Diagnosed and fixed Kotlin compiler errors including JVM getter signature clashes (`<get-donations>` vs `getDonations()`) and Jetpack Compose `@Composable` invocation scoping in `LazyColumn`.
* Generated automated JUnit unit tests covering the repository, category parsing, and geospatial Haversine distance calculations.

---

## 5. What the Student Should Review & Experiment With
1. **Gemini API Key:** Add your personal API key from [Google AI Studio](https://aistudio.google.com/) into `local.properties` (`GEMINI_API_KEY=...`) to test live camera recognition on novel dishes!
2. **Custom Categories:** Add or modify categories in `FoodCategory.kt` (e.g., adding organic farm produce, pet food donations).
3. **Location Defaults:** Update `DEFAULT_USER_LAT` and `DEFAULT_USER_LNG` in `LocationHelper.kt` to match your home city coordinates.
