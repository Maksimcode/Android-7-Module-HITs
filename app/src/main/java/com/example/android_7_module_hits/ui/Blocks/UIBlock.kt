package com.example.android_7_module_hits.ui.Blocks

import androidx.compose.ui.geometry.Offset
import com.example.android_7_module_hits.data.model.BlockType

data class UiBlock(
    val id: String,
    var content: String,
    var position: Offset,
    val type: BlockType,
    var editableFields: MutableMap<String, Any>,
    var nextBlockId: String? = null
)

enum class UiBlockType {
    DECLARE_VARIABLE, ASSIGN_VALUE
}