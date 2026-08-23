package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.BookmarkBorder
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PortfolioRepository
import com.example.data.models.PhotoCategory
import com.example.data.models.Photograph
import com.example.data.models.Project
import com.example.ui.components.HeroSection
import com.example.ui.components.PhotographicArtwork
import com.example.ui.components.ResponsivePhotographyGridSection
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBgSecondary
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.PhotoSortOrder
import com.example.ui.viewmodel.PortfolioUiState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    uiState: PortfolioUiState,
    onPhotoClick: (Photograph) -> Unit,
    onProjectClick: (Project) -> Unit,
    onCategorySelect: (PhotoCategory) -> Unit,
    onSortOrderChange: (PhotoSortOrder) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onResetFilters: () -> Unit = {},
    onToggleFavorite: (String) -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    // Filter and sort photos based on UI state
    val filteredPhotos = remember(uiState.selectedCategory, uiState.sortOrder, uiState.searchQuery, uiState.randomSeed, uiState.contentUpdateVersion) {
        var list = PortfolioRepository.photographs
        if (uiState.selectedCategory != PhotoCategory.ALL) {
            list = list.filter { it.category == uiState.selectedCategory }
        }
        if (uiState.searchQuery.isNotBlank()) {
            val q = uiState.searchQuery.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.bengaliTitle.contains(q) ||
                it.location.lowercase().contains(q) ||
                it.caption.lowercase().contains(q) ||
                it.category.label.lowercase().contains(q) ||
                it.exif.camera.lowercase().contains(q) ||
                it.exif.lens.lowercase().contains(q)
            }
        }
        when (uiState.sortOrder) {
            PhotoSortOrder.CURATED -> list
            PhotoSortOrder.RANDOM_SHUFFLE -> list.shuffled(kotlin.random.Random(uiState.randomSeed))
            PhotoSortOrder.YEAR_DESC -> list.sortedByDescending { it.year }
            PhotoSortOrder.YEAR_ASC -> list.sortedBy { it.year }
            PhotoSortOrder.TITLE_AZ -> list.sortedBy { it.title }
            PhotoSortOrder.LOCATION -> list.sortedBy { it.location }
        }
    }

    val activeHeroPhoto = remember(filteredPhotos, uiState.randomSeed) {
        filteredPhotos.firstOrNull() ?: PortfolioRepository.photographs.first()
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .testTag("home_lazy_column")
    ) {
        // 1. HERO SECTION (Editorial typographic header with link to About)
        item(key = "hero_section") {
            HeroSection(
                onScrollDown = {
                    scope.launch {
                        listState.animateScrollToItem(1)
                    }
                },
                onNavigateToAbout = {
                    onNavigate(NavigationSection.ABOUT)
                }
            )
        }

        // 2. FEATURED ALBUMS SLIDER (Placed directly below hero section, showing 3 small cards in a frame)
        item(key = "featured_albums_slider") {
            FeaturedAlbumsSliderSection(
                projects = PortfolioRepository.projects,
                isMonochrome = uiState.isMonochromeMode,
                showFilmGrain = uiState.isFilmGrainEnabled,
                onProjectClick = onProjectClick,
                onViewAllAlbums = { onNavigate(NavigationSection.ALBUMS) }
            )
        }

        // 3. EDITORIAL INTRO STATEMENT SECTION
        item(key = "intro_statement") {
            IntroStatementSection()
        }

        // 4. RESPONSIVE CURATED PHOTOGRAPHY GRID WITH THEMATIC FILTERS & SORTING
        item(key = "responsive_gallery_grid") {
            ResponsivePhotographyGridSection(
                photos = filteredPhotos,
                favoritePhotoIds = uiState.favoritePhotoIds,
                selectedCategory = uiState.selectedCategory,
                sortOrder = uiState.sortOrder,
                searchQuery = uiState.searchQuery,
                onCategorySelect = onCategorySelect,
                onSortOrderChange = onSortOrderChange,
                onSearchQueryChange = onSearchQueryChange,
                onResetFilters = onResetFilters,
                isMonochrome = uiState.isMonochromeMode,
                showFilmGrain = uiState.isFilmGrainEnabled,
                onPhotoClick = onPhotoClick,
                onToggleFavorite = onToggleFavorite
            )
        }

        // 5. PHILOSOPHY QUOTE BREAK
        item(key = "philosophy_quote") {
            PhilosophyQuoteSection()
        }

        // 6. FOOTER SECTION
        item(key = "footer_section") {
            FooterSection(
                onBackToTop = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                onNavigate = onNavigate
            )
        }
    }
}

