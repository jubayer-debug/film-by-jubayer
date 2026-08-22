package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.models.Photograph
import com.example.data.models.VisualMood
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PhotographicArtwork(
    photograph: Photograph,
    modifier: Modifier = Modifier,
    isMonochrome: Boolean = false,
    showFilmGrain: Boolean = true,
    isHeroZoom: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AtmosphereDrift")
    val driftAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "drift"
    )

    val colorFilter = if (isMonochrome) {
        ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
    } else null

    Box(modifier = modifier.background(Color(0xFF0D0D0D))) {
        // Base: Render Coil Real Photography if URL is available, or Fallback Canvas
        if (photograph.imageUrl.isNotBlank()) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photograph.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = photograph.title,
                contentScale = ContentScale.Crop,
                colorFilter = colorFilter,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    AtmosphericCanvasArt(photograph = photograph, driftAnim = driftAnim, isMonochrome = isMonochrome)
                },
                error = {
                    AtmosphericCanvasArt(photograph = photograph, driftAnim = driftAnim, isMonochrome = isMonochrome)
                }
            )
        } else {
            AtmosphericCanvasArt(photograph = photograph, driftAnim = driftAnim, isMonochrome = isMonochrome)
        }

        // Overlay: Cinematic photographic vignette and film grain
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color.Transparent, Color(0x55000000), Color(0xBB000000)),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = size.width.coerceAtLeast(size.height) * 0.75f
                )
            )

            // Film grain overlay emulation
            if (showFilmGrain) {
                drawFilmGrainTexture(this, driftAnim)
            }
        }
    }
}

@Composable
private fun AtmosphericCanvasArt(
    photograph: Photograph,
    driftAnim: Float,
    isMonochrome: Boolean
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        when (photograph.mood) {
            VisualMood.RIVER_DAWN -> drawRiverDawn(this, driftAnim, isMonochrome)
            VisualMood.MONSOON_MIST -> drawMonsoonMist(this, driftAnim, isMonochrome)
            VisualMood.OLD_DHAKA_NIGHT -> drawOldDhakaNight(this, driftAnim, isMonochrome)
            VisualMood.COASTAL_SILENCE -> drawCoastalSilence(this, driftAnim, isMonochrome)
            VisualMood.TEA_HIGHLANDS -> drawTeaHighlands(this, driftAnim, isMonochrome)
            VisualMood.PORTRAIT_LIGHT -> drawPortraitLight(this, driftAnim, isMonochrome)
            VisualMood.MEGHNA_DUSK -> drawMeghnaDusk(this, driftAnim, isMonochrome)
            VisualMood.JUTE_HARVEST -> drawJuteHarvest(this, driftAnim, isMonochrome)
            VisualMood.RIVER_STORM -> drawRiverStorm(this, driftAnim, isMonochrome)
            VisualMood.VILLAGE_SHADOW -> drawVillageShadow(this, driftAnim, isMonochrome)
        }
    }
}

