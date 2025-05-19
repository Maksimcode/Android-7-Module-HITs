package com.example.android_7_module_hits.ui.uiblocks
import com.example.android_7_module_hits.Blocks.BlockType

val availableBlocks = listOf(
    BlockTemplate("Declare", BlockType.DECLARE),
    BlockTemplate("Assignment", BlockType.ASSIGN),
    BlockTemplate("Condition", BlockType.CONDITION),
    BlockTemplate("Additional condition", BlockType.ELSE_IF),
    BlockTemplate("Else", BlockType.ELSE),
    BlockTemplate("End", BlockType.END)
)

data class BlockTemplate(val title: String, val type: BlockType)