package com.example.android_7_module_hits.ui.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_7_module_hits.data.model.BlockType
import com.example.android_7_module_hits.ui.Blocks.DeclareVariableBlockView
import com.example.android_7_module_hits.ui.Blocks.AssignValueBlockView


@Composable
fun EditorScreen(viewModel: EditorViewModel = viewModel()) {
    val blocks by viewModel.blocks.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(Color.White)

            // Рисуем стрелки между блоками
            blocks.forEach { block ->
                block.nextBlockId?.let { nextId ->
                    val nextBlock = blocks.find { it.id == nextId }
                    nextBlock?.let { target ->
                        drawLine(
                            color = Color.Blue,
                            start = block.position,
                            end = target.position,
                            strokeWidth = 4f
                        )
                    }
                }
            }
        }

        blocks.forEach { block ->
            when(block.type) {
                BlockType.DECLARE -> DeclareVariableBlockView(
                    uiBlock = block,
                    onEdit = { key, value ->
                        viewModel.updateBlockField(block.id, key, value)
                    },
                    onConnect = { fromId, toId ->
                        viewModel.connectBlocks(fromId, toId)
                    }
                )

                BlockType.ASSIGN -> AssignValueBlockView(
                    uiBlock = block,
                    onEdit = { key, value ->
                        viewModel.updateBlockField(block.id, key, value)
                    },
                    onConnect = { fromId, toId ->
                        viewModel.connectBlocks(fromId, toId)
                    }
                )

                else -> {}
            }
        }
    }
}
