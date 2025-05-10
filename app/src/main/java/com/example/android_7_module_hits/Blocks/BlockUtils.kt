package com.example.android_7_module_hits.Blocks

import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

fun distanceBetween(a: Offset, b: Offset): Float {
    val dx = a.x - b.x
    val dy = a.y - b.y
    return sqrt(dx * dx + dy * dy)
}