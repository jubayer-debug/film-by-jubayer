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
fun HomeScreen(
    uiState: PortfolioUiState,
    onPhotoClick: (Photograph) -> Unit,
    onProjectClick: (Project) -> Unit,
    onCategorySelect: (PhotoCategory) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val filteredPhotos = PortfolioRepository.photographs.let { list ->
        if (uiState.selectedCategory == PhotoCategory.ALL) list
        else list.filter { it.category == uiState.selectedCategory }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .testTag("home_lazy_column")
    ) {
        // 1. HERO SECTION (Nearly full screen viewport)
        item(key = "hero_section") {
            HeroSection(
                heroPhoto = PortfolioRepository.photographs.first(),
                isMonochrome = uiState.isMonochromeMode,
                showFilmGrain = uiState.isFilmGrainEnabled,
                onScrollDown = {
                    scope.launch {
                        listState.animateScrollToItem(1)
                    }
                },
                onPhotoClick = { onPhotoClick(PortfolioRepository.photographs.first()) }
            )
        }

        // 2. EDITORIAL INTRO STATEMENT SECTION
        item(key = "intro_statement") {
            IntroStatementSection()
        }

        // 3. CATEGORY FILTER BAR
        item(key = "category_filters") {
            CategoryFilterBar(
                selected = uiState.selectedCategory,
                onSelect = onCategorySelect
            )
        }

        // 4. ASYMMETRIC CURATED GALLERY
        item(key = "asymmetric_gallery") {
            AsymmetricGallerySection(
                photos = filteredPhotos,
                favoriteIds = uiState.favoritePhotoIds,
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
private fun HeroSection(
    heroPhoto: Photograph,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onScrollDown: () -> Unit,
    onPhotoClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(580.dp)
            .clickable { onPhotoClick() }
            .testTag("hero_section")
    ) {
        // Hero Background Artwork
        PhotographicArtwork(
            photograph = heroPhoto,
            isMonochrome = isMonochrome,
            showFilmGrain = showFilmGrain,
            modifier = Modifier.fillMaxSize()
        )

        // Dark gradient overlay to frame typography
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x990A0A0A),
                            Color(0x220A0A0A),
                            Color(0x880A0A0A),
                            GoblinBg
                        )
                    )
                )
        )

        // Editorial Hero Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Central / Upper Statement
            Column {
                Text(
                    text = "PHOTOGRAPHER & DOCUMENTARIAN",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    letterSpacing = 3.sp,
                    color = GoblinAccentWarm
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "A visual archive\nof places, people\nand passing light.",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Light,
                    fontSize = 34.sp,
                    lineHeight = 42.sp,
                    letterSpacing = (-0.5).sp,
                    color = GoblinTextPrimary
                )
            }

            // Bottom Hero Metadata + Scroll Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "DHAKA, BANGLADESH",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        letterSpacing = 1.8.sp,
                        color = GoblinTextSecondary
                    )
                    Text(
                        text = "VOL. 2026 • 23° 48' N",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 9.sp,
                        letterSpacing = 1.2.sp,
                        color = GoblinTextTertiary
                    )
                }

                // Scroll Down Trigger
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(20.dp))
                        .clickable { onScrollDown() }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXPLORE",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        letterSpacing = 2.sp,
                        color = GoblinTextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Scroll down",
                        tint = GoblinAccentWarm,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun IntroStatementSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 40.dp)
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
private fun CategoryFilterBar(
    selected: PhotoCategory,
    onSelect: (PhotoCategory) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SELECTED WORK",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                letterSpacing = 2.sp,
                color = GoblinTextPrimary
            )
            Text(
                text = "2022 — 2026",
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                color = GoblinTextTertiary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PhotoCategory.values().forEach { category ->
                val isSelected = selected == category
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = if (isSelected) 1.dp else 0.5.dp,
                            color = if (isSelected) GoblinAccentWarm else GoblinBorderSubtle,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(if (isSelected) Color(0x22C8A97E) else Color(0x11FFFFFF))
                        .clickable { onSelect(category) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                        .testTag("category_filter_${category.name}")
                ) {
                    Text(
                        text = category.label,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        letterSpacing = 1.5.sp,
                        color = if (isSelected) GoblinAccentWarm else GoblinTextSecondary
                    )
                }
            }
        }
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
            .padding(horizontal = 20.dp),
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
                .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(2.dp))
                .clip(RoundedCornerShape(2.dp))
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
                    .background(Color(0x770A0A0A))
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isFavorite) GoblinAccentWarm else GoblinTextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }

            // View tag bottom-left
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x880A0A0A))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = GoblinAccentWarm,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "VIEW",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 8.5.sp,
                    letterSpacing = 1.2.sp,
                    color = GoblinTextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Caption & Location label
        Text(
            text = photo.title.uppercase(),
            fontFamily = FontFamily.Serif,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp,
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
            .padding(vertical = 50.dp, horizontal = 24.dp)
            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
            .background(Color(0x33141414))
            .padding(vertical = 36.dp, horizontal = 20.dp),
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
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Light,
                fontSize = 19.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                color = GoblinTextPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "— GOBLIN, 2026",
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
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PROJECTS",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    letterSpacing = 2.sp,
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
                        .background(GoblinBgSecondary)
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
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                letterSpacing = 1.sp,
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
            .background(Color(0xFF080808))
            .padding(horizontal = 24.dp, vertical = 36.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "GOBLIN",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 2.5.sp,
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

        // Social / Channels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "WORK",
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                color = GoblinTextSecondary,
                modifier = Modifier.clickable { onNavigate(NavigationSection.WORK) }
            )
            Text(
                text = "PROJECTS",
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                color = GoblinTextSecondary,
                modifier = Modifier.clickable { onNavigate(NavigationSection.PROJECTS) }
            )
            Text(
                text = "ABOUT",
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                color = GoblinTextSecondary,
                modifier = Modifier.clickable { onNavigate(NavigationSection.ABOUT) }
            )
            Text(
                text = "CONTACT",
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                color = GoblinTextSecondary,
                modifier = Modifier.clickable { onNavigate(NavigationSection.CONTACT) }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "© 2026 GOBLIN. ALL PHOTOGRAPHS ARCHIVED UNDER INTERNATIONAL COPYRIGHT.",
            fontFamily = FontFamily.SansSerif,
            fontSize = 8.5.sp,
            letterSpacing = 1.2.sp,
            color = GoblinTextTertiary
        )
    }
}
