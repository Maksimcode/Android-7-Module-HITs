package com.example.android_7_module_hits.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.android_7_module_hits.ui.Blocks.UiBlock
import androidx.compose.ui.geometry.Offset

class EditorViewModel : ViewModel() {

    private val _blocks = MutableStateFlow<List<UiBlock>>(emptyList())
    val blocks: StateFlow<List<UiBlock>> = _blocks

    fun addBlock(block: UiBlock) {
        viewModelScope.launch {
            _blocks.value += block
        }
    }

    fun removeBlock(id: String) {
        viewModelScope.launch {
            _blocks.value = _blocks.value.filter { it.id != id }
        }
    }

    fun updateBlockPosition(id: String, newPosition: Offset) {
        viewModelScope.launch {
            _blocks.value = _blocks.value.map { block ->
                if (block.id == id) {
                    block.copy(position = newPosition)
                } else {
                    block
                }
            }
        }
    }

    fun updateBlockField(id: String, key: String, value: Any) {
        viewModelScope.launch {
            _blocks.value = _blocks.value.map { block ->
                if (block.id == id) {
                    val updatedFields = block.editableFields.toMutableMap().apply { this[key] = value }
                    block.copy(content = "$key = $value", editableFields = updatedFields)
                } else {
                    block
                }
            }
        }
    }

    fun connectBlocks(fromId: String, toId: String) {
        viewModelScope.launch {
            _blocks.value = _blocks.value.map { block ->
                if (block.id == fromId) {
                    block.copy(nextBlockId = toId)
                } else {
                    block
                }
            }
        }
    }
}