package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GoblinLightColorScheme = lightColorScheme(
    primary = GoblinTextPrimary,
    onPrimary = GoblinBg,
    primaryContainer = GoblinSurface,
    onPrimaryContainer = GoblinTextPrimary,
    secondary = GoblinTextSecondary,
    onSecondary = GoblinBg,
    secondaryContainer = GoblinSurfaceElevated,
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
    darkTheme: Boolean = false, // Pure crisp gallery white theme
    dynamicColor: Boolean = false, // Keep bespoke editorial monochrome palette
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GoblinLightColorScheme,
        typography = Typography,
        content = content
    )
}
