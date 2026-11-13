# FoodVerse – Bug & Issue Log

This document records the defects encountered during development, the root cause analysis, the precise fix applied, and the retest verification.

---

### Bug 01: Unresolved Reference `util` in Gradle DSL Properties Loading
* **Symptom:** Gradle script compilation failed with:  
  `Unresolved reference 'util' at line 23: val properties = java.util.Properties()`
* **Root Cause:** In Kotlin DSL (`build.gradle.kts`), explicit Java class references like `java.util.Properties` require imports or explicit package resolution.
* **Files Affected:** `app/build.gradle.kts`
* **Fix Applied:** Added `import java.util.Properties` and `import java.io.FileInputStream` at the top of `app/build.gradle.kts` and loaded `local.properties` cleanly via `Properties()`.
* **Retest Result:** Gradle configuration succeeded without errors.

---

### Bug 02: Kotlin Compilation Error – Direct Composable Invocation in LazyColumn Block
* **Symptom:** `compileDebugKotlin` failed with error:  
  `HomeScreen.kt:365:9 @Composable invocations can only happen from the context of a @Composable function`
* **Root Cause:** A `Spacer(modifier = Modifier.height(16.dp))` composable was placed directly inside `LazyColumn { ... }` (which expects `item { ... }` or `items { ... }` scopes) instead of being wrapped inside an `item { }` block.
* **Files Affected:** `app/src/main/java/com/example/foodverse/presentation/home/HomeScreen.kt`
* **Fix Applied:** Wrapped the spacer call inside an `item { Spacer(modifier = Modifier.height(16.dp)) }` block.
* **Retest Result:** Compilation error resolved.

---

### Bug 03: Missing Import for `Modifier.width()` in MyDonationsScreen
* **Symptom:** Kotlin compilation failed with:  
  `Unresolved reference 'width' at lines 219 and 234 in MyDonationsScreen.kt`
* **Root Cause:** `androidx.compose.foundation.layout.width` was not included in the import list of `MyDonationsScreen.kt`.
* **Files Affected:** `app/src/main/java/com/example/foodverse/presentation/mydonations/MyDonationsScreen.kt`
* **Fix Applied:** Added `import androidx.compose.foundation.layout.width` to `MyDonationsScreen.kt`.
* **Retest Result:** Compilation error resolved.

---

### Bug 04: JVM Signature Clash in `LocalDonationRepository`
* **Symptom:** Kotlin compilation failed with:  
  `Platform declaration clash: The following declarations have the same JVM signature (getDonations()Lkotlinx/coroutines/flow/Flow;)`
* **Root Cause:** Declaring a public Kotlin property `val donations: Flow<...>` automatically creates a synthetic JVM getter named `getDonations()`. Because the interface `DonationRepository` also defines `fun getDonations()`, the Kotlin compiler flagged a duplicate JVM bytecode method signature clash.
* **Files Affected:** `app/src/main/java/com/example/foodverse/data/repository/LocalDonationRepository.kt`, `MyDonationsScreen.kt`, `ProfileScreen.kt`
* **Fix Applied:** Kept the backing state flows `_donations` and `_userProfile` private, removed the redundant public properties, and exposed data solely via interface methods `override fun getDonations()` and `override fun getUserProfile()`. Updated callers to invoke the methods.
* **Retest Result:** Clean compilation with 0 signature conflicts.

---

### Bug 05: Material 3 Deprecations (`Divider`, `ArrowForward`, `ExitToApp`)
* **Symptom:** Compiler produced deprecation warnings for `Divider`, `Icons.Default.ArrowForward`, and `Icons.Default.ExitToApp`.
* **Root Cause:** Material 3 replaced `Divider` with `HorizontalDivider`, and directional icons now recommend auto-mirrored variants (`Icons.AutoMirrored.Filled.*`) for right-to-left layout compatibility.
* **Files Affected:** `AuthScreen.kt`, `AiRecognitionScreen.kt`, `ProfileScreen.kt`
* **Fix Applied:** Upgraded to `HorizontalDivider`, `Icons.AutoMirrored.Filled.ArrowForward`, and `Icons.AutoMirrored.Filled.ExitToApp`.
* **Retest Result:** Warnings eliminated; code follows latest Android Material 3 best practices.