@Composable
private fun IntroStatementSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 40.dp)
    ) {
        Text(
            text = "01 — PROLOGUE",
            fontFamily = FontFamily.SansSerif,
            fontSize = 10.sp,
            letterSpacing = 2.sp,
            color = GoblinAccentWarm
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "\"I photograph places\nthat disappear slowly.\"",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Light,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            fontStyle = FontStyle.Italic,
            color = GoblinTextPrimary
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "A photographic archive exploring landscape, memory, movement and ordinary moments across Bangladesh. Working between the riverbanks of the Meghna and the narrow nocturnal corridors of Old Dhaka, these images observe quiet resilience and the transformative power of natural light.",
            fontFamily = FontFamily.SansSerif,
            fontSize = 13.5.sp,
            lineHeight = 22.sp,
            color = GoblinTextSecondary
        )

        Spacer(modifier = Modifier.height(28.dp))

        HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
    }
}

@Composable
private fun PhilosophyQuoteSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 4.dp)
            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
            .background(Color(0xFFFAFAFA))
            .padding(vertical = 36.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "PHILOSOPHY",
                fontFamily = FontFamily.SansSerif,
                fontSize = 9.5.sp,
                letterSpacing = 3.sp,
                color = GoblinAccentWarm
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "\"Some places are remembered\nnot because they were extraordinary,\nbut because the light stayed.\"",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Normal,
                color = GoblinTextPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "— FILM BY JUBAYER, 2026",
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                color = GoblinTextTertiary
            )
        }
    }
}

