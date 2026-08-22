package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextTertiary
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Editorial 1-second Opening Loading Screen for Khonchitro
 * 
 * Features:
 * - Plain white background
 * - Centered minimal Khonchitro monogram & logo typography
 * - Smooth animated progress line calibrated over 1.0 second (1000ms)
 * - Dynamic ISO variable counter updating from 1 to 100
 * - Cinematic exit fade transition to reveal the full visual archive
 */
@Composable
fun LoadingScreen(
    isLoading: Boolean,
    onLoadingComplete: () -> Unit
) {
    val progress = remember { Animatable(0f) }
    var isoValue by remember { mutableIntStateOf(1) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            progress.snapTo(0f)
            isoValue = 1
            
            coroutineScope {
                // Animate progress smoothly from 0f to 1f over exactly 1000ms (1 sec)
                launch {
                    progress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                    )
                }

                // Interpolate the ISO variable from 1 to 100 across 1000ms
                launch {
                    val startTime = System.currentTimeMillis()
                    while (System.currentTimeMillis() - startTime < 1000) {
                        val elapsed = System.currentTimeMillis() - startTime
                        val currentFraction = (elapsed / 1000f).coerceIn(0f, 1f)
                        isoValue = (1 + (currentFraction * 99f)).toInt().coerceIn(1, 100)
                        delay(16) // ~60fps step
                    }
                    isoValue = 100
                }
            }

            // Brief hold and then smooth exit to full app
            delay(120)
            onLoadingComplete()
        }
    }

    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(tween(200)),
        exit = fadeOut(tween(400)) + scaleOut(targetScale = 1.03f, animationSpec = tween(400))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Monogram & Brand Logo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(GoblinTextPrimary)
                            .border(1.dp, GoblinTextPrimary, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "K",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "KHONCHITRO",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            letterSpacing = 2.0.sp,
                            color = GoblinTextPrimary
                        )
                        Text(
                            text = "ক্ষণচিত্র • PHOTOGRAPHY ARCHIVE",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 8.5.sp,
                            letterSpacing = 1.6.sp,
                            color = GoblinTextTertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // Animated Progress Line (220dp width, 2dp height)
                Box(
                    modifier = Modifier
                        .width(220.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(GoblinBorderSubtle)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(fraction = 1f)
                            .fillMaxWidth(fraction = progress.value)
                            .background(GoblinTextPrimary)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Dynamic ISO Indicator
                Row(
                    modifier = Modifier.width(220.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CALIBRATING FILM SENSOR",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        letterSpacing = 1.4.sp,
                        color = GoblinTextTertiary
                    )

                    Text(
                        text = "ISO $isoValue",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        letterSpacing = 1.2.sp,
                        color = GoblinTextPrimary
                    )
                }
            }
        }
    }
}
