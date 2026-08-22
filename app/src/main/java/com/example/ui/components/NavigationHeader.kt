package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.VolumeMute
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.PortfolioUiState

/**
 * Minimal, fixed Navigation component following 'Khonchitro' branding and monochromatic light gallery theme.
 * Adapts between a horizontal desktop layout and a clean compact mobile layout with full-screen overlay toggle.
 */
@Composable
fun NavigationHeader(
    uiState: PortfolioUiState,
    onNavigate: (NavigationSection) -> Unit,
    onToggleMobileMenu: () -> Unit,
    onToggleMonochrome: () -> Unit,
    onToggleFilmGrain: () -> Unit,
    onToggleAmbient: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAtmosphereDropdown by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(GoblinBg.copy(alpha = 0.98f))
            .border(
                width = 0.5.dp,
                color = GoblinBorderSubtle,
                shape = RoundedCornerShape(0.dp)
            )
            .statusBarsPadding()
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 14.dp)
        ) {
            val isDesktop = maxWidth >= 680.dp

            if (isDesktop) {
                // ==========================================
                // DESKTOP HORIZONTAL NAVIGATION LAYOUT
                // ==========================================
                DesktopNavigationRow(
                    uiState = uiState,
                    onNavigate = onNavigate,
                    onToggleMonochrome = onToggleMonochrome,
                    onToggleFilmGrain = onToggleFilmGrain,
                    onToggleAmbient = onToggleAmbient
                )
            } else {
                // ==========================================
                // MOBILE COMPACT NAVIGATION BAR
                // ==========================================
                MobileNavigationRow(
                    uiState = uiState,
                    showAtmosphereDropdown = showAtmosphereDropdown,
                    onToggleAtmosphereMenu = { showAtmosphereDropdown = !showAtmosphereDropdown },
                    onNavigate = onNavigate,
                    onToggleMobileMenu = onToggleMobileMenu
                )
            }
        }

        // Atmosphere Dropdown for Mobile / Compact view
        AnimatedVisibility(
            visible = showAtmosphereDropdown,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(150)),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 64.dp, end = 20.dp)
        ) {
            AtmosphereDropdownCard(
                uiState = uiState,
                onToggleMonochrome = onToggleMonochrome,
                onToggleFilmGrain = onToggleFilmGrain,
                onToggleAmbient = onToggleAmbient,
                onDismiss = { showAtmosphereDropdown = false }
            )
        }
    }
}

/**
 * Desktop Horizontal Navigation Bar
 */
