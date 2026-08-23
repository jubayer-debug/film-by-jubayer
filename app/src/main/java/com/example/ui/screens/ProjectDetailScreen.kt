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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.ui.components.PhotographyGridCard
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBgSecondary
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.PortfolioUiState
import kotlinx.coroutines.launch

enum class GalleryLayoutMode(val label: String, val iconDescription: String) {
    TWO_COLUMN("2-COLUMN GRID", "Grid view"),
    SINGLE_COLUMN("EXPANDED ESSAY", "Editorial list view")
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
    var layoutMode by remember { mutableStateOf(GalleryLayoutMode.TWO_COLUMN) }

    // Ensure we have at least 20 frames to display
    val frames = remember(project, uiState.contentUpdateVersion) {
        val baseFrames = if (project.frameList.isNotEmpty()) {
            project.frameList
        } else {
            // Fallback: build from photo IDs or presets
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

    val coverPhoto = remember(project, frames) {
        val existingPhoto = PortfolioRepository.getPhotoById(project.coverPhotoId)
        existingPhoto ?: frames.firstOrNull()?.toPhotograph(project, 0) ?: PortfolioRepository.photographs.first()
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("album_detail_screen")
    ) {
        // 1. Navigation Top Bar
        item(key = "nav_top_bar") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 12.dp),
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
                        text = "${frames.size} FRAMES",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextPrimary
                    )
                }
            }
        }

        // 2. Album Hero Header
        item(key = "album_hero_header") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
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

                Spacer(modifier = Modifier.height(16.dp))

                // Curatorial Essay Text
                Text(
                    text = project.essayText,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.sp,
                    lineHeight = 21.sp,
                    color = GoblinTextSecondary
                )

                if (project.quote.isNotBlank()) {
                    Spacer(modifier = Modifier.height(18.dp))
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
            }
        }

        // 3. Cover Hero Photo
        item(key = "cover_hero_frame") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clickable { onPhotoClick(coverPhoto) }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.6f)
                        .clip(RoundedCornerShape(2.dp))
                        .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(2.dp))
                ) {
                    PhotographicArtwork(
                        photograph = coverPhoto,
                        isMonochrome = uiState.isMonochromeMode,
                        showFilmGrain = uiState.isFilmGrainEnabled,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Cover Tag
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xCC000000))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "PRIMARY COVER • LEICA M11-P",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.5.sp,
                            letterSpacing = 1.5.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = coverPhoto.title.uppercase(),
                            fontFamily = FontFamily.Serif,
                            fontSize = 12.sp,
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Medium,
                            color = GoblinTextPrimary
                        )
                        Text(
                            text = "${coverPhoto.location} • ${coverPhoto.caption}",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 10.sp,
                            color = GoblinTextSecondary
                        )
                    }

                    Text(
                        text = "VIEW FULL ↗",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinAccentWarm
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        // 4. Gallery Grid Section Header & Mode Toggle
        item(key = "gallery_grid_controls") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "COMPLETE GALLERY ARCHIVE",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = GoblinAccentWarm
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "20+ Contact Sheet Frames (${frames.size} cataloged)",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 11.5.sp,
                        color = GoblinTextSecondary
                    )
                }

                // Grid View Mode Selector
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFEBEBE9))
                        .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(2.dp))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (layoutMode == GalleryLayoutMode.TWO_COLUMN) GoblinTextPrimary else Color.Transparent)
                            .clickable { layoutMode = GalleryLayoutMode.TWO_COLUMN }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "GRID",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (layoutMode == GalleryLayoutMode.TWO_COLUMN) Color.White else GoblinTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (layoutMode == GalleryLayoutMode.SINGLE_COLUMN) GoblinTextPrimary else Color.Transparent)
                            .clickable { layoutMode = GalleryLayoutMode.SINGLE_COLUMN }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ESSAY",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (layoutMode == GalleryLayoutMode.SINGLE_COLUMN) Color.White else GoblinTextSecondary
                        )
                    }
                }
            }
        }

        // 5. Gallery Grid Items (Modern Clean Minimal Layout - Minimum 20 Frames)
        if (layoutMode == GalleryLayoutMode.TWO_COLUMN) {
            // Group frames into pairs for 2-column grid layout
            val chunkedFrames = frames.chunked(2)
            itemsIndexed(chunkedFrames, key = { index, _ -> "grid_pair_$index" }) { rowIndex, pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pair.forEachIndexed { itemIdx, frame ->
                        val framePhoto = remember(frame) { frame.toPhotograph(project, rowIndex * 2 + itemIdx) }
                        Box(modifier = Modifier.weight(1f)) {
                            MinimalGridFrameCard(
                                frame = frame,
                                photo = framePhoto,
                                uiState = uiState,
                                onClick = { onPhotoClick(framePhoto) }
                            )
                        }
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {
            // Single Column Expanded Essay View
            itemsIndexed(frames, key = { idx, frame -> frame.id }) { index, frame ->
                val framePhoto = remember(frame) { frame.toPhotograph(project, index) }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                ) {
                    PhotographyGridCard(
                        photo = framePhoto,
                        isFavorite = uiState.favoritePhotoIds.contains(framePhoto.id),
                        aspectRatio = 1.45f,
                        isMonochrome = uiState.isMonochromeMode,
                        showFilmGrain = uiState.isFilmGrainEnabled,
                        onClick = { onPhotoClick(framePhoto) },
                        onToggleFavorite = {}
                    )
                }
            }
        }

        // 6. Archival Hahnemühle & Print Inquiries Box
        item(key = "archival_inquiry") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 32.dp)
            ) {
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(20.dp))

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
                            text = "All ${frames.size} frames in this series are individually printed on Hahnemühle Photo Rag 308gsm archival pigment cotton paper with genuine carbon monochrome ink sets. Certified and signed with embossed provenance stamp.",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = GoblinTextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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

        // 7. Footer
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
 * Minimal Clean Gallery Grid Frame Card
 */
@Composable
private fun MinimalGridFrameCard(
    frame: AlbumFrame,
    photo: Photograph,
    uiState: PortfolioUiState,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("frame_card_${frame.number}")
    ) {
        // Image Container with subtle border & frame number overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.25f)
                .clip(RoundedCornerShape(2.dp))
                .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(2.dp))
                .background(Color(0xFF0F0F0F))
        ) {
            PhotographicArtwork(
                photograph = photo,
                isMonochrome = uiState.isMonochromeMode,
                showFilmGrain = uiState.isFilmGrainEnabled,
                modifier = Modifier.fillMaxSize()
            )

            // Minimalist Frame Number Pill
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xB3000000))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "#${frame.number}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Frame Title & Minimal Details
        Text(
            text = frame.title,
            fontFamily = FontFamily.Serif,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = GoblinTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (frame.bengaliTitle.isNotBlank()) {
            Text(
                text = frame.bengaliTitle,
                fontFamily = FontFamily.Serif,
                fontSize = 9.sp,
                color = GoblinAccentWarm,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = frame.location.ifBlank { "Bangladesh Delta" },
            fontFamily = FontFamily.SansSerif,
            fontSize = 8.5.sp,
            color = GoblinTextTertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

/**
 * Extension helper to convert an AlbumFrame into a Photograph object
 */
private fun AlbumFrame.toPhotograph(project: Project, index: Int): Photograph {
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
