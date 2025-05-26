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