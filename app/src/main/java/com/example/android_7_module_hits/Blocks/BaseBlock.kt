package com.example.android_7_module_hits.Blocks

import androidx.compose.ui.geometry.Offset
import java.util.UUID

abstract class BaseBlock(
    override val id: String = UUID.randomUUID().toString(),
    override val type: BlockType,
    override val content: BlockContent,
    override var position: Offset = Offset.Zero
) : Block {

    override var parent: Block? = null
    private val _children = mutableListOf<Block>()
    override val children: List<Block> get() = _children

    override fun canAttachTo(other: Block): Boolean {

        return when (other.type) {
            BlockType.DECLARE -> this is DeclarationBlock
            else -> false
        }
    }

    override fun attachTo(other: Block) {
        detach()

        if (other is BaseBlock) {
            other._children.add(this)
        } else {
            throw IllegalArgumentException("Unsupported block implementation")
        }

        parent = other
    }

    override fun detach() {
        parent?.let { parentBlock ->
            if (parentBlock is BaseBlock) {
                parentBlock._children.remove(this)
            }
        }
        parent = null
    }
}