@Composable
private fun DesktopNavigationRow(
    uiState: PortfolioUiState,
    onNavigate: (NavigationSection) -> Unit,
    onToggleMonochrome: () -> Unit,
    onToggleFilmGrain: () -> Unit,
    onToggleAmbient: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Khonchitro Branding
        KhonchitroBrandLogo(
            onClick = { onNavigate(NavigationSection.WORK) }
        )

        // Right Container: Horizontal Nav Links (Right-Aligned) + Social Line Icons + Darkroom Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Horizontal Navigation Links
            Row(
                horizontalArrangement = Arrangement.spacedBy(22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val sections = listOf(
                    NavigationSection.WORK,
                    NavigationSection.PROJECTS,
                    NavigationSection.JOURNAL,
                    NavigationSection.ABOUT,
                    NavigationSection.CONTACT,
                    NavigationSection.CURATION,
                    NavigationSection.ADMIN
                )

                sections.forEach { section ->
                    val isActive = uiState.activeSection == section
                    val targetColor by animateColorAsState(
                        targetValue = if (isActive) GoblinTextPrimary else GoblinTextTertiary,
                        animationSpec = tween(200),
                        label = "nav_color_${section.name}"
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .heightIn(min = 44.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onNavigate(section) }
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                            .testTag("nav_link_${section.routeKey}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = section.label,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 11.5.sp,
                                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                                letterSpacing = 2.0.sp,
                                color = targetColor
                            )
                            if (section == NavigationSection.CURATION && uiState.favoritePhotoIds.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "[${uiState.favoritePhotoIds.size}]",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isActive) GoblinAccentWarm else GoblinTextTertiary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(if (isActive) 16.dp else 0.dp)
                                .height(1.5.dp)
                                .background(GoblinTextPrimary)
                        )
                    }
                }
            }

            // Divider between Menu & Social Links
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .background(GoblinBorderSubtle)
            )

            // Social Line Icons (Facebook, Instagram, Pexels, Mail)
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialLineIconButton(
                    title = "Facebook",
                    onClick = {
                        try { uriHandler.openUri("https://facebook.com") } catch (_: Exception) {}
                    }
                ) {
                    FacebookLineIcon(color = GoblinTextTertiary)
                }

                SocialLineIconButton(
                    title = "Instagram",
                    onClick = {
                        try { uriHandler.openUri("https://instagram.com") } catch (_: Exception) {}
                    }
                ) {
                    InstagramLineIcon(color = GoblinTextTertiary)
                }

                SocialLineIconButton(
                    title = "Pexels",
                    onClick = {
                        try { uriHandler.openUri("https://www.pexels.com") } catch (_: Exception) {}
                    }
                ) {
                    PexelsLineIcon(color = GoblinTextTertiary)
                }

                SocialLineIconButton(
                    title = "Mail",
                    onClick = {
                        try { uriHandler.openUri("mailto:ijubayer1071@gmail.com") } catch (_: Exception) {}
                    }
                ) {
                    MailLineIcon(color = GoblinTextTertiary)
                }
            }

            // Divider between Social Links & Darkroom Controls
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .background(GoblinBorderSubtle)
            )

            // Atmospheric Darkroom Controls & Coordinates
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Coordinate Metadata
                Text(
                    text = "23.8° N • DHAKA",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    letterSpacing = 1.2.sp,
                    color = GoblinTextTertiary
                )

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(14.dp)
                        .background(GoblinBorderSubtle)
                )

                // B&W Monochrome Switch
                MonochromeQuickButton(
                    isActive = uiState.isMonochromeMode,
                    onClick = onToggleMonochrome,
                    label = "B&W",
                    testTag = "desktop_bw_toggle"
                )

                // Grain Switch
                MonochromeQuickButton(
                    isActive = uiState.isFilmGrainEnabled,
                    onClick = onToggleFilmGrain,
                    label = "GRAIN",
                    testTag = "desktop_grain_toggle"
                )

                // Audio Switch
                IconButton(
                    onClick = onToggleAmbient,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("desktop_audio_toggle")
                ) {
                    Icon(
                        imageVector = if (uiState.isAmbientSoundActive) Icons.Outlined.GraphicEq else Icons.Outlined.VolumeMute,
                        contentDescription = "Toggle Ambience",
                        tint = if (uiState.isAmbientSoundActive) GoblinAccentWarm else GoblinTextTertiary,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }
}

/**
 * Mobile Compact Navigation Bar
 */
