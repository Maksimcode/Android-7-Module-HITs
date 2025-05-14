package com.example.android_7_module_hits.ui.uiblocks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_7_module_hits.Blocks.Block
import com.example.android_7_module_hits.Blocks.BlockContent
import com.example.android_7_module_hits.Blocks.BlockHasBody

@Composable
fun ConditionBlockView(content: BlockContent.Condition, block: Block){
    var isEditingFirstPart by remember { mutableStateOf(false) }
    var isEditingOperator by remember { mutableStateOf(false) }
    var isEditingSecondPart by remember { mutableStateOf(false) }

    var editedFirstPart by remember(content.firstPart ?: "0") { mutableStateOf(content.firstPart ?: "0") }
    var editedOperator by remember(content.operator ?: "==") { mutableStateOf(content.operator ?: "0") }
    var editedSecondPart by remember(content.secondPart ?: "0") { mutableStateOf(content.secondPart ?: "0") }

    val hasEndBlock by remember(block) { derivedStateOf { block.hasEndBlock() }}

    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!hasEndBlock) Color.Red else Color.LightGray)
    ){
        Column(modifier = Modifier.padding(8.dp)){
            if (isEditingFirstPart)
            {
                TextField(
                    value = editedFirstPart,
                    onValueChange = {newText ->
                        editedFirstPart = newText
                    },
                    label = { Text(text = "Первое выражение") },
                    modifier = Modifier.width(150.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            editedFirstPart = content.firstPart ?: "0"
                            isEditingFirstPart = false
                        }
                    ) {
                        Text("Отмена")
                    }

                    TextButton(
                        onClick = {
                            content.firstPart = editedFirstPart
                            isEditingFirstPart = false
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
            } else if (isEditingOperator) {

                TextField(
                    value = editedOperator,
                    onValueChange = {newText ->
                        editedOperator = newText
                    },
                    label = { Text(text = "Операция сравнения (>,<,=,!=,>=,<=)") },
                    modifier = Modifier.width(150.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            editedOperator = content.operator ?: "0"
                            isEditingOperator = false
                        }
                    ) {
                        Text("Отмена")
                    }

                    TextButton(
                        onClick = {
                            content.operator = editedOperator
                            isEditingOperator = false
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
            } else if (isEditingSecondPart){

                TextField(
                    value = editedSecondPart,
                    onValueChange = {newText ->
                        editedSecondPart = newText
                    },
                    label = { Text(text = "Второе выражение") },
                    modifier = Modifier.width(150.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            editedSecondPart = content.secondPart ?: "0"
                            isEditingSecondPart = false
                        }
                    ) {
                        Text("Отмена")
                    }

                    TextButton(
                        onClick = {
                            content.secondPart = editedSecondPart
                            isEditingSecondPart = false
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
            } else{
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text="if (")
                    Box(
                        modifier = Modifier
                            .background(Color.LightGray)
                            .clickable { isEditingFirstPart = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = editedFirstPart, color = Color.Blue)
                    }
                    Text(text = " ", color = Color.Black)

                    Box(
                        modifier = Modifier
                            .background(Color.LightGray)
                            .clickable { isEditingOperator = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = editedOperator, color = Color.Blue)
                    }
                    Text(text = " ", color = Color.Black)

                    Box(
                        modifier = Modifier
                            .background(Color.LightGray)
                            .clickable { isEditingSecondPart = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = editedSecondPart, color = Color.Blue)
                    }

                    Text(text = ") {", color = Color.Black)
                }
            }
        }
    }
}