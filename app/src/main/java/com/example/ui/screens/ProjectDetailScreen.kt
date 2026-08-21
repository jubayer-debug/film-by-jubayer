package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.models.Photograph
import com.example.data.models.Project
import com.example.ui.components.PhotographicArtwork
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.PortfolioUiState

@Composable
fun ProjectDetailScreen(
    project: Project,
    uiState: PortfolioUiState,
    onBack: () -> Unit,
    onPhotoClick: (Photograph) -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val projectPhotos = project.photoIds.mapNotNull { PortfolioRepository.getPhotoById(it) }
    val cover = PortfolioRepository.getPhotoById(project.coverPhotoId) ?: projectPhotos.firstOrNull() ?: PortfolioRepository.photographs.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("project_detail_screen")
    ) {
        // Top Back Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x88111111))
                        .testTag("project_detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to projects",
                        tint = GoblinTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "BACK TO PROJECTS",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = GoblinTextSecondary,
                    modifier = Modifier.clickable { onBack() }
                )
            }
        }

        // Project Hero Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "SERIES ${project.number}",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp,
                    color = GoblinAccentWarm
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = project.title,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Light,
                        fontSize = 32.sp,
                        letterSpacing = 2.sp,
                        color = GoblinTextPrimary
                    )
                    if (project.bengaliTitle.isNotEmpty()) {
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
                    fontSize = 13.sp,
                    color = GoblinTextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${project.location} • ${project.year}",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextTertiary
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Introductory Essay
                Text(
                    text = project.essayText,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.5.sp,
                    lineHeight = 22.sp,
                    color = GoblinTextSecondary
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        // Full-width Cover Photo
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .clickable { onPhotoClick(cover) }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.65f)
                        .clip(RoundedCornerShape(2.dp))
                        .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(2.dp))
                ) {
                    PhotographicArtwork(
                        photograph = cover,
                        isMonochrome = uiState.isMonochromeMode,
                        showFilmGrain = uiState.isFilmGrainEnabled,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = cover.title.uppercase(),
                    fontFamily = FontFamily.Serif,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    color = GoblinTextPrimary
                )
                Text(
                    text = "${cover.location} • ${cover.caption}",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    color = GoblinTextSecondary
                )
            }
        }

        // Quote Break in Essay
        if (project.quote.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\"${project.quote}\"",
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        color = GoblinAccentWarm
                    )
                }
            }
        }

        // Staggered photos in project
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                projectPhotos.drop(1).forEach { photo ->
                    EditorialPhotoCard(
                        photo = photo,
                        isFavorite = uiState.favoritePhotoIds.contains(photo.id),
                        isMonochrome = uiState.isMonochromeMode,
                        showFilmGrain = uiState.isFilmGrainEnabled,
                        aspect = 1.4f,
                        onClick = { onPhotoClick(photo) },
                        onToggleFavorite = {}
                    )
                }
            }
        }

        // Project Information metadata footer
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 36.dp)
            ) {
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "PROJECT ARCHIVE INFORMATION",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp,
                    color = GoblinAccentWarm
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Exhibition format: Fine art archival pigment prints on Hahnemühle Photo Rag 308gsm. Limited editions of 7 + 2 AP.",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.5.sp,
                    lineHeight = 18.sp,
                    color = GoblinTextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "PRINT INQUIRY →",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextPrimary,
                    modifier = Modifier.clickable { onNavigate(NavigationSection.CONTACT) }
                )
            }
        }

        item {
            FooterSection(onBackToTop = {}, onNavigate = onNavigate)
        }
    }
}
