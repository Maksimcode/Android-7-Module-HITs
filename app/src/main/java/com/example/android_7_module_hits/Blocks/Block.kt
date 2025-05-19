package com.example.android_7_module_hits.Blocks

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewmodel.compose.viewModel

interface Block {
    val id: String
    val type: BlockType
    val content: BlockContent
    var position: Offset

    var parent: Block?
    var child: Block?

    var rootBlock: Block?
    var EndBlock: Block?

    fun canAttachTo(other: Block): Boolean

    fun hasEndBlock(): Boolean = when (this) {
        is BlockHasBody -> this.EndBlock != null
        else -> false
    }

    fun hasRootBlock(): Boolean = when (this) {
        is EndBlock -> this.rootBlock != null
        else -> false
    }
}

