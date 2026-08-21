package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Photograph
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.PortfolioUiState

@Composable
fun CurationScreen(
    uiState: PortfolioUiState,
    savedPhotos: List<Photograph>,
    onPhotoClick: (Photograph) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("curation_screen_lazy_column")
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "PERSONAL EXHIBITION ARCHIVE",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    letterSpacing = 3.sp,
                    color = GoblinAccentWarm
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "MY CURATION",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Light,
                    fontSize = 32.sp,
                    letterSpacing = 2.sp,
                    color = GoblinTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${savedPhotos.size} PHOTOGRAPHS SHORTLISTED FOR EXHIBITION REVIEW",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    letterSpacing = 1.2.sp,
                    color = GoblinTextTertiary
                )
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        if (savedPhotos.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NO BOOKMARKED PHOTOGRAPHS",
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Explore the Selected Work gallery and tap the bookmark icon on any photograph to curate your personal collection.",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.5.sp,
                        color = GoblinTextTertiary
                    )
                }
            }
        } else {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    savedPhotos.forEach { photo ->
                        EditorialPhotoCard(
                            photo = photo,
                            isFavorite = true,
                            isMonochrome = uiState.isMonochromeMode,
                            showFilmGrain = uiState.isFilmGrainEnabled,
                            aspect = 1.4f,
                            onClick = { onPhotoClick(photo) },
                            onToggleFavorite = { onToggleFavorite(photo.id) }
                        )
                    }
                }
            }
        }

        item {
            FooterSection(onBackToTop = {}, onNavigate = onNavigate)
        }
    }
}
