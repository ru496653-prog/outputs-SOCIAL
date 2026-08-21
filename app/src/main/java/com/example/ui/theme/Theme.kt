package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val OutputsDarkColorScheme = darkColorScheme(
    primary = BrandTerracotta,
    onPrimary = CanvasDark,
    primaryContainer = BrandEspresso,
    onPrimaryContainer = TextPrimaryDark,
    secondary = BrandWarmAmber,
    onSecondary = CanvasDark,
    secondaryContainer = SurfaceElevatedDark,
    onSecondaryContainer = TextPrimaryDark,
    tertiary = BrandSage,
    onTertiary = CanvasDark,
    background = CanvasDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceCardDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderDark,
    outlineVariant = BorderDark.copy(alpha = 0.6f),
    error = BrandCrimsonRust,
    onError = TextPrimaryDark
)

private val OutputsLightColorScheme = lightColorScheme(
    primary = BrandEspresso,
    onPrimary = SurfaceLight,
    primaryContainer = SurfaceElevatedLight,
    onPrimaryContainer = BrandEspresso,
    secondary = BrandTerracotta,
    onSecondary = SurfaceLight,
    secondaryContainer = BrandSand.copy(alpha = 0.3f),
    onSecondaryContainer = TextPrimaryLight,
    tertiary = BrandSage,
    onTertiary = SurfaceLight,
    background = CanvasLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceCardLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight,
    outlineVariant = BorderLight.copy(alpha = 0.7f),
    error = BrandCrimsonRust,
    onError = SurfaceLight
)

@Composable
fun OutputsTheme(
    darkTheme: Boolean = true, // Default to deep cinematic dark theme for mystery vibe
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) OutputsDarkColorScheme else OutputsLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
