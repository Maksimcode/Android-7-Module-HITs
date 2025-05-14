package com.example.android_7_module_hits.Blocks

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

fun distanceBetween(a: Offset, b: Offset): Float {
    val dx = a.x - b.x
    val dy = a.y - b.y
    return sqrt(dx * dx + dy * dy)
}

fun findAttachableParent(draggedBlock: Block, currentPosition: Offset): Block? {

    var oldParent = draggedBlock.parent
    if (oldParent != null) {
        oldParent.child = null
        draggedBlock.parent = null
    }


    return BlockManager.allBlocks.firstOrNull { candidate ->
        if (candidate.id == draggedBlock.id) return null

        val distance = distanceBetween(candidate.position, currentPosition)

        distance < 200f && candidate.canAttachTo(draggedBlock)

    }
}



fun attachChild(parent: Block, child: Block) {
    child.parent?.let { oldParent ->
        oldParent.child = null
        child.parent = null
    }

    parent.child = child
    child.parent = parent
}


fun logAllBlocks() {
    println("Всего блоков: ${BlockManager.allBlocks.size}")
    BlockManager.allBlocks.forEachIndexed { index, block ->
        val content = block.content
        val name = when (content) {
            is BlockContent.Declare -> content.name
            is BlockContent.Assignment -> content.name
            is BlockContent.Condition -> "condition"
            is BlockContent.End -> "End"
            else -> "unknown"
        }
        println("Блок $index: ID=${block.id}, Name=${block}, End = ${block.EndBlock} Root = ${block.rootBlock}")
    }
}