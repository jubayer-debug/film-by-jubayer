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
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.BookmarkBorder
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.viewmodel.PortfolioUiState
import kotlinx.coroutines.launch

import com.example.ui.viewmodel.PhotoSortOrder

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

        // 2. EDITORIAL INTRO STATEMENT SECTION
        item(key = "intro_statement") {
            IntroStatementSection()
        }

        // 3. RESPONSIVE CURATED PHOTOGRAPHY GRID WITH THEMATIC FILTERS & SORTING
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

        // 4. PHILOSOPHY QUOTE BREAK
        item(key = "philosophy_quote") {
            PhilosophyQuoteSection()
        }

        // 6. FEATURED PROJECTS PREVIEW SECTION
        item(key = "featured_projects") {
            FeaturedProjectsPreview(
                projects = PortfolioRepository.projects.take(4),
                isMonochrome = uiState.isMonochromeMode,
                showFilmGrain = uiState.isFilmGrainEnabled,
                onProjectClick = onProjectClick,
                onViewAllProjects = { onNavigate(NavigationSection.PROJECTS) }
            )
        }

        // 7. FOOTER SECTION
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
private fun AsymmetricGallerySection(
    photos: List<Photograph>,
    favoriteIds: Set<String>,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onPhotoClick: (Photograph) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        var index = 0
        while (index < photos.size) {
            val remaining = photos.size - index

            when {
                // Layout 1: Asymmetric Staggered Pair (65% Large + 35% Vertical)
                remaining >= 2 && index % 3 == 0 -> {
                    val p1 = photos[index]
                    val p2 = photos[index + 1]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        EditorialPhotoCard(
                            photo = p1,
                            isFavorite = favoriteIds.contains(p1.id),
                            isMonochrome = isMonochrome,
                            showFilmGrain = showFilmGrain,
                            aspect = 1.25f,
                            modifier = Modifier.weight(0.62f),
                            onClick = { onPhotoClick(p1) },
                            onToggleFavorite = { onToggleFavorite(p1.id) }
                        )
                        EditorialPhotoCard(
                            photo = p2,
                            isFavorite = favoriteIds.contains(p2.id),
                            isMonochrome = isMonochrome,
                            showFilmGrain = showFilmGrain,
                            aspect = 0.82f,
                            modifier = Modifier.weight(0.38f),
                            onClick = { onPhotoClick(p2) },
                            onToggleFavorite = { onToggleFavorite(p2.id) }
                        )
                    }
                    index += 2
                }

                // Layout 2: Reverse Asymmetric Staggered Pair (35% Vertical + 65% Large)
                remaining >= 2 && index % 3 == 1 -> {
                    val p1 = photos[index]
                    val p2 = photos[index + 1]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        EditorialPhotoCard(
                            photo = p1,
                            isFavorite = favoriteIds.contains(p1.id),
                            isMonochrome = isMonochrome,
                            showFilmGrain = showFilmGrain,
                            aspect = 0.82f,
                            modifier = Modifier.weight(0.38f),
                            onClick = { onPhotoClick(p1) },
                            onToggleFavorite = { onToggleFavorite(p1.id) }
                        )
                        EditorialPhotoCard(
                            photo = p2,
                            isFavorite = favoriteIds.contains(p2.id),
                            isMonochrome = isMonochrome,
                            showFilmGrain = showFilmGrain,
                            aspect = 1.25f,
                            modifier = Modifier.weight(0.62f),
                            onClick = { onPhotoClick(p2) },
                            onToggleFavorite = { onToggleFavorite(p2.id) }
                        )
                    }
                    index += 2
                }

                // Layout 3: Full-width Cinematic Image
                else -> {
                    val p = photos[index]
                    EditorialPhotoCard(
                        photo = p,
                        isFavorite = favoriteIds.contains(p.id),
                        isMonochrome = isMonochrome,
                        showFilmGrain = showFilmGrain,
                        aspect = 1.65f,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPhotoClick(p) },
                        onToggleFavorite = { onToggleFavorite(p.id) }
                    )
                    index += 1
                }
            }
        }
    }
}

@Composable
fun EditorialPhotoCard(
    photo: Photograph,
    isFavorite: Boolean,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    aspect: Float,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .clickable { onClick() }
            .testTag("photo_card_${photo.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspect)
                .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(3.dp))
                .clip(RoundedCornerShape(3.dp))
        ) {
            PhotographicArtwork(
                photograph = photo,
                isMonochrome = isMonochrome,
                showFilmGrain = showFilmGrain,
                modifier = Modifier.fillMaxSize()
            )

            // Bookmark button top-right
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0x99000000))
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isFavorite) Color(0xFFE2A860) else Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            // View tag bottom-left
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x99000000))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = Color(0xFFE2A860),
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "VIEW",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 8.5.sp,
                    letterSpacing = 1.2.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Caption & Location label
        Text(
            text = photo.title.uppercase(),
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp,
            color = GoblinTextPrimary
        )
        Text(
            text = "${photo.location} • ${photo.year}",
            fontFamily = FontFamily.SansSerif,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            color = GoblinTextSecondary
        )
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
private fun FeaturedProjectsPreview(
    projects: List<Project>,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onProjectClick: (Project) -> Unit,
    onViewAllProjects: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PROJECTS",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextPrimary
                )
                Text(
                    text = "CURATED PHOTOGRAPHIC ESSAYS",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextTertiary
                )
            }

            Text(
                text = "VIEW ALL →",
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                color = GoblinAccentWarm,
                modifier = Modifier
                    .clickable { onViewAllProjects() }
                    .padding(8.dp)
                    .testTag("view_all_projects_btn")
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            projects.forEach { project ->
                val cover = PortfolioRepository.getPhotoById(project.coverPhotoId) ?: PortfolioRepository.photographs.first()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                        .background(Color(0xFFFAFAFA))
                        .clickable { onProjectClick(project) }
                        .padding(12.dp)
                        .testTag("project_card_${project.id}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(2.dp))
                    ) {
                        PhotographicArtwork(
                            photograph = cover,
                            isMonochrome = isMonochrome,
                            showFilmGrain = showFilmGrain,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${project.number} — ${project.title}",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                letterSpacing = 0.5.sp,
                                color = GoblinTextPrimary
                            )
                        }
                        Text(
                            text = project.subtitle,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.sp,
                            color = GoblinTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${project.location} • ${project.photoCount} PHOTOGRAPHS",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 9.sp,
                            letterSpacing = 1.2.sp,
                            color = GoblinAccentWarm
                        )
                    }
                }
            }
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
                NavigationSection.PROJECTS to "PROJECTS",
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
