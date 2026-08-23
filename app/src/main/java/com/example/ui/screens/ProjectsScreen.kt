package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PortfolioRepository
import com.example.data.models.Project
import com.example.ui.components.PhotographicArtwork
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

@Composable
fun ProjectsScreen(
    uiState: PortfolioUiState,
    onProjectClick: (Project) -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val projects = remember(uiState.contentUpdateVersion) { PortfolioRepository.projects }
    val carouselListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val currentVisibleIdx by remember { derivedStateOf { carouselListState.firstVisibleItemIndex } }
    val canScrollBack by remember { derivedStateOf { carouselListState.canScrollBackward } }
    val canScrollForward by remember { derivedStateOf { carouselListState.canScrollForward } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("projects_screen_lazy_column")
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "প্রকল্প সিরিজ • LONG-TERM BODIES OF WORK",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.5.sp,
                    letterSpacing = 2.5.sp,
                    color = GoblinAccentWarm
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Albums",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Light,
                    fontSize = 28.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Multi-year documentary explorations into specific geographic topologies, seasonal transitions, and deltaic living across Bangladesh.",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.5.sp,
                    lineHeight = 19.sp,
                    color = GoblinTextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        // Section 1: Interactive Horizontal Carousel / Image Slider
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                // Carousel Header with Arrow Buttons and Series Counter
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ViewCarousel,
                            contentDescription = null,
                            tint = GoblinAccentWarm,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "IMAGE SLIDER",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = GoblinTextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x15000000))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "0${currentVisibleIdx + 1} / 0${projects.size}",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoblinTextSecondary
                            )
                        }
                    }

                    // Left (<) and Right (>) Navigation Arrows
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    val targetIdx = (currentVisibleIdx - 1).coerceAtLeast(0)
                                    carouselListState.animateScrollToItem(targetIdx)
                                }
                            },
                            enabled = canScrollBack,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (canScrollBack) Color.Black else Color(0x10000000))
                                .border(0.5.dp, if (canScrollBack) Color.Black else Color(0x20000000), CircleShape)
                                .testTag("projects_carousel_prev_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Previous series",
                                tint = if (canScrollBack) Color.White else Color(0x40000000),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    val targetIdx = (currentVisibleIdx + 1).coerceAtMost(projects.size - 1)
                                    carouselListState.animateScrollToItem(targetIdx)
                                }
                            },
                            enabled = canScrollForward,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (canScrollForward) Color.Black else Color(0x10000000))
                                .border(0.5.dp, if (canScrollForward) Color.Black else Color(0x20000000), CircleShape)
                                .testTag("projects_carousel_next_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Next series",
                                tint = if (canScrollForward) Color.White else Color(0x40000000),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Horizontal Carousel LazyRow with exact card design
                LazyRow(
                    state = carouselListState,
                    contentPadding = PaddingValues(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(projects, key = { _, proj -> proj.id }) { _, project ->
                        CuratedProjectPhotoCard(
                            project = project,
                            isMonochrome = uiState.isMonochromeMode,
                            showFilmGrain = uiState.isFilmGrainEnabled,
                            onClick = { onProjectClick(project) },
                            modifier = Modifier.width(320.dp)
                        )
                    }
                }

                // Carousel Dots Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    projects.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .height(4.dp)
                                .width(if (index == currentVisibleIdx) 24.dp else 6.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (index == currentVisibleIdx) Color.Black else Color(0x30000000))
                                .clickable {
                                    coroutineScope.launch {
                                        carouselListState.animateScrollToItem(index)
                                    }
                                }
                        )
                    }
                }
            }
        }

        // Section 2: Detailed Curated Folio List
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "ALL ARCHIVED MONOGRAPHS",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.5.sp,
                    letterSpacing = 2.sp,
                    color = GoblinTextTertiary
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        itemsIndexed(projects, key = { _, p -> "list_${p.id}" }) { _, project ->
            CuratedProjectPhotoCard(
                project = project,
                isMonochrome = uiState.isMonochromeMode,
                showFilmGrain = uiState.isFilmGrainEnabled,
                onClick = { onProjectClick(project) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 8.dp)
            )
        }

        item {
            FooterSection(onBackToTop = {}, onNavigate = onNavigate)
        }
    }
}

/**
 * Curated Series & Photographic Essays Photo Card matching the reference design:
 * - Rounded container with full-bleed photograph
 * - Top-left badge: "[COUNT] FRAMES" with Layers icon
 * - Top-right badge: "[VIEWS]" with Eye icon
 * - Bottom dark gradient overlay
 * - Large bold uppercase title (e.g. "BOISHAKH 1433")
 * - Thin horizontal divider
 * - Bottom metadata row with Location pin and Calendar date
 */
@Composable
fun CuratedProjectPhotoCard(
    project: Project,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cover = PortfolioRepository.getPhotoById(project.coverPhotoId) ?: PortfolioRepository.photographs.first()

    Box(
        modifier = modifier
            .aspectRatio(1.45f)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0x33000000), RoundedCornerShape(16.dp))
            .background(Color(0xFF141414))
            .clickable { onClick() }
            .testTag("curated_project_card_${project.id}")
    ) {
        // Full Bleed Photograph
        PhotographicArtwork(
            photograph = cover,
            isMonochrome = isMonochrome,
            showFilmGrain = showFilmGrain,
            modifier = Modifier.fillMaxSize()
        )

        // Subtle overall top-to-bottom vignette
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x55000000),
                            Color.Transparent,
                            Color(0xAA000000),
                            Color(0xFA000000)
                        )
                    )
                )
        )

        // Top Badges Row (Frames Count & Views Count)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Top Left Pill Badge: "[N] FRAMES" with Layers Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xE018181B))
                    .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "${project.photoCount} FRAMES",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = Color.White
                )
            }

            // Top Right Pill Badge: "[VIEWS]" with Eye Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xE018181B))
                    .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = GoblinAccentWarm,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "${project.viewCount}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = Color.White
                )
            }
        }

        // Bottom Details Overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Bold Capitalized Series Title
            Text(
                text = project.title,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                letterSpacing = 0.8.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Thin Horizontal Divider
            HorizontalDivider(
                color = Color(0x33FFFFFF),
                thickness = 0.8.dp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Bottom Meta Row: Location Pin & Calendar Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFA3A3A3),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = project.location,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp,
                        color = Color(0xFFD4D4D4)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFFA3A3A3),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = project.dateFormatted,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp,
                        color = Color(0xFFD4D4D4)
                    )
                }
            }
        }
    }
}
