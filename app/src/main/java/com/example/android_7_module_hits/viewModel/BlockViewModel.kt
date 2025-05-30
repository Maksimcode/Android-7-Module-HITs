package com.example.android_7_module_hits.viewModel

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockHasBody
import com.example.android_7_module_hits.saving.BlockRepository
import com.example.android_7_module_hits.utils.distanceBetween
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BlockViewModel(application: Application) : AndroidViewModel(application) {
    private val _blocks = MutableStateFlow<List<Block>>(emptyList())
    val blocks: StateFlow<List<Block>> get() = _blocks

    private val repository = BlockRepository(application)

    fun loadBlocks(fileName: String) {
        viewModelScope.launch {
            repository.loadBlocks(fileName)?.let { loadedBlocks ->
                _blocks.value = loadedBlocks
            }
        }
    }

    fun saveBlocks(fileName: String) {
        viewModelScope.launch {
            repository.saveBlocks(_blocks.value, fileName)
        }
    }

    fun deleteSaveFile(fileName: String, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.deleteSaveFile(fileName)
            onComplete?.invoke()
        }
    }


    fun addBlock(block: Block) {
        viewModelScope.launch {
            _blocks.value = _blocks.value + block
        }
    }

    fun clearBlocks() {
        _blocks.value = emptyList()
    }

    fun deleteBlock(blockId: String) {
        val targetBlock = _blocks.value.firstOrNull { it.id == blockId } ?: return

        val parent = targetBlock.parent
        if (parent != null) {
            if (parent.child?.id == targetBlock.id) {
                parent.child = null
            }
            if (parent is BlockHasBody) {
                parent.nestedChildren.removeAll { it.id == targetBlock.id }
            }
        }

        val child = targetBlock.child
        if (child != null) {
            if (child.parent?.id == targetBlock.id) {
                child.parent = null
            }
        }

        fun collectNestedIds(block: Block): Set<String> {
            val ids = mutableSetOf<String>()
            if (block is BlockHasBody) {
                block.nestedChildren.forEach { nestedChild ->
                    ids.add(nestedChild.id)
                    ids.addAll(collectNestedIds(nestedChild))
                }
            }
            return ids
        }

        val nestedIds =
            if (targetBlock is BlockHasBody) collectNestedIds(targetBlock) else emptySet()

        targetBlock.parent = null
        targetBlock.child = null
        if (targetBlock is BlockHasBody) {
            targetBlock.nestedChildren.clear()
        }
        removeBlockFromAllBodies(targetBlock)

        _blocks.value = _blocks.value.filter {
            it.id != targetBlock.id && !nestedIds.contains(it.id)
        }
    }

    fun attachBlock(parent: Block, child: Block, asNested: Boolean) {
        val parentBlock = _blocks.value.firstOrNull { it.id == parent.id } ?: return
        val childBlock = _blocks.value.firstOrNull { it.id == child.id } ?: return

        childBlock.parent?.let { oldParent ->
            detachChild(oldParent, childBlock)
        }

        val bodyParent = findBodyParent(parent)


        if (asNested || bodyParent != null) {
            if (parentBlock is BlockHasBody && parentBlock.canAcceptNestedChildren()) {
                if (parentBlock.nestedChildren.isEmpty()) {
                    updateNestedRelationsWithoutParent(parentBlock, childBlock)
                } else {
                    val lastNested = parentBlock.nestedChildren.last()
                    childBlock.parent = lastNested
                    updateNestedRelationsWithParent(parentBlock, childBlock, lastNested)
                }
            } else if ( bodyParent is BlockHasBody && bodyParent.canAcceptNestedChildren()) {
                if (bodyParent.nestedChildren.isEmpty()) {
                    updateNestedRelationsWithoutParent(bodyParent, childBlock)
                } else {
                    val lastNested = bodyParent.nestedChildren.last()
                    childBlock.parent = lastNested
                    updateNestedRelationsWithParent(bodyParent, childBlock, lastNested)
                }
            } else{
                println("Родительский блок не поддерживает вложенные прикрепления")
                return
            }
        } else {

            if (parentBlock.child == null) {
                updateLinearRelations(parentBlock, childBlock)
            } else {
                println("Родитель уже имеет дочерний блок")
                return
            }
        }

        viewModelScope.launch {
            _blocks.value = _blocks.value.map { block ->
                when (block.id) {
                    parentBlock.id -> parentBlock
                    childBlock.id -> childBlock
                    else -> block
                }
            }
        }
    }

    private fun updateNestedRelationsWithoutParent(parentBlock: BlockHasBody, newNestedChild: Block) {
        viewModelScope.launch {
            parentBlock.nestedChildren.add(newNestedChild)
            newNestedChild.parent = null // Нет родителя
            _blocks.value = _blocks.value.map { block ->
                when (block.id) {
                    parentBlock.id -> parentBlock
                    newNestedChild.id -> newNestedChild
                    else -> block
                }
            }
        }
    }

    private fun updateNestedRelationsWithParent(
        parentBlock: BlockHasBody,
        newNestedChild: Block,
        previousInBody: Block
    ) {
        viewModelScope.launch {
            parentBlock.nestedChildren.add(newNestedChild)
            newNestedChild.parent = previousInBody
            previousInBody.child = newNestedChild
            _blocks.value = _blocks.value.map { block ->
                when (block.id) {
                    parentBlock.id -> parentBlock
                    newNestedChild.id -> newNestedChild
                    else -> block
                }
            }
        }
    }

    private fun detachChild(parent: Block?, child: Block?) {
        viewModelScope.launch {
            if (parent?.child?.id == child?.id) {
                parent?.child = null
            }
            if (child != null) removeBlockFromAllBodies(child)
            child?.parent = null
        }
    }

    private fun findBodyParent(block: Block): BlockHasBody? {
        return _blocks.value.filterIsInstance<BlockHasBody>().firstOrNull { body ->
            body.nestedChildren.any { it.id == block.id }
        }
    }

    fun removeBlockFromAllBodies(block: Block) {
        _blocks.value = _blocks.value.map { current ->
            if (current is BlockHasBody) {
                current.nestedChildren.removeAll { it.id == block.id }
            }
            current
        }
    }

    fun updateBlockAndDescendantsPosition(blockId: String, newPosition: Offset) {
        val targetBlock = _blocks.value.find { it.id == blockId } ?: return

        val dx = newPosition.x - targetBlock.position.x
        val dy = newPosition.y - targetBlock.position.y

        targetBlock.position = newPosition

        updateDescendantsPosition(targetBlock, dx, dy)

        viewModelScope.launch {
            _blocks.value = _blocks.value.map { it }
        }
    }

    private fun updateDescendantsPosition(block: Block, dx: Float, dy: Float) {
        block.child?.let { linearChild ->
            linearChild.position = Offset(linearChild.position.x + dx, linearChild.position.y + dy)
            updateDescendantsPosition(linearChild, dx, dy)
        }
        if (block is BlockHasBody) {
            block.nestedChildren.forEach { nestedChild ->
                nestedChild.position =
                    Offset(nestedChild.position.x + dx, nestedChild.position.y + dy)
                updateDescendantsPosition(nestedChild, dx, dy)
            }
        }
    }


    fun findAttachableParent(
        draggedBlock: Block,
        dropPosition: Offset,
        asNested: Boolean
    ): Block? {
        draggedBlock.parent?.let { oldParent ->
            detachChild(oldParent, null)
        }
        removeBlockFromAllBodies(draggedBlock)

        return _blocks.value.firstOrNull { candidate ->
            if (candidate.id == draggedBlock.id) return@firstOrNull false

            val distance = distanceBetween(
                Offset(candidate.position.x + 50f, candidate.position.y + 50f),
                dropPosition
            )
            if (distance >= 200f) return@firstOrNull false

            if (asNested) {
                candidate is BlockHasBody && candidate.canAttachNested()
            } else {
                candidate.canAttachTo(draggedBlock)
            }
        }
    }

    private fun updateLinearRelations(oldParent: Block?, newChild: Block?) {
        viewModelScope.launch {
            _blocks.value = _blocks.value.map { block ->
                when {
                    block == oldParent -> {
                        block.child = newChild
                        block
                    }

                    block == newChild -> {
                        block.parent = oldParent
                        block
                    }

                    else -> block
                }
            }
        }
    }

}



