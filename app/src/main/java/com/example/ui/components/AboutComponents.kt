package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AboutData
import com.example.data.models.AboutProjectLink
import com.example.data.models.JourneyItem
import com.example.data.models.Photograph
import com.example.data.models.PracticeItem
import com.example.ui.viewmodel.NavigationSection

// Editorial Dark Monochromatic Palette
val EditorialBg = Color(0xFF090909)
val EditorialCardBg = Color(0xFF100F0E)
val EditorialForeground = Color(0xFFF1EFE9)
val EditorialMuted = Color(0xFF8D8D8D)
val EditorialSubtleText = Color(0xFF6B6862)
val EditorialBorder = Color(0x24FFFFFF)
val EditorialBorderHover = Color(0x40FFFFFF)

/**
 * 1. Hero / About Introduction: Asymmetric editorial composition with generous whitespace
 */
@Composable
fun AboutHero(
    aboutData: AboutData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 36.dp)
    ) {
        // Tagline / Archive identity
        Text(
            text = "PORTFOLIO & ARTIST ARCHIVE",
            fontFamily = FontFamily.SansSerif,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 3.sp,
            color = EditorialMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Large Editorial Serif Heading
        Text(
            text = aboutData.heroHeading,
            fontFamily = FontFamily.Serif,
            fontSize = 44.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = 2.5.sp,
            color = EditorialForeground
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Artist Statement Intro
        Text(
            text = aboutData.shortIntro,
            fontFamily = FontFamily.Serif,
            fontSize = 18.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Normal,
            color = EditorialForeground
        )

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = EditorialBorder, thickness = 0.5.dp)
    }
}

/**
 * 2. Large Photographic Portrait / Feature Image with responsive aspect ratio & subtle hover
 */
@Composable
fun AboutPortrait(
    photograph: Photograph?,
    aboutData: AboutData,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onPhotoClick: (Photograph) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val imageScale by animateFloatAsState(
        targetValue = if (isHovered) 1.03f else 1.0f,
        animationSpec = tween(durationMillis = 600),
        label = "portraitScale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        if (photograph != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.95f)
                    .clip(RoundedCornerShape(2.dp))
                    .border(0.5.dp, EditorialBorder, RoundedCornerShape(2.dp))
                    .background(EditorialCardBg)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onPhotoClick(photograph) }
                    .testTag("about_portrait_photo")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(imageScale)
                ) {
                    PhotographicArtwork(
                        photograph = photograph,
                        isMonochrome = isMonochrome,
                        showFilmGrain = showFilmGrain,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${aboutData.brandTitle} • ${photograph.location.uppercase()}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    letterSpacing = 1.6.sp,
                    color = EditorialMuted
                )
                Text(
                    text = photograph.exif.camera,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.5.sp,
                    letterSpacing = 1.2.sp,
                    color = EditorialSubtleText
                )
            }
        }
    }
}

/**
 * 3. Artist Introduction: Concise narrative & deltaic context
 */
@Composable
fun ArtistIntroduction(
    aboutData: AboutData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = aboutData.bioParagraph1,
            fontFamily = FontFamily.Serif,
            fontSize = 15.5.sp,
            lineHeight = 26.sp,
            color = EditorialForeground
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = aboutData.bioParagraph2,
            fontFamily = FontFamily.SansSerif,
            fontSize = 13.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal,
            color = EditorialMuted
        )

        Spacer(modifier = Modifier.height(28.dp))
        HorizontalDivider(color = EditorialBorder, thickness = 0.5.dp)
    }
}

/**
 * 4. Large Typographic Artist Statement: Immense vertical negative space
 */
@Composable
fun ArtistStatement(
    statement: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        Text(
            text = "PHILOSOPHY",
            fontFamily = FontFamily.SansSerif,
            fontSize = 9.sp,
            letterSpacing = 3.sp,
            color = EditorialSubtleText
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "\"$statement\"",
            fontFamily = FontFamily.Serif,
            fontSize = 24.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            letterSpacing = 1.sp,
            color = EditorialForeground
        )

        Spacer(modifier = Modifier.height(36.dp))
        HorizontalDivider(color = EditorialBorder, thickness = 0.5.dp)
    }
}

/**
 * 5. Practice Section: Numbered horizontal list with smooth hover & navigation
 */
