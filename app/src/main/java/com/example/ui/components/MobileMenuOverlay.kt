package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.NavigationSection

@Composable
fun MobileMenuOverlay(
    isOpen: Boolean,
    activeSection: NavigationSection,
    onNavigate: (NavigationSection) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isOpen,
        enter = fadeIn(tween(300)) + slideInVertically(tween(400)) { -it / 3 },
        exit = fadeOut(tween(250)) + slideOutVertically(tween(350)) { -it / 3 },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GoblinBg)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 28.dp, vertical = 20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header in menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GOBLIN",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = 3.sp,
                            color = GoblinTextPrimary
                        )
                        Text(
                            text = "PORTFOLIO ARCHIVE",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 9.sp,
                            letterSpacing = 2.sp,
                            color = GoblinTextTertiary
                        )
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("close_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Menu",
                            tint = GoblinTextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Navigation Items List
                Column(
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    val navItems = listOf(
                        NavigationSection.WORK to "01",
                        NavigationSection.PROJECTS to "02",
                        NavigationSection.JOURNAL to "03",
                        NavigationSection.ABOUT to "04",
                        NavigationSection.CONTACT to "05",
                        NavigationSection.CURATION to "06"
                    )

                    navItems.forEach { (section, indexStr) ->
                        val isSelected = activeSection == section
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onNavigate(section)
                                }
                                .padding(vertical = 4.dp)
                                .testTag("nav_item_${section.routeKey}"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = indexStr,
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 12.sp,
                                    letterSpacing = 1.sp,
                                    color = if (isSelected) GoblinAccentWarm else GoblinTextTertiary,
                                    modifier = Modifier.padding(end = 16.dp)
                                )
                                Text(
                                    text = section.label,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 32.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Light,
                                    letterSpacing = 2.sp,
                                    color = if (isSelected) GoblinAccentWarm else GoblinTextPrimary
                                )
                            }

                            if (isSelected) {
                                Text(
                                    text = "CURRENT",
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 9.sp,
                                    letterSpacing = 1.5.sp,
                                    color = GoblinAccentWarm
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)

                Spacer(modifier = Modifier.height(20.dp))

                // Footer inside overlay
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "DHAKA, BANGLADESH",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 10.sp,
                            letterSpacing = 1.5.sp,
                            color = GoblinTextSecondary
                        )
                        Text(
                            text = "© 2026 GOBLIN ARCHIVE",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 9.sp,
                            letterSpacing = 1.2.sp,
                            color = GoblinTextTertiary
                        )
                    }

                    Text(
                        text = "LEICA M • DNG",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinAccentWarm
                    )
                }
            }
        }
    }
}
