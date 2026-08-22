package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PortfolioRepository
import com.example.data.models.Exhibition
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
    CURATION("SAVED", "curation"),
    ADMIN("ADMIN", "admin")
}

enum class AdminTab(val label: String, val iconLabel: String) {
    PHOTOS("PHOTOGRAPHS", "📷"),
    PROJECTS("PROJECTS", "📁"),
    JOURNAL("FIELD JOURNAL", "✍️"),
    EXHIBITIONS("EXHIBITIONS", "🏛️"),
    TOOLS("PRESETS & BACKUP", "⚙️")
}

enum class PhotoSortOrder(val label: String, val shortLabel: String) {
    CURATED("Curated Selection", "Curated"),
    RANDOM_SHUFFLE("Random Serendipity", "Random"),
    YEAR_DESC("Year: 2026 → 2024", "Latest"),
    YEAR_ASC("Year: 2024 → 2026", "Oldest"),
    TITLE_AZ("Title: A to Z", "Title"),
    LOCATION("Location: A to Z", "Location")
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
    val sortOrder: PhotoSortOrder = PhotoSortOrder.CURATED,
    val randomSeed: Long = System.currentTimeMillis(),
    val searchQuery: String = "",
    val selectedProject: Project? = null,
    val selectedJournal: JournalEntry? = null,
    val lightboxPhoto: Photograph? = null,
    val isLightboxExifOpen: Boolean = false,
    val isMobileMenuOpen: Boolean = false,
    val isMonochromeMode: Boolean = false,
    val isFilmGrainEnabled: Boolean = true,
    val isAmbientSoundActive: Boolean = false,
    val favoritePhotoIds: Set<String> = emptySet(),
    val contactForm: ContactFormState = ContactFormState(),
    // Admin CMS state
    val adminTab: AdminTab = AdminTab.PHOTOS,
    val adminEditingPhoto: Photograph? = null,
    val isAddingPhoto: Boolean = false,
    val adminEditingProject: Project? = null,
    val isAddingProject: Boolean = false,
    val adminEditingJournal: JournalEntry? = null,
    val isAddingJournal: Boolean = false,
    val adminEditingExhibitionIndex: Int? = null,
    val adminEditingExhibition: Exhibition? = null,
    val isAddingExhibition: Boolean = false,
    val adminSnackbarMessage: String? = null,
    val contentUpdateVersion: Long = 0L
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

    // --- Admin CMS Methods ---
    fun selectAdminTab(tab: AdminTab) {
        _uiState.update { it.copy(adminTab = tab) }
    }

    fun startAddPhoto() {
        val newPhoto = Photograph(
            id = "photo_${System.currentTimeMillis() % 100000}",
            title = "",
            bengaliTitle = "",
            location = "Dhaka, Bangladesh",
            year = "2026",
            category = PhotoCategory.RIVER,
            orientation = com.example.data.models.PhotoOrientation.LANDSCAPE,
            caption = "",
            story = "",
            mood = com.example.data.models.VisualMood.RIVER_DAWN,
            exif = com.example.data.models.CameraExif(
                camera = "Leica M11-P",
                lens = "Summilux-M 35mm f/1.4",
                aperture = "f/2.8",
                shutter = "1/250s",
                iso = "ISO 100",
                focalLength = "35mm"
            ),
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=600&q=80"
        )
        _uiState.update {
            it.copy(
                adminEditingPhoto = newPhoto,
                isAddingPhoto = true
            )
        }
    }

    fun startEditPhoto(photo: Photograph) {
        _uiState.update {
            it.copy(
                adminEditingPhoto = photo,
                isAddingPhoto = false
            )
        }
    }

    fun openAdminEditFromLightbox() {
        val photo = _uiState.value.lightboxPhoto ?: return
        _uiState.update {
            it.copy(
                activeSection = NavigationSection.ADMIN,
                adminTab = AdminTab.PHOTOS,
                adminEditingPhoto = photo,
                isAddingPhoto = false,
                lightboxPhoto = null
            )
        }
    }

    fun cancelEditPhoto() {
        _uiState.update {
            it.copy(
                adminEditingPhoto = null,
                isAddingPhoto = false
            )
        }
    }

    fun savePhoto(photo: Photograph) {
        if (_uiState.value.isAddingPhoto) {
            PortfolioRepository.addPhotograph(photo)
            showAdminSnackbar("Added photograph \"${photo.title.ifBlank { "Untitled" }}\"")
        } else {
            PortfolioRepository.updatePhotograph(photo)
            showAdminSnackbar("Updated photograph \"${photo.title.ifBlank { "Untitled" }}\"")
        }
        _uiState.update {
            it.copy(
                adminEditingPhoto = null,
                isAddingPhoto = false,
                contentUpdateVersion = System.currentTimeMillis()
            )
        }
    }

    fun deletePhoto(photoId: String) {
        PortfolioRepository.deletePhotograph(photoId)
        showAdminSnackbar("Deleted photograph from archive")
        _uiState.update {
            it.copy(
                adminEditingPhoto = null,
                isAddingPhoto = false,
                contentUpdateVersion = System.currentTimeMillis()
            )
        }
    }

