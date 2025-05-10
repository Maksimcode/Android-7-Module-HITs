package com.example.android_7_module_hits.Blocks

import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

fun distanceBetween(a: Offset, b: Offset): Float {
    val dx = a.x - b.x
    val dy = a.y - b.y
    return sqrt(dx * dx + dy * dy)
}

fun findAttachableParent(allBlocks: List<Block>, draggedBlock: Block): Block? {

    val draggedBlockCenter = Offset(draggedBlock.position.x + 50f, draggedBlock.position.y + 50f)

    return allBlocks.firstOrNull { candidate ->
        if (candidate.id == draggedBlock.id) return@firstOrNull false
        if (candidate == draggedBlock.parent) return@firstOrNull false // Нельзя прикрепиться к своему ребенку

        // Проверяем, не является ли candidate потомком draggedBlock (чтобы избежать зацикливания)
        var current: Block? = candidate
        while (current != null) {
            if (current.id == draggedBlock.id) return@firstOrNull false // candidate - потомок draggedBlock
            current = current.child
        }

        val candidateCenter = Offset(candidate.position.x + 50f, candidate.position.y + 50f) // Предполагаем, что центр блока находится в 50f, 50f

        val distance = distanceBetween(candidateCenter, draggedBlockCenter)

        // Проверяем расстояние и возможность прикрепления
        distance < 100f && candidate.canAttachTo(draggedBlock)
    }
}
fun attachChild(parent: Block, child: Block) {
    // Отсоединяем от старого родителя
    child.parent?.let { oldParent ->
        oldParent.child = null
        child.parent = null
    }

    // Присоединяем к новому родителю
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