package com.example.android_7_module_hits.Blocks

import androidx.compose.ui.geometry.Offset

interface Block {
    val id: String
    val type: BlockType
    val content: BlockContent
    var position: Offset

    var parent: Block?
    val children: List<Block>

    fun canAttachTo(other: Block): Boolean

    fun attachTo(other: Block)

    fun detach()
}

