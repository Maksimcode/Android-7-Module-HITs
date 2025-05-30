package com.example.android_7_module_hits.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)


// Header
val headerTitleStyle = TextStyle(
    color = MainTextColor,
    fontSize = 48.sp
)
val headerTextStyle = TextStyle(
    fontSize = 24.sp,
    color = LibrarySubTitle
)


// EmptyStateContent
val EmptyTextContentStyle = TextStyle(
    fontWeight = FontWeight(400),
    fontSize = 20.sp,
    color = MainTextColor
)


// Notifications
val NotificationTitleStyle = TextStyle(
    fontSize = 20.sp,
    lineHeight = 28.sp,
    fontWeight = FontWeight(700),
)
val NotificationTextStyle = TextStyle(
    fontSize = 18.sp,
    lineHeight = 24.sp,
    fontWeight = FontWeight(400),
)

// ProjectSaveBlock
val SaveBlockDateStyle = TextStyle(
    color = ProjectSaveBlockContentColor,
    fontSize = 20.sp,
    fontWeight = FontWeight(300)
)
val SaveBlockTitleStyle = TextStyle(
    color = ProjectSaveBlockContentColor,
    fontSize = 28.sp,
    fontWeight = FontWeight(600)
)

// TabButton
val TabButtonTextStyle = TextStyle(
    fontWeight = FontWeight(400),
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp,
)