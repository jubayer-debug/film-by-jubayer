package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PhotoOrientation
import com.example.data.models.Photograph
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary

@Composable
fun ImageLightbox(
    photo: Photograph,
    currentIndex: Int,
    totalCount: Int,
    isFavorite: Boolean,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    isExifOpen: Boolean,
    onClose: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleExif: () -> Unit,
    onEditInAdmin: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Main Image Area with Zoom & Drag gestures
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(photo.id) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 3.5f)
                        if (scale > 1f) {
                            offsetX += pan.x
                            offsetY += pan.y
                        } else {
                            offsetX = 0f
                            offsetY = 0f
                        }
                    }
                }
                .pointerInput(photo.id) {
                    var totalDragX = 0f
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            totalDragX += dragAmount
                        },
                        onDragEnd = {
                            if (scale <= 1.05f) {
                                if (totalDragX < -80f) {
                                    onNext()
                                } else if (totalDragX > 80f) {
                                    onPrevious()
                                }
                            }
                            totalDragX = 0f
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            val aspect = when (photo.orientation) {
                PhotoOrientation.PORTRAIT -> 3f / 4f
                PhotoOrientation.PANORAMIC -> 16f / 9f
                PhotoOrientation.SQUARE -> 1f
                PhotoOrientation.LANDSCAPE -> 4f / 3f
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(if (photo.orientation == PhotoOrientation.PORTRAIT) 0.85f else 0.96f)
                    .aspectRatio(aspect)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
                    .clip(RoundedCornerShape(2.dp))
                    .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(2.dp))
                    .testTag("lightbox_artwork_canvas")
            ) {
                PhotographicArtwork(
                    photograph = photo,
                    isMonochrome = isMonochrome,
                    showFilmGrain = showFilmGrain,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Left Navigation Arrow Button
        IconButton(
            onClick = {
                scale = 1f
                offsetX = 0f
                offsetY = 0f
                onPrevious()
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xDDFFFFFF))
                .border(0.5.dp, GoblinBorderSubtle, CircleShape)
                .testTag("lightbox_prev_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous Image",
                tint = GoblinTextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        // Right Navigation Arrow Button
        IconButton(
            onClick = {
                scale = 1f
                offsetX = 0f
                offsetY = 0f
                onNext()
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xDDFFFFFF))
                .border(0.5.dp, GoblinBorderSubtle, CircleShape)
                .testTag("lightbox_next_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next Image",
                tint = GoblinTextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        // TOP CONTROLS BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GoblinBg.copy(alpha = 0.96f), Color.Transparent)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close Lightbox
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xEEFFFFFF))
                    .border(0.5.dp, GoblinBorderSubtle, CircleShape)
                    .testTag("close_lightbox_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Lightbox",
                    tint = GoblinTextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Image Index Counter (e.g. "03 / 18")
            val indexFormatted = String.format("%02d", currentIndex + 1)
            val totalFormatted = String.format("%02d", totalCount)
            Text(
                text = "$indexFormatted / $totalFormatted",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                color = GoblinTextSecondary
            )

            // Right Action: Edit + Bookmark + EXIF
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onEditInAdmin != null) {
                    IconButton(
                        onClick = onEditInAdmin,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xEEFFFFFF))
                            .border(0.5.dp, GoblinBorderSubtle, CircleShape)
                            .testTag("lightbox_admin_edit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit photo content",
                            tint = GoblinAccentWarm,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                IconButton(
                    onClick = { onToggleFavorite(photo.id) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xEEFFFFFF))
                        .border(0.5.dp, GoblinBorderSubtle, CircleShape)
                        .testTag("lightbox_bookmark_button")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save to curation",
                        tint = if (isFavorite) GoblinAccentWarm else GoblinTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onToggleExif,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isExifOpen) GoblinAccentWarm else Color(0xEEFFFFFF))
                        .border(0.5.dp, GoblinBorderSubtle, CircleShape)
                        .testTag("lightbox_exif_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isExifOpen) Icons.Filled.Info else Icons.Outlined.Info,
                        contentDescription = "Technical EXIF",
                        tint = if (isExifOpen) Color.White else GoblinTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // BOTTOM METADATA BAR
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, GoblinBg.copy(alpha = 0.95f), GoblinBg)
                    )
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = photo.title.uppercase(),
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            letterSpacing = 1.5.sp,
                            color = GoblinTextPrimary
                        )
                        if (photo.bengaliTitle.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(${photo.bengaliTitle})",
                                fontFamily = FontFamily.Serif,
                                fontSize = 13.sp,
                                color = GoblinTextTertiary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${photo.location} • ${photo.year} • ${photo.category.label}",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 11.sp,
                        letterSpacing = 1.2.sp,
                        color = GoblinAccentWarm
                    )
                }

                if (scale > 1f) {
                    Text(
                        text = "ZOOM ${"%.1f".format(scale)}X",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        color = GoblinTextTertiary,
                        modifier = Modifier
                            .clickable {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                            }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = photo.caption,
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.5.sp,
                lineHeight = 18.sp,
                color = GoblinTextSecondary
            )
        }

        // SLIDEOUT EXIF / STORY PANEL
        AnimatedVisibility(
            visible = isExifOpen,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .border(1.dp, GoblinBorderSubtle, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFFFFFFF))
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TECHNICAL METADATA & FIELD NOTES",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 2.sp,
                        color = GoblinAccentWarm
                    )
                    IconButton(onClick = onToggleExif, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close EXIF",
                            tint = GoblinTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // EXIF Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        ExifItem(label = "CAMERA", value = photo.exif.camera)
                        ExifItem(label = "APERTURE", value = photo.exif.aperture)
                        ExifItem(label = "ISO", value = photo.exif.iso)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        ExifItem(label = "LENS", value = photo.exif.lens)
                        ExifItem(label = "SHUTTER", value = photo.exif.shutter)
                        ExifItem(label = "FORMAT", value = photo.exif.format)
                    }
                }

                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 12.dp))

                Text(
                    text = "FIELD ESSAY",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextTertiary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = photo.story,
                    fontFamily = FontFamily.Serif,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    fontStyle = FontStyle.Italic,
                    color = GoblinTextPrimary
                )
            }
        }
    }
}

@Composable
private fun ExifItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontFamily = FontFamily.SansSerif,
            fontSize = 9.sp,
            letterSpacing = 1.2.sp,
            color = GoblinTextTertiary
        )
        Text(
            text = value,
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = GoblinTextPrimary
        )
    }
}
