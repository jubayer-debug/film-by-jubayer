package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.PortfolioRepository
import com.example.data.models.Photograph
import com.example.ui.components.AboutHero
import com.example.ui.components.AboutImageBreak
import com.example.ui.components.AboutPortrait
import com.example.ui.components.ArtistIntroduction
import com.example.ui.components.ArtistStatement
import com.example.ui.components.ContactCTA
import com.example.ui.components.EditorialBg
import com.example.ui.components.EditorialFooter
import com.example.ui.components.JourneyTimeline
import com.example.ui.components.LocationMetadata
import com.example.ui.components.PersonalPhilosophy
import com.example.ui.components.PracticeList
import com.example.ui.components.SelectedProjects
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.PortfolioUiState

/**
 * Editorial Photography About Screen
 * Dark Monochromatic Exhibition Canvas (#090909) with original artist identity & spacious rhythm
 */
@Composable
fun AboutScreen(
    uiState: PortfolioUiState,
    onNavigate: (NavigationSection) -> Unit,
    onPhotoClick: (Photograph) -> Unit = {},
    onProjectClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val aboutData = remember(uiState.contentUpdateVersion) { PortfolioRepository.aboutData }
    val portraitPhoto = remember(uiState.contentUpdateVersion) {
        PortfolioRepository.photographs.firstOrNull { it.id == aboutData.portraitPhotoId }
            ?: PortfolioRepository.photographs.firstOrNull()
    }
    val secondaryPhoto = remember(uiState.contentUpdateVersion) {
        PortfolioRepository.photographs.firstOrNull { it.id == aboutData.secondaryPhotoId }
            ?: PortfolioRepository.photographs.getOrNull(1)
    }

    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(EditorialBg)
            .statusBarsPadding()
            .testTag("about_screen_lazy_column")
    ) {
        // 1. TEXT: About Hero (Editorial Title & Asymmetric Introduction)
        item {
            AboutHero(
                aboutData = aboutData
            )
        }

        // 2. IMAGE: Large Portrait / Environmental Photograph
        item {
            AboutPortrait(
                photograph = portraitPhoto,
                aboutData = aboutData,
                isMonochrome = uiState.isMonochromeMode,
                showFilmGrain = uiState.isFilmGrainEnabled,
                onPhotoClick = onPhotoClick
            )
        }

        // 3. WHITESPACE & BIOGRAPHY: Artist Introduction (Deltaic observations)
        item {
            ArtistIntroduction(
                aboutData = aboutData
            )
        }

        // 4. LARGE STATEMENT: Multi-line Typographic Statement
        item {
            ArtistStatement(
                statement = aboutData.primaryStatement
            )
        }

        // 5. LIST: Practice Areas (01 Landscape, 02 Documentary, etc.)
        item {
            PracticeList(
                practices = aboutData.practices,
                onSelectPractice = { onNavigate(NavigationSection.PHOTOS) }
            )
        }

        // 6. IMAGE BREAK: Secondary Wide Cinematic Landscape Photograph (Visual Pause)
        item {
            AboutImageBreak(
                photograph = secondaryPhoto,
                isMonochrome = uiState.isMonochromeMode,
                showFilmGrain = uiState.isFilmGrainEnabled,
                onPhotoClick = onPhotoClick
            )
        }

        // 7. METADATA: Location / Working Medium / Explorations / Availability
        item {
            LocationMetadata(
                metadataList = aboutData.metadataList
            )
        }

        // 8. PHILOSOPHY: Off-center Large Typography
        item {
            PersonalPhilosophy(
                philosophy = aboutData.personalPhilosophy
            )
        }

        // 9. TIMELINE: Journey Chronology
        item {
            JourneyTimeline(
                journeyItems = aboutData.journeyItems
            )
        }

        // 10. PROJECTS: Selected Editorial Projects Links
        item {
            SelectedProjects(
                projects = aboutData.selectedProjects,
                onSelectProject = { projectId ->
                    val proj = PortfolioRepository.getProjectById(projectId)
                    if (proj != null) {
                        onProjectClick(projectId)
                    } else {
                        onNavigate(NavigationSection.ALBUMS)
                    }
                }
            )
        }

        // 11. CONTACT: Spacious Closing CTA
        item {
            ContactCTA(
                email = aboutData.contactEmail,
                onNavigateToContact = { onNavigate(NavigationSection.CONTACT) }
            )
        }

        // 12. FOOTER: Minimal Exhibition Colophon
        item {
            EditorialFooter(
                onBackToTop = {
                    kotlinx.coroutines.CoroutineScope(coroutineScope.coroutineContext).let {
                        coroutineScope.run {
                            // scroll to top
                        }
                    }
                },
                onNavigate = onNavigate
            )
        }
    }
}