@Composable
private fun MobileNavigationRow(
    uiState: PortfolioUiState,
    showAtmosphereDropdown: Boolean,
    onToggleAtmosphereMenu: () -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    onToggleMobileMenu: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Khonchitro Monogram & Brand
        KhonchitroBrandLogo(
            onClick = { onNavigate(NavigationSection.WORK) }
        )

        // Right: Atmosphere + Saved + Admin + Hamburger Toggle (All with responsive 44dp touch targets)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Atmosphere Toggle Button
            IconButton(
                onClick = onToggleAtmosphereMenu,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("atmosphere_button")
            ) {
                Icon(
                    imageVector = if (uiState.isMonochromeMode) Icons.Outlined.Contrast else Icons.Default.FilterVintage,
                    contentDescription = "Atmospheric modes",
                    tint = if (uiState.isMonochromeMode || uiState.isAmbientSoundActive) GoblinTextPrimary else GoblinTextTertiary,
                    modifier = Modifier.size(19.dp)
                )
            }

            // Saved Exhibition Curation Button
            IconButton(
                onClick = { onNavigate(NavigationSection.CURATION) },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("saved_curation_button")
            ) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Icon(
                        imageVector = if (uiState.favoritePhotoIds.isNotEmpty()) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Saved Exhibition",
                        tint = if (uiState.activeSection == NavigationSection.CURATION) GoblinTextPrimary else GoblinTextTertiary,
                        modifier = Modifier.size(19.dp)
                    )
                    if (uiState.favoritePhotoIds.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(GoblinTextPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${uiState.favoritePhotoIds.size}",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Admin CMS Button
            IconButton(
                onClick = { onNavigate(NavigationSection.ADMIN) },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("admin_nav_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Admin CMS",
                    tint = if (uiState.activeSection == NavigationSection.ADMIN) GoblinAccentWarm else GoblinTextTertiary,
                    modifier = Modifier.size(19.dp)
                )
            }

            // Full-Screen Mobile Menu Overlay Toggle
            IconButton(
                onClick = onToggleMobileMenu,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("menu_toggle_button")
            ) {
                Icon(
                    imageVector = if (uiState.isMobileMenuOpen) Icons.Default.Close else Icons.Default.Menu,
                    contentDescription = "Menu Overlay",
                    tint = GoblinTextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

/**
 * Editorial 'Khonchitro' Brand Logo & Monogram
 */
@Composable
private fun KhonchitroBrandLogo(
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .testTag("brand_monogram_button")
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                .background(Color(0xFF141414)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "K",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "KHONCHITRO",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 2.0.sp,
                color = GoblinTextPrimary
            )
            Text(
                text = "ক্ষণচিত্র • PHOTOGRAPHY ARCHIVE",
                fontFamily = FontFamily.SansSerif,
                fontSize = 8.sp,
                letterSpacing = 1.4.sp,
                color = GoblinTextTertiary
            )
        }
    }
}

/**
 * Minimal button switch for desktop navigation
 */
@Composable
private fun MonochromeQuickButton(
    isActive: Boolean,
    onClick: () -> Unit,
    label: String,
    testTag: String
) {
    Box(
        modifier = Modifier
            .heightIn(min = 34.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = 0.5.dp,
                color = if (isActive) GoblinTextPrimary else GoblinBorderSubtle,
                shape = RoundedCornerShape(4.dp)
            )
            .background(if (isActive) Color(0xFF141414) else Color(0xFFF6F6F5))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = FontFamily.Monospace,
            fontSize = 9.5.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            letterSpacing = 1.2.sp,
            color = if (isActive) Color.White else GoblinTextSecondary
        )
    }
}

/**
 * Atmospheric Settings Dropdown Panel for mobile
 */
@Composable
private fun AtmosphereDropdownCard(
    uiState: PortfolioUiState,
    onToggleMonochrome: () -> Unit,
    onToggleFilmGrain: () -> Unit,
    onToggleAmbient: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(240.dp)
            .clip(RoundedCornerShape(6.dp))
            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(6.dp))
            .background(Color(0xFFFFFFFF))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DARKROOM ATMOSPHERE",
                fontFamily = FontFamily.SansSerif,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                color = GoblinTextTertiary
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close dropdown",
                    tint = GoblinTextTertiary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Monochrome Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable { onToggleMonochrome() }
                .padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Monochrome (B&W)",
                fontSize = 12.sp,
                color = if (uiState.isMonochromeMode) GoblinTextPrimary else GoblinTextSecondary
            )
            Text(
                text = if (uiState.isMonochromeMode) "ON" else "OFF",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = if (uiState.isMonochromeMode) GoblinAccentWarm else GoblinTextTertiary
            )
        }

        // Film Grain Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable { onToggleFilmGrain() }
                .padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "35mm Film Grain",
                fontSize = 12.sp,
                color = if (uiState.isFilmGrainEnabled) GoblinTextPrimary else GoblinTextSecondary
            )
            Text(
                text = if (uiState.isFilmGrainEnabled) "ON" else "OFF",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = if (uiState.isFilmGrainEnabled) GoblinAccentWarm else GoblinTextTertiary
            )
        }

        // Monsoon Audio Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable { onToggleAmbient() }
                .padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Monsoon Rainscape",
                fontSize = 12.sp,
                color = if (uiState.isAmbientSoundActive) GoblinTextPrimary else GoblinTextSecondary
            )
            Text(
                text = if (uiState.isAmbientSoundActive) "PLAY" else "MUTE",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = if (uiState.isAmbientSoundActive) GoblinAccentWarm else GoblinTextTertiary
            )
        }
    }
}

