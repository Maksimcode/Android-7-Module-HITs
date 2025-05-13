package com.example.android_7_module_hits.Blocks

import androidx.compose.runtime.mutableStateListOf

object BlockManager {
    private val _allBlocks = mutableStateListOf<Block>()
    val allBlocks: List<Block> = _allBlocks

    fun addBlock(block: Block) {
        _allBlocks.add(block)
    }

    fun removeBlock(block: Block) {
        _allBlocks.remove(block)
    }

    fun updateBlocks(newList: List<Block>) {
        _allBlocks.clear()
        _allBlocks.addAll(newList)
    }
}
