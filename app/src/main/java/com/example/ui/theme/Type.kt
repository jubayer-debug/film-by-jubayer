package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Primary Clean Modern Sans-Serif for Headings, Titles, Philosophy & Monograms
val EditorialSerif = FontFamily.SansSerif

// Clean Modern Sans-Serif for Navigation, Metadata, Captions, Body
val EditorialSans = FontFamily.SansSerif

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp,
        color = GoblinTextPrimary
    ),
    displayMedium = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.2).sp,
        color = GoblinTextPrimary
    ),
    displaySmall = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp,
        color = GoblinTextPrimary
    ),
    headlineLarge = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp,
        color = GoblinTextPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
        color = GoblinTextPrimary
    ),
    titleLarge = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp,
        color = GoblinTextPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.2.sp,
        color = GoblinTextPrimary
    ),
    bodyLarge = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        color = GoblinTextSecondary
    ),
    bodyMedium = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.15.sp,
        color = GoblinTextSecondary
    ),
    labelLarge = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.2.sp,
        color = GoblinTextPrimary
    ),
    labelMedium = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 1.0.sp,
        color = GoblinTextSecondary
    ),
    labelSmall = TextStyle(
        fontFamily = EditorialSans,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.8.sp,
        color = GoblinTextTertiary
    )
)

