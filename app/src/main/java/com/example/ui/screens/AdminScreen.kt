package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.PortfolioRepository
import com.example.data.models.CameraExif
import com.example.data.models.Exhibition
import com.example.data.models.JournalEntry
import com.example.data.models.PhotoCategory
import com.example.data.models.PhotoOrientation
import com.example.data.models.Photograph
import com.example.data.models.Project
import com.example.data.models.VisualMood
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBgSecondary
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.AdminTab
import com.example.ui.viewmodel.NavigationSection
import com.example.ui.viewmodel.PortfolioUiState
import com.example.ui.viewmodel.PortfolioViewModel

@Composable
fun AdminScreen(
    viewModel: PortfolioViewModel,
    uiState: PortfolioUiState,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val photographs = PortfolioRepository.photographs
    val projects = PortfolioRepository.projects
    val journalEntries = PortfolioRepository.journalEntries
    val exhibitions = PortfolioRepository.exhibitions

    var adminSearchQuery by remember { mutableStateOf("") }
    var adminCategoryFilter by remember { mutableStateOf(PhotoCategory.ALL) }
    var photoPendingDeleteId by remember { mutableStateOf<String?>(null) }
    var projectPendingDeleteId by remember { mutableStateOf<String?>(null) }
    var journalPendingDeleteId by remember { mutableStateOf<String?>(null) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("admin_screen_root")
    ) {
        if (!uiState.isAdminAuthenticated) {
            // ADMIN AUTHENTICATION GATE
            AdminLoginGate(
                defaultEmail = uiState.adminEmail,
                errorMessage = uiState.adminAuthError,
                onLogin = { email, pass -> viewModel.loginAdmin(email, pass) },
                onClearError = { viewModel.clearAdminAuthError() },
                onExit = { onNavigate(NavigationSection.WORK) }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {

                // 1. ADMIN HEADER BAR
                AdminHeaderBar(
                    adminEmail = uiState.adminEmail,
                    onLockSession = { viewModel.lockAdminSession() },
                    onExitAdmin = { onNavigate(NavigationSection.WORK) }
                )

                // 2. ADMIN TABS BAR
                AdminTabsBar(
                    currentTab = uiState.adminTab,
                    onSelectTab = { viewModel.selectAdminTab(it) },
                    photoCount = photographs.size,
                    projectCount = projects.size,
                    journalCount = journalEntries.size
                )

                // 3. TAB CONTENT
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    when (uiState.adminTab) {
                        AdminTab.PHOTOS -> {
                            AdminPhotosTab(
                                photographs = photographs,
                                searchQuery = adminSearchQuery,
                                selectedCategory = adminCategoryFilter,
                                onSearchChange = { adminSearchQuery = it },
                                onCategoryChange = { adminCategoryFilter = it },
                                onAddPhoto = { viewModel.startAddPhoto() },
                                onEditPhoto = { viewModel.startEditPhoto(it) },
                                onDeletePhoto = { photoPendingDeleteId = it.id },
                                onViewPhoto = {
                                    viewModel.openLightbox(it)
                                }
                            )
                        }
                        AdminTab.PROJECTS -> {
                            AdminProjectsTab(
                                projects = projects,
                                onAddProject = { viewModel.startAddProject() },
                                onEditProject = { viewModel.startEditProject(it) },
                                onDeleteProject = { projectPendingDeleteId = it.id }
                            )
                        }
                        AdminTab.JOURNAL -> {
                            AdminJournalTab(
                                journalEntries = journalEntries,
                                onAddJournal = { viewModel.startAddJournal() },
                                onEditJournal = { viewModel.startEditJournal(it) },
                                onDeleteJournal = { journalPendingDeleteId = it.id }
                            )
                        }
                        AdminTab.EXHIBITIONS -> {
                            AdminExhibitionsTab(
                                exhibitions = exhibitions,
                                onAddExhibition = { viewModel.startAddExhibition() },
                                onEditExhibition = { index, ex -> viewModel.startEditExhibition(index, ex) },
                                onDeleteExhibition = { viewModel.deleteExhibition(it) }
                            )
                        }
                        AdminTab.TOOLS -> {
                            AdminToolsTab(
                                adminEmail = uiState.adminEmail,
                                photoCount = photographs.size,
                                projectCount = projects.size,
                                journalCount = journalEntries.size,
                                exhibitionCount = exhibitions.size,
                                onUpdateCredentials = { newEmail, curPass, newPass ->
                                    viewModel.updateAdminCredentials(newEmail, curPass, newPass)
                                },
                                onResetCredentials = { viewModel.resetAdminCredentialsToDefault() },
                                onLockSession = { viewModel.lockAdminSession() },
                                onResetArchive = { showResetConfirmDialog = true },
                                onAddSamplePhoto = { viewModel.appendEditorialPresets() }
                            )
                        }
                    }
                }
            }
        }

        // --- EDIT DIALOGS ---

        // Photo Edit / Add Dialog
        if (uiState.adminEditingPhoto != null) {
            PhotoEditorDialog(
                photo = uiState.adminEditingPhoto,
                isNew = uiState.isAddingPhoto,
                onSave = { updated -> viewModel.savePhoto(updated) },
                onCancel = { viewModel.cancelEditPhoto() },
                onDelete = { photoId -> viewModel.deletePhoto(photoId) }
            )
        }

        // Project Edit / Add Dialog
        if (uiState.adminEditingProject != null) {
            ProjectEditorDialog(
                project = uiState.adminEditingProject,
                isNew = uiState.isAddingProject,
                availablePhotos = photographs,
                onSave = { updated -> viewModel.saveProject(updated) },
                onCancel = { viewModel.cancelEditProject() },
                onDelete = { projId -> viewModel.deleteProject(projId) }
            )
        }

        // Journal Edit / Add Dialog
        if (uiState.adminEditingJournal != null) {
            JournalEditorDialog(
                journal = uiState.adminEditingJournal,
                isNew = uiState.isAddingJournal,
                availablePhotos = photographs,
                onSave = { updated -> viewModel.saveJournal(updated) },
                onCancel = { viewModel.cancelEditJournal() },
                onDelete = { jId -> viewModel.deleteJournal(jId) }
            )
        }

        // Exhibition Edit / Add Dialog
        if (uiState.adminEditingExhibition != null) {
            ExhibitionEditorDialog(
                exhibition = uiState.adminEditingExhibition,
                isNew = uiState.isAddingExhibition,
                onSave = { updated -> viewModel.saveExhibition(updated) },
                onCancel = { viewModel.cancelEditExhibition() }
            )
        }

        // Delete Confirmation for Photo
        if (photoPendingDeleteId != null) {
            AlertDialog(
                onDismissRequest = { photoPendingDeleteId = null },
                title = { Text("Delete Photograph?", fontFamily = FontFamily.Serif) },
                text = { Text("This will permanently remove this image from the portfolio archive.", fontFamily = FontFamily.SansSerif) },
                confirmButton = {
                    Button(
                        onClick = {
                            photoPendingDeleteId?.let { viewModel.deletePhoto(it) }
                            photoPendingDeleteId = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { photoPendingDeleteId = null }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Delete Confirmation for Project
        if (projectPendingDeleteId != null) {
            AlertDialog(
                onDismissRequest = { projectPendingDeleteId = null },
                title = { Text("Delete Project Series?", fontFamily = FontFamily.Serif) },
                text = { Text("This will remove the curated project from the portfolio.", fontFamily = FontFamily.SansSerif) },
                confirmButton = {
                    Button(
                        onClick = {
                            projectPendingDeleteId?.let { viewModel.deleteProject(it) }
                            projectPendingDeleteId = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { projectPendingDeleteId = null }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Delete Confirmation for Journal
        if (journalPendingDeleteId != null) {
            AlertDialog(
                onDismissRequest = { journalPendingDeleteId = null },
                title = { Text("Delete Journal Entry?", fontFamily = FontFamily.Serif) },
                text = { Text("This will remove this article from the field journal.", fontFamily = FontFamily.SansSerif) },
                confirmButton = {
                    Button(
                        onClick = {
                            journalPendingDeleteId?.let { viewModel.deleteJournal(it) }
                            journalPendingDeleteId = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { journalPendingDeleteId = null }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Reset Archive Confirm Dialog
        if (showResetConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showResetConfirmDialog = false },
                title = { Text("Restore Factory Curated Archive?", fontFamily = FontFamily.Serif) },
                text = { Text("This will reset all photographs, projects, journal entries, and exhibitions back to the original curated editorial dataset.", fontFamily = FontFamily.SansSerif) },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.resetCuratedArchive()
                            showResetConfirmDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm)
                    ) {
                        Text("Reset Archive", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetConfirmDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Admin Notification Toast / Snackbar
        if (uiState.adminSnackbarMessage != null) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = Color(0xFF1E1E1E),
                contentColor = Color.White,
                action = {
                    TextButton(onClick = { viewModel.dismissAdminSnackbar() }) {
                        Text("OK", color = GoblinAccentWarm)
                    }
                }
            ) {
                Text(
                    text = uiState.adminSnackbarMessage ?: "",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 1. ADMIN HEADER BAR
// -------------------------------------------------------------
@Composable
private fun AdminHeaderBar(
    adminEmail: String,
    onLockSession: () -> Unit,
    onExitAdmin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF121212))
            .padding(horizontal = 4.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoblinAccentWarm),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Admin CMS",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "CONTENT STUDIO",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 1.5.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF2E7D32))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "AUTH",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = adminEmail,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.5.sp,
                        letterSpacing = 0.5.sp,
                        color = Color(0xFFAAAAAA),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Actions: Lock & Exit
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onLockSession,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF222222))
                        .border(0.5.dp, Color(0xFF444444), CircleShape)
                        .testTag("admin_lock_session_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Session",
                        tint = Color(0xFFFFB74D),
                        modifier = Modifier.size(15.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(0.5.dp, Color(0xFF444444), RoundedCornerShape(16.dp))
                        .background(Color(0xFF222222))
                        .clickable { onExitAdmin() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("admin_exit_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "GALLERY",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 9.5.sp,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. ADMIN TABS BAR
// -------------------------------------------------------------
@Composable
private fun AdminTabsBar(
    currentTab: AdminTab,
    onSelectTab: (AdminTab) -> Unit,
    photoCount: Int,
    projectCount: Int,
    journalCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AdminTab.values().forEach { tab ->
            val isSelected = tab == currentTab
            val countLabel = when (tab) {
                AdminTab.PHOTOS -> " ($photoCount)"
                AdminTab.PROJECTS -> " ($projectCount)"
                AdminTab.JOURNAL -> " ($journalCount)"
                else -> ""
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) GoblinAccentWarm else Color(0xFF2A2A2A))
                    .border(0.5.dp, if (isSelected) GoblinAccentWarm else Color(0xFF3A3A3A), RoundedCornerShape(20.dp))
                    .clickable { onSelectTab(tab) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("admin_tab_${tab.name}"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${tab.iconLabel} ${tab.label}$countLabel",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    letterSpacing = 1.sp,
                    color = if (isSelected) Color.White else Color(0xFFCCCCCC)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 3. PHOTOS TAB
// -------------------------------------------------------------
@Composable
private fun AdminPhotosTab(
    photographs: List<Photograph>,
    searchQuery: String,
    selectedCategory: PhotoCategory,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (PhotoCategory) -> Unit,
    onAddPhoto: () -> Unit,
    onEditPhoto: (Photograph) -> Unit,
    onDeletePhoto: (Photograph) -> Unit,
    onViewPhoto: (Photograph) -> Unit
) {
    val filtered = remember(photographs, searchQuery, selectedCategory) {
        var list = photographs
        if (selectedCategory != PhotoCategory.ALL) {
            list = list.filter { it.category == selectedCategory }
        }
        if (searchQuery.isNotBlank()) {
            val q = searchQuery.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.bengaliTitle.contains(q) ||
                it.location.lowercase().contains(q) ||
                it.year.contains(q)
            }
        }
        list
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            // Action bar: Search + Add Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PHOTOGRAPHY ARCHIVE",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextPrimary
                    )
                    Text(
                        text = "${filtered.size} of ${photographs.size} images displayed",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        color = GoblinTextTertiary
                    )
                }

                // Add Photo Button
                Button(
                    onClick = onAddPhoto,
                    colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("admin_add_photo_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "ADD PHOTO", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search text field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth().testTag("admin_photo_search_field"),
                placeholder = { Text("Search photos by title, location, year...", fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoblinAccentWarm) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoblinAccentWarm,
                    unfocusedBorderColor = GoblinBorderSubtle
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter chips row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(PhotoCategory.values()) { cat ->
                    val isSelected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) GoblinAccentWarm.copy(alpha = 0.15f) else Color.White)
                            .border(
                                1.dp,
                                if (isSelected) GoblinAccentWarm else GoblinBorderSubtle,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { onCategoryChange(cat) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = cat.label,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 9.5.sp,
                            letterSpacing = 1.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) GoblinAccentWarm else GoblinTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
        }

        // List of Photo Cards
        items(filtered, key = { it.id }) { photo ->
            AdminPhotoListItem(
                photo = photo,
                onEdit = { onEditPhoto(photo) },
                onDelete = { onDeletePhoto(photo) },
                onView = { onViewPhoto(photo) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun AdminPhotoListItem(
    photo: Photograph,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onView: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_photo_item_${photo.id}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp, brush = Brush.linearGradient(listOf(GoblinBorderSubtle, GoblinBorderSubtle)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                    .clickable { onView() }
            ) {
                AsyncImage(
                    model = photo.thumbUrl.ifBlank { photo.imageUrl },
                    contentDescription = photo.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = photo.title.ifBlank { "Untitled Photo" },
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = GoblinTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (photo.isCuratedFeatured) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GoblinAccentWarm.copy(alpha = 0.15f))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "FEATURED",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoblinAccentWarm
                            )
                        }
                    }
                }

                if (photo.bengaliTitle.isNotBlank()) {
                    Text(
                        text = photo.bengaliTitle,
                        fontFamily = FontFamily.Serif,
                        fontSize = 11.sp,
                        color = GoblinTextTertiary
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${photo.location} • ${photo.year} • ${photo.category.label}",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    color = GoblinAccentWarm
                )

                Text(
                    text = "${photo.exif.camera} • ${photo.exif.lens}",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 9.sp,
                    color = GoblinTextTertiary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action buttons
            Column(horizontalAlignment = Alignment.End) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F0EE))
                        .testTag("admin_edit_photo_${photo.id}")
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = GoblinTextPrimary, modifier = Modifier.size(16.dp))
                }

                Spacer(modifier = Modifier.height(6.dp))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEBEE))
                        .testTag("admin_delete_photo_${photo.id}")
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. PROJECTS TAB
// -------------------------------------------------------------
@Composable
private fun AdminProjectsTab(
    projects: List<Project>,
    onAddProject: () -> Unit,
    onEditProject: (Project) -> Unit,
    onDeleteProject: (Project) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CURATED SERIES & ESSAYS",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextPrimary
                    )
                    Text(
                        text = "${projects.size} curated project essays published",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        color = GoblinTextTertiary
                    )
                }

                Button(
                    onClick = onAddProject,
                    colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("admin_add_project_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "NEW SERIES", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
        }

        items(projects, key = { it.id }) { project ->
            val coverPhoto = PortfolioRepository.getPhotoById(project.coverPhotoId) ?: PortfolioRepository.photographs.firstOrNull()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_project_item_${project.id}"),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                    ) {
                        if (coverPhoto != null) {
                            AsyncImage(
                                model = coverPhoto.thumbUrl.ifBlank { coverPhoto.imageUrl },
                                contentDescription = project.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${project.number} — ${project.title}",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = GoblinTextPrimary
                        )
                        Text(
                            text = project.subtitle,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.sp,
                            color = GoblinTextSecondary,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${project.location} • ${project.year} • ${project.photoCount} Photos",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 9.5.sp,
                            color = GoblinAccentWarm
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        IconButton(
                            onClick = { onEditProject(project) },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF0F0EE))
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = GoblinTextPrimary, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        IconButton(
                            onClick = { onDeleteProject(project) },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFEBEE))
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828), modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// -------------------------------------------------------------
// 5. JOURNAL TAB
// -------------------------------------------------------------
@Composable
private fun AdminJournalTab(
    journalEntries: List<JournalEntry>,
    onAddJournal: () -> Unit,
    onEditJournal: (JournalEntry) -> Unit,
    onDeleteJournal: (JournalEntry) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "FIELD JOURNAL & ARTICLES",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextPrimary
                    )
                    Text(
                        text = "${journalEntries.size} articles in journal archive",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        color = GoblinTextTertiary
                    )
                }

                Button(
                    onClick = onAddJournal,
                    colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("admin_add_journal_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "NEW ESSAY", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
        }

        items(journalEntries, key = { it.id }) { journal ->
            val coverPhoto = PortfolioRepository.getPhotoById(journal.coverPhotoId) ?: PortfolioRepository.photographs.firstOrNull()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_journal_item_${journal.id}"),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                    ) {
                        if (coverPhoto != null) {
                            AsyncImage(
                                model = coverPhoto.thumbUrl.ifBlank { coverPhoto.imageUrl },
                                contentDescription = journal.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = journal.title,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = GoblinTextPrimary
                        )
                        Text(
                            text = "${journal.date} • ${journal.readTime}",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 10.sp,
                            color = GoblinAccentWarm
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = journal.location,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 9.5.sp,
                            color = GoblinTextTertiary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        IconButton(
                            onClick = { onEditJournal(journal) },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF0F0EE))
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = GoblinTextPrimary, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        IconButton(
                            onClick = { onDeleteJournal(journal) },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFEBEE))
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828), modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// -------------------------------------------------------------
// 6. EXHIBITIONS TAB
// -------------------------------------------------------------
@Composable
private fun AdminExhibitionsTab(
    exhibitions: List<Exhibition>,
    onAddExhibition: () -> Unit,
    onEditExhibition: (Int, Exhibition) -> Unit,
    onDeleteExhibition: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "EXHIBITIONS & ARCHIVES",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextPrimary
                    )
                    Text(
                        text = "History shown on the About screen",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        color = GoblinTextTertiary
                    )
                }

                Button(
                    onClick = onAddExhibition,
                    colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("admin_add_exhibition_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "ADD RECORD", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
        }

        itemsIndexed(exhibitions) { index, ex ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ex.year,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GoblinAccentWarm,
                        modifier = Modifier.width(50.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ex.title,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = GoblinTextPrimary
                        )
                        Text(
                            text = "${ex.venue} • ${ex.location}",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.sp,
                            color = GoblinTextSecondary
                        )
                        Text(
                            text = ex.type,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 9.sp,
                            color = GoblinTextTertiary
                        )
                    }

                    Row {
                        IconButton(
                            onClick = { onEditExhibition(index, ex) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = GoblinTextPrimary, modifier = Modifier.size(16.dp))
                        }
                        IconButton(
                            onClick = { onDeleteExhibition(index) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828), modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// -------------------------------------------------------------
// 7. TOOLS & BACKUP TAB
// -------------------------------------------------------------
@Composable
private fun AdminToolsTab(
    adminEmail: String,
    photoCount: Int,
    projectCount: Int,
    journalCount: Int,
    exhibitionCount: Int,
    onUpdateCredentials: (String, String, String) -> Boolean,
    onResetCredentials: () -> Unit,
    onLockSession: () -> Unit,
    onResetArchive: () -> Unit,
    onAddSamplePhoto: () -> Unit
) {
    var editCredentialsOpen by remember { mutableStateOf(false) }
    var inputEmail by remember(adminEmail) { mutableStateOf(adminEmail) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var credentialFormError by remember { mutableStateOf<String?>(null) }
    var isNewPasswordVisible by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ARCHIVE STATISTICS & ACCESS",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = GoblinAccentWarm
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "SYSTEM CONTROLS",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Light,
                fontSize = 24.sp,
                letterSpacing = 1.sp,
                color = GoblinTextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
        }

        // Stats Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStatCard(title = "PHOTOS", count = photoCount.toString(), modifier = Modifier.weight(1f))
                AdminStatCard(title = "SERIES", count = projectCount.toString(), modifier = Modifier.weight(1f))
                AdminStatCard(title = "ESSAYS", count = journalCount.toString(), modifier = Modifier.weight(1f))
                AdminStatCard(title = "EXHIBITS", count = exhibitionCount.toString(), modifier = Modifier.weight(1f))
            }
        }

        // 1. CURATOR CREDENTIALS & SECURITY CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_security_card"),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(GoblinAccentWarm.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = GoblinAccentWarm,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "ADMIN SECURITY & ACCESS",
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    letterSpacing = 1.2.sp,
                                    color = GoblinTextPrimary
                                )
                                Text(
                                    text = "Master credentials for Film by Jubayer Studio",
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 10.sp,
                                    color = GoblinTextTertiary
                                )
                            }
                        }

                        IconButton(
                            onClick = onLockSession,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF5F5F3))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock Session",
                                tint = GoblinAccentWarm,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Current Admin Account display
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFF9F9F8))
                            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(6.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "CURRENT CURATOR EMAIL",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 9.sp,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoblinTextTertiary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = adminEmail,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GoblinTextPrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF2E7D32).copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Toggle Update Credentials Form
                    if (!editCredentialsOpen) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    editCredentialsOpen = true
                                    inputEmail = adminEmail
                                    currentPassword = ""
                                    newPassword = ""
                                    confirmPassword = ""
                                    credentialFormError = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("admin_change_credentials_btn")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "CHANGE EMAIL / PASSWORD", fontSize = 10.5.sp, letterSpacing = 0.8.sp)
                            }

                            OutlinedButton(
                                onClick = onResetCredentials,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "RESET DEFAULT", fontSize = 10.5.sp)
                            }
                        }
                    } else {
                        // Editable Form
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFAFAFA))
                                .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(6.dp))
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "UPDATE ADMIN CREDENTIALS",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp,
                                letterSpacing = 1.sp,
                                color = GoblinTextPrimary
                            )

                            if (credentialFormError != null) {
                                Text(
                                    text = credentialFormError ?: "",
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 11.sp,
                                    color = Color(0xFFC62828)
                                )
                            }

                            OutlinedTextField(
                                value = inputEmail,
                                onValueChange = {
                                    inputEmail = it
                                    credentialFormError = null
                                },
                                label = { Text("Admin Email Address", fontSize = 11.sp) },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("admin_change_email_field"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoblinAccentWarm,
                                    unfocusedBorderColor = GoblinBorderSubtle
                                )
                            )

                            OutlinedTextField(
                                value = currentPassword,
                                onValueChange = {
                                    currentPassword = it
                                    credentialFormError = null
                                },
                                label = { Text("Current Password", fontSize = 11.sp) },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("admin_current_pass_field"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoblinAccentWarm,
                                    unfocusedBorderColor = GoblinBorderSubtle
                                )
                            )

                            OutlinedTextField(
                                value = newPassword,
                                onValueChange = {
                                    newPassword = it
                                    credentialFormError = null
                                },
                                label = { Text("New Password (min 6 characters)", fontSize = 11.sp) },
                                leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                trailingIcon = {
                                    IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isNewPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Toggle Visibility",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                },
                                visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("admin_new_pass_field"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoblinAccentWarm,
                                    unfocusedBorderColor = GoblinBorderSubtle
                                )
                            )

                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = {
                                    confirmPassword = it
                                    credentialFormError = null
                                },
                                label = { Text("Confirm New Password", fontSize = 11.sp) },
                                leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("admin_confirm_pass_field"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoblinAccentWarm,
                                    unfocusedBorderColor = GoblinBorderSubtle
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { editCredentialsOpen = false }) {
                                    Text("Cancel", color = GoblinTextSecondary)
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Button(
                                    onClick = {
                                        if (currentPassword.isBlank()) {
                                            credentialFormError = "Please enter your current password."
                                            return@Button
                                        }
                                        if (inputEmail.isBlank() || !inputEmail.contains("@")) {
                                            credentialFormError = "Please enter a valid email address."
                                            return@Button
                                        }
                                        if (newPassword.length < 6) {
                                            credentialFormError = "New password must be at least 6 characters."
                                            return@Button
                                        }
                                        if (newPassword != confirmPassword) {
                                            credentialFormError = "New password confirmation does not match."
                                            return@Button
                                        }

                                        val success = onUpdateCredentials(inputEmail, currentPassword, newPassword)
                                        if (success) {
                                            editCredentialsOpen = false
                                        } else {
                                            credentialFormError = "Current password was incorrect."
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.testTag("admin_save_credentials_btn")
                                ) {
                                    Text("SAVE CREDENTIALS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Preset Importer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ONE-CLICK EDITORIAL SAMPLE INJECTION",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinAccentWarm
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Instantly load high-resolution documentary photographs captured across Bangladesh into your live portfolio database.",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = GoblinTextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onAddSamplePhoto,
                        colors = ButtonDefaults.buttonColors(containerColor = GoblinTextPrimary),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.testTag("admin_inject_sample_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "INJECT SAMPLE PHOTOGRAPH", fontSize = 11.sp, letterSpacing = 1.2.sp)
                    }
                }
            }
        }

        // Reset to Curated Factory State
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "RESTORE FACTORY CURATION",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                        color = Color(0xFFC62828)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Revert all photographs, projects, journal entries, and exhibition records back to the master curated setup for Film by Jubayer.",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = GoblinTextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onResetArchive,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.testTag("admin_reset_archive_btn")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color(0xFFC62828), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "RESTORE ARCHIVE DEFAULTS", color = Color(0xFFC62828), fontSize = 11.sp, letterSpacing = 1.2.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun AdminStatCard(title: String, count: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = count,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = GoblinTextPrimary
            )
            Text(
                text = title,
                fontFamily = FontFamily.SansSerif,
                fontSize = 8.5.sp,
                letterSpacing = 1.sp,
                color = GoblinAccentWarm
            )
        }
    }
}

// -------------------------------------------------------------
// 8. PHOTO EDITOR DIALOG
// -------------------------------------------------------------
@Composable
private fun PhotoEditorDialog(
    photo: Photograph,
    isNew: Boolean,
    onSave: (Photograph) -> Unit,
    onCancel: () -> Unit,
    onDelete: (String) -> Unit
) {
    var title by remember { mutableStateOf(photo.title) }
    var bengaliTitle by remember { mutableStateOf(photo.bengaliTitle) }
    var location by remember { mutableStateOf(photo.location) }
    var year by remember { mutableStateOf(photo.year) }
    var imageUrl by remember { mutableStateOf(photo.imageUrl) }
    var thumbUrl by remember { mutableStateOf(photo.thumbUrl) }
    var category by remember { mutableStateOf(photo.category) }
    var orientation by remember { mutableStateOf(photo.orientation) }
    var caption by remember { mutableStateOf(photo.caption) }
    var story by remember { mutableStateOf(photo.story) }
    var isCuratedFeatured by remember { mutableStateOf(photo.isCuratedFeatured) }

    // EXIF
    var camera by remember { mutableStateOf(photo.exif.camera) }
    var lens by remember { mutableStateOf(photo.exif.lens) }
    var aperture by remember { mutableStateOf(photo.exif.aperture) }
    var shutter by remember { mutableStateOf(photo.exif.shutter) }
    var iso by remember { mutableStateOf(photo.exif.iso) }
    var focalLength by remember { mutableStateOf(photo.exif.focalLength) }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GoblinBg)
                .statusBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // Top Dialog Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onCancel) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNew) "ADD NEW PHOTOGRAPH" else "EDIT PHOTOGRAPH",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.5.sp,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            val updated = photo.copy(
                                title = title.trim(),
                                bengaliTitle = bengaliTitle.trim(),
                                location = location.trim(),
                                year = year.trim(),
                                imageUrl = imageUrl.trim(),
                                thumbUrl = if (thumbUrl.isNotBlank()) thumbUrl.trim() else imageUrl.trim(),
                                category = category,
                                orientation = orientation,
                                caption = caption.trim(),
                                story = story.trim(),
                                isCuratedFeatured = isCuratedFeatured,
                                exif = CameraExif(
                                    camera = camera.trim(),
                                    lens = lens.trim(),
                                    aperture = aperture.trim(),
                                    shutter = shutter.trim(),
                                    iso = iso.trim(),
                                    focalLength = focalLength.trim()
                                )
                            )
                            onSave(updated)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.testTag("admin_save_photo_btn")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SAVE PHOTO", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }

                // Scrollable Form Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // 1. Image Preview Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFEFEF)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUrl.isNotBlank()) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Preview",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text("Image Preview will appear here", color = GoblinTextTertiary, fontSize = 12.sp)
                        }
                    }

                    // 2. Preset Quick-Fill Chips
                    Column {
                        Text(
                            text = "QUICK IMAGE PRESETS (1-TAP SELECT)",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 10.sp,
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoblinAccentWarm
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(PortfolioRepository.photoPresets) { preset ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(12.dp))
                                        .clickable {
                                            imageUrl = preset.second
                                            thumbUrl = preset.second
                                            if (title.isBlank()) title = preset.first
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = preset.first,
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 10.sp,
                                        color = GoblinTextPrimary
                                    )
                                }
                            }
                        }
                    }

                    // 3. Image URL Field
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = {
                            imageUrl = it
                            if (thumbUrl.isBlank()) thumbUrl = it
                        },
                        label = { Text("Image URL (Web / Unsplash / Direct link)") },
                        modifier = Modifier.fillMaxWidth().testTag("admin_photo_url_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(6.dp)
                    )

                    // 4. Title & Bengali Title
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title (English)") },
                            modifier = Modifier.weight(1f).testTag("admin_photo_title_input"),
                            shape = RoundedCornerShape(6.dp)
                        )
                        OutlinedTextField(
                            value = bengaliTitle,
                            onValueChange = { bengaliTitle = it },
                            label = { Text("Bengali Title (বাংলা)") },
                            modifier = Modifier.weight(1f).testTag("admin_photo_bengali_title_input"),
                            shape = RoundedCornerShape(6.dp)
                        )
                    }

                    // 5. Location & Year
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location (e.g. Meghna River)") },
                            modifier = Modifier.weight(1.3f).testTag("admin_photo_location_input"),
                            shape = RoundedCornerShape(6.dp)
                        )
                        OutlinedTextField(
                            value = year,
                            onValueChange = { year = it },
                            label = { Text("Year") },
                            modifier = Modifier.weight(0.7f).testTag("admin_photo_year_input"),
                            shape = RoundedCornerShape(6.dp)
                        )
                    }

                    // 6. Category Selector
                    Column {
                        Text("Category / Theme", fontFamily = FontFamily.SansSerif, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoblinTextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(PhotoCategory.values().filter { it != PhotoCategory.ALL }) { cat ->
                                val isSelected = cat == category
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isSelected) GoblinAccentWarm else Color.White)
                                        .border(1.dp, if (isSelected) GoblinAccentWarm else GoblinBorderSubtle, RoundedCornerShape(14.dp))
                                        .clickable { category = cat }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = cat.label,
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else GoblinTextPrimary
                                    )
                                }
                            }
                        }
                    }

                    // 7. Orientation Selector
                    Column {
                        Text("Orientation", fontFamily = FontFamily.SansSerif, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoblinTextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            PhotoOrientation.values().forEach { orient ->
                                val isSelected = orient == orientation
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isSelected) GoblinTextPrimary else Color.White)
                                        .border(1.dp, if (isSelected) GoblinTextPrimary else GoblinBorderSubtle, RoundedCornerShape(14.dp))
                                        .clickable { orientation = orient }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = orient.name,
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 9.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else GoblinTextPrimary
                                    )
                                }
                            }
                        }
                    }

                    // 8. Featured Showcase Switch
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White)
                            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(6.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Featured in Curated Showcase", fontFamily = FontFamily.SansSerif, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text("Highlights this photo on top showcase feeds", fontSize = 10.sp, color = GoblinTextTertiary)
                        }
                        Switch(
                            checked = isCuratedFeatured,
                            onCheckedChange = { isCuratedFeatured = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoblinAccentWarm, checkedTrackColor = GoblinAccentWarm.copy(alpha = 0.3f))
                        )
                    }

                    // 9. Caption
                    OutlinedTextField(
                        value = caption,
                        onValueChange = { caption = it },
                        label = { Text("Caption (1-2 sentences)") },
                        modifier = Modifier.fillMaxWidth().testTag("admin_photo_caption_input"),
                        shape = RoundedCornerShape(6.dp),
                        minLines = 2
                    )

                    // 10. Field Story
                    OutlinedTextField(
                        value = story,
                        onValueChange = { story = it },
                        label = { Text("Field Essay / Story Narrative") },
                        modifier = Modifier.fillMaxWidth().testTag("admin_photo_story_input"),
                        shape = RoundedCornerShape(6.dp),
                        minLines = 3
                    )

                    // 11. Camera EXIF Section
                    Text("Technical Camera EXIF", fontFamily = FontFamily.SansSerif, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoblinAccentWarm)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = camera,
                            onValueChange = { camera = it },
                            label = { Text("Camera") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        OutlinedTextField(
                            value = lens,
                            onValueChange = { lens = it },
                            label = { Text("Lens") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = aperture,
                            onValueChange = { aperture = it },
                            label = { Text("Aperture") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        OutlinedTextField(
                            value = shutter,
                            onValueChange = { shutter = it },
                            label = { Text("Shutter") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        OutlinedTextField(
                            value = iso,
                            onValueChange = { iso = it },
                            label = { Text("ISO") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp)
                        )
                    }

                    // Delete button if existing photo
                    if (!isNew) {
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { onDelete(photo.id) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFC62828), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("DELETE PHOTOGRAPH", color = Color(0xFFC62828), fontSize = 11.sp, letterSpacing = 1.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 9. PROJECT EDITOR DIALOG
// -------------------------------------------------------------
@Composable
private fun ProjectEditorDialog(
    project: Project,
    isNew: Boolean,
    availablePhotos: List<Photograph>,
    onSave: (Project) -> Unit,
    onCancel: () -> Unit,
    onDelete: (String) -> Unit
) {
    var number by remember { mutableStateOf(project.number) }
    var title by remember { mutableStateOf(project.title) }
    var bengaliTitle by remember { mutableStateOf(project.bengaliTitle) }
    var subtitle by remember { mutableStateOf(project.subtitle) }
    var location by remember { mutableStateOf(project.location) }
    var year by remember { mutableStateOf(project.year) }
    var photoCount by remember { mutableStateOf(project.photoCount.toString()) }
    var coverPhotoId by remember { mutableStateOf(project.coverPhotoId) }
    var description by remember { mutableStateOf(project.description) }
    var essayText by remember { mutableStateOf(project.essayText) }
    var quote by remember { mutableStateOf(project.quote) }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GoblinBg)
                .statusBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onCancel) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNew) "ADD NEW SERIES" else "EDIT SERIES",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            val updated = project.copy(
                                number = number.trim(),
                                title = title.trim(),
                                bengaliTitle = bengaliTitle.trim(),
                                subtitle = subtitle.trim(),
                                location = location.trim(),
                                year = year.trim(),
                                photoCount = photoCount.toIntOrNull() ?: 10,
                                coverPhotoId = coverPhotoId,
                                description = description.trim(),
                                essayText = essayText.trim(),
                                quote = quote.trim()
                            )
                            onSave(updated)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SAVE SERIES", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = number,
                            onValueChange = { number = it },
                            label = { Text("Number (e.g. 01)") },
                            modifier = Modifier.weight(0.5f)
                        )
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title (e.g. MONSOON)") },
                            modifier = Modifier.weight(1.5f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = bengaliTitle,
                            onValueChange = { bengaliTitle = it },
                            label = { Text("Bengali Title") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = year,
                            onValueChange = { year = it },
                            label = { Text("Year Range") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = subtitle,
                        onValueChange = { subtitle = it },
                        label = { Text("Subtitle / Theme line") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Cover Photo Selector
                    Column {
                        Text("Select Cover Photo", fontFamily = FontFamily.SansSerif, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoblinTextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(availablePhotos) { photo ->
                                val isSelected = photo.id == coverPhotoId
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .border(if (isSelected) 2.5.dp else 0.5.dp, if (isSelected) GoblinAccentWarm else GoblinBorderSubtle, RoundedCornerShape(4.dp))
                                        .clickable { coverPhotoId = photo.id }
                                ) {
                                    AsyncImage(
                                        model = photo.thumbUrl.ifBlank { photo.imageUrl },
                                        contentDescription = photo.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Short Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    OutlinedTextField(
                        value = essayText,
                        onValueChange = { essayText = it },
                        label = { Text("Full Curatorial Essay") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )

                    OutlinedTextField(
                        value = quote,
                        onValueChange = { quote = it },
                        label = { Text("Pull Quote") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!isNew) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { onDelete(project.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("DELETE PROJECT", color = Color(0xFFC62828), fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 10. JOURNAL EDITOR DIALOG
// -------------------------------------------------------------
@Composable
private fun JournalEditorDialog(
    journal: JournalEntry,
    isNew: Boolean,
    availablePhotos: List<Photograph>,
    onSave: (JournalEntry) -> Unit,
    onCancel: () -> Unit,
    onDelete: (String) -> Unit
) {
    var title by remember { mutableStateOf(journal.title) }
    var bengaliTitle by remember { mutableStateOf(journal.bengaliTitle) }
    var date by remember { mutableStateOf(journal.date) }
    var readTime by remember { mutableStateOf(journal.readTime) }
    var location by remember { mutableStateOf(journal.location) }
    var excerpt by remember { mutableStateOf(journal.excerpt) }
    var content by remember { mutableStateOf(journal.content) }
    var coverPhotoId by remember { mutableStateOf(journal.coverPhotoId) }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GoblinBg)
                .statusBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onCancel) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNew) "NEW FIELD ESSAY" else "EDIT ESSAY",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            val updated = journal.copy(
                                title = title.trim(),
                                bengaliTitle = bengaliTitle.trim(),
                                date = date.trim(),
                                readTime = readTime.trim(),
                                location = location.trim(),
                                excerpt = excerpt.trim(),
                                content = content.trim(),
                                coverPhotoId = coverPhotoId
                            )
                            onSave(updated)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PUBLISH", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Article Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = bengaliTitle,
                            onValueChange = { bengaliTitle = it },
                            label = { Text("Bengali Title") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = readTime,
                            onValueChange = { readTime = it },
                            label = { Text("Read Time (e.g. 5 MIN READ)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = { Text("Date (e.g. MARCH 2026)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Field Location") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Cover selector
                    Column {
                        Text("Cover Photograph", fontFamily = FontFamily.SansSerif, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoblinTextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(availablePhotos) { photo ->
                                val isSelected = photo.id == coverPhotoId
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .border(if (isSelected) 2.5.dp else 0.5.dp, if (isSelected) GoblinAccentWarm else GoblinBorderSubtle, RoundedCornerShape(4.dp))
                                        .clickable { coverPhotoId = photo.id }
                                ) {
                                    AsyncImage(
                                        model = photo.thumbUrl.ifBlank { photo.imageUrl },
                                        contentDescription = photo.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = excerpt,
                        onValueChange = { excerpt = it },
                        label = { Text("Excerpt Summary") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Full Essay Content") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 6
                    )

                    if (!isNew) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { onDelete(journal.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("DELETE ESSAY", color = Color(0xFFC62828), fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 11. EXHIBITION EDITOR DIALOG
// -------------------------------------------------------------
@Composable
private fun ExhibitionEditorDialog(
    exhibition: Exhibition,
    isNew: Boolean,
    onSave: (Exhibition) -> Unit,
    onCancel: () -> Unit
) {
    var year by remember { mutableStateOf(exhibition.year) }
    var title by remember { mutableStateOf(exhibition.title) }
    var venue by remember { mutableStateOf(exhibition.venue) }
    var location by remember { mutableStateOf(exhibition.location) }
    var type by remember { mutableStateOf(exhibition.type) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(if (isNew) "Add Exhibition" else "Edit Exhibition", fontFamily = FontFamily.Serif) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it },
                    label = { Text("Year (e.g. 2026)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Exhibition Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = venue,
                    onValueChange = { venue = it },
                    label = { Text("Venue / Gallery") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location (e.g. Dhaka, Bangladesh)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Type (Solo Exhibition / Group / Featured)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        exhibition.copy(
                            year = year.trim(),
                            title = title.trim(),
                            venue = venue.trim(),
                            location = location.trim(),
                            type = type.trim()
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm)
            ) {
                Text("Save", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}

// -------------------------------------------------------------
// 12. ADMIN LOGIN GATE
// -------------------------------------------------------------
@Composable
private fun AdminLoginGate(
    defaultEmail: String,
    errorMessage: String?,
    onLogin: (String, String) -> Unit,
    onClearError: () -> Unit,
    onExit: () -> Unit
) {
    var email by remember(defaultEmail) { mutableStateOf(defaultEmail) }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0E))
            .testTag("admin_login_gate_root"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Security / Lock Emblem
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(GoblinAccentWarm.copy(alpha = 0.25f), Color(0xFF1E1E1C))))
                    .border(1.dp, GoblinAccentWarm.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Admin Lock",
                    tint = GoblinAccentWarm,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title & Subtitle
            Text(
                text = "KHONCHITRO (ক্ষণচিত্র)",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                letterSpacing = 2.5.sp,
                fontWeight = FontWeight.Bold,
                color = GoblinAccentWarm
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "CURATOR CMS STUDIO",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Light,
                fontSize = 22.sp,
                letterSpacing = 1.5.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "এডমিন কন্ট্রোল প্যানেল • Restricted Access",
                fontFamily = FontFamily.SansSerif,
                fontSize = 11.5.sp,
                letterSpacing = 0.8.sp,
                color = Color(0xFFAAAAAA)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_login_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF181816)),
                border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp, brush = Brush.verticalGradient(listOf(Color(0xFF333330), Color(0xFF20201E))))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "MASTER AUTHENTICATION",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinAccentWarm
                    )

                    // Error Message Display
                    if (errorMessage != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF3E1414))
                                .border(0.5.dp, Color(0xFFC62828), RoundedCornerShape(6.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = errorMessage,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 11.sp,
                                color = Color(0xFFFF8A80)
                            )
                        }
                    }

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            onClearError()
                        },
                        label = { Text("Admin Email", color = Color(0xFF888888), fontSize = 11.sp) },
                        placeholder = { Text("ijubayer1071@gmail.com", color = Color(0xFF555555), fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = GoblinAccentWarm,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            if (email.isNotEmpty()) {
                                IconButton(onClick = { email = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = Color(0xFF666666),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_login_email_field"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color(0xFFEEEEEE),
                            focusedContainerColor = Color(0xFF10100F),
                            unfocusedContainerColor = Color(0xFF10100F),
                            focusedBorderColor = GoblinAccentWarm,
                            unfocusedBorderColor = Color(0xFF333330)
                        )
                    )

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            onClearError()
                        },
                        label = { Text("Master Password", color = Color(0xFF888888), fontSize = 11.sp) },
                        placeholder = { Text("Enter password", color = Color(0xFF555555), fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = GoblinAccentWarm,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Color(0xFF888888),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (email.isNotBlank() && password.isNotBlank()) {
                                    onLogin(email, password)
                                }
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_login_password_field"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color(0xFFEEEEEE),
                            focusedContainerColor = Color(0xFF10100F),
                            unfocusedContainerColor = Color(0xFF10100F),
                            focusedBorderColor = GoblinAccentWarm,
                            unfocusedBorderColor = Color(0xFF333330)
                        )
                    )

                    // Quick-Fill / Curator Credential Hint Pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1F1F1D))
                            .border(0.5.dp, Color(0xFF383834), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "CURATOR ACCOUNT",
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = GoblinAccentWarm
                                )
                                Text(
                                    text = "ijubayer1071@gmail.com",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = Color(0xFFCCCCCC)
                                )
                                Text(
                                    text = "Default Key: jubayer2026",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    color = Color(0xFF888888)
                                )
                            }

                            Button(
                                onClick = {
                                    email = "ijubayer1071@gmail.com"
                                    password = "jubayer2026"
                                    onClearError()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A26)),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.testTag("admin_autofill_credentials_btn")
                            ) {
                                Text(
                                    text = "AUTO-FILL",
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = GoblinAccentWarm
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Submit Sign In Button
                    Button(
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                onLogin(email, password)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoblinAccentWarm),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("admin_login_submit_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LockOpen,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "UNLOCK CONTENT STUDIO",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Exit Back to Portfolio
            TextButton(
                onClick = onExit,
                modifier = Modifier.testTag("admin_login_exit_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color(0xFFAAAAAA),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "RETURN TO PORTFOLIO",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.5.sp,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFFAAAAAA)
                )
            }
        }
    }
}