@Composable
private fun FeaturedAlbumsSliderSection(
    projects: List<Project>,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onProjectClick: (Project) -> Unit,
    onViewAllAlbums: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sliderState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val currentIdx by remember { derivedStateOf { sliderState.firstVisibleItemIndex } }
    val canScrollBack by remember { derivedStateOf { sliderState.canScrollBackward } }
    val canScrollForward by remember { derivedStateOf { sliderState.canScrollForward } }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .testTag("featured_albums_slider_section")
    ) {
        // Section Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewCarousel,
                        contentDescription = null,
                        tint = GoblinAccentWarm,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "FEATURED ALBUMS",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = GoblinTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "0${currentIdx + 1} / 0${projects.size} • 3 in a frame",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.5.sp,
                    color = GoblinTextTertiary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Prev Arrow Button
                IconButton(
                    onClick = {
                        scope.launch {
                            val target = (currentIdx - 1).coerceAtLeast(0)
                            sliderState.animateScrollToItem(target)
                        }
                    },
                    enabled = canScrollBack,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(if (canScrollBack) Color.Black else Color(0x10000000))
                        .testTag("slider_prev_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous Album",
                        tint = if (canScrollBack) Color.White else Color(0x40000000),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Next Arrow Button
                IconButton(
                    onClick = {
                        scope.launch {
                            val target = (currentIdx + 1).coerceAtMost(projects.size - 1)
                            sliderState.animateScrollToItem(target)
                        }
                    },
                    enabled = canScrollForward,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(if (canScrollForward) Color.Black else Color(0x10000000))
                        .testTag("slider_next_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next Album",
                        tint = if (canScrollForward) Color.White else Color(0x40000000),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "ALL →",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = GoblinAccentWarm,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onViewAllAlbums() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("slider_view_all_button")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Small Photo Cards showing 3 cards in a frame
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val totalWidth = maxWidth
            val horizontalPadding = 6.dp
            val spacing = 8.dp
            // Calculate cardWidth so exactly 3 cards fit in a frame with paddings and spacings
            val computedWidth = (totalWidth - (horizontalPadding * 2) - (spacing * 2)) / 3
            val cardWidth = computedWidth.coerceAtLeast(100.dp)

            LazyRow(
                state = sliderState,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(projects, key = { _, proj -> "slider_${proj.id}" }) { _, project ->
                    SmallAlbumSliderCard(
                        project = project,
                        cardWidth = cardWidth,
                        isMonochrome = isMonochrome,
                        showFilmGrain = showFilmGrain,
                        onClick = { onProjectClick(project) }
                    )
                }
            }
        }
    }
}

/**
 * Compact, small photographic card for the 3-in-a-frame Featured Albums slider
 */
@Composable
private fun SmallAlbumSliderCard(
    project: Project,
    cardWidth: androidx.compose.ui.unit.Dp,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onClick: () -> Unit
) {
    val cover = remember(project) {
        PortfolioRepository.getPhotoById(project.coverPhotoId) ?: PortfolioRepository.photographs.first()
    }

    Box(
        modifier = Modifier
            .width(cardWidth)
            .aspectRatio(0.82f)
            .clip(RoundedCornerShape(8.dp))
            .border(0.5.dp, Color(0x33000000), RoundedCornerShape(8.dp))
            .background(Color(0xFF141414))
            .clickable { onClick() }
            .testTag("small_album_card_${project.id}")
    ) {
        // Full Bleed Photograph
        PhotographicArtwork(
            photograph = cover,
            isMonochrome = isMonochrome,
            showFilmGrain = showFilmGrain,
            modifier = Modifier.fillMaxSize()
        )

        // Subtle gradient overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x60000000),
                            Color.Transparent,
                            Color(0x80000000),
                            Color(0xF0000000)
                        )
                    )
                )
        )

        // Top Badges (Pill for frames count & view count)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Frame Count Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xDD18181B))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${project.photoCount}F",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Views Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xDD18181B))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = GoblinAccentWarm,
                    modifier = Modifier.size(8.dp)
                )
                Text(
                    text = "${project.viewCount}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Bottom Text Overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(horizontal = 6.dp, vertical = 6.dp)
        ) {
            Text(
                text = project.title,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                letterSpacing = 0.2.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${project.location} • ${project.year}",
                fontFamily = FontFamily.SansSerif,
                fontSize = 7.5.sp,
                color = Color(0xCCFFFFFF),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun FooterSection(
    onBackToTop: () -> Unit,
    onNavigate: (NavigationSection) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F6))
            .padding(horizontal = 6.dp, vertical = 36.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "FILM BY JUBAYER",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 2.0.sp,
                    color = GoblinTextPrimary
                )
                Text(
                    text = "DHAKA, BANGLADESH",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextTertiary
                )
            }

            // Back to top button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .clickable { onBackToTop() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("back_to_top_button"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = GoblinAccentWarm,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "BACK TO TOP",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation Links with responsive scroll & 44dp touch targets
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val footerNavs = listOf(
                NavigationSection.WORK to "PHOTOS",
                NavigationSection.ALBUMS to "ALBUMS",
                NavigationSection.JOURNAL to "JOURNAL",
                NavigationSection.ABOUT to "ABOUT",
                NavigationSection.CONTACT to "CONTACT",
                NavigationSection.ADMIN to "ADMIN CMS"
            )

            footerNavs.forEach { (section, label) ->
                Box(
                    modifier = Modifier
                        .heightIn(min = 44.dp)
                        .clickable { onNavigate(section) }
                        .padding(vertical = 12.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.5.sp,
                        letterSpacing = 1.6.sp,
                        fontWeight = if (section == NavigationSection.ADMIN) FontWeight.Bold else FontWeight.Normal,
                        color = if (section == NavigationSection.ADMIN) GoblinAccentWarm else GoblinTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "© 2026 KHONCHITRO (ক্ষণচিত্র). ALL PHOTOGRAPHS ARCHIVED UNDER INTERNATIONAL COPYRIGHT.",
            fontFamily = FontFamily.SansSerif,
            fontSize = 8.5.sp,
            letterSpacing = 1.2.sp,
            color = GoblinTextTertiary
        )
    }
}
