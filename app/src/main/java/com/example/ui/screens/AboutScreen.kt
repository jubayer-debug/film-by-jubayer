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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PortfolioRepository
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
fun AboutScreen(
    uiState: PortfolioUiState,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val portraitPhoto = remember(uiState.contentUpdateVersion) {
        PortfolioRepository.photographs.firstOrNull { it.id == "photo_06" } ?: PortfolioRepository.photographs.firstOrNull()
    }
    val exhibitions = remember(uiState.contentUpdateVersion) { PortfolioRepository.exhibitions }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("about_screen_lazy_column")
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "BIOGRAPHY & STATEMENT",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    letterSpacing = 3.sp,
                    color = GoblinAccentWarm
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ABOUT THE PHOTOGRAPHER",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Light,
                    fontSize = 28.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextPrimary
                )
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        // Portrait Photo Frame
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                if (portraitPhoto != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.2f)
                            .clip(RoundedCornerShape(4.dp))
                            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                    ) {
                        PhotographicArtwork(
                            photograph = portraitPhoto,
                            isMonochrome = uiState.isMonochromeMode,
                            showFilmGrain = uiState.isFilmGrainEnabled,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "KHONCHITRO (ক্ষণচিত্র) • DHAKA, BANGLADESH",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.5.sp,
                        letterSpacing = 1.8.sp,
                        color = GoblinTextTertiary
                    )
                }
            }
        }

        // Biography Text
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "\"I am Jubayer, a documentary photographer based in Bangladesh, working between landscape, cultural memory and everyday human observation.\n\nMy work is drawn toward quiet places—the riverbanks, roads, villages and fleeting moments that often disappear before anyone notices them.\"",
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    fontStyle = FontStyle.Italic,
                    color = GoblinTextPrimary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Over the past eight years, my practice has centered on the human geography of the Bengal delta. Using rangefinder cameras and natural light, I document how seasonal weather cycles, river erosion, and rural migration reshape memory and domestic environments.",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.5.sp,
                    lineHeight = 22.sp,
                    color = GoblinTextSecondary
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Metadata Details
                AboutMetadataRow(label = "BASED IN", value = "Dhaka, Bangladesh")
                AboutMetadataRow(label = "PRIMARY MEDIUM", value = "Leica M Rangefinder • Archival Pigment Prints")
                AboutMetadataRow(label = "AVAILABLE FOR", value = "Editorial Assignments • Documentary • Commissions")

                Spacer(modifier = Modifier.height(28.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        // Exhibitions Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "SELECTED EXHIBITIONS & ARCHIVES",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = GoblinAccentWarm
                )
                Spacer(modifier = Modifier.height(16.dp))

                exhibitions.forEach { ex ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = ex.year,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GoblinAccentWarm,
                            modifier = Modifier.width(48.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ex.title,
                                fontFamily = FontFamily.Serif,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = GoblinTextPrimary
                            )
                            Text(
                                text = "${ex.venue} • ${ex.location}",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 11.sp,
                                color = GoblinTextSecondary
                            )
                        }
                        Text(
                            text = ex.type,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 9.sp,
                            letterSpacing = 1.sp,
                            color = GoblinTextTertiary
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

@Composable
private fun AboutMetadataRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            fontFamily = FontFamily.SansSerif,
            fontSize = 9.sp,
            letterSpacing = 1.5.sp,
            color = GoblinTextTertiary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontFamily = FontFamily.SansSerif,
            fontSize = 13.sp,
            color = GoblinTextPrimary
        )
    }
}
