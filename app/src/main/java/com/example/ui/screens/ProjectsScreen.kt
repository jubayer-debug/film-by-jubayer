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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun ProjectsScreen(
    uiState: PortfolioUiState,
    onProjectClick: (Project) -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val projects = remember(uiState.contentUpdateVersion) { PortfolioRepository.projects }

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
                    .padding(horizontal = 12.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "ESSAYS & ARCHIVES",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    letterSpacing = 3.sp,
                    color = GoblinAccentWarm
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "PROJECTS",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Light,
                    fontSize = 32.sp,
                    letterSpacing = 2.sp,
                    color = GoblinTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Long-form documentary series examining the ecological, cultural, and spiritual rhythms of Bangladesh.",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = GoblinTextSecondary
                )
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        items(projects, key = { it.id }) { project ->
            val cover = PortfolioRepository.getPhotoById(project.coverPhotoId) ?: PortfolioRepository.photographs.first()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                    .background(GoblinBgSecondary)
                    .clickable { onProjectClick(project) }
                    .padding(16.dp)
                    .testTag("project_item_${project.id}")
            ) {
                // Large Cover preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.75f)
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

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${project.number} — ${project.title}",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp,
                        color = GoblinTextPrimary
                    )
                    Text(
                        text = "${project.photoCount} WORKS",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinAccentWarm
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = project.subtitle,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    color = GoblinAccentWarm
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = project.description,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    color = GoblinTextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${project.location} • ${project.year}",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        letterSpacing = 1.2.sp,
                        color = GoblinTextTertiary
                    )
                    Text(
                        text = "EXPLORE ESSAY →",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextPrimary
                    )
                }
            }
        }

        item {
            FooterSection(onBackToTop = {}, onNavigate = onNavigate)
        }
    }
}
