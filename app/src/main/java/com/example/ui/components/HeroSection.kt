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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoblinTextPrimary
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Editorial Hero Section with White Background and All Black Typography
 *
 * Displays:
 * - Headline: "Here we go... With the clicks of Jubayer Ahmed." (with "Jubayer Ahmed" in cursive/display font with increased size, linked to About page)
 * - Description: "Landscapes, Documentary, Rural, Wildlife, Nature, and Visual Storytelling Photography Based in Habiganj, Bangladesh."
 * - Description (small): "Exploring fleeting light, rural textures, candid people, greenery, rivers, traditions, and everyday moments—archiving"
 */
@Composable
fun HeroSection(
    onScrollDown: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    // -------------------------------------------------------------------------
    // ENTRANCE ANIMATIONS
    // -------------------------------------------------------------------------
    val badgeAlpha = remember { Animatable(0f) }
    val badgeOffsetY = remember { Animatable(20f) }

    val headlineAlpha = remember { Animatable(0f) }
    val headlineOffsetY = remember { Animatable(28f) }

    val descAlpha = remember { Animatable(0f) }
    val descOffsetY = remember { Animatable(24f) }

    val footerAlpha = remember { Animatable(0f) }
    val footerOffsetY = remember { Animatable(16f) }

    LaunchedEffect(Unit) {
        // 1. Badge entrance
        launch {
            badgeAlpha.animateTo(1f, tween(400))
            badgeOffsetY.animateTo(0f, spring(dampingRatio = 0.8f, stiffness = 150f))
        }

        // 2. Headline entrance
        delay(100)
        launch {
            headlineAlpha.animateTo(1f, tween(500))
            headlineOffsetY.animateTo(0f, spring(dampingRatio = 0.85f, stiffness = 140f))
        }

        // 3. Description entrance
        delay(120)
        launch {
            descAlpha.animateTo(1f, tween(500))
            descOffsetY.animateTo(0f, spring(dampingRatio = 0.85f, stiffness = 140f))
        }

        // 4. Action buttons & technical footer
        delay(120)
        launch {
            footerAlpha.animateTo(1f, tween(500))
            footerOffsetY.animateTo(0f, spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = 130f))
        }
    }

    // Gentle breathing bounce animation for scroll button
    val infiniteTransition = rememberInfiniteTransition(label = "ScrollIndicatorBounce")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    // Build the hero title with site's primary sans-serif font and underlined clickable "Jubayer Ahmed"
    val annotatedHeading = remember {
        buildAnnotatedString {
            append("Here we go... With the clicks of ")
            pushStringAnnotation(tag = "ABOUT_LINK", annotation = "about")
            withStyle(
                style = SpanStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Jubayer Ahmed")
            }
            pop()
            append(".")
        }
    }

    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_section")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 1. TOP SUB-HEADER BADGE
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(0, badgeOffsetY.value.roundToInt()) }
                        .alpha(badgeAlpha.value),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "HABIGANJ, BANGLADESH • VISUAL ARCHIVE",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 9.sp,
                            letterSpacing = 1.8.sp,
                            color = Color.Black
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(3.dp))
                            .background(Color.White)
                            .padding(horizontal = 6.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = "EST. 2024",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            fontSize = 8.sp,
                            letterSpacing = 1.2.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. HERO HEADING WITH CLICKABLE "JUBAYER AHMED"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(0, headlineOffsetY.value.roundToInt()) }
                        .alpha(headlineAlpha.value)
                ) {
                    ClickableText(
                        text = annotatedHeading,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontSize = 28.sp,
                            lineHeight = 36.sp,
                            letterSpacing = (-0.3).sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.testTag("hero_heading_text"),
                        onClick = { offset ->
                            annotatedHeading.getStringAnnotations(
                                tag = "ABOUT_LINK",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                onNavigateToAbout()
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3. DESCRIPTION (Unified single text block with site's primary sans-serif font)
                Text(
                    text = "Landscapes, documentary, rural, wildlife, nature, and visual storytelling photography based in Habiganj, Bangladesh. Exploring fleeting light, rural textures, candid people, greenery, rivers, traditions, and everyday moments—archiving.",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Normal,
                    color = GoblinTextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(0, descOffsetY.value.roundToInt()) }
                        .alpha(descAlpha.value)
                )
            }
        }
    }
}