    // Projects Admin
    fun startAddProject() {
        val newProj = Project(
            id = "proj_${System.currentTimeMillis() % 100000}",
            number = String.format("%02d", PortfolioRepository.projects.size + 1),
            title = "",
            bengaliTitle = "",
            subtitle = "",
            location = "Bangladesh",
            year = "2026",
            photoCount = 10,
            coverPhotoId = PortfolioRepository.photographs.firstOrNull()?.id ?: "photo_01",
            description = "",
            essayText = "",
            photoIds = PortfolioRepository.photographs.take(4).map { it.id },
            quote = ""
        )
        _uiState.update {
            it.copy(
                adminEditingProject = newProj,
                isAddingProject = true
            )
        }
    }

    fun startEditProject(project: Project) {
        _uiState.update {
            it.copy(
                adminEditingProject = project,
                isAddingProject = false
            )
        }
    }

    fun cancelEditProject() {
        _uiState.update {
            it.copy(
                adminEditingProject = null,
                isAddingProject = false
            )
        }
    }

    fun saveProject(project: Project) {
        if (_uiState.value.isAddingProject) {
            PortfolioRepository.addProject(project)
            showAdminSnackbar("Added series \"${project.title.ifBlank { "Untitled" }}\"")
        } else {
            PortfolioRepository.updateProject(project)
            showAdminSnackbar("Updated series \"${project.title.ifBlank { "Untitled" }}\"")
        }
        _uiState.update {
            it.copy(
                adminEditingProject = null,
                isAddingProject = false,
                contentUpdateVersion = System.currentTimeMillis()
            )
        }
    }

    fun deleteProject(projectId: String) {
        PortfolioRepository.deleteProject(projectId)
        showAdminSnackbar("Removed project series")
        _uiState.update {
            it.copy(
                adminEditingProject = null,
                isAddingProject = false,
                contentUpdateVersion = System.currentTimeMillis()
            )
        }
    }

    // Journal Admin
    fun startAddJournal() {
        val newJournal = JournalEntry(
            id = "journal_${System.currentTimeMillis() % 100000}",
            title = "",
            bengaliTitle = "",
            date = "MARCH 2026",
            readTime = "5 MIN READ",
            location = "Field Diary",
            excerpt = "",
            content = "",
            coverPhotoId = PortfolioRepository.photographs.firstOrNull()?.id ?: "photo_01"
        )
        _uiState.update {
            it.copy(
                adminEditingJournal = newJournal,
                isAddingJournal = true
            )
        }
    }

    fun startEditJournal(journal: JournalEntry) {
        _uiState.update {
            it.copy(
                adminEditingJournal = journal,
                isAddingJournal = false
            )
        }
    }

    fun cancelEditJournal() {
        _uiState.update {
            it.copy(
                adminEditingJournal = null,
                isAddingJournal = false
            )
        }
    }

    fun saveJournal(journal: JournalEntry) {
        if (_uiState.value.isAddingJournal) {
            PortfolioRepository.addJournalEntry(journal)
            showAdminSnackbar("Published article \"${journal.title.ifBlank { "Untitled" }}\"")
        } else {
            PortfolioRepository.updateJournalEntry(journal)
            showAdminSnackbar("Updated article \"${journal.title.ifBlank { "Untitled" }}\"")
        }
        _uiState.update {
            it.copy(
                adminEditingJournal = null,
                isAddingJournal = false,
                contentUpdateVersion = System.currentTimeMillis()
            )
        }
    }

    fun deleteJournal(journalId: String) {
        PortfolioRepository.deleteJournalEntry(journalId)
        showAdminSnackbar("Removed journal entry")
        _uiState.update {
            it.copy(
                adminEditingJournal = null,
                isAddingJournal = false,
                contentUpdateVersion = System.currentTimeMillis()
            )
        }
    }

    // Exhibition Admin
    fun startAddExhibition() {
        val newExhibition = com.example.data.models.Exhibition(
            year = "2026",
            title = "",
            venue = "",
            location = "Dhaka, Bangladesh",
            type = "Solo Exhibition"
        )
        _uiState.update {
            it.copy(
                adminEditingExhibition = newExhibition,
                adminEditingExhibitionIndex = null,
                isAddingExhibition = true
            )
        }
    }

    fun startEditExhibition(index: Int, exhibition: com.example.data.models.Exhibition) {
        _uiState.update {
            it.copy(
                adminEditingExhibition = exhibition,
                adminEditingExhibitionIndex = index,
                isAddingExhibition = false
            )
        }
    }

    fun cancelEditExhibition() {
        _uiState.update {
            it.copy(
                adminEditingExhibition = null,
                adminEditingExhibitionIndex = null,
                isAddingExhibition = false
            )
        }
    }

