package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

enum class BudgetCurrency(val symbol: String, val code: String, val rateToBdt: Double) {
    BDT("৳", "BDT", 1.0),
    USD("$", "USD", 120.0)
}

enum class CommissionTier(
    val title: String,
    val subtitle: String,
    val baseBdtMin: Int,
    val baseBdtMax: Int,
    val typicalDeliverable: String
) {
    EDITORIAL(
        title = "Editorial Assignment",
        subtitle = "Single or multi-day editorial photojournalism & portraiture",
        baseBdtMin = 35000,
        baseBdtMax = 65000,
        typicalDeliverable = "15-25 Curated High-Res Color Graded Master TIFFs + Full Editorial Press Rights"
    ),
    DOCUMENTARY(
        title = "Documentary Commission",
        subtitle = "In-depth regional story, cultural monograph or NGO investigation",
        baseBdtMin = 95000,
        baseBdtMax = 180000,
        typicalDeliverable = "50+ Archive Grade Images + Comprehensive Field Essay + Curatorial Caption Guide"
    ),
    FINE_ART_PRINTS(
        title = "Fine Art Print Acquisition",
        subtitle = "Museum-grade limited silver gelatin & cotton rag editions",
        baseBdtMin = 22000,
        baseBdtMax = 55000,
        typicalDeliverable = "Signed & Numbered Certificate of Authenticity + Archival Mylar Sleeve"
    ),
    COMMERCIAL(
        title = "Commercial & Brand Story",
        subtitle = "Visual branding, architectural atmosphere & campaigns",
        baseBdtMin = 120000,
        baseBdtMax = 250000,
        typicalDeliverable = "Global Multi-Platform Usage Rights + Color Matched RAW Delivery + Fast Turnaround"
    ),
    EXHIBITION_LOAN(
        title = "Exhibition Loan / Curatorial",
        subtitle = "Curatorial licensing, museum prints & retrospective loan",
        baseBdtMin = 45000,
        baseBdtMax = 90000,
        typicalDeliverable = "Framed Exhibition Loan + Institutional Catalog License + Wall Text Rights"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    formState: ContactFormState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onProjectTypeChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onApplyBudget: (inquiryType: String, budgetText: String, scopeDescription: String) -> Unit = { _, _, _ -> },
    onSubmit: () -> Unit,
    onResetSuccess: () -> Unit,
    onNavigate: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val projectTypes = listOf(
        "Editorial Assignment",
        "Documentary Commission",
        "Fine Art Print Acquisition",
        "Commercial & Brand Story",
        "Exhibition Loan / Curatorial",
        "General Conversation"
    )

    // Budget Calculator Local State
    var selectedCurrency by remember { mutableStateOf(BudgetCurrency.BDT) }
    var selectedTier by remember { mutableStateOf(CommissionTier.EDITORIAL) }
    var durationDaysIndex by remember { mutableIntStateOf(1) } // 0=1 day, 1=2-3 days, 2=1 week, 3=2+ weeks
    var isWorldwideLicensing by remember { mutableStateOf(false) }
    var includesArchivalFraming by remember { mutableStateOf(false) }
    var printSizeIndex by remember { mutableIntStateOf(1) } // 0=A3+, 1=20x30", 2=30x45" Master
    var printQuantity by remember { mutableIntStateOf(1) }
    var appliedNotification by remember { mutableStateOf<String?>(null) }

    // Calculate budget values
    val calculatedMinBdt: Int
    val calculatedMaxBdt: Int
    val scopeSummaryText: String

    when (selectedTier) {
        CommissionTier.FINE_ART_PRINTS -> {
            val sizeMultiplier = when (printSizeIndex) {
                0 -> 1.0
                1 -> 1.7
                else -> 2.8
            }
            val framingAdd = if (includesArchivalFraming) 9000 * printQuantity else 0
            val baseMin = (selectedTier.baseBdtMin * sizeMultiplier * printQuantity).toInt() + framingAdd
            val baseMax = (selectedTier.baseBdtMax * sizeMultiplier * printQuantity).toInt() + framingAdd
            calculatedMinBdt = baseMin
            calculatedMaxBdt = baseMax

            val sizeLabel = when (printSizeIndex) {
                0 -> "A3+ (13x19\") Archival Pigment"
                1 -> "20x30\" Limited Edition of 10"
                else -> "30x45\" Masterpiece Collector Edition of 3"
            }
            val frameLabel = if (includesArchivalFraming) "with Museum UV Acrylic & Matting" else "Unframed (Archival Sleeve)"
            scopeSummaryText = "Acquisition of $printQuantity x $sizeLabel ($frameLabel)"
        }
        else -> {
            val durationMultiplier = when (durationDaysIndex) {
                0 -> 1.0
                1 -> 1.8
                2 -> 3.2
                else -> 5.5
            }
            val licensingMultiplier = if (isWorldwideLicensing) 1.5 else 1.0
            calculatedMinBdt = (selectedTier.baseBdtMin * durationMultiplier * licensingMultiplier).toInt()
            calculatedMaxBdt = (selectedTier.baseBdtMax * durationMultiplier * licensingMultiplier).toInt()

            val durationLabel = when (durationDaysIndex) {
                0 -> "1-Day Field Production"
                1 -> "2-3 Days Field Production"
                2 -> "1-Week In-Depth Production"
                else -> "Multi-Week Field Documentary"
            }
            val licenseLabel = if (isWorldwideLicensing) "Global Full Commercial Rights" else "Standard Editorial / Regional Rights"
            scopeSummaryText = "$durationLabel • $licenseLabel • ${selectedTier.typicalDeliverable}"
        }
    }

    val formattedMin: String
    val formattedMax: String
    if (selectedCurrency == BudgetCurrency.BDT) {
        formattedMin = "৳${String.format("%,d", calculatedMinBdt)}"
        formattedMax = "৳${String.format("%,d", calculatedMaxBdt)} BDT"
    } else {
        val minUsd = (calculatedMinBdt / selectedCurrency.rateToBdt).toInt()
        val maxUsd = (calculatedMaxBdt / selectedCurrency.rateToBdt).toInt()
        formattedMin = "$${String.format("%,d", minUsd)}"
        formattedMax = "$${String.format("%,d", maxUsd)} USD"
    }
    val budgetRangeFormatted = "$formattedMin – $formattedMax"

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(GoblinBg)
            .statusBarsPadding()
            .testTag("contact_screen_root")
    ) {
        val isDesktop = maxWidth >= 760.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("contact_screen_lazy_column")
        ) {
            // 1. HERO HEADER SECTION
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = if (isDesktop) 20.dp else 12.dp, vertical = 28.dp)
                ) {
                    Text(
                        text = "COMMISSIONS, PRINTS & DIALOGUE",
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
                        fontSize = if (isDesktop) 44.sp else 34.sp,
                        lineHeight = if (isDesktop) 52.sp else 42.sp,
                        letterSpacing = 1.sp,
                        color = GoblinTextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "For editorial assignments, documentary series, visual storytelling commissions, and museum-grade fine art acquisitions across Bangladesh and worldwide.",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 13.5.sp,
                        lineHeight = 22.sp,
                        color = GoblinTextSecondary,
                        modifier = Modifier.fillMaxWidth(if (isDesktop) 0.7f else 1f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
                }
            }

            // 2. MAIN CONTENT (Interactive Budget Calculator + Inquiry Form)
            item {
                if (isDesktop) {
                    // DESKTOP: 2-Column Responsive Layout
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Left Column: Interactive Budget Estimator
                        Box(modifier = Modifier.weight(1.1f)) {
                            BudgetCalculatorCard(
                                selectedCurrency = selectedCurrency,
                                selectedTier = selectedTier,
                                durationDaysIndex = durationDaysIndex,
                                isWorldwideLicensing = isWorldwideLicensing,
                                includesArchivalFraming = includesArchivalFraming,
                                printSizeIndex = printSizeIndex,
                                printQuantity = printQuantity,
                                budgetRangeFormatted = budgetRangeFormatted,
                                scopeSummaryText = scopeSummaryText,
                                appliedNotification = appliedNotification,
                                onSelectCurrency = { selectedCurrency = it },
                                onSelectTier = {
                                    selectedTier = it
                                    onProjectTypeChange(it.title)
                                },
                                onSelectDuration = { durationDaysIndex = it },
                                onToggleLicensing = { isWorldwideLicensing = !isWorldwideLicensing },
                                onToggleFraming = { includesArchivalFraming = !includesArchivalFraming },
                                onSelectPrintSize = { printSizeIndex = it },
                                onSelectPrintQuantity = { printQuantity = it },
                                onApplyBudget = {
                                    onApplyBudget(selectedTier.title, budgetRangeFormatted, scopeSummaryText)
                                    appliedNotification = "Budget estimate applied to inquiry form below!"
                                }
                            )
                        }

                        // Right Column: Direct Contact Form
                        Box(modifier = Modifier.weight(0.9f)) {
                            ContactFormCard(
                                formState = formState,
                                projectTypes = projectTypes,
                                onNameChange = onNameChange,
                                onEmailChange = onEmailChange,
                                onProjectTypeChange = onProjectTypeChange,
                                onMessageChange = onMessageChange,
                                onSubmit = onSubmit,
                                onResetSuccess = onResetSuccess
                            )
                        }
                    }
                } else {
                    // MOBILE: Stacked Responsive Layout
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(28.dp)
                    ) {
                        BudgetCalculatorCard(
                            selectedCurrency = selectedCurrency,
                            selectedTier = selectedTier,
                            durationDaysIndex = durationDaysIndex,
                            isWorldwideLicensing = isWorldwideLicensing,
                            includesArchivalFraming = includesArchivalFraming,
                            printSizeIndex = printSizeIndex,
                            printQuantity = printQuantity,
                            budgetRangeFormatted = budgetRangeFormatted,
                            scopeSummaryText = scopeSummaryText,
                            appliedNotification = appliedNotification,
                            onSelectCurrency = { selectedCurrency = it },
                            onSelectTier = {
                                selectedTier = it
                                onProjectTypeChange(it.title)
                            },
                            onSelectDuration = { durationDaysIndex = it },
                            onToggleLicensing = { isWorldwideLicensing = !isWorldwideLicensing },
                            onToggleFraming = { includesArchivalFraming = !includesArchivalFraming },
                            onSelectPrintSize = { printSizeIndex = it },
                            onSelectPrintQuantity = { printQuantity = it },
                            onApplyBudget = {
                                onApplyBudget(selectedTier.title, budgetRangeFormatted, scopeSummaryText)
                                appliedNotification = "Budget estimate applied to inquiry form below!"
                            }
                        )

                        ContactFormCard(
                            formState = formState,
                            projectTypes = projectTypes,
                            onNameChange = onNameChange,
                            onEmailChange = onEmailChange,
                            onProjectTypeChange = onProjectTypeChange,
                            onMessageChange = onMessageChange,
                            onSubmit = onSubmit,
                            onResetSuccess = onResetSuccess
                        )
                    }
                }
            }

            // 3. DIRECT CHANNELS & STUDIO DETAILS
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = if (isDesktop) 20.dp else 12.dp, vertical = 32.dp)
                ) {
                    HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "DIRECT CHANNELS & STUDIO ARCHIVE",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp,
                        letterSpacing = 2.sp,
                        color = GoblinAccentWarm
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isDesktop) Arrangement.spacedBy(48.dp) else Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = GoblinAccentWarm,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "PRIMARY INQUIRIES", fontSize = 9.sp, letterSpacing = 1.2.sp, color = GoblinTextTertiary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "ijubayer1071@gmail.com", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = GoblinTextPrimary)
                            Text(text = "hello@khonchitro.com", fontSize = 12.sp, color = GoblinTextSecondary)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = GoblinAccentWarm,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "STUDIO & ARCHIVE", fontSize = 9.sp, letterSpacing = 1.2.sp, color = GoblinTextTertiary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Dhanmondi, Dhaka 1205", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = GoblinTextPrimary)
                            Text(text = "Bangladesh (UTC +6)", fontSize = 12.sp, color = GoblinTextSecondary)
                        }
                    }
                }
            }

            // 4. FOOTER
            item {
                FooterSection(onBackToTop = {}, onNavigate = onNavigate)
            }
        }
    }
}

