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
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.AttachmentHighlightHeight
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.AttachmentHighlightWidth
import kotlin.math.roundToInt

@Composable
fun AttachmentHighlight(position: Offset, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.3f))
            .size(width = AttachmentHighlightWidth, height = AttachmentHighlightHeight)
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
    )
}
