package com.seminario2026.eventpulse.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val CyberColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = MidnightVoid,
    primaryContainer = DeepSpace,
    onPrimaryContainer = CyberCyan,
    secondary = NeonMagenta,
    onSecondary = SoftWhite,
    secondaryContainer = DarkCharcoal,
    onSecondaryContainer = NeonMagenta,
    tertiary = AcidLime,
    onTertiary = MidnightVoid,
    background = MidnightVoid,
    onBackground = SoftWhite,
    surface = DeepSpace,
    onSurface = SoftWhite,
    surfaceVariant = DarkCharcoal,
    onSurfaceVariant = MutedSilver,
    error = NeonMagenta,
    onError = SoftWhite
)

@Composable
fun EventPulseTheme(
    darkTheme: Boolean = true, // Force dark cyberpunk look by default
    content: @Composable () -> Unit
) {
    val colorScheme = CyberColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = MidnightVoid.toArgb()
            window.navigationBarColor = MidnightVoid.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