private fun drawRiverDawn(scope: DrawScope, drift: Float, isMono: Boolean) {
    val w = scope.size.width
    val h = scope.size.height

    val skyColors = if (isMono) {
        listOf(Color(0xFF1E1E1E), Color(0xFF2E2E2E), Color(0xFF3E3E3E), Color(0xFF4A4A4A))
    } else {
        listOf(Color(0xFF181E24), Color(0xFF2C3E4C), Color(0xFF5B6E7D), Color(0xFFB89B7B))
    }

    // Sky & horizon gradient
    scope.drawRect(
        brush = Brush.verticalGradient(
            colors = skyColors,
            startY = 0f,
            endY = h * 0.62f
        ),
        size = Size(w, h * 0.62f)
    )

    // Glowing subtle sun horizon
    val sunY = h * 0.52f
    val sunColor = if (isMono) Color(0x66DDDDDD) else Color(0x55E8B878)
    scope.drawCircle(
        color = sunColor,
        radius = w * 0.16f,
        center = Offset(w * 0.45f, sunY)
    )

    // Distant river bank silhouette
    val bankPath = Path().apply {
        moveTo(0f, h * 0.58f)
        cubicTo(w * 0.25f, h * 0.57f, w * 0.5f, h * 0.60f, w * 0.75f, h * 0.58f)
        lineTo(w, h * 0.59f)
        lineTo(w, h * 0.63f)
        lineTo(0f, h * 0.63f)
        close()
    }
    val bankColor = if (isMono) Color(0xFF1A1A1A) else Color(0xFF1B2329)
    scope.drawPath(bankPath, color = bankColor)

    // Water surface gradient with reflection
    val waterColors = if (isMono) {
        listOf(Color(0xFF333333), Color(0xFF222222), Color(0xFF111111))
    } else {
        listOf(Color(0xFF3E4F5D), Color(0xFF22313D), Color(0xFF101920))
    }
    scope.drawRect(
        brush = Brush.verticalGradient(
            colors = waterColors,
            startY = h * 0.60f,
            endY = h
        ),
        topLeft = Offset(0f, h * 0.60f),
        size = Size(w, h * 0.40f)
    )

    // Shimmering water light reflection
    val reflectColor = if (isMono) Color(0x22CCCCCC) else Color(0x25E8C89A)
    for (i in 0..12) {
        val yPos = h * (0.64f + i * 0.026f)
        val waveW = w * (0.12f + i * 0.025f)
        val xCenter = w * (0.45f + sin(drift * 4f + i) * 0.015f)
        scope.drawLine(
            color = reflectColor,
            start = Offset(xCenter - waveW / 2, yPos),
            end = Offset(xCenter + waveW / 2, yPos),
            strokeWidth = (1.5f + i * 0.3f)
        )
    }

    // Wooden boat silhouette (Iconic Bengal Nauka)
    val boatX = w * (0.52f + drift * 0.02f)
    val boatY = h * 0.68f
    val boatScale = w * 0.22f

    val boatPath = Path().apply {
        moveTo(boatX - boatScale * 0.6f, boatY - boatScale * 0.08f)
        cubicTo(
            boatX - boatScale * 0.3f, boatY + boatScale * 0.12f,
            boatX + boatScale * 0.3f, boatY + boatScale * 0.12f,
            boatX + boatScale * 0.6f, boatY - boatScale * 0.08f
        )
        cubicTo(
            boatX + boatScale * 0.2f, boatY + boatScale * 0.04f,
            boatX - boatScale * 0.2f, boatY + boatScale * 0.04f,
            boatX - boatScale * 0.6f, boatY - boatScale * 0.08f
        )
        close()
    }
    val boatColor = if (isMono) Color(0xFF080808) else Color(0xFF0A0E12)
    scope.drawPath(boatPath, color = boatColor)

    // Boatman silhouette with oar
    scope.drawCircle(
        color = boatColor,
        radius = boatScale * 0.04f,
        center = Offset(boatX - boatScale * 0.15f, boatY - boatScale * 0.14f)
    )
    val bodyPath = Path().apply {
        moveTo(boatX - boatScale * 0.18f, boatY - boatScale * 0.10f)
        lineTo(boatX - boatScale * 0.12f, boatY - boatScale * 0.10f)
        lineTo(boatX - boatScale * 0.08f, boatY + boatScale * 0.02f)
        lineTo(boatX - boatScale * 0.22f, boatY + boatScale * 0.02f)
        close()
    }
    scope.drawPath(bodyPath, color = boatColor)

    // Oar line
    scope.drawLine(
        color = boatColor,
        start = Offset(boatX - boatScale * 0.14f, boatY - boatScale * 0.06f),
        end = Offset(boatX - boatScale * 0.40f, boatY + boatScale * 0.22f),
        strokeWidth = 2.5f
    )
}

