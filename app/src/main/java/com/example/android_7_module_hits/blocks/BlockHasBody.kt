package com.example.android_7_module_hits.blocks

import androidx.compose.ui.geometry.Offset
import java.util.UUID


abstract class BlockHasBody(
    override val id: String = UUID.randomUUID().toString(),
    override val type: BlockType,
    override val content: BlockContent,
    override var position: Offset = Offset.Zero
) : Block {
    override var parent: Block? = null
    override var child: Block? = null

    override val nestedChildren: MutableList<Block> = mutableListOf()

    override fun canAttachTo(other: Block): Boolean {
        return child == null
    }

    fun canAttachNested(): Boolean {
        return true
    }

    override fun canAcceptNestedChildren(): Boolean {
        return true
    }

}