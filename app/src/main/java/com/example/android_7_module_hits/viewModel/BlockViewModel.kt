package com.example.android_7_module_hits.viewModel

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockHasBody
import com.example.android_7_module_hits.blocks.ConditionBlock
import com.example.android_7_module_hits.blocks.ElseBlock
import com.example.android_7_module_hits.blocks.ElseIfBlock
import com.example.android_7_module_hits.blocks.EndBlock
import com.example.android_7_module_hits.saving.BlockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class BlockViewModel(application: Application) : AndroidViewModel(application) {
    private val _blocks = MutableStateFlow<List<Block>>(emptyList())
    val blocks: StateFlow<List<Block>> get() = _blocks

    private val repository = BlockRepository(application)

    fun loadBlocks() {
        viewModelScope.launch {
            repository.loadBlocks()?.let { loadedBlocks ->
                _blocks.value = loadedBlocks
            }
        }
    }

    fun saveBlocks() {
        viewModelScope.launch {
            repository.saveBlocks(_blocks.value)
        }
    }

    fun setInitialBlocks(blocks: List<Block>) {
        viewModelScope.launch {
            _blocks.value = blocks
        }
    }

    fun addBlock(block: Block) {
        viewModelScope.launch {
            _blocks.value = _blocks.value + block
        }
    }

    fun updateBlockPosition(blockId: String, newPosition: Offset) {
        val updated = _blocks.value.map { block ->
            if (block.id == blockId)
                block.position = newPosition
            block
        }
        _blocks.value = updated
    }

    fun deleteBlock(blockId: String) {
        val targetBlock = _blocks.value.find{it.id ==blockId}
        targetBlock?.parent?.child = null
        targetBlock?.child?.parent = null
        targetBlock?.parent = null
        targetBlock?.child = null
        targetBlock?.EndBlock?.rootBlock = null
        targetBlock?.rootBlock?.EndBlock = null
        targetBlock?.EndBlock = null
        targetBlock?.rootBlock = null

        _blocks.value = _blocks.value.filter { it.id != blockId }
    }

    fun findAttachableParent(draggedBlock: Block, currentPosition: Offset): Block? {
        val draggedBlock = _blocks.value.find { it == draggedBlock } ?: return null
        val oldParent = draggedBlock.parent

        oldParent?.let {
            updateChildAndParentRelations(oldParent, null)
        }

        return _blocks.value.firstOrNull { candidate ->
            if (candidate.id == draggedBlock.id) return@firstOrNull false

            val distance = distanceBetween(candidate.position, currentPosition)

            distance < 200f && candidate.canAttachTo(draggedBlock)
        }
    }

    private fun distanceBetween(a: Offset, b: Offset): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return sqrt(dx * dx + dy * dy)
    }

    private fun updateChildAndParentRelations(oldParent: Block?, newChild: Block?) {
        viewModelScope.launch {
            _blocks.value = _blocks.value.map { block ->
                when {
                    block == oldParent -> {
                        val updated = block
                        updated.child = newChild
                        updated
                    }
                    block == newChild -> {
                        val updated = block
                        updated.parent = oldParent
                        updated
                    }
                    else -> block
                }
            }
        }
    }

    fun attachChild(parent: Block, child: Block) {
        val parent = _blocks.value.find { it == parent } ?: return
        val child = _blocks.value.find { it == child } ?: return

        child.parent?.let { oldParent ->
            updateChildAndParentRelations(oldParent, null)
        }

        viewModelScope.launch {
            _blocks.value = _blocks.value.map { block ->
                if (block.id == parent.id) {
                    val updated = block
                    updated.child = child
                    updated
                } else if (block.id == child.id) {
                    val updated = block
                    updated.parent = parent
                    updated
                } else {
                    block
                }
            }
        }
    }

    fun attachHasBodyBlock(currentBlock: Block, parentBlock: Block){
        when (currentBlock) {
            is EndBlock ->{
                if (parentBlock is BlockHasBody && parentBlock.EndBlock == null){
                    parentBlock.EndBlock = currentBlock
                    currentBlock.rootBlock = parentBlock
                }
                else{
                    parentBlock.parent?.let { attachHasBodyBlock(currentBlock ,it) }
                }
            }
            is ElseBlock, is ElseIfBlock -> {
                if (parentBlock is EndBlock) {
                    if (parentBlock.rootBlock is ConditionBlock ||
                        parentBlock.rootBlock is ElseIfBlock
                    ) {
                        currentBlock.rootBlock = parentBlock
                        parentBlock.EndBlock = currentBlock
                    }
                }
            }
        }
    }

    fun updateBlockAndChildrenPosition(blockId: String, newPosition: Offset) {
        _blocks.update { currentBlocks ->
            currentBlocks.map { block ->
                if (block.id == blockId) {
                    val dx = newPosition.x - block.position.x
                    val dy = newPosition.y - block.position.y

                    block.position = newPosition
                    updateChildrenPositions(block.child, dx, dy)

                    block
                } else block
            }
        }
    }

    private fun updateChildrenPositions(child: Block?, dx: Float, dy: Float) {
        var currentChild = child
        while (currentChild != null) {
            currentChild.position = Offset(currentChild.position.x + dx, currentChild.position.y + dy)
            currentChild = currentChild.child
        }
    }
}

fun logAllBlocks(blocks : List<Block>){
    blocks.forEach {
        println("ID: ${it.id}, parent: ${it.parent?.id}, child: ${it.child?.id}, root: ${it.rootBlock?.id}, end: ${it.EndBlock?.id}")
    }
}