private fun drawMonsoonMist(scope: DrawScope, drift: Float, isMono: Boolean) {
    val w = scope.size.width
    val h = scope.size.height

    val skyColors = if (isMono) {
        listOf(Color(0xFF141414), Color(0xFF262626), Color(0xFF383838), Color(0xFF282828))
    } else {
        listOf(Color(0xFF101C1A), Color(0xFF1A332C), Color(0xFF2A483E), Color(0xFF1F382F))
    }

    scope.drawRect(
        brush = Brush.verticalGradient(
            colors = skyColors,
            startY = 0f,
            endY = h
        )
    )

    // Deep lush paddy horizon
    val fieldPath = Path().apply {
        moveTo(0f, h * 0.48f)
        cubicTo(w * 0.3f, h * 0.46f, w * 0.7f, h * 0.50f, w, h * 0.47f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    val fieldColor = if (isMono) Color(0xFF181818) else Color(0xFF13271E)
    scope.drawPath(fieldPath, color = fieldColor)

    // Palm tree silhouettes in mist
    val palmColor = if (isMono) Color(0xFF0F0F0F) else Color(0xFF0B1712)
    for (i in 0..3) {
        val palmX = w * (0.15f + i * 0.24f)
        val palmBaseY = h * 0.52f
        val palmTopY = h * (0.28f + (i % 2) * 0.06f)

        // Trunk
        scope.drawLine(
            color = palmColor,
            start = Offset(palmX, palmBaseY),
            end = Offset(palmX + (i - 1.5f) * 8f, palmTopY),
            strokeWidth = 3f
        )
        // Fronds
        for (f in 0..5) {
            val angle = f * 60f + i * 15f
            val rad = Math.toRadians(angle.toDouble())
            val fx = palmX + (i - 1.5f) * 8f + (cos(rad) * w * 0.08).toFloat()
            val fy = palmTopY + (sin(rad) * w * 0.04).toFloat() + 5f
            scope.drawLine(
                color = palmColor,
                start = Offset(palmX + (i - 1.5f) * 8f, palmTopY),
                end = Offset(fx, fy),
                strokeWidth = 2f
            )
        }
    }

    // Monsoon rain streaks (Curated subtle angle)
    val rainColor = if (isMono) Color(0x18FFFFFF) else Color(0x1DF0F8FF)
    for (i in 0..40) {
        val rx = (w * (i * 0.025f + drift * 0.1f)) % w
        val ry = (h * (i * 0.05f + (i * 7 % 23) * 0.04f)) % h
        scope.drawLine(
            color = rainColor,
            start = Offset(rx, ry),
            end = Offset(rx - 8f, ry + 24f),
            strokeWidth = 1.2f
        )
    }

    // Flooded water foreground reflection
    scope.drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, if (isMono) Color(0x88111111) else Color(0x990A1A14), if (isMono) Color(0xDD080808) else Color(0xEE06120D)),
            startY = h * 0.65f,
            endY = h
        ),
        topLeft = Offset(0f, h * 0.65f),
        size = Size(w, h * 0.35f)
    )
}

private fun drawOldDhakaNight(scope: DrawScope, drift: Float, isMono: Boolean) {
    val w = scope.size.width
    val h = scope.size.height

    val bgColors = if (isMono) {
        listOf(Color(0xFF0F0F0F), Color(0xFF1C1C1C), Color(0xFF0A0A0A))
    } else {
        listOf(Color(0xFF0B0F1A), Color(0xFF141926), Color(0xFF080B12))
    }
    scope.drawRect(brush = Brush.verticalGradient(bgColors))

    // Historic alleyway buildings perspective
    val leftBuilding = Path().apply {
        moveTo(0f, 0f)
        lineTo(w * 0.32f, 0f)
        lineTo(w * 0.24f, h)
        lineTo(0f, h)
        close()
    }
    val rightBuilding = Path().apply {
        moveTo(w, 0f)
        lineTo(w * 0.68f, 0f)
        lineTo(w * 0.76f, h)
        lineTo(w, h)
        close()
    }
    val wallColor = if (isMono) Color(0xFF161616) else Color(0xFF18151D)
    scope.drawPath(leftBuilding, color = wallColor)
    scope.drawPath(rightBuilding, color = wallColor)

    // Warm tungsten / incandescent lantern light in the narrow canyon alley
    val lanternX = w * 0.48f
    val lanternY = h * 0.42f
    val lanternColor = if (isMono) Color(0x66E0E0E0) else Color(0x66FFB03B)

    scope.drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(lanternColor, lanternColor.copy(alpha = 0.2f), Color.Transparent),
            center = Offset(lanternX, lanternY),
            radius = w * 0.35f
        ),
        center = Offset(lanternX, lanternY),
        radius = w * 0.35f
    )

    // Hanging wire & lamp fixture
    scope.drawLine(
        color = Color(0xFF333333),
        start = Offset(w * 0.30f, h * 0.25f),
        end = Offset(lanternX, lanternY),
        strokeWidth = 2f
    )
    scope.drawCircle(
        color = if (isMono) Color(0xFFF2F0EA) else Color(0xFFFFD180),
        radius = 7f,
        center = Offset(lanternX, lanternY)
    )

    // Wet cobblestone reflections on the ground
    val reflectColor = if (isMono) Color(0x28CCCCCC) else Color(0x35E6A845)
    for (i in 0..15) {
        val yPos = h * (0.68f + i * 0.02f)
        val stoneW = w * (0.08f + i * 0.03f)
        val stoneX = w * (0.48f + sin(i * 1.5f + drift * 2f) * 0.05f)
        scope.drawLine(
            color = reflectColor,
            start = Offset(stoneX - stoneW / 2, yPos),
            end = Offset(stoneX + stoneW / 2, yPos),
            strokeWidth = 2f
        )
    }
}

