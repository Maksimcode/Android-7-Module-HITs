package com.example.android_7_module_hits.ui.uiblocks
import com.example.android_7_module_hits.blocks.BlockType

val availableBlocks = listOf(
    BlockTemplate("Declare", BlockType.DECLARE),
    BlockTemplate("Assignment", BlockType.ASSIGN),
    BlockTemplate("Condition", BlockType.CONDITION),
    BlockTemplate("Additional condition", BlockType.ELSE_IF),
    BlockTemplate("Else", BlockType.ELSE),
    BlockTemplate("While", BlockType.WHILE),
    BlockTemplate("End", BlockType.END),
    BlockTemplate("For", BlockType.FOR)
)

data class BlockTemplate(val title: String, val type: BlockType)