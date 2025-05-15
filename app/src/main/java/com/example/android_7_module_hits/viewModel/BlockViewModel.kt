package com.example.android_7_module_hits.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.android_7_module_hits.Blocks.Block

class BlockViewModel : ViewModel() {
    private val _blocks = mutableStateOf<List<Block>>(emptyList())
    val blocks: State<List<Block>> get() = _blocks

//    fun loadBlocks(savedJson: String?) {
//        if (savedJson != null) {
//            val loadedBlocks = deserializeBlocks(savedJson)
//            _blocks.value = loadedBlocks
//        }
//    }

    fun updateBlockPosition(blockId: String, newPosition: Offset) {
        val updated = _blocks.value.map { block ->
            if (block.id == blockId)
                block.position = newPosition
            block
        }
        _blocks.value = updated
    }

    fun deleteBlock(blockId: String) {
        _blocks.value = _blocks.value.filter { it.id != blockId }
    }

    fun attachBlock(childId: String, parentId: String) {
        // Реализация прикрепления дочернего блока к родителю
    }

//    fun saveBlocksToFile(context: Context) {
//        val jsonData = serializeBlocks(_blocks.value.map { it.toBlockState() })
//        saveStateToFile(context, "project_state.json", jsonData)
//    }
}