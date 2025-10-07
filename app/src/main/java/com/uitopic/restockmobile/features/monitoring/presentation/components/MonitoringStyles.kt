package com.uitopic.restockmobile.features.monitoring.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uitopic.restockmobile.R

val MonitoringBackground = Color(0xFFF3F4F6)
val MonitoringCardBackground = Color(0xFFFFFFFF)
val MonitoringPrimaryText = Color(0xFF1F2A37)
val MonitoringSecondaryText = Color(0xFF6B7280)
val MonitoringMutedText = Color(0xFF94A3B8)
val MonitoringGreen = Color(0xFF0F9D58)
val MonitoringOrange = Color(0xFFF79009)
val MonitoringRed = Color(0xFFD14343)
val MonitoringDark = Color(0xFF111827)
val MonitoringNeutral = Color(0xFF475569)
val MonitoringBlue = Color(0xFF2563EB)
val MonitoringBorder = Color(0xFFE2E8F0)
val MonitoringShadowColor = Color(0x1A111827)

val MonitoringPadding = 24.dp

val MonitoringFontFamily = FontFamily(
    Font(resId = R.font.poppins_regular, weight = FontWeight.Normal),
    Font(resId = R.font.poppins_medium, weight = FontWeight.Medium),
    Font(resId = R.font.poppins_semibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.poppins_bold, weight = FontWeight.Bold)
)

val MonitoringTitleStyle = TextStyle(
    fontFamily = MonitoringFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 24.sp,
    color = MonitoringPrimaryText
)

val MonitoringSubtitleStyle = TextStyle(
    fontFamily = MonitoringFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    color = MonitoringSecondaryText
)

val MonitoringBodyStyle = TextStyle(
    fontFamily = MonitoringFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    color = MonitoringSecondaryText
)

val MonitoringLabelStyle = TextStyle(
    fontFamily = MonitoringFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    color = MonitoringMutedText
)

val MonitoringTableHeaderStyle = TextStyle(
    fontFamily = MonitoringFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 13.sp,
    color = MonitoringMutedText
)

val MonitoringTableCellStyle = TextStyle(
    fontFamily = MonitoringFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    color = MonitoringPrimaryText
)

@Composable
fun MonitoringTypography(body: @Composable () -> Unit) {
    MaterialTheme(
        typography = MaterialTheme.typography.copy(
            headlineSmall = MonitoringTitleStyle,
            titleMedium = MonitoringSubtitleStyle,
            bodyMedium = MonitoringBodyStyle
        ),
        content = body
    )
}

