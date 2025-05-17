package com.example.android_7_module_hits.ui.uiblocks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import com.example.android_7_module_hits.Blocks.DataType

@Composable
fun DeclareBlockView(content: BlockContent.Declare, block: Block){
    var isEditingName by remember { mutableStateOf(false) }
    var isEditingType by remember { mutableStateOf(false) }

    var editedName by remember(content.name ?: "Variable") { mutableStateOf(content.name ?: "Variable") }
    var editedType by remember(content.type ?: DataType.INTEGER) { mutableStateOf(content.type ?: DataType.INTEGER) }

    val options = listOf(DataType.INTEGER, DataType.STRING, DataType.BOOLEAN)

    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (isEditingName) {
                TextField(
                    value = editedName,
                    onValueChange = { newText ->
                        editedName = newText
                    },
                    label = { Text("Имя переменной") },
                    modifier = Modifier
                        .width(150.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            editedName = content.name ?: "Variable"
                            isEditingName = false
                        }
                    ) {
                        Text("Отмена")
                    }

                    TextButton(
                        onClick = {
                            content.name = editedName
                            isEditingName = false
                        }
                    ) {
                        Text("Сохранить")
                    }
                }

            } else if(isEditingType){
                DropdownMenu(
                    expanded = isEditingType,
                    onDismissRequest = { isEditingType = false }) {
                    options.forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(text = displayText(type))
                            },
                            onClick = {
                                editedType = type
                                content.type = editedType
                                isEditingType = false
                            }
                        )

                    }
                }

            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(Color.LightGray)
                            .clickable { isEditingType = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = displayText(editedType), color = Color.Blue)
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier
                            .background(Color.LightGray)
                            .clickable { isEditingName = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = editedName, color = Color.Blue)
                    }

                    Text(text = ";", color = Color.Black)
                }
            }
        }
    }
}

fun displayText(selectedType: DataType): String{
    return when(selectedType){
        DataType.INTEGER -> "int"
        DataType.STRING -> "string"
        DataType.BOOLEAN -> "bool"
        else -> "pupu"
    }
}