package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PortfolioRepository
import com.example.data.models.PhotoCategory
import com.example.data.models.PhotoOrientation
import com.example.data.models.Photograph
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.PhotoSortOrder

/**
 * Display modes for the responsive photographic gallery
 */
enum class GridDisplayMode(val label: String, val icon: ImageVector) {
    MASONRY("MASONRY", Icons.Default.GridView),
    BALANCED("2-COL", Icons.Default.ViewAgenda),
    CINEMATIC("CINEMATIC", Icons.Default.ViewDay)
}

/**
 * Standalone Scrollable Responsive Photography Grid with Theme Filtering, Sorting, and Lazy Loading
 */
@Composable
fun ResponsivePhotographyGrid(
    photos: List<Photograph>,
    favoritePhotoIds: Set<String>,
    onPhotoClick: (Photograph) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedCategory: PhotoCategory = PhotoCategory.ALL,
    sortOrder: PhotoSortOrder = PhotoSortOrder.CURATED,
    searchQuery: String = "",
    onCategorySelect: (PhotoCategory) -> Unit = {},
    onSortOrderChange: (PhotoSortOrder) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onResetFilters: () -> Unit = {},
    isMonochrome: Boolean = false,
    showFilmGrain: Boolean = true,
    initialMode: GridDisplayMode = GridDisplayMode.MASONRY,
    showFilteringSystem: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    headerContent: @Composable (() -> Unit)? = null
) {
    var displayMode by remember { mutableStateOf(initialMode) }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val screenWidth = maxWidth

        // Adaptive column count calculated based on viewport dimensions
        val (masonryColumns, balancedColumns) = when {
            screenWidth > 960.dp -> 4 to 3
            screenWidth > 680.dp -> 3 to 2
            else -> 2 to 2
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            // Optional Header / Prologue
            headerContent?.invoke()

            // Filtering, Sorting, and Search Control Header
            if (showFilteringSystem) {
                PhotographyFilteringSystem(
                    selectedCategory = selectedCategory,
                    sortOrder = sortOrder,
                    searchQuery = searchQuery,
                    photoCount = photos.size,
                    currentMode = displayMode,
                    onCategorySelect = onCategorySelect,
                    onSortOrderChange = onSortOrderChange,
                    onSearchQueryChange = onSearchQueryChange,
                    onResetFilters = onResetFilters,
                    onModeChange = { displayMode = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            if (photos.isEmpty()) {
                EmptyGalleryState(
                    hasActiveFilters = selectedCategory != PhotoCategory.ALL || searchQuery.isNotBlank(),
                    onReset = onResetFilters,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp)
                )
            } else {
                when (displayMode) {
                    GridDisplayMode.MASONRY -> {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(masonryColumns),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("responsive_masonry_grid"),
                            contentPadding = contentPadding,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalItemSpacing = 16.dp
                        ) {
                            items(
                                items = photos,
                                key = { it.id }
                            ) { photo ->
                                val cardAspect = when (photo.orientation) {
                                    PhotoOrientation.PORTRAIT -> 0.78f
                                    PhotoOrientation.LANDSCAPE -> 1.45f
                                    PhotoOrientation.SQUARE -> 1.0f
                                    PhotoOrientation.PANORAMIC -> 1.9f
                                }

                                PhotographyGridCard(
                                    photo = photo,
                                    isFavorite = favoritePhotoIds.contains(photo.id),
                                    aspectRatio = cardAspect,
                                    isMonochrome = isMonochrome,
                                    showFilmGrain = showFilmGrain,
                                    showExifPill = true,
                                    onClick = { onPhotoClick(photo) },
                                    onToggleFavorite = { onToggleFavorite(photo.id) }
                                )
                            }
                        }
                    }

                    GridDisplayMode.BALANCED -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(balancedColumns),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("responsive_balanced_grid"),
                            contentPadding = contentPadding,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                items = photos,
                                key = { it.id }
                            ) { photo ->
                                PhotographyGridCard(
                                    photo = photo,
                                    isFavorite = favoritePhotoIds.contains(photo.id),
                                    aspectRatio = 1.25f,
                                    isMonochrome = isMonochrome,
                                    showFilmGrain = showFilmGrain,
                                    showExifPill = false,
                                    onClick = { onPhotoClick(photo) },
                                    onToggleFavorite = { onToggleFavorite(photo.id) }
                                )
                            }
                        }
                    }

                    GridDisplayMode.CINEMATIC -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("responsive_cinematic_grid"),
                            contentPadding = contentPadding,
                            verticalArrangement = Arrangement.spacedBy(28.dp)
                        ) {
                            items(
                                items = photos,
                                key = { it.id }
                            ) { photo ->
                                CinematicFullBleedCard(
                                    photo = photo,
                                    isFavorite = favoritePhotoIds.contains(photo.id),
                                    isMonochrome = isMonochrome,
                                    showFilmGrain = showFilmGrain,
                                    onClick = { onPhotoClick(photo) },
                                    onToggleFavorite = { onToggleFavorite(photo.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Embeddable Responsive Section for parent LazyColumn structures (e.g. HomeScreen)
 */
@Composable
fun ResponsivePhotographyGridSection(
    photos: List<Photograph>,
    favoritePhotoIds: Set<String>,
    onPhotoClick: (Photograph) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedCategory: PhotoCategory = PhotoCategory.ALL,
    sortOrder: PhotoSortOrder = PhotoSortOrder.CURATED,
    searchQuery: String = "",
    onCategorySelect: (PhotoCategory) -> Unit = {},
    onSortOrderChange: (PhotoSortOrder) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onResetFilters: () -> Unit = {},
    showFilteringSystem: Boolean = true,
    isMonochrome: Boolean = false,
    showFilmGrain: Boolean = true,
    initialMode: GridDisplayMode = GridDisplayMode.MASONRY
) {
    var displayMode by remember { mutableStateOf(initialMode) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("responsive_grid_section")
    ) {
        // Complete Thematic Filter Bar + Sorting & Search Controls
        if (showFilteringSystem) {
            PhotographyFilteringSystem(
                selectedCategory = selectedCategory,
                sortOrder = sortOrder,
                searchQuery = searchQuery,
                photoCount = photos.size,
                currentMode = displayMode,
                onCategorySelect = onCategorySelect,
                onSortOrderChange = onSortOrderChange,
                onSearchQueryChange = onSearchQueryChange,
                onResetFilters = onResetFilters,
                onModeChange = { displayMode = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))
        }

        if (photos.isEmpty()) {
            EmptyGalleryState(
                hasActiveFilters = selectedCategory != PhotoCategory.ALL || searchQuery.isNotBlank(),
                onReset = onResetFilters,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 36.dp)
            )
        } else {
            AnimatedContent(
                targetState = displayMode,
                transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(200)) },
                label = "grid_mode_anim"
            ) { mode ->
                when (mode) {
                    GridDisplayMode.MASONRY, GridDisplayMode.BALANCED -> {
                        // 2-Column Balanced / Staggered Layout for parent scroll
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            val chunked = photos.chunked(2)
                            chunked.forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    val first = pair[0]
                                    val aspectFirst = if (mode == GridDisplayMode.MASONRY) {
                                        when (first.orientation) {
                                            PhotoOrientation.PORTRAIT -> 0.82f
                                            PhotoOrientation.LANDSCAPE -> 1.35f
                                            PhotoOrientation.SQUARE -> 1.0f
                                            PhotoOrientation.PANORAMIC -> 1.8f
                                        }
                                    } else 1.25f

                                    PhotographyGridCard(
                                        photo = first,
                                        isFavorite = favoritePhotoIds.contains(first.id),
                                        aspectRatio = aspectFirst,
                                        isMonochrome = isMonochrome,
                                        showFilmGrain = showFilmGrain,
                                        showExifPill = true,
                                        modifier = Modifier.weight(1f),
                                        onClick = { onPhotoClick(first) },
                                        onToggleFavorite = { onToggleFavorite(first.id) }
                                    )

                                    if (pair.size > 1) {
                                        val second = pair[1]
                                        val aspectSecond = if (mode == GridDisplayMode.MASONRY) {
                                            when (second.orientation) {
                                                PhotoOrientation.PORTRAIT -> 0.82f
                                                PhotoOrientation.LANDSCAPE -> 1.35f
                                                PhotoOrientation.SQUARE -> 1.0f
                                                PhotoOrientation.PANORAMIC -> 1.8f
                                            }
                                        } else 1.25f

                                        PhotographyGridCard(
                                            photo = second,
                                            isFavorite = favoritePhotoIds.contains(second.id),
                                            aspectRatio = aspectSecond,
                                            isMonochrome = isMonochrome,
                                            showFilmGrain = showFilmGrain,
                                            showExifPill = true,
                                            modifier = Modifier.weight(1f),
                                            onClick = { onPhotoClick(second) },
                                            onToggleFavorite = { onToggleFavorite(second.id) }
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    GridDisplayMode.CINEMATIC -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            photos.forEach { photo ->
                                CinematicFullBleedCard(
                                    photo = photo,
                                    isFavorite = favoritePhotoIds.contains(photo.id),
                                    isMonochrome = isMonochrome,
                                    showFilmGrain = showFilmGrain,
                                    onClick = { onPhotoClick(photo) },
                                    onToggleFavorite = { onToggleFavorite(photo.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Top Filtering & Sorting System:
 * - Thematic Filter Chips with Icons and Photo Counts
 * - Sort Order Dropdown Pill
 * - Expandable Quick Search Bar
 * - Layout Mode Selector
 * - Active Filter Indicator
 */
@Composable
fun PhotographyFilteringSystem(
    selectedCategory: PhotoCategory,
    sortOrder: PhotoSortOrder,
    searchQuery: String,
    photoCount: Int,
    currentMode: GridDisplayMode,
    onCategorySelect: (PhotoCategory) -> Unit,
    onSortOrderChange: (PhotoSortOrder) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onResetFilters: () -> Unit,
    onModeChange: (GridDisplayMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearchExpanded by remember { mutableStateOf(searchQuery.isNotBlank()) }
    var isSortMenuOpen by remember { mutableStateOf(false) }

    val allPhotos = PortfolioRepository.photographs
    val totalCount = allPhotos.size

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 1. THEMATIC FILTER CHIPS (Scrollable Row with category icons and counters)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PhotoCategory.values().forEach { category ->
                val isSelected = selectedCategory == category
                val count = if (category == PhotoCategory.ALL) totalCount else allPhotos.count { it.category == category }
                val icon = getCategoryIcon(category)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = if (isSelected) 1.dp else 0.5.dp,
                            color = if (isSelected) GoblinAccentWarm else GoblinBorderSubtle,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .background(if (isSelected) Color(0xFF181818) else Color(0xFFF7F7F6))
                        .clickable { onCategorySelect(category) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("theme_filter_${category.name}"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) Color(0xFFE2A860) else GoblinTextSecondary,
                            modifier = Modifier.size(13.dp)
                        )

                        Text(
                            text = category.label,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 9.5.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            letterSpacing = 1.2.sp,
                            color = if (isSelected) Color.White else GoblinTextPrimary
                        )

                        // Count Badge
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isSelected) Color(0x33E2A860) else Color(0x15000000))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = count.toString(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color(0xFFE2A860) else GoblinTextTertiary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 2. SORTING, SEARCH, AND LAYOUT CONTROL TOOLBAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Sort Selection & Search Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sort Dropdown Pill
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(16.dp))
                            .background(if (sortOrder != PhotoSortOrder.CURATED) Color(0xFFEDE9E3) else Color(0xFFF7F7F6))
                            .clickable { isSortMenuOpen = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("sort_order_selector"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort order",
                            tint = if (sortOrder != PhotoSortOrder.CURATED) GoblinAccentWarm else GoblinTextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = sortOrder.shortLabel.uppercase(),
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp,
                            color = if (sortOrder != PhotoSortOrder.CURATED) GoblinAccentWarm else GoblinTextPrimary
                        )
                    }

                    DropdownMenu(
                        expanded = isSortMenuOpen,
                        onDismissRequest = { isSortMenuOpen = false },
                        modifier = Modifier
                            .background(Color.White)
                            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(8.dp))
                    ) {
                        PhotoSortOrder.values().forEach { order ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = order.label,
                                            fontFamily = FontFamily.SansSerif,
                                            fontSize = 11.5.sp,
                                            fontWeight = if (sortOrder == order) FontWeight.Bold else FontWeight.Normal,
                                            color = if (sortOrder == order) GoblinAccentWarm else GoblinTextPrimary
                                        )
                                        if (sortOrder == order) {
                                            Text(
                                                text = "✓",
                                                color = GoblinAccentWarm,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    onSortOrderChange(order)
                                    isSortMenuOpen = false
                                },
                                modifier = Modifier.testTag("sort_option_${order.name}")
                            )
                        }
                    }
                }

                // Random Shuffle Serendipity Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            0.5.dp,
                            if (sortOrder == PhotoSortOrder.RANDOM_SHUFFLE) GoblinAccentWarm else GoblinBorderSubtle,
                            RoundedCornerShape(16.dp)
                        )
                        .background(if (sortOrder == PhotoSortOrder.RANDOM_SHUFFLE) Color(0xFFEDE9E3) else Color(0xFFF7F7F6))
                        .clickable { onSortOrderChange(PhotoSortOrder.RANDOM_SHUFFLE) }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("random_shuffle_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Pick random order",
                            tint = if (sortOrder == PhotoSortOrder.RANDOM_SHUFFLE) GoblinAccentWarm else GoblinTextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "RANDOM",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 8.sp,
                            fontWeight = if (sortOrder == PhotoSortOrder.RANDOM_SHUFFLE) FontWeight.Bold else FontWeight.Normal,
                            letterSpacing = 0.8.sp,
                            color = if (sortOrder == PhotoSortOrder.RANDOM_SHUFFLE) GoblinAccentWarm else GoblinTextSecondary
                        )
                    }
                }

                // Search Toggle Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(16.dp))
                        .background(if (isSearchExpanded || searchQuery.isNotBlank()) Color(0xFFEDE9E3) else Color(0xFFF7F7F6))
                        .clickable { isSearchExpanded = !isSearchExpanded }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("toggle_search_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search archive",
                            tint = if (searchQuery.isNotBlank()) GoblinAccentWarm else GoblinTextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        if (searchQuery.isNotBlank()) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "\"$searchQuery\"",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 8.5.sp,
                                color = GoblinAccentWarm,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Right: Display Mode Switcher
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(18.dp))
                    .background(Color(0xFFF4F4F3))
                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                GridDisplayMode.values().forEach { mode ->
                    val isSelected = currentMode == mode
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Color(0xFF141414) else Color.Transparent)
                            .clickable { onModeChange(mode) }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                            .testTag("grid_mode_btn_${mode.name}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = mode.icon,
                                contentDescription = mode.label,
                                tint = if (isSelected) Color.White else GoblinTextSecondary,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = mode.label,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 8.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                letterSpacing = 0.8.sp,
                                color = if (isSelected) Color.White else GoblinTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // 3. EXPANDABLE SEARCH BAR
        AnimatedVisibility(
            visible = isSearchExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(0.5.dp, GoblinAccentWarm.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = GoblinAccentWarm,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search locations, cameras, lenses, or essays...",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 12.sp,
                                color = GoblinTextTertiary
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            textStyle = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 12.sp,
                                color = GoblinTextPrimary
                            ),
                            cursorBrush = SolidColor(GoblinAccentWarm),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_input_field")
                        )
                    }
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = GoblinTextTertiary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. ACTIVE FILTER & SORT STATUS BREADCRUMB
        val hasActiveFilter = selectedCategory != PhotoCategory.ALL || sortOrder != PhotoSortOrder.CURATED || searchQuery.isNotBlank()
        if (hasActiveFilter) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .animateContentSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "SHOWING $photoCount OF $totalCount",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        color = GoblinAccentWarm
                    )
                    Text(
                        text = "•",
                        fontSize = 9.sp,
                        color = GoblinTextTertiary
                    )
                    Text(
                        text = if (selectedCategory != PhotoCategory.ALL) selectedCategory.label else "ALL",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 9.sp,
                        color = GoblinTextSecondary
                    )
                    if (sortOrder != PhotoSortOrder.CURATED) {
                        Text(
                            text = "• ${sortOrder.shortLabel}",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 9.sp,
                            color = GoblinTextTertiary
                        )
                    }
                }

                // Reset Filters Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onResetFilters() }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .testTag("reset_all_filters_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Reset all",
                        tint = GoblinAccentWarm,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "RESET",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = GoblinAccentWarm
                    )
                }
            }
        }
    }
}

private fun getCategoryIcon(category: PhotoCategory): ImageVector {
    return when (category) {
        PhotoCategory.ALL -> Icons.Default.AutoAwesome
        PhotoCategory.LANDSCAPE -> Icons.Default.Landscape
        PhotoCategory.RIVER -> Icons.Default.Water
        PhotoCategory.PORTRAIT -> Icons.Default.Person
        PhotoCategory.MONSOON -> Icons.Default.Cloud
        PhotoCategory.STREET -> Icons.Default.LocationCity
    }
}

/**
 * Rich Photography Card for Masonry & Balanced Grids
 */
@Composable
fun PhotographyGridCard(
    photo: Photograph,
    isFavorite: Boolean,
    aspectRatio: Float,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
    showExifPill: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "press_scale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .scale(scaleAnim)
            .clip(RoundedCornerShape(4.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .testTag("photography_card_${photo.id}")
    ) {
        // Photographic Artwork Viewport
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .clip(RoundedCornerShape(4.dp))
                .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
        ) {
            PhotographicArtwork(
                photograph = photo,
                isMonochrome = isMonochrome,
                showFilmGrain = showFilmGrain,
                modifier = Modifier.fillMaxSize()
            )

            // Multi-stop subtle gradient protection for overlays
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color(0x66000000),
                                0.35f to Color.Transparent,
                                0.65f to Color.Transparent,
                                1.0f to Color(0x88000000)
                            )
                        )
                    )
            )

            // Top Row: EXIF Technical Pill + Bookmark Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showExifPill) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x99000000))
                            .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CenterFocusStrong,
                                contentDescription = null,
                                tint = Color(0xFFE2A860),
                                modifier = Modifier.size(9.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${photo.exif.focalLength} • ${photo.exif.aperture}",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.sp,
                                letterSpacing = 1.sp,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.size(1.dp))
                }

                // Bookmark Icon Button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xAA000000))
                        .border(0.5.dp, Color(0x33FFFFFF), CircleShape)
                        .testTag("card_bookmark_${photo.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark photograph",
                        tint = if (isFavorite) Color(0xFFE2A860) else Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            // Bottom-left View badge
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(3.dp))
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
                    fontSize = 8.sp,
                    letterSpacing = 1.2.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(7.dp))

        // Editorial Metadata Labels
        Text(
            text = photo.title.uppercase(),
            fontFamily = FontFamily.Serif,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = GoblinTextPrimary
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${photo.location} • ${photo.year}",
                fontFamily = FontFamily.SansSerif,
                fontSize = 9.5.sp,
                letterSpacing = 0.8.sp,
                color = GoblinTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = photo.category.label,
                fontFamily = FontFamily.Monospace,
                fontSize = 8.sp,
                letterSpacing = 1.sp,
                color = GoblinAccentWarm
            )
        }
    }
}

