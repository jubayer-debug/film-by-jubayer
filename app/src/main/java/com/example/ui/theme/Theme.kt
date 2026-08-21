package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GoblinColorScheme = darkColorScheme(
    primary = GoblinTextPrimary,
    onPrimary = GoblinBg,
    primaryContainer = GoblinSurfaceElevated,
    onPrimaryContainer = GoblinTextPrimary,
    secondary = GoblinTextSecondary,
    onSecondary = GoblinBg,
    secondaryContainer = GoblinSurface,
    onSecondaryContainer = GoblinTextPrimary,
    tertiary = GoblinAccentWarm,
    onTertiary = GoblinBg,
    background = GoblinBg,
    onBackground = GoblinTextPrimary,
    surface = GoblinBgSecondary,
    onSurface = GoblinTextPrimary,
    surfaceVariant = GoblinSurface,
    onSurfaceVariant = GoblinTextSecondary,
    outline = GoblinBorderSubtle,
    outlineVariant = GoblinBorderFocused
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to cinematic dark aesthetic
    dynamicColor: Boolean = false, // Keep bespoke editorial monochrome palette
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GoblinColorScheme,
        typography = Typography,
        content = content
    )
}
