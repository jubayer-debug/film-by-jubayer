package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Photograph
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBorderSubtle
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Editorial Full-Bleed Hero Component for 'Film by Jubayer'
 *
 * Features:
 * - Expansive landscape photography viewport showcase
 * - Staggered Framer-style spring entrance animations for background zoom and typography
 * - Multi-stop film gradient mask for high visual hierarchy
 * - Minimal 'Film by Jubayer' branding & location/technical spec
 * - Continuous breathing bounce scroll indicator with direct tap action
 */
@Composable
fun HeroSection(
    heroPhoto: Photograph,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onScrollDown: () -> Unit,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // -------------------------------------------------------------------------
    // FRAMER-MOTION STYLE SPRING & STAGGERED ENTRANCE ANIMATIONS
    // -------------------------------------------------------------------------
    val photoScale = remember { Animatable(1.08f) }
    val photoAlpha = remember { Animatable(0f) }

    val brandAlpha = remember { Animatable(0f) }
    val brandOffsetY = remember { Animatable(24f) }

    val headlineAlpha = remember { Animatable(0f) }
    val headlineOffsetY = remember { Animatable(32f) }

    val metadataAlpha = remember { Animatable(0f) }
    val metadataOffsetY = remember { Animatable(20f) }

    val scrollIndicatorAlpha = remember { Animatable(0f) }
    val scrollIndicatorOffsetY = remember { Animatable(16f) }

    LaunchedEffect(Unit) {
        // 1. Initial background image reveal & subtle settle zoom
        launch {
            photoAlpha.animateTo(1f, tween(900, easing = FastOutSlowInEasing))
        }
        launch {
            photoScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        // 2. Brand badge entrance (staggered delay 150ms)
        delay(150)
        launch {
            brandAlpha.animateTo(1f, tween(500))
        }
        launch {
            brandOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.8f,
                    stiffness = 140f
                )
            )
        }

        // 3. Headline & quote entrance (staggered delay 300ms)
        delay(150)
        launch {
            headlineAlpha.animateTo(1f, tween(600))
        }
        launch {
            headlineOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.85f,
                    stiffness = 120f
                )
            )
        }

        // 4. Coordinates & metadata entrance (staggered delay 450ms)
        delay(150)
        launch {
            metadataAlpha.animateTo(1f, tween(500))
        }
        launch {
            metadataOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.75f,
                    stiffness = 130f
                )
            )
        }

        // 5. Scroll indicator entrance (staggered delay 600ms)
        delay(150)
        launch {
            scrollIndicatorAlpha.animateTo(1f, tween(600))
        }
        launch {
            scrollIndicatorOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = 120f
                )
            )
        }
    }

    // Continuous breathing bounce animation for scroll indicator
    val infiniteTransition = rememberInfiniteTransition(label = "ScrollIndicatorBounce")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_section")
    ) {
        // Calculate expansive hero viewport height
        val heroHeight = (maxHeight * 0.88f).coerceIn(580.dp, 800.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heroHeight)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onPhotoClick() }
        ) {
            // -----------------------------------------------------------------
            // 1. POWERFUL LANDSCAPE BACKGROUND ARTWORK WITH SPRING ZOOM
            // -----------------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(photoScale.value)
                    .alpha(photoAlpha.value)
            ) {
                PhotographicArtwork(
                    photograph = heroPhoto,
                    isMonochrome = isMonochrome,
                    showFilmGrain = showFilmGrain,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // -----------------------------------------------------------------
            // 2. EDITORIAL MULTI-STOP GRADIENT OVERLAY
            // -----------------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color(0x99000000),
                                0.25f to Color(0x22000000),
                                0.60f to Color(0x55000000),
                                0.85f to Color(0xCC000000),
                                0.98f to Color(0xFAFFFFFF),
                                1.0f to GoblinBg
                            )
                        )
                    )
            )

            // -----------------------------------------------------------------
            // 3. EDITORIAL HERO FOREGROUND CONTENT
            // -----------------------------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row: Minimal 'Film by Jubayer' Branding & Darkroom Spec
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(0, brandOffsetY.value.roundToInt()) }
                        .alpha(brandAlpha.value),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2A860))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "FILM BY JUBAYER",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                letterSpacing = 3.5.sp,
                                color = Color(0xFFF6F5F2)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A CINEMATIC VISUAL ARCHIVE",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.5.sp,
                            letterSpacing = 2.0.sp,
                            color = Color(0xFFB5B3AE)
                        )
                    }

                    // Technical EXIF Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .border(0.5.dp, Color(0x44FFFFFF), RoundedCornerShape(4.dp))
                            .background(Color(0x77000000))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CenterFocusStrong,
                                contentDescription = "Camera metadata",
                                tint = Color(0xFFCCCCCC),
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LEICA M • 35MM",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.5.sp,
                                letterSpacing = 1.4.sp,
                                color = Color(0xFFEEEEEE)
                            )
                        }
                    }
                }

                // Middle: Evocative Headline & Essay Lead
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(0, headlineOffsetY.value.roundToInt()) }
                        .alpha(headlineAlpha.value)
                ) {
                    Text(
                        text = "BANGLADESH LANDSCAPE DOCUMENTARY",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.5.sp,
                        letterSpacing = 2.8.sp,
                        color = Color(0xFFE2A860)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Light, water &\nshifting silt across\nthe river delta.",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Light,
                        fontSize = 36.sp,
                        lineHeight = 44.sp,
                        letterSpacing = (-0.5).sp,
                        color = Color(0xFFFFFFFF)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Documenting transient landscapes, tidal waters, and human resilience through analog discipline.",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = Color(0xFFDDDDDC),
                        modifier = Modifier.fillMaxWidth(0.88f)
                    )
                }

                // Bottom: Landscape Geo Coordinates + Interactive Scroll Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Geo Coordinates & Season Info
                    Column(
                        modifier = Modifier
                            .offset { IntOffset(0, metadataOffsetY.value.roundToInt()) }
                            .alpha(metadataAlpha.value)
                    ) {
                        Text(
                            text = heroPhoto.location.uppercase(),
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                            letterSpacing = 2.0.sp,
                            color = Color(0xFFFFFFFF)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "23° 48' N • MONSOON ARCHIVE",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.5.sp,
                            letterSpacing = 1.4.sp,
                            color = Color(0xFFB5B3AE)
                        )
                    }

                    // Interactive Framer-Style Scroll Indicator
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(0, scrollIndicatorOffsetY.value.roundToInt()) }
                            .alpha(scrollIndicatorAlpha.value)
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .border(0.5.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
                                .background(Color(0x99000000))
                                .clickable { onScrollDown() }
                                .padding(horizontal = 14.dp, vertical = 9.dp)
                                .testTag("scroll_indicator_button"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SCROLL TO EXPLORE",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 2.0.sp,
                                color = Color(0xFFFFFFFF)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Scroll down",
                                tint = Color(0xFFE2A860),
                                modifier = Modifier
                                    .size(13.dp)
                                    .offset(y = bounceOffset.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
