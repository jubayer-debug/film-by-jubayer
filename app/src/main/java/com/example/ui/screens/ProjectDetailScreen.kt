package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PortfolioRepository
import com.example.data.models.AlbumFrame
import com.example.data.models.CameraExif
import com.example.data.models.PhotoCategory
import com.example.data.models.Photograph
import com.example.data.models.Project
import com.example.data.models.VisualMood
import com.example.ui.components.PhotographicArtwork
import com.example.ui.screens.FooterSection
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.PortfolioUiState
import kotlinx.coroutines.launch

/**
 * Represents a rhythmic gallery row layout configuration for varied photo sizes
 */
private sealed class GalleryRowLayout {
    data class FullWidthHero(val frame: AlbumFrame, val aspect: Float = 1.55f) : GalleryRowLayout()
    data class CenteredFeature(val frame: AlbumFrame, val aspect: Float = 1.35f) : GalleryRowLayout()
    data class AsymmetricPair(
        val frame1: AlbumFrame,
        val aspect1: Float,
        val weight1: Float,
        val frame2: AlbumFrame,
        val aspect2: Float,
        val weight2: Float
    ) : GalleryRowLayout()
    data class TripleRow(
        val frame1: AlbumFrame,
        val frame2: AlbumFrame,
        val frame3: AlbumFrame,
        val aspect: Float = 0.95f
    ) : GalleryRowLayout()
    data class EqualPair(
        val frame1: AlbumFrame,
        val frame2: AlbumFrame,
        val aspect: Float = 1.15f
    ) : GalleryRowLayout()
}

