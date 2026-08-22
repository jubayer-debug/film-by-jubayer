package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.PortfolioRepository
import com.example.ui.components.ImageLightbox
import com.example.ui.components.MobileMenuOverlay
import com.example.ui.components.NavigationHeader
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.CurationScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JournalDetailScreen
import com.example.ui.screens.JournalScreen
import com.example.ui.screens.ProjectDetailScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.PortfolioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                GoblinPortfolioApp()
            }
        }
    }
}

@Composable
fun GoblinPortfolioApp(
    viewModel: PortfolioViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle Hardware & Gesture Back
    BackHandler(
        enabled = uiState.lightboxPhoto != null ||
                uiState.selectedProject != null ||
                uiState.selectedJournal != null ||
                uiState.isMobileMenuOpen ||
                uiState.activeSection != NavigationSection.WORK
    ) {
        when {
            uiState.lightboxPhoto != null -> viewModel.closeLightbox()
            uiState.isMobileMenuOpen -> viewModel.setMobileMenu(false)
            uiState.selectedProject != null -> viewModel.closeProject()
            uiState.selectedJournal != null -> viewModel.closeJournal()
            uiState.activeSection != NavigationSection.WORK -> viewModel.navigateTo(NavigationSection.WORK)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GoblinBg)
    ) {
        Scaffold(
            topBar = {
                // Fixed Minimal Navigation Header
                if (uiState.lightboxPhoto == null) {
                    NavigationHeader(
                        uiState = uiState,
                        onNavigate = { viewModel.navigateTo(it) },
                        onToggleMobileMenu = { viewModel.toggleMobileMenu() },
                        onToggleMonochrome = { viewModel.toggleMonochromeMode() },
                        onToggleFilmGrain = { viewModel.toggleFilmGrain() },
                        onToggleAmbient = { viewModel.toggleAmbientSound() }
                    )
                }
            },
            containerColor = GoblinBg
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = if (uiState.lightboxPhoto == null) innerPadding.calculateTopPadding() else androidx.compose.ui.unit.Dp.Unspecified)
            ) {
                // Page Transition Engine (Subtle photographic book page fade)
                AnimatedContent(
                    targetState = Triple(uiState.activeSection, uiState.selectedProject, uiState.selectedJournal),
                    transitionSpec = {
                        fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(300))
                    },
                    label = "page_transition"
                ) { (_, activeProject, activeJournal) ->
                    when {
                        // Project Detail Essay View
                        activeProject != null -> {
                            ProjectDetailScreen(
                                project = activeProject,
                                uiState = uiState,
                                onBack = { viewModel.closeProject() },
                                onPhotoClick = { viewModel.openLightbox(it) },
                                onNavigate = { viewModel.navigateTo(it) }
                            )
                        }

                        // Journal Detail Reading View
                        activeJournal != null -> {
                            JournalDetailScreen(
                                journal = activeJournal,
                                uiState = uiState,
                                onBack = { viewModel.closeJournal() },
                                onNavigate = { viewModel.navigateTo(it) }
                            )
                        }

                        // Primary Section Views
                        else -> {
                            when (uiState.activeSection) {
                                NavigationSection.WORK -> {
                                    HomeScreen(
                                        uiState = uiState,
                                        onPhotoClick = { viewModel.openLightbox(it) },
                                        onProjectClick = { viewModel.openProject(it) },
                                        onCategorySelect = { viewModel.selectCategory(it) },
                                        onSortOrderChange = { viewModel.setSortOrder(it) },
                                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                        onResetFilters = { viewModel.resetFilters() },
                                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                                        onNavigate = { viewModel.navigateTo(it) }
                                    )
                                }

                                NavigationSection.PROJECTS -> {
                                    ProjectsScreen(
                                        uiState = uiState,
                                        onProjectClick = { viewModel.openProject(it) },
                                        onNavigate = { viewModel.navigateTo(it) }
                                    )
                                }

                                NavigationSection.JOURNAL -> {
                                    JournalScreen(
                                        uiState = uiState,
                                        onJournalClick = { viewModel.openJournal(it) },
                                        onNavigate = { viewModel.navigateTo(it) }
                                    )
                                }

                                NavigationSection.ABOUT -> {
                                    AboutScreen(
                                        uiState = uiState,
                                        onNavigate = { viewModel.navigateTo(it) }
                                    )
                                }

                                NavigationSection.CONTACT -> {
                                    ContactScreen(
                                        formState = uiState.contactForm,
                                        onNameChange = { viewModel.updateContactName(it) },
                                        onEmailChange = { viewModel.updateContactEmail(it) },
                                        onProjectTypeChange = { viewModel.updateContactProjectType(it) },
                                        onMessageChange = { viewModel.updateContactMessage(it) },
                                        onSubmit = { viewModel.submitContactForm() },
                                        onResetSuccess = { viewModel.resetContactSuccess() },
                                        onNavigate = { viewModel.navigateTo(it) }
                                    )
                                }

                                NavigationSection.CURATION -> {
                                    CurationScreen(
                                        uiState = uiState,
                                        savedPhotos = viewModel.getCuratedSavedPhotos(),
                                        onPhotoClick = { viewModel.openLightbox(it) },
                                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                                        onNavigate = { viewModel.navigateTo(it) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // FULLSCREEN MOBILE MENU OVERLAY
        MobileMenuOverlay(
            isOpen = uiState.isMobileMenuOpen,
            activeSection = uiState.activeSection,
            onNavigate = { viewModel.navigateTo(it) },
            onClose = { viewModel.setMobileMenu(false) },
            savedCount = uiState.favoritePhotoIds.size
        )

        // FULLSCREEN LIGHTBOX IMAGE VIEWER
        uiState.lightboxPhoto?.let { currentPhoto ->
            val allPhotos = viewModel.getFilteredPhotos()
            val currentIndex = allPhotos.indexOfFirst { it.id == currentPhoto.id }.coerceAtLeast(0)
            val isFav = uiState.favoritePhotoIds.contains(currentPhoto.id)

            ImageLightbox(
                photo = currentPhoto,
                currentIndex = currentIndex,
                totalCount = allPhotos.size,
                isFavorite = isFav,
                isMonochrome = uiState.isMonochromeMode,
                showFilmGrain = uiState.isFilmGrainEnabled,
                isExifOpen = uiState.isLightboxExifOpen,
                onClose = { viewModel.closeLightbox() },
                onNext = { viewModel.nextLightboxPhoto() },
                onPrevious = { viewModel.previousLightboxPhoto() },
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                onToggleExif = { viewModel.toggleLightboxExif() }
            )
        }
    }
}
