package com.example.android_7_module_hits.Blocks

import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

fun distanceBetween(a: Offset, b: Offset): Float {
    val dx = a.x - b.x
    val dy = a.y - b.y
    return sqrt(dx * dx + dy * dy)
}

fun findAttachableParent(allBlocks: List<Block>, draggedBlock: Block): Block? {
    return allBlocks.firstOrNull { candidate ->
        val distance = distanceBetween(candidate.position, draggedBlock.position)
        distance < 500f && candidate.canAttachTo(draggedBlock) && candidate.id != draggedBlock.id
    }
}

fun attachChild(parent: Block, child: Block) {
    // Сначала отсоединяем от текущего родителя
    child.parent?.let { oldParent ->
        if (oldParent is BaseBlock) {
            oldParent.child = null
        }
    }

    // Присоединяем к новому родителю
    if (parent is BaseBlock) {
        parent.child = child
        child.parent = parent


    }
}

fun getAllBlocks(allBlocks: List<Block>): List<Block> {
    return allBlocks
}