/**
 * Interactive Budget & Scope Estimator Component
 */
@Composable
private fun BudgetCalculatorCard(
    selectedCurrency: BudgetCurrency,
    selectedTier: CommissionTier,
    durationDaysIndex: Int,
    isWorldwideLicensing: Boolean,
    includesArchivalFraming: Boolean,
    printSizeIndex: Int,
    printQuantity: Int,
    budgetRangeFormatted: String,
    scopeSummaryText: String,
    appliedNotification: String?,
    onSelectCurrency: (BudgetCurrency) -> Unit,
    onSelectTier: (CommissionTier) -> Unit,
    onSelectDuration: (Int) -> Unit,
    onToggleLicensing: () -> Unit,
    onToggleFraming: () -> Unit,
    onSelectPrintSize: (Int) -> Unit,
    onSelectPrintQuantity: (Int) -> Unit,
    onApplyBudget: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(6.dp))
            .background(Color(0xFFFAFAF9))
            .padding(20.dp)
            .testTag("budget_calculator_card")
    ) {
        // Card Header + Currency Switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = null,
                    tint = GoblinAccentWarm,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "COMMISSION & PRINT BUDGET",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.8.sp,
                        color = GoblinTextPrimary
                    )
                    Text(
                        text = "REAL-TIME INVESTMENT ESTIMATOR",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 8.5.sp,
                        letterSpacing = 1.2.sp,
                        color = GoblinTextTertiary
                    )
                }
            }

            // Currency Switcher Pills
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(2.dp)
            ) {
                BudgetCurrency.entries.forEach { curr ->
                    val isSelected = selectedCurrency == curr
                    Box(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 38.dp, minHeight = 28.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) Color(0xFF141414) else Color.Transparent)
                            .clickable { onSelectCurrency(curr) }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = curr.code,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else GoblinTextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = GoblinBorderSubtle, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(14.dp))

        // Step 1: Select Service / Commission Tier
        Text(
            text = "1. SELECT ENGAGEMENT TYPE",
            fontFamily = FontFamily.SansSerif,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            color = GoblinTextTertiary
        )
        Spacer(modifier = Modifier.height(8.dp))

        CommissionTier.entries.forEach { tier ->
            val isSelected = selectedTier == tier
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) GoblinAccentWarm else GoblinBorderSubtle,
                animationSpec = tween(200),
                label = "tier_border"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(if (isSelected) 1.dp else 0.5.dp, borderColor, RoundedCornerShape(4.dp))
                    .background(if (isSelected) Color(0x12967246) else Color.White)
                    .clickable { onSelectTier(tier) }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tier.title,
                        fontFamily = FontFamily.Serif,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 13.sp,
                        color = GoblinTextPrimary
                    )
                    Text(
                        text = tier.subtitle,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 10.5.sp,
                        color = GoblinTextSecondary
                    )
                }

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(GoblinAccentWarm),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step 2: Tier Parameters
        if (selectedTier == CommissionTier.FINE_ART_PRINTS) {
            // Fine Art Print Options
            Text(
                text = "2. PRINT SIZE & FINISHING",
                fontFamily = FontFamily.SansSerif,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                color = GoblinTextTertiary
            )
            Spacer(modifier = Modifier.height(8.dp))

            val printSizes = listOf(
                "A3+ (13x19\") Studio Edition",
                "20x30\" Limited Edition of 10",
                "30x45\" Master Edition of 3"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                printSizes.forEachIndexed { idx, sizeLabel ->
                    val isSelected = printSizeIndex == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 44.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(if (isSelected) 1.dp else 0.5.dp, if (isSelected) GoblinAccentWarm else GoblinBorderSubtle, RoundedCornerShape(4.dp))
                            .background(if (isSelected) Color(0x18967246) else Color.White)
                            .clickable { onSelectPrintSize(idx) }
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = sizeLabel,
                            fontSize = 9.5.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 13.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) GoblinAccentWarm else GoblinTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Framing Checkbox Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .clickable { onToggleFraming() }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Museum-Grade UV Acrylic & Teak Frame",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = GoblinTextPrimary
                    )
                    Text(
                        text = "Acid-free cotton rag matting + handcrafted minimalist teak frame",
                        fontSize = 9.5.sp,
                        color = GoblinTextTertiary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, if (includesArchivalFraming) GoblinAccentWarm else GoblinBorderSubtle, RoundedCornerShape(4.dp))
                        .background(if (includesArchivalFraming) GoblinAccentWarm else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    if (includesArchivalFraming) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                    }
                }
            }
        } else {
            // Commission Duration & Scope
            Text(
                text = "2. PRODUCTION DURATION",
                fontFamily = FontFamily.SansSerif,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                color = GoblinTextTertiary
            )
            Spacer(modifier = Modifier.height(8.dp))

            val durations = listOf("1 Day", "2–3 Days", "1 Week", "2+ Weeks")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                durations.forEachIndexed { idx, label ->
                    val isSelected = durationDaysIndex == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 44.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(if (isSelected) 1.dp else 0.5.dp, if (isSelected) GoblinAccentWarm else GoblinBorderSubtle, RoundedCornerShape(4.dp))
                            .background(if (isSelected) Color(0x18967246) else Color.White)
                            .clickable { onSelectDuration(idx) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) GoblinAccentWarm else GoblinTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Licensing Upgrade Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .clickable { onToggleLicensing() }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Global Full Commercial Rights & Buyout",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = GoblinTextPrimary
                    )
                    Text(
                        text = "Unrestricted worldwide advertising, print & digital distribution",
                        fontSize = 9.5.sp,
                        color = GoblinTextTertiary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, if (isWorldwideLicensing) GoblinAccentWarm else GoblinBorderSubtle, RoundedCornerShape(4.dp))
                        .background(if (isWorldwideLicensing) GoblinAccentWarm else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    if (isWorldwideLicensing) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ESTIMATED INVESTMENT RESULT BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, GoblinAccentWarm, RoundedCornerShape(4.dp))
                .background(Color(0xFF141414))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ESTIMATED INVESTMENT RANGE",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 8.5.sp,
                        letterSpacing = 1.8.sp,
                        color = GoblinAccentWarm
                    )
                    Text(
                        text = selectedCurrency.code,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.5.sp,
                        color = Color(0xFFAAAAAA)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = budgetRangeFormatted,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    letterSpacing = 1.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = scopeSummaryText,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = Color(0xFFCCCCCC)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action: Apply Budget to Form
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, GoblinAccentWarm, RoundedCornerShape(4.dp))
                .background(Color(0x22967246))
                .clickable { onApplyBudget() }
                .padding(vertical = 12.dp)
                .testTag("apply_budget_button"),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = GoblinAccentWarm,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "APPLY ESTIMATE TO INQUIRY FORM",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = GoblinAccentWarm
                )
            }
        }

        if (appliedNotification != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = appliedNotification,
                fontFamily = FontFamily.SansSerif,
                fontSize = 11.sp,
                color = GoblinAccentWarm,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Clean Minimal Contact Inquiry Form Component
 */
@Composable
private fun ContactFormCard(
    formState: ContactFormState,
    projectTypes: List<String>,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onProjectTypeChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onResetSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .border(0.5.dp, GoblinBorderSubtle, RoundedCornerShape(6.dp))
            .background(Color.White)
            .padding(20.dp)
            .testTag("contact_form_card")
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
                        text = "INQUIRY RECEIVED",
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
                        textAlign = TextAlign.Center,
                        color = GoblinTextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .heightIn(min = 44.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(0.5.dp, GoblinAccentWarm, RoundedCornerShape(16.dp))
                            .clickable { onResetSuccess() }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SEND ANOTHER INQUIRY",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.5.sp,
                            color = GoblinAccentWarm
                        )
                    }
                }
            }
        } else {
            Text(
                text = "PROJECT INQUIRY FORM",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.8.sp,
                color = GoblinTextPrimary
            )
            Text(
                text = "SUBMIT DIRECT COMMISSION DETAILS",
                fontFamily = FontFamily.SansSerif,
                fontSize = 8.5.sp,
                letterSpacing = 1.2.sp,
                color = GoblinTextTertiary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Project Type Selector
            Text(
                text = "INQUIRY CATEGORY",
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
                            .heightIn(min = 40.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                width = if (isSelected) 1.dp else 0.5.dp,
                                color = if (isSelected) GoblinAccentWarm else GoblinBorderSubtle,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .background(if (isSelected) Color(0x15967246) else Color(0xFFF7F7F6))
                            .clickable { onProjectTypeChange(type) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
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

            Spacer(modifier = Modifier.height(14.dp))

            // Name Field
            OutlinedTextField(
                value = formState.name,
                onValueChange = onNameChange,
                label = { Text("Your Name or Organization", fontSize = 12.sp, color = GoblinTextTertiary) },
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

            Spacer(modifier = Modifier.height(12.dp))

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

            Spacer(modifier = Modifier.height(12.dp))

            // Message Field
            OutlinedTextField(
                value = formState.message,
                onValueChange = onMessageChange,
                label = { Text("Project Scope, Timeline & Location", fontSize = 12.sp, color = GoblinTextTertiary) },
                minLines = 4,
                maxLines = 7,
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

            Spacer(modifier = Modifier.height(18.dp))

            // Clean Responsive Submit Button (Minimum 48dp height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
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
                        text = "TRANSMIT INQUIRY →",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
