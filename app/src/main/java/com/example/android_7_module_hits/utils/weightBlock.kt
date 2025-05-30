package com.example.android_7_module_hits.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.DeclarationBlock
import com.example.android_7_module_hits.blocks.ForBlock
import com.example.android_7_module_hits.blocks.FunsBlock

fun weightBlock(block: Block): Dp {
    return when (block) {
        is DeclarationBlock, is FunsBlock, is ForBlock -> 100.dp
        else -> 50.dp
    }
}