private fun drawCoastalSilence(scope: DrawScope, drift: Float, isMono: Boolean) {
    val w = scope.size.width
    val h = scope.size.height

    val skyColors = if (isMono) {
        listOf(Color(0xFF222222), Color(0xFF363636), Color(0xFF282828))
    } else {
        listOf(Color(0xFF252F38), Color(0xFF455563), Color(0xFF303B45))
    }
    scope.drawRect(
        brush = Brush.verticalGradient(
            colors = skyColors,
            startY = 0f,
            endY = h * 0.55f
        ),
        size = Size(w, h * 0.55f)
    )

    // Vast tidal water
    val waterColors = if (isMono) {
        listOf(Color(0xFF1E1E1E), Color(0xFF141414), Color(0xFF0A0A0A))
    } else {
        listOf(Color(0xFF2A3640), Color(0xFF18232C), Color(0xFF0F151C))
    }
    scope.drawRect(
        brush = Brush.verticalGradient(
            colors = waterColors,
            startY = h * 0.55f,
            endY = h
        ),
        topLeft = Offset(0f, h * 0.55f),
        size = Size(w, h * 0.45f)
    )

    // Mangrove dead roots & submerged branches silhouettes
    val rootColor = if (isMono) Color(0xFF080808) else Color(0xFF0B1015)
    for (i in 0..4) {
        val rootX = w * (0.2f + i * 0.16f)
        val rootBaseY = h * 0.72f
        val rootTopY = h * (0.42f + (i % 3) * 0.05f)

        scope.drawLine(
            color = rootColor,
            start = Offset(rootX, rootBaseY),
            end = Offset(rootX + (i - 2) * 12f, rootTopY),
            strokeWidth = 4f
        )
        // Root reflection in still water
        scope.drawLine(
            color = rootColor.copy(alpha = 0.4f),
            start = Offset(rootX, rootBaseY),
            end = Offset(rootX + (i - 2) * 12f, rootBaseY + (rootBaseY - rootTopY) * 0.6f),
            strokeWidth = 3f
        )
    }
}

