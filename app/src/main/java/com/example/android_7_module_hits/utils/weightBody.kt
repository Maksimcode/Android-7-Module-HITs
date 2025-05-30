package com.example.android_7_module_hits.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android_7_module_hits.blocks.Block
import kotlin.collections.forEach

fun weightBody(body: MutableList<Block>): Dp {
    var count = 0.dp
    body.forEach { block ->
        count += weightBlock(block)
    }
    return count
}