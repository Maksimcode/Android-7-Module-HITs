package com.example.android_7_module_hits.notifications

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

sealed class UiNotification {
    abstract val message: String
    abstract val backgroundColor: Color
    abstract val textColor: Color

    @Composable
    abstract fun Render()
}