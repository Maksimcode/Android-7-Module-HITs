package com.example.android_7_module_hits.Blocks

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

// В BlockManager:
object BlockManager {
    private val _allBlocks = mutableStateListOf<Block>()
    val allBlocks: SnapshotStateList<Block> = _allBlocks

    fun addBlock(block: Block) {
        _allBlocks.add(block)
    }

    fun removeBlock(block: Block) {
        _allBlocks.remove(block)
        block.parent?.let { it.child = null }
        block.child?.let { it.parent = null }
    }

    fun updateBlock(updatedBlock: Block) {
        val index = _allBlocks.indexOfFirst { it.id == updatedBlock.id }
        if (index != -1) {
            _allBlocks[index] = updatedBlock
        }
    }

    fun updateAllBlocks(newList: List<Block>) {
        _allBlocks.clear()
        _allBlocks.addAll(newList)
    }
}