@Composable
fun ProjectDetailScreen(
    project: Project,
    uiState: PortfolioUiState,
    onBack: () -> Unit,
    onPhotoClick: (Photograph) -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Ensure we have at least 20 frames to display
    val frames = remember(project, uiState.contentUpdateVersion) {
        val baseFrames = if (project.frameList.isNotEmpty()) {
            project.frameList
        } else {
            val photos = project.photoIds.mapNotNull { PortfolioRepository.getPhotoById(it) }
            val presets = PortfolioRepository.photoPresets
            (0 until 20).map { i ->
                val fallbackPhoto = photos.getOrNull(i % photos.size.coerceAtLeast(1))
                val preset = presets[i % presets.size]
                AlbumFrame(
                    id = "frame_${project.id}_${i + 1}",
                    number = String.format("%02d", i + 1),
                    title = fallbackPhoto?.title ?: preset.first,
                    bengaliTitle = fallbackPhoto?.bengaliTitle ?: "চিত্রপট ${i + 1}",
                    location = fallbackPhoto?.location ?: project.location,
                    year = project.year,
                    exif = "Leica M11-P • 35mm f/1.4 • 1/500s • ISO 100",
                    imageUrl = fallbackPhoto?.imageUrl?.ifBlank { preset.second } ?: preset.second,
                    caption = fallbackPhoto?.caption ?: "Documentary contact sheet frame ${i + 1} from ${project.title}."
                )
            }
        }

        // Guarantee minimum 20 frames
        if (baseFrames.size < 20) {
            val presets = PortfolioRepository.photoPresets
            val extended = baseFrames.toMutableList()
            var counter = baseFrames.size + 1
            while (extended.size < 20) {
                val preset = presets[counter % presets.size]
                extended.add(
                    AlbumFrame(
                        id = "frame_${project.id}_${counter}",
                        number = String.format("%02d", counter),
                        title = "${preset.first} (Part ${counter})",
                        bengaliTitle = "চিত্রপট ${counter}",
                        location = project.location,
                        year = project.year,
                        exif = "Leica M11-P • 35mm f/1.4 • 1/500s • ISO 100",
                        imageUrl = preset.second,
                        caption = "Documentary contact sheet frame ${counter} from ${project.title}."
                    )
                )
                counter++
            }
            extended
        } else {
            baseFrames
        }
    }

    // Build varied size gallery rows
    val galleryRows = remember(frames) {
        buildVariedGalleryRows(frames)
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("album_detail_screen")
    ) {
        // 1. Top Navigation Bar
        item(key = "nav_top_bar") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onBack() }
                        .padding(vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F3F2))
                            .border(0.5.dp, GoblinBorderSubtle, CircleShape)
                            .testTag("album_detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Albums",
                            tint = GoblinTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "ALBUMS ARCHIVE",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.5.sp,
                            letterSpacing = 2.sp,
                            color = GoblinAccentWarm
                        )
                        Text(
                            text = "← BACK TO ALBUMS",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.5.sp,
                            color = GoblinTextPrimary
                        )
                    }
                }

                // Album Frame Counter Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFF0EFEB))
                        .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(2.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${frames.size} PHOTOGRAPHS",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextPrimary
                    )
                }
            }
        }

        // 2. Album Hero Header (Editorial Title & Curatorial Statement)
        item(key = "album_hero_header") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "SERIES ${project.number}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.5.sp,
                        color = GoblinAccentWarm
                    )
                    Text(
                        text = "•",
                        color = GoblinBorderSubtle,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "${project.location.uppercase()} (${project.year})",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 9.5.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextTertiary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = project.title,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Light,
                        fontSize = 32.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextPrimary,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (project.bengaliTitle.isNotBlank()) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = project.bengaliTitle,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = GoblinAccentWarm
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = project.subtitle,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp,
                    color = GoblinTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Curatorial Essay Text
                Text(
                    text = project.essayText,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.sp,
                    lineHeight = 21.sp,
                    color = GoblinTextSecondary
                )

                if (project.quote.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF7F7F6))
                            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(2.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "“${project.quote}”",
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic,
                                fontSize = 14.5.sp,
                                lineHeight = 22.sp,
                                textAlign = TextAlign.Center,
                                color = GoblinTextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "— Curatorial Field Log",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.5.sp,
                                letterSpacing = 1.5.sp,
                                color = GoblinAccentWarm
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // 3. Gallery Header
        item(key = "gallery_exhibition_heading") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GALLERY EXHIBITION",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.5.sp,
                    color = GoblinAccentWarm
                )

                Text(
                    text = "TAP TO EXPAND",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.5.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextTertiary
                )
            }
        }

        // 4. Pure Visual Gallery with Varied Sizes for Each Photo (NO METADATA ON PHOTOS)
        itemsIndexed(galleryRows, key = { index, _ -> "gallery_row_$index" }) { rowIndex, rowLayout ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                when (rowLayout) {
                    is GalleryRowLayout.FullWidthHero -> {
                        val photo = remember(rowLayout.frame) { rowLayout.frame.toPhotograph(project) }
                        PureGalleryPhotoItem(
                            photo = photo,
                            aspectRatio = rowLayout.aspect,
                            uiState = uiState,
                            onClick = { onPhotoClick(photo) }
                        )
                    }

                    is GalleryRowLayout.CenteredFeature -> {
                        val photo = remember(rowLayout.frame) { rowLayout.frame.toPhotograph(project) }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp)
                        ) {
                            PureGalleryPhotoItem(
                                photo = photo,
                                aspectRatio = rowLayout.aspect,
                                uiState = uiState,
                                onClick = { onPhotoClick(photo) }
                            )
                        }
                    }

                    is GalleryRowLayout.AsymmetricPair -> {
                        val photo1 = remember(rowLayout.frame1) { rowLayout.frame1.toPhotograph(project) }
                        val photo2 = remember(rowLayout.frame2) { rowLayout.frame2.toPhotograph(project) }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(rowLayout.weight1)) {
                                PureGalleryPhotoItem(
                                    photo = photo1,
                                    aspectRatio = rowLayout.aspect1,
                                    uiState = uiState,
                                    onClick = { onPhotoClick(photo1) }
                                )
                            }
                            Box(modifier = Modifier.weight(rowLayout.weight2)) {
                                PureGalleryPhotoItem(
                                    photo = photo2,
                                    aspectRatio = rowLayout.aspect2,
                                    uiState = uiState,
                                    onClick = { onPhotoClick(photo2) }
                                )
                            }
                        }
                    }

                    is GalleryRowLayout.EqualPair -> {
                        val photo1 = remember(rowLayout.frame1) { rowLayout.frame1.toPhotograph(project) }
                        val photo2 = remember(rowLayout.frame2) { rowLayout.frame2.toPhotograph(project) }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                PureGalleryPhotoItem(
                                    photo = photo1,
                                    aspectRatio = rowLayout.aspect,
                                    uiState = uiState,
                                    onClick = { onPhotoClick(photo1) }
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                PureGalleryPhotoItem(
                                    photo = photo2,
                                    aspectRatio = rowLayout.aspect,
                                    uiState = uiState,
                                    onClick = { onPhotoClick(photo2) }
                                )
                            }
                        }
                    }

                    is GalleryRowLayout.TripleRow -> {
                        val photo1 = remember(rowLayout.frame1) { rowLayout.frame1.toPhotograph(project) }
                        val photo2 = remember(rowLayout.frame2) { rowLayout.frame2.toPhotograph(project) }
                        val photo3 = remember(rowLayout.frame3) { rowLayout.frame3.toPhotograph(project) }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                PureGalleryPhotoItem(
                                    photo = photo1,
                                    aspectRatio = rowLayout.aspect,
                                    uiState = uiState,
                                    onClick = { onPhotoClick(photo1) }
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                PureGalleryPhotoItem(
                                    photo = photo2,
                                    aspectRatio = rowLayout.aspect,
                                    uiState = uiState,
                                    onClick = { onPhotoClick(photo2) }
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                PureGalleryPhotoItem(
                                    photo = photo3,
                                    aspectRatio = rowLayout.aspect,
                                    uiState = uiState,
                                    onClick = { onPhotoClick(photo3) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Archival Master Prints Box
        item(key = "archival_inquiry") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 24.dp)
            ) {
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(18.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF6F6F5))
                        .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(2.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "ARCHIVAL MASTER PRINT SPECS",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = GoblinAccentWarm
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "All ${frames.size} photographs in this series are individually printed on Hahnemühle Photo Rag 308gsm archival pigment cotton paper with genuine carbon monochrome ink sets. Certified and signed with embossed provenance stamp.",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = GoblinTextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier
                                .clickable { onNavigate(NavigationSection.CONTACT) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "INQUIRE ABOUT ALBUM PRINTS →",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                color = GoblinTextPrimary
                            )
                        }
                    }
                }
            }
        }

        // 6. Footer
        item(key = "footer") {
            FooterSection(
                onBackToTop = {
                    scope.launch { listState.animateScrollToItem(0) }
                },
                onNavigate = onNavigate
            )
        }
    }
}