/**
 * Large Immersive Full-Bleed Cinematic Card
 */
@Composable
fun CinematicFullBleedCard(
    photo: Photograph,
    isFavorite: Boolean,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
            .background(Color(0xFFFAFAFA))
            .clickable { onClick() }
            .testTag("cinematic_card_${photo.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.65f)
        ) {
            PhotographicArtwork(
                photograph = photo,
                isMonochrome = isMonochrome,
                showFilmGrain = showFilmGrain,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xAA000000))
                    .border(0.5.dp, Color(0x44FFFFFF), CircleShape)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isFavorite) Color(0xFFE2A860) else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${photo.location.uppercase()} • ${photo.year}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    letterSpacing = 1.8.sp,
                    color = GoblinAccentWarm
                )

                Text(
                    text = "${photo.exif.camera} • ${photo.exif.lens}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.5.sp,
                    letterSpacing = 1.sp,
                    color = GoblinTextTertiary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = photo.title,
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
                color = GoblinTextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = photo.caption,
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.5.sp,
                lineHeight = 18.sp,
                color = GoblinTextSecondary
            )
        }
    }
}

@Composable
private fun EmptyGalleryState(
    hasActiveFilters: Boolean,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = null,
            tint = GoblinTextTertiary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "NO PHOTOGRAPHS FOUND",
            fontFamily = FontFamily.Serif,
            fontSize = 15.sp,
            letterSpacing = 1.5.sp,
            color = GoblinTextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (hasActiveFilters) "No photographs match your active theme or search filter." else "The archive is currently empty.",
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            color = GoblinTextTertiary
        )
        if (hasActiveFilters) {
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(0.5.dp, GoblinAccentWarm, RoundedCornerShape(16.dp))
                    .background(Color(0x15967246))
                    .clickable { onReset() }
                    .padding(horizontal = 14.dp, vertical = 7.dp)
                    .testTag("empty_state_reset_btn")
            ) {
                Text(
                    text = "RESET ALL FILTERS",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    color = GoblinAccentWarm
                )
            }
        }
    }
}
