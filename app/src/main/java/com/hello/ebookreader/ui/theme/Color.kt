package com.hello.ebookreader.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

object LightColors {
    val PrimaryColor = Color(0xFF1E88E5)
    val OnPrimaryColor = Color(0xFFFFFFFF)
    val SecondaryColor = Color(0xFFFF6E40)
    val BackgroundColor = Color(0xFFF5F5F5)
    val SurfaceColor = Color(0xFFFFFFFF)
    val TextPrimaryColor = Color(0xFF212121)
    val TextSecondaryColor = Color(0xFF757575)
    val AccentColor1 = Color(0xFF00C853)
    val ErrorColor = Color(0xFFD50000)
}

object DarkColors {
    val PrimaryColor = Color(0xFF3F51B5)
    val OnPrimaryColor = Color(0xFFFFFFFF)
    val SecondaryColor = Color(0xFFFF4081)
    val BackgroundColor = Color(0xFF121212)
    val SurfaceColor = Color(0xFF1E1E1E)
    val TextPrimaryColor = Color(0xFFFFFFFF)
    val TextSecondaryColor = Color(0xFFB0B0B0)
    val AccentColor1 = Color(0xFF00BFA5)
    val ErrorColor = Color(0xFFFF5252)
}

object AppColors {
    val PrimaryColor @Composable @ReadOnlyComposable get() = if (isSystemInDarkTheme()) DarkColors.PrimaryColor else LightColors.PrimaryColor
    val OnPrimaryColor @Composable @ReadOnlyComposable get() = if (isSystemInDarkTheme()) DarkColors.OnPrimaryColor else LightColors.OnPrimaryColor
    val SecondaryColor @Composable @ReadOnlyComposable get() = if (isSystemInDarkTheme()) DarkColors.SecondaryColor else LightColors.SecondaryColor
    val BackgroundColor @Composable @ReadOnlyComposable get() = if (isSystemInDarkTheme()) DarkColors.BackgroundColor else LightColors.BackgroundColor
    val SurfaceColor @Composable @ReadOnlyComposable get() = if (isSystemInDarkTheme()) DarkColors.SurfaceColor else LightColors.SurfaceColor
    val TextPrimaryColor @Composable @ReadOnlyComposable get() = if (isSystemInDarkTheme()) DarkColors.TextPrimaryColor else LightColors.TextPrimaryColor
    val TextSecondaryColor @Composable @ReadOnlyComposable get() = if (isSystemInDarkTheme()) DarkColors.TextSecondaryColor else LightColors.TextSecondaryColor
    val AccentColor1 @Composable @ReadOnlyComposable get() = if (isSystemInDarkTheme()) DarkColors.AccentColor1 else LightColors.AccentColor1
    val ErrorColor @Composable @ReadOnlyComposable get() = if (isSystemInDarkTheme()) DarkColors.ErrorColor else LightColors.ErrorColor
}

// Night mode colors remain the same as they are specific to the reading mode
val NightModeBackground = Color(0xFF121212)
val NightModeText = Color(0xFFE0E0E0)