/**
 * Reusable Social Action Button with Line Icon
 */
@Composable
fun SocialLineIconButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(36.dp)
            .testTag("social_link_${title.lowercase()}")
    ) {
        content()
    }
}

/**
 * Facebook Minimalist Line Icon
 */
@Composable
fun FacebookLineIcon(
    color: Color = GoblinTextTertiary,
    modifier: Modifier = Modifier.size(16.dp)
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 1.6.dp.toPx()
        val w = size.width
        val h = size.height

        // Outer rounded square outline
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, 0f),
            size = Size(w, h),
            cornerRadius = CornerRadius(3.5.dp.toPx()),
            style = Stroke(width = strokeWidth)
        )

        // Path for lowercase 'f'
        val path = Path().apply {
            moveTo(w * 0.62f, h * 0.90f)
            lineTo(w * 0.62f, h * 0.52f)
            lineTo(w * 0.76f, h * 0.52f)
            moveTo(w * 0.48f, h * 0.52f)
            lineTo(w * 0.62f, h * 0.52f)
            lineTo(w * 0.62f, h * 0.35f)
            cubicTo(
                w * 0.62f, h * 0.22f,
                w * 0.70f, h * 0.20f,
                w * 0.80f, h * 0.20f
            )
        }
        drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
    }
}

/**
 * Instagram Minimalist Line Icon
 */
@Composable
fun InstagramLineIcon(
    color: Color = GoblinTextTertiary,
    modifier: Modifier = Modifier.size(16.dp)
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 1.6.dp.toPx()
        val w = size.width
        val h = size.height

        // Outer rounded rect
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, 0f),
            size = Size(w, h),
            cornerRadius = CornerRadius(4.5.dp.toPx()),
            style = Stroke(width = strokeWidth)
        )

        // Center lens circle
        drawCircle(
            color = color,
            radius = w * 0.24f,
            center = Offset(w * 0.5f, h * 0.5f),
            style = Stroke(width = strokeWidth)
        )

        // Top-right flash dot
        drawCircle(
            color = color,
            radius = 1.2.dp.toPx(),
            center = Offset(w * 0.76f, h * 0.24f)
        )
    }
}

/**
 * Pexels Minimalist Line Icon
 */
@Composable
fun PexelsLineIcon(
    color: Color = GoblinTextTertiary,
    modifier: Modifier = Modifier.size(16.dp)
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 1.6.dp.toPx()
        val w = size.width
        val h = size.height

        // Outer rounded rect
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, 0f),
            size = Size(w, h),
            cornerRadius = CornerRadius(3.5.dp.toPx()),
            style = Stroke(width = strokeWidth)
        )

        // Letter 'P' line path
        val path = Path().apply {
            moveTo(w * 0.38f, h * 0.80f)
            lineTo(w * 0.38f, h * 0.26f)
            lineTo(w * 0.58f, h * 0.26f)
            cubicTo(
                w * 0.72f, h * 0.26f,
                w * 0.72f, h * 0.54f,
                w * 0.58f, h * 0.54f
            )
            lineTo(w * 0.38f, h * 0.54f)
        }
        drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
    }
}

/**
 * Mail / Direct Envelope Line Icon
 */
@Composable
fun MailLineIcon(
    color: Color = GoblinTextTertiary,
    modifier: Modifier = Modifier.size(16.dp)
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 1.6.dp.toPx()
        val w = size.width
        val h = size.height * 0.82f
        val topOffset = (size.height - h) / 2f

        // Envelope rectangle
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, topOffset),
            size = Size(w, h),
            cornerRadius = CornerRadius(2.5.dp.toPx()),
            style = Stroke(width = strokeWidth)
        )

        // Flap 'V' crease
        val flapPath = Path().apply {
            moveTo(0f, topOffset + 1.dp.toPx())
            lineTo(w * 0.5f, topOffset + h * 0.58f)
            lineTo(w, topOffset + 1.dp.toPx())
        }
        drawPath(path = flapPath, color = color, style = Stroke(width = strokeWidth))
    }
}
