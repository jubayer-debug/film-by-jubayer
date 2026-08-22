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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoblinAccentWarm
import com.example.ui.theme.GoblinBg
import com.example.ui.theme.GoblinBgSecondary
import com.example.ui.theme.GoblinBorderFocused
import com.example.ui.theme.GoblinBorderSubtle
import com.example.ui.theme.GoblinTextPrimary
import com.example.ui.theme.GoblinTextSecondary
import com.example.ui.theme.GoblinTextTertiary
import com.example.ui.viewmodel.ContactFormState
import com.example.ui.viewmodel.NavigationSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    formState: ContactFormState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onProjectTypeChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onResetSuccess: () -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val projectTypes = listOf(
        "Editorial Assignment",
        "Documentary Commission",
        "Fine Art Print Acquisition",
        "Exhibition Inquiry",
        "General Conversation"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("contact_screen_lazy_column")
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "COMMISSIONS & DIALOGUE",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    letterSpacing = 3.sp,
                    color = GoblinAccentWarm
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "LET'S WORK\nTOGETHER.",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Light,
                    fontSize = 36.sp,
                    lineHeight = 44.sp,
                    letterSpacing = 1.sp,
                    color = GoblinTextPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "For editorial work, photography projects, creative collaborations and selected fine art commissions.",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.5.sp,
                    lineHeight = 22.sp,
                    color = GoblinTextSecondary
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        // Contact Form Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                if (formState.isSuccess) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .border(1.dp, GoblinAccentWarm, RoundedCornerShape(4.dp))
                            .background(GoblinBgSecondary)
                            .padding(24.dp)
                            .testTag("contact_success_banner")
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = GoblinAccentWarm,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "MESSAGE RECEIVED",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                letterSpacing = 2.sp,
                                color = GoblinTextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Thank you for reaching out. Jubayer will review your project details and respond via email within 24 hours.",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp,
                                color = GoblinTextSecondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "SEND ANOTHER INQUIRY",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.5.sp,
                                color = GoblinAccentWarm,
                                modifier = Modifier.clickable { onResetSuccess() }
                            )
                        }
                    }
                } else {
                    Text(
                        text = "PROJECT INQUIRY FORM",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp,
                        letterSpacing = 2.sp,
                        color = GoblinAccentWarm
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Project Type Selector
                    Text(
                        text = "INQUIRY TYPE",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 9.5.sp,
                        letterSpacing = 1.5.sp,
                        color = GoblinTextTertiary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        projectTypes.forEach { type ->
                            val isSelected = formState.projectType == type
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(
                                        width = if (isSelected) 1.dp else 0.5.dp,
                                        color = if (isSelected) GoblinAccentWarm else GoblinBorderSubtle,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .background(if (isSelected) Color(0x15967246) else Color(0xFFF7F7F6))
                                    .clickable { onProjectTypeChange(type) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = type,
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 10.sp,
                                    color = if (isSelected) GoblinAccentWarm else GoblinTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Name Field
                    OutlinedTextField(
                        value = formState.name,
                        onValueChange = onNameChange,
                        label = { Text("Your Name", fontSize = 12.sp, color = GoblinTextTertiary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoblinBorderFocused,
                            unfocusedBorderColor = GoblinBorderSubtle,
                            focusedTextColor = GoblinTextPrimary,
                            unfocusedTextColor = GoblinTextPrimary,
                            focusedContainerColor = GoblinBgSecondary,
                            unfocusedContainerColor = GoblinBgSecondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_name_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Email Field
                    OutlinedTextField(
                        value = formState.email,
                        onValueChange = onEmailChange,
                        label = { Text("Email Address", fontSize = 12.sp, color = GoblinTextTertiary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoblinBorderFocused,
                            unfocusedBorderColor = GoblinBorderSubtle,
                            focusedTextColor = GoblinTextPrimary,
                            unfocusedTextColor = GoblinTextPrimary,
                            focusedContainerColor = GoblinBgSecondary,
                            unfocusedContainerColor = GoblinBgSecondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_email_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Message Field
                    OutlinedTextField(
                        value = formState.message,
                        onValueChange = onMessageChange,
                        label = { Text("Message & Project Description", fontSize = 12.sp, color = GoblinTextTertiary) },
                        minLines = 4,
                        maxLines = 6,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoblinBorderFocused,
                            unfocusedBorderColor = GoblinBorderSubtle,
                            focusedTextColor = GoblinTextPrimary,
                            unfocusedTextColor = GoblinTextPrimary,
                            focusedContainerColor = GoblinBgSecondary,
                            unfocusedContainerColor = GoblinBgSecondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_message_input")
                    )

                    if (formState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = formState.errorMessage,
                            color = Color(0xFFCC3333),
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Clean Minimal Submit Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .border(1.dp, GoblinBorderFocused, RoundedCornerShape(4.dp))
                            .background(Color(0xFF141414))
                            .clickable(enabled = !formState.isSubmitting) { onSubmit() }
                            .padding(vertical = 14.dp)
                            .testTag("contact_submit_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (formState.isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "SEND INQUIRY →",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 2.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))
                HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
            }
        }

        // Direct Channels & Location
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "DIRECT CHANNELS",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.5.sp,
                    letterSpacing = 2.sp,
                    color = GoblinAccentWarm
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = GoblinAccentWarm,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "EMAIL", fontSize = 9.sp, letterSpacing = 1.2.sp, color = GoblinTextTertiary)
                        Text(text = "hello@filmbyjubayer.com", fontSize = 13.sp, color = GoblinTextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = GoblinAccentWarm,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "STUDIO ARCHIVE", fontSize = 9.sp, letterSpacing = 1.2.sp, color = GoblinTextTertiary)
                        Text(text = "Dhanmondi, Dhaka 1205, Bangladesh", fontSize = 13.sp, color = GoblinTextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    Text(text = "SOCIAL / INSTAGRAM", fontSize = 9.sp, letterSpacing = 1.2.sp, color = GoblinTextTertiary)
                    Text(text = "@filmbyjubayer", fontSize = 13.sp, color = GoblinTextPrimary)
                }
            }
        }

        item {
            FooterSection(onBackToTop = {}, onNavigate = onNavigate)
        }
    }
}
