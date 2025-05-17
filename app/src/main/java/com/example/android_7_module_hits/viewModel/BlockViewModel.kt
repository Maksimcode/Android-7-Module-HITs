package com.example.android_7_module_hits.viewModel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_7_module_hits.Blocks.BaseBlock
import com.example.android_7_module_hits.Blocks.Block
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class BlockViewModel : ViewModel() {
    private val _blocks = MutableStateFlow<List<Block>>(emptyList())
    val blocks: StateFlow<List<Block>> get() = _blocks

//    fun loadBlocks(savedJson: String?) {
//        if (savedJson != null) {
//            val loadedBlocks = deserializeBlocks(savedJson)
//            _blocks.value = loadedBlocks
//        }
//    }

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

//    fun saveBlocksToFile(context: Context) {
//        val jsonData = serializeBlocks(_blocks.value.map { it.toBlockState() })
//        saveStateToFile(context, "project_state.json", jsonData)
//    }
}