/**
 * Pure Photo Item with NO metadata overlays or text underneath.
 * Clean, modern gallery presentation with subtle rounded corners and fine border.
 */
@Composable
private fun PureGalleryPhotoItem(
    photo: Photograph,
    aspectRatio: Float,
    uiState: PortfolioUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clip(RoundedCornerShape(3.dp))
            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(3.dp))
            .background(Color(0xFF101010))
            .clickable { onClick() }
            .testTag("gallery_photo_${photo.id}")
    ) {
        PhotographicArtwork(
            photograph = photo,
            isMonochrome = uiState.isMonochromeMode,
            showFilmGrain = uiState.isFilmGrainEnabled,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Helper to build varied-size editorial gallery layouts from frames
 */
private fun buildVariedGalleryRows(frames: List<AlbumFrame>): List<GalleryRowLayout> {
    val rows = mutableListOf<GalleryRowLayout>()
    var i = 0
    val total = frames.size

    var patternIndex = 0
    while (i < total) {
        when (patternIndex % 7) {
            0 -> {
                // Wide Landscape Feature
                rows.add(GalleryRowLayout.FullWidthHero(frames[i], aspect = 1.55f))
                i += 1
            }
            1 -> {
                // Asymmetric Pair: Tall portrait (left) + Standard landscape (right)
                if (i + 1 < total) {
                    rows.add(
                        GalleryRowLayout.AsymmetricPair(
                            frame1 = frames[i],
                            aspect1 = 0.78f, // Tall portrait
                            weight1 = 1.0f,
                            frame2 = frames[i + 1],
                            aspect2 = 1.30f, // Landscape
                            weight2 = 1.25f
                        )
                    )
                    i += 2
                } else {
                    rows.add(GalleryRowLayout.FullWidthHero(frames[i], aspect = 1.4f))
                    i += 1
                }
            }
            2 -> {
                // Triple Compact Grid (3 in a frame)
                if (i + 2 < total) {
                    rows.add(
                        GalleryRowLayout.TripleRow(
                            frame1 = frames[i],
                            frame2 = frames[i + 1],
                            frame3 = frames[i + 2],
                            aspect = 0.95f
                        )
                    )
                    i += 3
                } else if (i + 1 < total) {
                    rows.add(GalleryRowLayout.EqualPair(frames[i], frames[i + 1], aspect = 1.15f))
                    i += 2
                } else {
                    rows.add(GalleryRowLayout.FullWidthHero(frames[i], aspect = 1.4f))
                    i += 1
                }
            }
            3 -> {
                // Asymmetric Pair: Standard landscape (left) + Tall portrait (right)
                if (i + 1 < total) {
                    rows.add(
                        GalleryRowLayout.AsymmetricPair(
                            frame1 = frames[i],
                            aspect1 = 1.28f, // Landscape
                            weight1 = 1.2f,
                            frame2 = frames[i + 1],
                            aspect2 = 0.82f, // Tall portrait
                            weight2 = 0.95f
                        )
                    )
                    i += 2
                } else {
                    rows.add(GalleryRowLayout.FullWidthHero(frames[i], aspect = 1.45f))
                    i += 1
                }
            }
            4 -> {
                // Centered Cinematic Frame
                rows.add(GalleryRowLayout.CenteredFeature(frames[i], aspect = 1.4f))
                i += 1
            }
            5 -> {
                // Equal Symmetrical Pair (e.g. square-ish / soft landscape)
                if (i + 1 < total) {
                    rows.add(
                        GalleryRowLayout.EqualPair(
                            frame1 = frames[i],
                            frame2 = frames[i + 1],
                            aspect = 1.12f
                        )
                    )
                    i += 2
                } else {
                    rows.add(GalleryRowLayout.FullWidthHero(frames[i], aspect = 1.4f))
                    i += 1
                }
            }
            6 -> {
                // Asymmetric Pair: Wide (left) + Square (right)
                if (i + 1 < total) {
                    rows.add(
                        GalleryRowLayout.AsymmetricPair(
                            frame1 = frames[i],
                            aspect1 = 1.45f,
                            weight1 = 1.3f,
                            frame2 = frames[i + 1],
                            aspect2 = 1.0f,
                            weight2 = 1.0f
                        )
                    )
                    i += 2
                } else {
                    rows.add(GalleryRowLayout.FullWidthHero(frames[i], aspect = 1.45f))
                    i += 1
                }
            }
        }
        patternIndex++
    }

    return rows
}

/**
 * Extension helper to convert an AlbumFrame into a Photograph object
 */
private fun AlbumFrame.toPhotograph(project: Project): Photograph {
    val photoCategory = when {
        project.title.contains("Padma", ignoreCase = true) || project.title.contains("Delta", ignoreCase = true) -> PhotoCategory.RIVER
        project.title.contains("Monsoon", ignoreCase = true) -> PhotoCategory.MONSOON
        project.title.contains("Dhaka", ignoreCase = true) -> PhotoCategory.STREET
        project.title.contains("Tea", ignoreCase = true) || project.title.contains("Highlands", ignoreCase = true) -> PhotoCategory.LANDSCAPE
        else -> PhotoCategory.PORTRAIT
    }
    val mood = when {
        project.title.contains("Padma", ignoreCase = true) -> VisualMood.RIVER_DAWN
        project.title.contains("Monsoon", ignoreCase = true) -> VisualMood.MONSOON_MIST
        project.title.contains("Dhaka", ignoreCase = true) -> VisualMood.OLD_DHAKA_NIGHT
        project.title.contains("Tea", ignoreCase = true) -> VisualMood.TEA_HIGHLANDS
        else -> VisualMood.VILLAGE_SHADOW
    }
    val exifParts = this.exif.split("•").map { it.trim() }
    val cameraName = exifParts.getOrNull(0) ?: "Leica M11-P"
    val lensName = exifParts.getOrNull(1) ?: "Summilux 35mm f/1.4"
    val shutterSpeed = exifParts.getOrNull(2) ?: "1/500s"
    val isoSpeed = exifParts.getOrNull(3) ?: "ISO 100"

    return Photograph(
        id = this.id,
        title = this.title,
        bengaliTitle = this.bengaliTitle,
        location = if (this.location.isNotBlank()) this.location else project.location,
        year = this.year,
        category = photoCategory,
        caption = this.caption.ifBlank { "${project.title} — Frame ${this.number}" },
        story = "From Album Series ${project.number}: \"${project.title}\" (${project.subtitle}). ${this.caption}",
        mood = mood,
        exif = CameraExif(
            camera = cameraName,
            lens = lensName,
            aperture = "f/2.0",
            shutter = shutterSpeed,
            iso = isoSpeed,
            focalLength = "35mm"
        ),
        imageUrl = this.imageUrl,
        thumbUrl = this.imageUrl
    )
}
