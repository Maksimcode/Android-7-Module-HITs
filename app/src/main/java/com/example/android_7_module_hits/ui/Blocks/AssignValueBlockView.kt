package com.example.android_7_module_hits.ui.Blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_7_module_hits.data.model.tryConnectBlockToOthers
import com.example.android_7_module_hits.ui.editor.EditorViewModel
import kotlin.math.roundToInt

@Composable
fun AssignValueBlockView(
    uiBlock: UiBlock,
    onEdit: (String, String) -> Unit,
    onConnect: (String, String) -> Unit,
    viewModel: EditorViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var variableName by remember(uiBlock) {
        mutableStateOf(
            uiBlock.editableFields["variableName"] as? String ?: "x"
        )
    }
    var expression by remember(uiBlock) {
        mutableStateOf(
            uiBlock.editableFields["expression"] as? String ?: "0"
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Присвоить значение") },
            text = {
                Column {
                    // Поле для имени переменной
                    Text("Имя переменной:")
                    TextField(
                        value = variableName,
                        onValueChange = { variableName = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Поле для выражения
                    Text("Значение / выражение:")
                    TextField(
                        value = expression,
                        onValueChange = { expression = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onEdit("variableName", variableName)
                    onEdit("expression", expression)
                    showDialog = false
                }) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box (
        modifier = Modifier
            .offset {
                IntOffset(
                    x = offsetX.roundToInt(),
                    y = offsetY.roundToInt()
                ) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag =  {change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y},
                    onDragEnd = {
                        println("DEBUG: onDragEnd вызван!")
                        val allBlocks = viewModel.blocks.value

                        val currentBlock = uiBlock.copy(position = Offset(offsetX, offsetY))

                        tryConnectBlockToOthers(
                            currentBlock = currentBlock,
                            allBlocks = allBlocks,
                            onConnect = { fromId, toId ->
                                println("Соединение: $fromId -> $toId")
                                viewModel.connectBlocks(fromId, toId)
                            }
                        )
                    }
                )}
            .offset { IntOffset(uiBlock.position.x.toInt(), uiBlock.position.y.toInt()) }
            .background(Color.Cyan)
            .clickable { showDialog = true }
            .padding(8.dp)
            .width(200.dp)
    ) {
        Text("$variableName = $expression")
    }
}