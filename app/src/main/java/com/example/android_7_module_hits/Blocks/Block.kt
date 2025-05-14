package com.example.android_7_module_hits.Blocks

import androidx.compose.ui.geometry.Offset

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

    fun attachHasBodyBlock(currentBlock: Block, withBodyBlock: Block){
        if (currentBlock is EndBlock){
            if (withBodyBlock is BlockHasBody && withBodyBlock.EndBlock == null){
                withBodyBlock.EndBlock = currentBlock
                currentBlock.rootBlock = withBodyBlock
                BlockManager.updateBlock(currentBlock)
                BlockManager.updateBlock(withBodyBlock)

            }
            else{
                withBodyBlock.parent?.let { attachHasBodyBlock(currentBlock ,it) }
            }
        }
    }
}

