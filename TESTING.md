# FoodVerse – Test Verification Report

**Date of Execution:** November 13, 2026  
**Test Suite:** Android Local Unit Tests & UI Functional Verification  
**Status:** **ALL TESTS PASSED (100% Success Rate)**

---

## 1. Automated Unit Test Results

The test suite was executed via Gradle task `:app:testDebugUnitTest`.

| Test Class | Test Case | Target Component | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| `DonationRepositoryTest` | `getDonations_returnsInitialSampleData` | `LocalDonationRepository` | Returns non-empty list with initial donations pre-sorted by distance | List contains 6 realistic donations with calculated distance | **PASS** |
| `DonationRepositoryTest` | `addDonation_addsItemAndUpdatesUserImpact` | `LocalDonationRepository` | New donation inserted at index 0; user donations count incremented; meals saved increased | Item added at top; donations count +1; meals saved increased by quantity | **PASS** |
| `DonationRepositoryTest` | `updateStatus_changesStatusToReservedAndCollected` | `LocalDonationRepository` | Status changes from `AVAILABLE` to `RESERVED` and then `COLLECTED` with claimer name | Status updated; claimed timestamp recorded; Flow emits update | **PASS** |
| `DonationRepositoryTest` | `locationHelper_calculatesDistanceAccurately` | `LocationHelper` | Haversine formula calculates distance within 4.0 - 6.5 km; formats meters (<1km) and km (>=1km) | 5.0 km calculated; "3.4 km away" and "450m away" formatted | **PASS** |
| `DonationRepositoryTest` | `foodCategory_parsesVariousStringsCorrectly` | `FoodCategory` | Matches exact and fuzzy strings for bakery, fruit/veg, dairy, beverages, canned goods | All category variants parsed to valid enum values | **PASS** |
| `ExampleUnitTest` | `addition_isCorrect` | Core environment | Basic host unit test harness sanity check | 2 + 2 = 4 | **PASS** |

---

## 2. Functional & Manual Verification Matrix

| Feature Area | Verification Scenario | Steps Executed | Observed Result | Status |
|---|---|---|---|---|
| **Splash & Onboarding** | First launch sequence | 1. App launches with animated brand icon.<br>2. Transitions to 3-slide Onboarding.<br>3. "Get Started" navigates to Auth. | Smooth animation; pager scrolls through all 3 slides; button navigates to Auth screen. | **PASS** |
| **Authentication** | Demo Guest 1-Click Login | Tap "Continue as Demo Guest / Evaluator". | Bypasses manual entry; initializes demo profile ("Alex Chen"); immediately lands on Home. | **PASS** |
| **Authentication** | Input Validation | Submit with blank email or password < 6 chars. | Red warning banner displayed: "Please enter a valid email address." | **PASS** |
| **Home Dashboard** | Metric Cards & AI Banner | Inspect Meals Saved, Active Listings, and Hero Card. | Correct values displayed; tapping hero card opens AI Food Recognition scanner. | **PASS** |
| **Feature 1: AI Recognition** | Photo Selection / Demo Select | Choose a sample dish (Rice & Curry, Bakery, Fruit) or upload photo. | Image loads; multi-step progress indicator runs ("Optimizing image...", "Gemini AI identifying dish..."). | **PASS** |
| **Feature 1: AI Extraction** | Data Extraction & Safety Banner | Inspect AI Recognition Result card. | Dish name, category, description, and visual freshness observation extracted. Strict food safety disclaimer banner displayed. | **PASS** |
| **Feature 1: Donation Publishing** | Form Submission | Fill quantity, prep time, expiry, address, phone; click "Publish Food Donation". | Confirmation dialog appears: "Donation Published!"; item appears in My Donations and Discovery feed. | **PASS** |
| **Feature 2: Nearby Discovery** | Proximity Sorting | Open "Discover" tab. | Donations sorted with closest first (e.g., "350m away", "1.1 km away"). | **PASS** |
| **Feature 2: Filtering & Search** | Live Search & Category Chips | Select "Bakery" chip or search for "Biryani". | Instant filtering; non-matching items hidden; shows count and empty state if no match. | **PASS** |
| **Feature 2: Location Handling** | Permission Denial Fallback | Refuse location permission or run without GPS. | Informational badge shows "Downtown (Tap to enable GPS)"; reference distance calculated seamlessly without crash. | **PASS** |
| **Feature 2: Reservation & Details** | Reserve Food Flow | Tap donation card $\rightarrow$ click "Request & Reserve This Food". | Confirmation dialog shows expiry warning; confirms $\rightarrow$ status changes to `RESERVED`; button changes to "Mark as Successfully Collected". | **PASS** |
| **Donation Actions** | Google Maps & Phone Dial Intents | Click "Navigate with Google Maps" or "Call Donor". | Native Android Intent created with correct `geo:` and `tel:` URIs. | **PASS** |
| **My Donations** | Active vs Completed Tabs | Switch between Active and History tabs. | Active shows pending items; marking collected moves item to History. | **PASS** |
| **Profile** | Impact & Sign Out | Check impact stats; click "Sign Out". | Displays meals saved; signs out to Auth screen. | **PASS** |
