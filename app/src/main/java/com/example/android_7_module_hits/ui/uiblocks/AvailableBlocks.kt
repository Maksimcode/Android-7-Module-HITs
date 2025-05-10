package com.example.android_7_module_hits.ui.uiblocks
import com.example.android_7_module_hits.Blocks.BlockType

val availableBlocks = listOf(
    BlockTemplate("Declare", BlockType.DECLARE),
    BlockTemplate("Assignment", BlockType.ASSIGN)
)

data class BlockTemplate(val title: String, val type: BlockType)