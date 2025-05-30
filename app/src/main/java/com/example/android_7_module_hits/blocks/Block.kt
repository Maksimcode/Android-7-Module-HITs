package com.example.android_7_module_hits.blocks

import androidx.compose.ui.geometry.Offset

interface Block {
    val id: String
    val type: BlockType
    val content: BlockContent
    var position: Offset

    var parent: Block?
    var child: Block?

    val nestedChildren: MutableList<Block>

    fun canAttachTo(other: Block): Boolean

    fun canAcceptNestedChildren(): Boolean
}


