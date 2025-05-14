package com.example.android_7_module_hits.Blocks

import androidx.compose.ui.geometry.Offset
import com.example.android_7_module_hits.interpreter.interpret
import java.util.UUID


abstract class BlockHasBody(
    override val id: String = UUID.randomUUID().toString(),
    override val type: BlockType,
    override val content: BlockContent,
    override var position: Offset = Offset.Zero
) : Block {
    override var parent: Block? = null
    override var child: Block? = null

    var EndBlock: Block? = null

    override fun canAttachTo(other: Block): Boolean {
        if (child != null) {
            return false
        } else {
            return true
        }
    }

    fun attachToEnd(block: Block) {
        if (block.type == BlockType.END) {
            EndBlock = block;
        } else {
            block.child?.let { attachToEnd(it) }
        }
    }
}