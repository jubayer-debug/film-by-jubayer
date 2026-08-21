package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PortfolioRepository
import com.example.data.models.JournalEntry
import com.example.data.models.PhotoCategory
import com.example.data.models.Photograph
import com.example.data.models.Project
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class NavigationSection(val label: String, val routeKey: String) {
    WORK("WORK", "work"),
    PROJECTS("PROJECTS", "projects"),
    JOURNAL("JOURNAL", "journal"),
    ABOUT("ABOUT", "about"),
    CONTACT("CONTACT", "contact"),
    CURATION("SAVED", "curation")
}

data class ContactFormState(
    val name: String = "",
    val email: String = "",
    val projectType: String = "Editorial Assignment",
    val message: String = "",
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

data class PortfolioUiState(
    val activeSection: NavigationSection = NavigationSection.WORK,
    val selectedCategory: PhotoCategory = PhotoCategory.ALL,
    val selectedProject: Project? = null,
    val selectedJournal: JournalEntry? = null,
    val lightboxPhoto: Photograph? = null,
    val isLightboxExifOpen: Boolean = false,
    val isMobileMenuOpen: Boolean = false,
    val isMonochromeMode: Boolean = false,
    val isFilmGrainEnabled: Boolean = true,
    val isAmbientSoundActive: Boolean = false,
    val favoritePhotoIds: Set<String> = emptySet(),
    val contactForm: ContactFormState = ContactFormState()
)

class PortfolioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PortfolioUiState())
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()

    fun navigateTo(section: NavigationSection) {
        _uiState.update {
            it.copy(
                activeSection = section,
                selectedProject = null,
                selectedJournal = null,
                isMobileMenuOpen = false
            )
        }
    }

    fun openProject(project: Project) {
        _uiState.update {
            it.copy(
                selectedProject = project,
                activeSection = NavigationSection.PROJECTS,
                isMobileMenuOpen = false
            )
        }
    }

    fun closeProject() {
        _uiState.update { it.copy(selectedProject = null) }
    }

    fun openJournal(journal: JournalEntry) {
        _uiState.update {
            it.copy(
                selectedJournal = journal,
                activeSection = NavigationSection.JOURNAL,
                isMobileMenuOpen = false
            )
        }
    }

    fun closeJournal() {
        _uiState.update { it.copy(selectedJournal = null) }
    }

    fun openLightbox(photograph: Photograph) {
        _uiState.update {
            it.copy(
                lightboxPhoto = photograph,
                isLightboxExifOpen = false
            )
        }
    }

    fun closeLightbox() {
        _uiState.update {
            it.copy(
                lightboxPhoto = null,
                isLightboxExifOpen = false
            )
        }
    }

    fun nextLightboxPhoto() {
        val current = _uiState.value.lightboxPhoto ?: return
        val list = getFilteredPhotos()
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex != -1) {
            val nextIndex = (currentIndex + 1) % list.size
            _uiState.update { it.copy(lightboxPhoto = list[nextIndex]) }
        }
    }

    fun previousLightboxPhoto() {
        val current = _uiState.value.lightboxPhoto ?: return
        val list = getFilteredPhotos()
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex != -1) {
            val prevIndex = if (currentIndex - 1 < 0) list.size - 1 else currentIndex - 1
            _uiState.update { it.copy(lightboxPhoto = list[prevIndex]) }
        }
    }

    fun toggleLightboxExif() {
        _uiState.update { it.copy(isLightboxExifOpen = !it.isLightboxExifOpen) }
    }

    fun toggleFavorite(photoId: String) {
        _uiState.update { current ->
            val updated = current.favoritePhotoIds.toMutableSet()
            if (updated.contains(photoId)) {
                updated.remove(photoId)
            } else {
                updated.add(photoId)
            }
            current.copy(favoritePhotoIds = updated)
        }
    }

    fun selectCategory(category: PhotoCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun toggleMobileMenu() {
        _uiState.update { it.copy(isMobileMenuOpen = !it.isMobileMenuOpen) }
    }

    fun setMobileMenu(open: Boolean) {
        _uiState.update { it.copy(isMobileMenuOpen = open) }
    }

    fun toggleMonochromeMode() {
        _uiState.update { it.copy(isMonochromeMode = !it.isMonochromeMode) }
    }

    fun toggleFilmGrain() {
        _uiState.update { it.copy(isFilmGrainEnabled = !it.isFilmGrainEnabled) }
    }

    fun toggleAmbientSound() {
        _uiState.update { it.copy(isAmbientSoundActive = !it.isAmbientSoundActive) }
    }

    fun getFilteredPhotos(): List<Photograph> {
        val cat = _uiState.value.selectedCategory
        return if (cat == PhotoCategory.ALL) {
            PortfolioRepository.photographs
        } else {
            PortfolioRepository.photographs.filter { it.category == cat }
        }
    }

    fun getCuratedSavedPhotos(): List<Photograph> {
        val ids = _uiState.value.favoritePhotoIds
        return PortfolioRepository.photographs.filter { ids.contains(it.id) }
    }

    // Contact Form handlers
    fun updateContactName(name: String) {
        _uiState.update { it.copy(contactForm = it.contactForm.copy(name = name, errorMessage = null)) }
    }

    fun updateContactEmail(email: String) {
        _uiState.update { it.copy(contactForm = it.contactForm.copy(email = email, errorMessage = null)) }
    }

    fun updateContactProjectType(type: String) {
        _uiState.update { it.copy(contactForm = it.contactForm.copy(projectType = type)) }
    }

    fun updateContactMessage(msg: String) {
        _uiState.update { it.copy(contactForm = it.contactForm.copy(message = msg, errorMessage = null)) }
    }

    fun submitContactForm() {
        val form = _uiState.value.contactForm
        if (form.name.isBlank()) {
            _uiState.update { it.copy(contactForm = it.contactForm.copy(errorMessage = "Please enter your name")) }
            return
        }
        if (form.email.isBlank() || !form.email.contains("@")) {
            _uiState.update { it.copy(contactForm = it.contactForm.copy(errorMessage = "Please provide a valid email address")) }
            return
        }
        if (form.message.isBlank()) {
            _uiState.update { it.copy(contactForm = it.contactForm.copy(errorMessage = "Please share a brief message regarding your project")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(contactForm = it.contactForm.copy(isSubmitting = true, errorMessage = null)) }
            delay(1200) // Realistic sending animation
            _uiState.update {
                it.copy(
                    contactForm = it.contactForm.copy(
                        isSubmitting = false,
                        isSuccess = true,
                        name = "",
                        email = "",
                        message = ""
                    )
                )
            }
        }
    }

    fun resetContactSuccess() {
        _uiState.update { it.copy(contactForm = it.contactForm.copy(isSuccess = false)) }
    }
}
