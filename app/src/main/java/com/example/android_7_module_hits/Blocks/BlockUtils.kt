package com.example.android_7_module_hits.Blocks

import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

fun distanceBetween(a: Offset, b: Offset): Float {
    val dx = a.x - b.x
    val dy = a.y - b.y
    return sqrt(dx * dx + dy * dy)
}

fun findAttachableParent(allBlocks: List<Block>, draggedBlock: Block, currentPosition: Offset): Block? {

    val currentPositionCenter = Offset(currentPosition.x + 50f, currentPosition.y + 50f)

    return allBlocks.firstOrNull { candidate ->
        if (candidate.id == draggedBlock.id) return null
        if (candidate == draggedBlock.parent) return null

        var current: Block? = candidate
        while (current != null) {
            if (current.id == draggedBlock.id) return null
            current = current.child
        }

        val candidateCenter = Offset(candidate.position.x + 50f, candidate.position.y + 50f) // Предполагаем, что центр блока находится в 50f, 50f

        val distance = distanceBetween(candidateCenter, currentPositionCenter)

        distance < 150f && candidate.canAttachTo(draggedBlock)
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

fun moveChildren(parent: Block, newPosition: Offset) {
    var currentChild = parent.child

    while (currentChild != null) {
        val offsetFromParent = Offset(
            currentChild.position.x - parent.position.x,
            currentChild.position.y - parent.position.y
        )

        currentChild.position = Offset(newPosition.x + offsetFromParent.x, newPosition.y + offsetFromParent.y)

        moveChildren(currentChild, currentChild.position)

        currentChild = currentChild.child
    }
}

fun isNearAttachmentZone(parent: Block, draggedBlock: Block): Boolean {
    val distance = distanceBetween(parent.position, draggedBlock.position)
    return distance < 50f && parent.canAttachTo(draggedBlock)
}

fun logAllBlocks(allBlocks: List<Block>) {
    println("Всего блоков: ${allBlocks.size}")
    allBlocks.forEachIndexed { index, block ->
        val content = block.content
        val name = when (content) {
            is BlockContent.Declare -> content.name
            is BlockContent.Assignment -> content.name
            else -> "unknown"
        }
        println("Блок $index: ID=${block.id}, Name=$name, Parent=${block.parent?.id}, Child=${block.child?.id} Content=$content")
    }
}