@Composable
fun PracticeList(
    practices: List<PracticeItem>,
    onSelectPractice: (PracticeItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Text(
            text = "PRACTICE",
            fontFamily = FontFamily.SansSerif,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 3.sp,
            color = EditorialMuted
        )

        Spacer(modifier = Modifier.height(18.dp))

        practices.forEach { practice ->
            val interactionSource = remember { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onSelectPractice(practice) }
                    .padding(vertical = 12.dp)
                    .testTag("practice_item_${practice.index}"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = practice.index,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                        color = if (isHovered) EditorialForeground else EditorialSubtleText,
                        modifier = Modifier.width(36.dp)
                    )
                    Column {
                        Text(
                            text = practice.title,
                            fontFamily = FontFamily.Serif,
                            fontSize = 16.sp,
                            letterSpacing = 1.5.sp,
                            fontWeight = if (isHovered) FontWeight.Medium else FontWeight.Normal,
                            color = if (isHovered) EditorialForeground else EditorialForeground.copy(alpha = 0.9f)
                        )
                        Text(
                            text = practice.description,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.sp,
                            color = EditorialSubtleText
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = if (isHovered) EditorialForeground else EditorialSubtleText,
                    modifier = Modifier.size(16.dp)
                )
            }
            HorizontalDivider(color = EditorialBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
        }
    }
}

/**
 * 6. Location / Identity Metadata: Small uppercase labels & generous spacing
 */
@Composable
fun LocationMetadata(
    metadataList: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        metadataList.forEach { (label, value) ->
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = label,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp,
                    color = EditorialSubtleText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.5.sp,
                    letterSpacing = 0.5.sp,
                    color = EditorialForeground
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
        HorizontalDivider(color = EditorialBorder, thickness = 0.5.dp)
    }
}

/**
 * 7. Secondary Wide Image Break: A cinematic landscape visual pause
 */
@Composable
fun AboutImageBreak(
    photograph: Photograph?,
    isMonochrome: Boolean,
    showFilmGrain: Boolean,
    onPhotoClick: (Photograph) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        if (photograph != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.85f)
                    .clip(RoundedCornerShape(2.dp))
                    .border(0.5.dp, EditorialBorder, RoundedCornerShape(2.dp))
                    .background(EditorialCardBg)
                    .clickable { onPhotoClick(photograph) }
                    .testTag("about_image_break")
            ) {
                PhotographicArtwork(
                    photograph = photograph,
                    isMonochrome = isMonochrome,
                    showFilmGrain = showFilmGrain,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${photograph.title.uppercase()} • ${photograph.year}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.5.sp,
                    letterSpacing = 1.4.sp,
                    color = EditorialMuted
                )
                Text(
                    text = photograph.caption,
                    fontFamily = FontFamily.Serif,
                    fontSize = 9.sp,
                    fontStyle = FontStyle.Italic,
                    color = EditorialSubtleText
                )
            }
        }
    }
}

/**
 * 8. Personal Philosophy: Large typography, off-center placement
 */
@Composable
fun PersonalPhilosophy(
    philosophy: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 36.dp)
    ) {
        Text(
            text = "PERSPECTIVE",
            fontFamily = FontFamily.SansSerif,
            fontSize = 9.sp,
            letterSpacing = 3.sp,
            color = EditorialSubtleText
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "\"$philosophy\"",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Light,
            color = EditorialForeground.copy(alpha = 0.95f)
        )

        Spacer(modifier = Modifier.height(28.dp))
        HorizontalDivider(color = EditorialBorder, thickness = 0.5.dp)
    }
}

/**
 * 9. Journey Timeline: Minimal chronological record with thin separators
 */
@Composable
fun JourneyTimeline(
    journeyItems: List<JourneyItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Text(
            text = "JOURNEY",
            fontFamily = FontFamily.SansSerif,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 3.sp,
            color = EditorialMuted
        )

        Spacer(modifier = Modifier.height(18.dp))

        journeyItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = item.year,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    letterSpacing = 1.5.sp,
                    color = EditorialForeground,
                    modifier = Modifier.width(60.dp)
                )
                Text(
                    text = item.title,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.5.sp,
                    lineHeight = 20.sp,
                    color = EditorialMuted,
                    modifier = Modifier.weight(1f)
                )
            }
            HorizontalDivider(color = EditorialBorder.copy(alpha = 0.4f), thickness = 0.5.dp)
        }
    }
}