    fun saveExhibition(exhibition: com.example.data.models.Exhibition) {
        val index = _uiState.value.adminEditingExhibitionIndex
        if (index != null && !_uiState.value.isAddingExhibition) {
            PortfolioRepository.updateExhibition(index, exhibition)
            showAdminSnackbar("Updated exhibition \"${exhibition.title}\"")
        } else {
            PortfolioRepository.addExhibition(exhibition)
            showAdminSnackbar("Added exhibition \"${exhibition.title}\"")
        }
        _uiState.update {
            it.copy(
                adminEditingExhibition = null,
                adminEditingExhibitionIndex = null,
                isAddingExhibition = false,
                contentUpdateVersion = System.currentTimeMillis()
            )
        }
    }

    fun deleteExhibition(index: Int) {
        PortfolioRepository.deleteExhibition(index)
        showAdminSnackbar("Removed exhibition entry")
        _uiState.update {
            it.copy(
                adminEditingExhibition = null,
                adminEditingExhibitionIndex = null,
                isAddingExhibition = false,
                contentUpdateVersion = System.currentTimeMillis()
            )
        }
    }

    fun resetCuratedArchive() {
        PortfolioRepository.resetToDefaults()
        showAdminSnackbar("Archive restored to default curated gallery")
        _uiState.update {
            it.copy(contentUpdateVersion = System.currentTimeMillis())
        }
    }

    fun appendEditorialPresets() {
        val presets = PortfolioRepository.photoPresets
        val randomPreset = presets.random()
        val photo = Photograph(
            id = "photo_${System.currentTimeMillis() % 100000}",
            title = randomPreset.first,
            bengaliTitle = "নতুন চিত্রপট",
            location = "Bangladesh Delta",
            year = "2026",
            category = PhotoCategory.RIVER,
            orientation = com.example.data.models.PhotoOrientation.LANDSCAPE,
            caption = "Documentary frame captured along the waterways.",
            story = "Natural light reflecting off the Bengal waterways at dawn, preserving the fleeting visual heritage.",
            mood = com.example.data.models.VisualMood.RIVER_DAWN,
            exif = com.example.data.models.CameraExif(
                camera = "Leica M11-P",
                lens = "Summilux 35mm f/1.4",
                aperture = "f/2.0",
                shutter = "1/500s",
                iso = "ISO 100",
                focalLength = "35mm"
            ),
            isCuratedFeatured = true,
            imageUrl = randomPreset.second,
            thumbUrl = randomPreset.second
        )
        PortfolioRepository.addPhotograph(photo)
        showAdminSnackbar("Added sample photo \"${photo.title}\" to archive")
        _uiState.update { it.copy(contentUpdateVersion = System.currentTimeMillis()) }
    }

    fun showAdminSnackbar(msg: String) {
        _uiState.update { it.copy(adminSnackbarMessage = msg) }
        viewModelScope.launch {
            delay(3000)
            _uiState.update { if (it.adminSnackbarMessage == msg) it.copy(adminSnackbarMessage = null) else it }
        }
    }

    fun dismissAdminSnackbar() {
        _uiState.update { it.copy(adminSnackbarMessage = null) }
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

    fun setSortOrder(order: PhotoSortOrder) {
        _uiState.update { it.copy(sortOrder = order) }
    }

    fun reshuffleRandom() {
        _uiState.update {
            it.copy(
                sortOrder = PhotoSortOrder.RANDOM_SHUFFLE,
                randomSeed = System.currentTimeMillis()
            )
        }
    }

    fun pickRandomPhotograph() {
        val currentId = _uiState.value.lightboxPhoto?.id
        val randomPhoto = PortfolioRepository.getRandomPhotograph(excludeId = currentId)
        openLightbox(randomPhoto)
    }

    fun pickRandomTheme() {
        val nonAllCategories = PhotoCategory.values().filter { it != PhotoCategory.ALL }
        val randomCategory = nonAllCategories.random()
        selectCategory(randomCategory)
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun resetFilters() {
        _uiState.update {
            it.copy(
                selectedCategory = PhotoCategory.ALL,
                sortOrder = PhotoSortOrder.CURATED,
                searchQuery = ""
            )
        }
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
        val state = _uiState.value
        var list = PortfolioRepository.photographs

        // Filter by Theme / Category
        if (state.selectedCategory != PhotoCategory.ALL) {
            list = list.filter { it.category == state.selectedCategory }
        }

        // Filter by Search Query
        if (state.searchQuery.isNotBlank()) {
            val q = state.searchQuery.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.bengaliTitle.contains(q) ||
                it.location.lowercase().contains(q) ||
                it.caption.lowercase().contains(q) ||
                it.category.label.lowercase().contains(q) ||
                it.exif.camera.lowercase().contains(q) ||
                it.exif.lens.lowercase().contains(q)
            }
        }

        // Apply Sorting
        return when (state.sortOrder) {
            PhotoSortOrder.CURATED -> list
            PhotoSortOrder.RANDOM_SHUFFLE -> list.shuffled(kotlin.random.Random(state.randomSeed))
            PhotoSortOrder.YEAR_DESC -> list.sortedByDescending { it.year }
            PhotoSortOrder.YEAR_ASC -> list.sortedBy { it.year }
            PhotoSortOrder.TITLE_AZ -> list.sortedBy { it.title }
            PhotoSortOrder.LOCATION -> list.sortedBy { it.location }
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
