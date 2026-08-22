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
import androidx.compose.foundation.layout.heightIn
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
        enter = fadeIn(tween(280)) + slideInVertically(tween(350)) { -it / 4 },
        exit = fadeOut(tween(220)) + slideOutVertically(tween(300)) { -it / 4 },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0E0D0C))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 26.dp, vertical = 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header: Monogram [K] + Brand & Minimal Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onNavigate(NavigationSection.PHOTOS)
                            onClose()
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .border(0.75.dp, Color(0xFF383530), RoundedCornerShape(3.dp))
                                .background(Color(0xFF181715)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "K",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFFFAF7F0)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "KHONCHITRO",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                letterSpacing = 2.4.sp,
                                color = Color(0xFFFAF7F0)
                            )
                            Text(
                                text = "ক্ষণচিত্র • PHOTOGRAPHY ARCHIVE",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 8.sp,
                                letterSpacing = 1.6.sp,
                                color = Color(0xFF888278)
                            )
                        }
                    }

                    // Minimal Close Button
                    Box(
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onClose() }
                            .padding(4.dp)
                            .testTag("close_menu_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .border(0.75.dp, Color(0xFF383530), RoundedCornerShape(3.dp))
                                .background(Color(0xFF181715), RoundedCornerShape(3.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "CLOSE",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 2.0.sp,
                                color = Color(0xFFFAF7F0)
                            )
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = Color(0xFFFAF7F0),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Middle: Large, Elegant, Vertically Spaced Menu Items
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    val primaryNavItems = listOf(
                        NavigationSection.PHOTOS to ("01" to "Photos"),
                        NavigationSection.ALBUMS to ("02" to "Albums"),
                        NavigationSection.TOP10 to ("03" to "#TOP 10"),
                        NavigationSection.JOURNAL to ("04" to "Journal"),
                        NavigationSection.ABOUT to ("05" to "About")
                    )

                    primaryNavItems.forEach { (section, meta) ->
                        val (indexStr, labelStr) = meta
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
                                .padding(vertical = 8.dp)
                                .testTag("nav_item_${section.routeKey}"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = indexStr,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.8.sp,
                                    color = if (isSelected) Color(0xFFFAF7F0) else Color(0xFF6B655D),
                                    modifier = Modifier.width(36.dp)
                                )
                                Text(
                                    text = labelStr,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 32.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    letterSpacing = 2.sp,
                                    color = if (isSelected) Color(0xFFFAF7F0) else Color(0xFFC8C2B8)
                                )
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFAF7F0))
                                )
                            }
                        }
                    }

                    // Secondary Quiet Links: Saved Curation & Studio CMS
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (savedCount > 0) "SAVED ARCHIVE ($savedCount)" else "SAVED ARCHIVE",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 10.5.sp,
                            letterSpacing = 2.0.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (activeSection == NavigationSection.CURATION) Color(0xFFFAF7F0) else Color(0xFF7A746B),
                            modifier = Modifier
                                .clickable {
                                    onNavigate(NavigationSection.CURATION)
                                    onClose()
                                }
                                .padding(vertical = 6.dp)
                        )

                        Text(
                            text = "•",
                            fontSize = 10.sp,
                            color = Color(0xFF4A453E)
                        )

                        Text(
                            text = "CONTACT",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 10.5.sp,
                            letterSpacing = 2.0.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (activeSection == NavigationSection.CONTACT) Color(0xFFFAF7F0) else Color(0xFF7A746B),
                            modifier = Modifier
                                .clickable {
                                    onNavigate(NavigationSection.CONTACT)
                                    onClose()
                                }
                                .padding(vertical = 6.dp)
                        )

                        Text(
                            text = "•",
                            fontSize = 10.sp,
                            color = Color(0xFF4A453E)
                        )

                        Text(
                            text = "STUDIO CMS",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 10.5.sp,
                            letterSpacing = 2.0.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (activeSection == NavigationSection.ADMIN) Color(0xFFFAF7F0) else Color(0xFF7A746B),
                            modifier = Modifier
                                .clickable {
                                    onNavigate(NavigationSection.ADMIN)
                                    onClose()
                                }
                                .padding(vertical = 6.dp)
                        )
                    }
                }

                // Bottom: Social Channels & Archival Metadata
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(color = Color(0xFF262420), thickness = 0.5.dp)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Social Channels Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CONNECT",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            letterSpacing = 1.6.sp,
                            color = Color(0xFF7A746B)
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
                                FacebookLineIcon(color = Color(0xFFB5AFA4))
                            }

                            SocialLineIconButton(
                                title = "Instagram",
                                onClick = {
                                    try { uriHandler.openUri("https://instagram.com") } catch (_: Exception) {}
                                }
                            ) {
                                InstagramLineIcon(color = Color(0xFFB5AFA4))
                            }

                            SocialLineIconButton(
                                title = "Pexels",
                                onClick = {
                                    try { uriHandler.openUri("https://www.pexels.com") } catch (_: Exception) {}
                                }
                            ) {
                                PexelsLineIcon(color = Color(0xFFB5AFA4))
                            }

                            SocialLineIconButton(
                                title = "Mail",
                                onClick = {
                                    try { uriHandler.openUri("mailto:ijubayer1071@gmail.com") } catch (_: Exception) {}
                                }
                            ) {
                                MailLineIcon(color = Color(0xFFB5AFA4))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DHAKA • 23.8° N",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            letterSpacing = 1.4.sp,
                            color = Color(0xFF7A746B)
                        )

                        Text(
                            text = "LEICA M • MONOCHROM",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            letterSpacing = 1.4.sp,
                            color = Color(0xFF7A746B)
                        )
                    }
                }
            }
        }
    }
}