/**
 * 10. Selected Projects: Large editorial links with subtle underline & arrow
 */
@Composable
fun SelectedProjects(
    projects: List<AboutProjectLink>,
    onSelectProject: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        Text(
            text = "SELECTED PROJECTS",
            fontFamily = FontFamily.SansSerif,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 3.sp,
            color = EditorialMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        projects.forEach { project ->
            val interactionSource = remember { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onSelectProject(project.id) }
                    .padding(vertical = 14.dp)
                    .testTag("about_project_${project.id}"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        fontFamily = FontFamily.Serif,
                        fontSize = 19.sp,
                        letterSpacing = 1.2.sp,
                        fontWeight = if (isHovered) FontWeight.Medium else FontWeight.Normal,
                        color = if (isHovered) Color.White else EditorialForeground
                    )
                    Text(
                        text = "${project.subtitle} • ${project.year}",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 11.sp,
                        color = EditorialSubtleText
                    )
                }

                Icon(
                    imageVector = Icons.Default.NorthEast,
                    contentDescription = null,
                    tint = if (isHovered) EditorialForeground else EditorialSubtleText,
                    modifier = Modifier.size(17.dp)
                )
            }
            HorizontalDivider(color = EditorialBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
        }
    }
}

/**
 * 11. Contact CTA: Spacious closing section with direct email link
 */
@Composable
fun ContactCTA(
    email: String,
    onNavigateToContact: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        Text(
            text = "FOR PROJECTS,\nCOLLABORATIONS\nAND STORIES.",
            fontFamily = FontFamily.Serif,
            fontSize = 26.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = 1.5.sp,
            color = EditorialForeground
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "EMAIL",
            fontFamily = FontFamily.SansSerif,
            fontSize = 9.sp,
            letterSpacing = 2.5.sp,
            color = EditorialSubtleText
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = email,
            fontFamily = FontFamily.Monospace,
            fontSize = 13.5.sp,
            letterSpacing = 1.sp,
            color = EditorialForeground,
            modifier = Modifier
                .clickable {
                    try { uriHandler.openUri("mailto:$email") } catch (_: Exception) {}
                }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .clickable { onNavigateToContact() }
                .padding(vertical = 8.dp)
                .testTag("about_contact_cta")
        ) {
            Text(
                text = "GET IN TOUCH",
                fontFamily = FontFamily.SansSerif,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp,
                color = EditorialForeground
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = EditorialForeground,
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.height(36.dp))
        HorizontalDivider(color = EditorialBorder, thickness = 0.5.dp)
    }
}

/**
 * 12. Minimal Editorial Exhibition Colophon & Footer
 */
@Composable
fun EditorialFooter(
    onBackToTop: () -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(EditorialBg)
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "© 2026 KHONCHITRO",
                fontFamily = FontFamily.SansSerif,
                fontSize = 9.sp,
                letterSpacing = 2.sp,
                color = EditorialMuted
            )

            Text(
                text = "BANGLADESH",
                fontFamily = FontFamily.SansSerif,
                fontSize = 9.sp,
                letterSpacing = 2.sp,
                color = EditorialSubtleText
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onBackToTop() }
                    .padding(vertical = 4.dp, horizontal = 6.dp)
            ) {
                Text(
                    text = "TOP ↑",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp,
                    color = EditorialForeground
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
        HorizontalDivider(color = EditorialBorder.copy(alpha = 0.4f), thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "INSTAGRAM",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 8.5.sp,
                    letterSpacing = 1.8.sp,
                    color = EditorialSubtleText,
                    modifier = Modifier.clickable {
                        try { uriHandler.openUri("https://instagram.com") } catch (_: Exception) {}
                    }
                )
                Text(
                    text = "EMAIL",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 8.5.sp,
                    letterSpacing = 1.8.sp,
                    color = EditorialSubtleText,
                    modifier = Modifier.clickable {
                        try { uriHandler.openUri("mailto:ijubayer1071@gmail.com") } catch (_: Exception) {}
                    }
                )
            }

            Text(
                text = "FINE ART ARCHIVE",
                fontFamily = FontFamily.Monospace,
                fontSize = 8.sp,
                letterSpacing = 1.4.sp,
                color = EditorialSubtleText
            )
        }
    }
}
