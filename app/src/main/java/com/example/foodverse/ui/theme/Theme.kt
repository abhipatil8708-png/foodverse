package com.example.foodverse.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = FoodVerseGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = FoodVerseGreenLight,
    onPrimaryContainer = FoodVerseGreenDark,
    secondary = FoodVerseTeal,
    onSecondary = Color.White,
    secondaryContainer = FoodVerseTealLight,
    onSecondaryContainer = FoodVerseTeal,
    tertiary = FoodVerseWarmOrange,
    onTertiary = Color.White,
    tertiaryContainer = FoodVerseWarmOrangeLight,
    onTertiaryContainer = FoodVerseWarmOrange,
    background = FoodVerseBackground,
    onBackground = FoodVerseTextPrimary,
    surface = FoodVerseSurface,
    onSurface = FoodVerseTextPrimary,
    surfaceVariant = FoodVerseSurfaceVariant,
    onSurfaceVariant = FoodVerseTextSecondary,
    outline = FoodVerseBorder,
    error = FoodVerseRed,
    onError = Color.White
)

@Composable
fun FoodverseTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = Color.Transparent.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
