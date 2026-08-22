package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.NavigationSection

/**
 * Minimal Full-Screen Overlay Navigation for Mobile devices,
 * following the 'Khonchitro' branding and monochromatic gallery white theme.
 */
@Composable
fun MobileMenuOverlay(
    isOpen: Boolean,
    activeSection: NavigationSection,
    onNavigate: (NavigationSection) -> Unit,
    onClose: () -> Unit,
    savedCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    AnimatedVisibility(
        visible = isOpen,
        enter = fadeIn(tween(250)) + slideInVertically(tween(350)) { -it / 4 },
        exit = fadeOut(tween(200)) + slideOutVertically(tween(300)) { -it / 4 },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GoblinBg)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 28.dp, vertical = 18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header: Khonchitro Branding + Minimal Close Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                                .background(Color(0xFF141414)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "K",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "KHONCHITRO",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                letterSpacing = 2.2.sp,
                                color = GoblinTextPrimary
                            )
                            Text(
                                text = "ক্ষণচিত্র • PHOTOGRAPHY ARCHIVE",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 8.5.sp,
                                letterSpacing = 1.4.sp,
                                color = GoblinTextTertiary
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("close_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Menu",
                            tint = GoblinTextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Middle: Editorial Navigation Links
                Column(
                    verticalArrangement = Arrangement.spacedBy(22.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    val navItems = listOf(
                        NavigationSection.WORK to "01",
                        NavigationSection.PROJECTS to "02",
                        NavigationSection.JOURNAL to "03",
                        NavigationSection.ABOUT to "04",
                        NavigationSection.CONTACT to "05",
                        NavigationSection.CURATION to "06",
                        NavigationSection.ADMIN to "07"
                    )

                    navItems.forEach { (section, indexStr) ->
                        val isSelected = activeSection == section
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    onNavigate(section)
                                    onClose()
                                }
                                .padding(vertical = 6.dp)
                                .testTag("nav_item_${section.routeKey}"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = indexStr,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.5.sp,
                                    color = if (isSelected) GoblinTextPrimary else GoblinTextTertiary,
                                    modifier = Modifier.width(36.dp)
                                )
                                Text(
                                    text = section.label,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 28.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    letterSpacing = 2.sp,
                                    color = if (isSelected) GoblinTextPrimary else GoblinTextSecondary
                                )
                                if (section == NavigationSection.CURATION && savedCount > 0) {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "[$savedCount]",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 14.sp,
                                        color = GoblinTextTertiary
                                    )
                                }
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(GoblinTextPrimary)
                                )
                            }
                        }
                    }
                }

                // Bottom: Social Links, Coordinates & Spec info
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Social Channels Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SOCIAL CHANNELS",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.5.sp,
                            letterSpacing = 1.4.sp,
                            color = GoblinTextTertiary
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SocialLineIconButton(
                                title = "Facebook",
                                onClick = {
                                    try { uriHandler.openUri("https://facebook.com") } catch (_: Exception) {}
                                }
                            ) {
                                FacebookLineIcon(color = GoblinTextSecondary)
                            }

                            SocialLineIconButton(
                                title = "Instagram",
                                onClick = {
                                    try { uriHandler.openUri("https://instagram.com") } catch (_: Exception) {}
                                }
                            ) {
                                InstagramLineIcon(color = GoblinTextSecondary)
                            }

                            SocialLineIconButton(
                                title = "Pexels",
                                onClick = {
                                    try { uriHandler.openUri("https://www.pexels.com") } catch (_: Exception) {}
                                }
                            ) {
                                PexelsLineIcon(color = GoblinTextSecondary)
                            }

                            SocialLineIconButton(
                                title = "Mail",
                                onClick = {
                                    try { uriHandler.openUri("mailto:ijubayer1071@gmail.com") } catch (_: Exception) {}
                                }
                            ) {
                                MailLineIcon(color = GoblinTextSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "DHAKA, BANGLADESH",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.5.sp,
                                letterSpacing = 1.5.sp,
                                color = GoblinTextSecondary
                            )
                            Text(
                                text = "23.8103° N • 90.4125° E",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.5.sp,
                                letterSpacing = 1.2.sp,
                                color = GoblinTextTertiary
                            )
                        }

                        Text(
                            text = "LEICA M • MONOCHROM",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.5.sp,
                            letterSpacing = 1.5.sp,
                            color = GoblinTextTertiary
                        )
                    }
                }
            }
        }
    }
}
