package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.PortfolioUiState

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
    var showAtmosphereMenu by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        GoblinBg.copy(alpha = 0.95f),
                        GoblinBg.copy(alpha = 0.85f),
                        Color.Transparent
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LEFT: Brand Monogram & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onNavigate(NavigationSection.WORK) }
                    .testTag("brand_monogram_button")
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.dp, GoblinBorderSubtle, CircleShape)
                        .background(Color(0xFF141414)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "G",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = GoblinTextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "GOBLIN",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        letterSpacing = 2.5.sp,
                        color = GoblinTextPrimary
                    )
                    Text(
                        text = "ARCHIVE / 2026",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 8.5.sp,
                        letterSpacing = 1.8.sp,
                        color = GoblinTextTertiary
                    )
                }
            }

            // CENTER: Location metadata pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(12.dp))
                    .background(Color(0x33141414))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(if (uiState.isAmbientSoundActive) GoblinAccentWarm else Color(0xFF4CAF50))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DHAKA, BD • 23.8° N",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 9.sp,
                        letterSpacing = 1.2.sp,
                        color = GoblinTextSecondary
                    )
                }
            }

            // RIGHT: Action Icons & Menu
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Atmosphere Controls Button
                IconButton(
                    onClick = { showAtmosphereMenu = !showAtmosphereMenu },
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("atmosphere_button")
                ) {
                    Icon(
                        imageVector = if (uiState.isMonochromeMode) Icons.Outlined.Contrast else Icons.Default.FilterVintage,
                        contentDescription = "Atmospheric modes",
                        tint = if (uiState.isMonochromeMode) GoblinAccentWarm else GoblinTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Saved Curation Count Button
                IconButton(
                    onClick = { onNavigate(NavigationSection.CURATION) },
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("saved_curation_button")
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Icon(
                            imageVector = if (uiState.favoritePhotoIds.isNotEmpty()) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Saved Exhibition",
                            tint = if (uiState.activeSection == NavigationSection.CURATION) GoblinAccentWarm else GoblinTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        if (uiState.favoritePhotoIds.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(GoblinAccentWarm),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${uiState.favoritePhotoIds.size}",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                // Mobile / Quick Menu Button
                IconButton(
                    onClick = onToggleMobileMenu,
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("menu_toggle_button")
                ) {
                    Icon(
                        imageVector = if (uiState.isMobileMenuOpen) Icons.Default.Close else Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = GoblinTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Atmosphere controls dropdown panel
        AnimatedVisibility(
            visible = showAtmosphereMenu,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, GoblinBorderSubtle, RoundedCornerShape(8.dp))
                    .background(Color(0xF0121212))
                    .padding(12.dp)
            ) {
                Text(
                    text = "ATMOSPHERE & DISPLAY",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextTertiary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Monochrome Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onToggleMonochrome() }
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Monochrome (B&W)",
                        fontSize = 12.sp,
                        color = if (uiState.isMonochromeMode) GoblinAccentWarm else GoblinTextPrimary
                    )
                    Text(
                        text = if (uiState.isMonochromeMode) "ON" else "OFF",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (uiState.isMonochromeMode) GoblinAccentWarm else GoblinTextTertiary
                    )
                }

                // Film Grain Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onToggleFilmGrain() }
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "35mm Film Grain",
                        fontSize = 12.sp,
                        color = if (uiState.isFilmGrainEnabled) GoblinAccentWarm else GoblinTextPrimary
                    )
                    Text(
                        text = if (uiState.isFilmGrainEnabled) "ON" else "OFF",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (uiState.isFilmGrainEnabled) GoblinAccentWarm else GoblinTextTertiary
                    )
                }

                // Ambient Audio Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onToggleAmbient() }
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Monsoon Rainscape",
                        fontSize = 12.sp,
                        color = if (uiState.isAmbientSoundActive) GoblinAccentWarm else GoblinTextPrimary
                    )
                    Text(
                        text = if (uiState.isAmbientSoundActive) "PLAYING" else "MUTED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (uiState.isAmbientSoundActive) GoblinAccentWarm else GoblinTextTertiary
                    )
                }
            }
        }
    }
}
