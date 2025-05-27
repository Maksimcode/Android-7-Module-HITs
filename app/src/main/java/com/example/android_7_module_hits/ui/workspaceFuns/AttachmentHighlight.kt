package com.example.android_7_module_hits.ui.workspaceFuns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun AttachmentHighlight(position: Offset) {
    Box(
        modifier = Modifier
            .background(Color.Green.copy(alpha = 0.3f))
            .size(width = 200.dp, height = 16.dp)
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt())}
    )
}