private fun drawTeaHighlands(scope: DrawScope, drift: Float, isMono: Boolean) {
    val w = scope.size.width
    val h = scope.size.height

    val skyColors = if (isMono) {
        listOf(Color(0xFF1A1A1A), Color(0xFF333333), Color(0xFF444444))
    } else {
        listOf(Color(0xFF172422), Color(0xFF2B423D), Color(0xFF4A6B63))
    }
    scope.drawRect(brush = Brush.verticalGradient(skyColors))

    // Tiered rolling green ridges
    for (r in 0..4) {
        val yOffset = h * (0.35f + r * 0.13f)
        val ridgePath = Path().apply {
            moveTo(0f, yOffset)
            cubicTo(
                w * 0.25f, yOffset - 30f + sin(r * 2f + drift) * 15f,
                w * 0.75f, yOffset + 40f - cos(r * 1.5f + drift) * 15f,
                w, yOffset
            )
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        val alphaStep = 0.4f + r * 0.15f
        val ridgeColor = if (isMono) {
            Color((0x18 + r * 0x10), (0x18 + r * 0x10), (0x18 + r * 0x10))
        } else {
            Color(0xFF122E22).copy(alpha = alphaStep)
        }
        scope.drawPath(ridgePath, color = ridgeColor)

        // Contour lines representing tea plant hedgerows
        scope.drawPath(
            ridgePath,
            color = if (isMono) Color(0x33FFFFFF) else Color(0x3388D49E),
            style = Stroke(width = 1.5f)
        )
    }
}

private fun drawPortraitLight(scope: DrawScope, drift: Float, isMono: Boolean) {
    val w = scope.size.width
    val h = scope.size.height

    // Chiaroscuro studio background
    val bgColors = if (isMono) {
        listOf(Color(0xFF1F1F1F), Color(0xFF0F0F0F), Color(0xFF050505))
    } else {
        listOf(Color(0xFF231E1B), Color(0xFF161210), Color(0xFF0A0807))
    }
    scope.drawRect(brush = Brush.radialGradient(bgColors, center = Offset(w * 0.65f, h * 0.35f), radius = w * 0.8f))

    // Silhouette of photographer with camera profile
    val headCenter = Offset(w * 0.5f, h * 0.38f)
    val headRadius = w * 0.18f

    // Soft side-light glow on profile
    val rimLight = if (isMono) Color(0x44FFFFFF) else Color(0x44D9BC9E)
    scope.drawCircle(
        color = rimLight,
        radius = headRadius * 1.15f,
        center = Offset(headCenter.x + 8f, headCenter.y)
    )

    val figureColor = if (isMono) Color(0xFF0A0A0A) else Color(0xFF0D0B0A)
    // Head & Shoulders
    scope.drawCircle(color = figureColor, radius = headRadius, center = headCenter)

    val bodyPath = Path().apply {
        moveTo(headCenter.x - headRadius * 1.6f, h)
        cubicTo(headCenter.x - headRadius * 1.2f, h * 0.52f, headCenter.x + headRadius * 1.2f, h * 0.52f, headCenter.x + headRadius * 1.6f, h)
        close()
    }
    scope.drawPath(bodyPath, color = figureColor)

    // Camera silhouette held at chest/eye
    val camX = headCenter.x - w * 0.05f
    val camY = headCenter.y + h * 0.14f
    val camW = w * 0.24f
    val camH = h * 0.10f

    scope.drawRect(
        color = if (isMono) Color(0xFF1A1A1A) else Color(0xFF1E1A18),
        topLeft = Offset(camX, camY),
        size = Size(camW, camH)
    )
    // Camera lens circle
    scope.drawCircle(
        color = if (isMono) Color(0xFF333333) else Color(0xFFC8A97E),
        radius = camH * 0.45f,
        center = Offset(camX + camW * 0.5f, camY + camH * 0.5f),
        style = Stroke(width = 3f)
    )
}

private fun drawMeghnaDusk(scope: DrawScope, drift: Float, isMono: Boolean) {
    val w = scope.size.width
    val h = scope.size.height

    val skyColors = if (isMono) {
        listOf(Color(0xFF111111), Color(0xFF2A2A2A), Color(0xFF444444), Color(0xFF303030))
    } else {
        listOf(Color(0xFF191624), Color(0xFF3E2B48), Color(0xFF75454E), Color(0xFFB86B52))
    }
    scope.drawRect(brush = Brush.verticalGradient(skyColors, startY = 0f, endY = h * 0.58f), size = Size(w, h * 0.58f))

    // Water surface in dusk twilight
    val waterColors = if (isMono) {
        listOf(Color(0xFF2E2E2E), Color(0xFF1A1A1A), Color(0xFF0D0D0D))
    } else {
        listOf(Color(0xFF5A3945), Color(0xFF2F1D2B), Color(0xFF140D17))
    }
    scope.drawRect(
        brush = Brush.verticalGradient(waterColors, startY = h * 0.58f, endY = h),
        topLeft = Offset(0f, h * 0.58f),
        size = Size(w, h * 0.42f)
    )

    // Bamboo fishing scaffolding silhouette (Char Ghor)
    val scaffoldColor = if (isMono) Color(0xFF0A0A0A) else Color(0xFF0E0B12)
    val sx = w * 0.65f
    val sy = h * 0.58f
    scope.drawLine(color = scaffoldColor, start = Offset(sx - 40f, sy + 60f), end = Offset(sx + 20f, sy - 50f), strokeWidth = 3f)
    scope.drawLine(color = scaffoldColor, start = Offset(sx + 40f, sy + 60f), end = Offset(sx - 20f, sy - 50f), strokeWidth = 3f)
    scope.drawLine(color = scaffoldColor, start = Offset(sx - 35f, sy - 15f), end = Offset(sx + 35f, sy - 15f), strokeWidth = 2.5f)
}

private fun drawJuteHarvest(scope: DrawScope, drift: Float, isMono: Boolean) {
    val w = scope.size.width
    val h = scope.size.height

    val skyColors = if (isMono) {
        listOf(Color(0xFF222222), Color(0xFF3B3B3B), Color(0xFF2E2E2E))
    } else {
        listOf(Color(0xFF233036), Color(0xFF4A5F67), Color(0xFF7D8C86))
    }
    scope.drawRect(brush = Brush.verticalGradient(skyColors, startY = 0f, endY = h * 0.5f), size = Size(w, h * 0.5f))

    // Water canal
    scope.drawRect(
        color = if (isMono) Color(0xFF141414) else Color(0xFF1E2822),
        topLeft = Offset(0f, h * 0.5f),
        size = Size(w, h * 0.5f)
    )

    // Golden bundles of jute fibers drying
    val fiberColor = if (isMono) Color(0xFFCCCCCC) else Color(0xFFD4A359)
    for (i in 0..8) {
        val fx = w * (0.15f + i * 0.09f)
        val fy = h * 0.52f
        val fPath = Path().apply {
            moveTo(fx, fy - 60f)
            cubicTo(fx - 25f, fy + 40f, fx + 25f, fy + 40f, fx, fy - 60f)
            close()
        }
        scope.drawPath(fPath, color = fiberColor.copy(alpha = 0.85f))
    }
}

private fun drawRiverStorm(scope: DrawScope, drift: Float, isMono: Boolean) {
    val w = scope.size.width
    val h = scope.size.height

    // Ominous deep bruised storm clouds (Kalbaishakhi)
    val skyColors = if (isMono) {
        listOf(Color(0xFF0A0A0A), Color(0xFF1A1A1A), Color(0xFF262626), Color(0xFF121212))
    } else {
        listOf(Color(0xFF090E14), Color(0xFF14202B), Color(0xFF203241), Color(0xFF101920))
    }
    scope.drawRect(brush = Brush.verticalGradient(skyColors))

    // Tumultuous wave surges
    val waterColor = if (isMono) Color(0xFF080808) else Color(0xFF0C1318)
    val surgePath = Path().apply {
        moveTo(0f, h * 0.62f)
        for (i in 0..10) {
            val px = w * (i / 10f)
            val py = h * (0.62f + sin(i * 1.5f + drift * 5f) * 0.04f)
            lineTo(px, py)
        }
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    scope.drawPath(surgePath, color = waterColor)
}

private fun drawVillageShadow(scope: DrawScope, drift: Float, isMono: Boolean) {
    val w = scope.size.width
    val h = scope.size.height

    // Warm courtyard afternoon light & shadow
    val bgColors = if (isMono) {
        listOf(Color(0xFF1A1A1A), Color(0xFF2E2E2E), Color(0xFF181818))
    } else {
        listOf(Color(0xFF2A241F), Color(0xFF453930), Color(0xFF201B17))
    }
    scope.drawRect(brush = Brush.verticalGradient(bgColors))

    // Mud veranda edge
    val veranda = Path().apply {
        moveTo(0f, h * 0.45f)
        lineTo(w, h * 0.35f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    scope.drawPath(veranda, color = if (isMono) Color(0xFF121212) else Color(0xFF1A1410))

    // Palm leaf shadow patterns (Gobos)
    val shadowColor = Color(0x66000000)
    for (i in 0..6) {
        val sx = w * (0.2f + i * 0.12f)
        val sy = h * (0.45f + i * 0.06f)
        scope.drawOval(
            color = shadowColor,
            topLeft = Offset(sx - 35f, sy - 15f),
            size = Size(70f, 30f)
        )
    }
}

private fun drawFilmGrainTexture(scope: DrawScope, drift: Float) {
    val w = scope.size.width
    val h = scope.size.height
    val grainColor = Color(0x0EFFFFFF)

    // Subtle distributed film noise
    val cols = 28
    val rows = 28
    val colStep = w / cols
    val rowStep = h / rows

    for (c in 0 until cols) {
        for (r in 0 until rows) {
            val pseudoRandom = sin((c * 17.3f + r * 31.7f + drift * 10f).toDouble()).toFloat()
            if (pseudoRandom > 0.35f) {
                val gx = c * colStep + (pseudoRandom * 8f)
                val gy = r * rowStep + (pseudoRandom * 8f)
                scope.drawCircle(
                    color = grainColor,
                    radius = 1.2f,
                    center = Offset(gx, gy)
                )
            }
        }
    }
}
