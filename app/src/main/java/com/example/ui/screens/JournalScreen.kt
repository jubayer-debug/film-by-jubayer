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
import com.example.data.models.JournalEntry
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
fun JournalScreen(
    uiState: PortfolioUiState,
    onJournalClick: (JournalEntry) -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("journal_screen_lazy_column")
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "FIELD NOTES & ESSAYS",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    letterSpacing = 3.sp,
                    color = GoblinAccentWarm
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "JOURNAL",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Light,
                    fontSize = 32.sp,
                    letterSpacing = 2.sp,
                    color = GoblinTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Reflections on photography, light, and observation across the Bengal landscape.",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = GoblinTextSecondary
                )
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        items(PortfolioRepository.journalEntries, key = { it.id }) { journal ->
            val cover = PortfolioRepository.getPhotoById(journal.coverPhotoId) ?: PortfolioRepository.photographs.first()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                    .background(GoblinBgSecondary)
                    .clickable { onJournalClick(journal) }
                    .padding(16.dp)
                    .testTag("journal_item_${journal.id}")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.8f)
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
                        text = journal.date,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        letterSpacing = 2.sp,
                        color = GoblinAccentWarm
                    )
                    Text(
                        text = journal.readTime,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 9.sp,
                        letterSpacing = 1.2.sp,
                        color = GoblinTextTertiary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = journal.title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp,
                    color = GoblinTextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = journal.location,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    color = GoblinTextTertiary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = journal.excerpt,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.5.sp,
                    lineHeight = 19.sp,
                    color = GoblinTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "READ ESSAY →",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextPrimary
                )
            }
        }

        item {
            FooterSection(onBackToTop = {}, onNavigate = onNavigate)
        }
    }
}

@Composable
fun JournalDetailScreen(
    journal: JournalEntry,
    uiState: PortfolioUiState,
    onBack: () -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val cover = PortfolioRepository.getPhotoById(journal.coverPhotoId) ?: PortfolioRepository.photographs.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("journal_detail_screen")
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
                        .background(Color(0xFFF4F4F3))
                        .border(0.5.dp, GoblinBorderSubtle, CircleShape)
                        .testTag("journal_detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to journal",
                        tint = GoblinTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "BACK TO JOURNAL",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = GoblinTextSecondary,
                    modifier = Modifier.clickable { onBack() }
                )
            }
        }

        // Title and Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "${journal.date} • ${journal.location}",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.5.sp,
                    letterSpacing = 2.sp,
                    color = GoblinAccentWarm
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = journal.title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Light,
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    letterSpacing = 1.sp,
                    color = GoblinTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = journal.readTime,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 9.5.sp,
                    letterSpacing = 1.5.sp,
                    color = GoblinTextTertiary
                )

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        // Cover artwork
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.6f)
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
                    text = cover.caption,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.5.sp,
                    color = GoblinTextSecondary
                )
            }
        }

        // Quote block
        if (journal.quote.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\"${journal.quote}\"",
                        fontFamily = FontFamily.Serif,
                        fontSize = 17.sp,
                        lineHeight = 26.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        color = GoblinAccentWarm
                    )
                }
            }
        }

        // Full content paragraphs
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                journal.content.split("\n\n").forEach { paragraph ->
                    Text(
                        text = paragraph,
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.5.sp,
                        lineHeight = 24.sp,
                        color = GoblinTextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(28.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        item {
            FooterSection(onBackToTop = {}, onNavigate = onNavigate)
        }
    }
}
