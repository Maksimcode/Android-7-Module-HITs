package com.example.android_7_module_hits.saving

import com.example.android_7_module_hits.blocks.BlockType
import kotlinx.serialization.Serializable

@Serializable
data class BlockState(
    val id: String,
    val type: BlockType,
    val content: BlockContentState,
    val positionX: Float,
    val positionY: Float,
    val parentId: String? = null,
    val childId: String? = null,
    val rootBlockId: String? = null,
    val endBlockId: String? = null
)