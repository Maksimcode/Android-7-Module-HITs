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

@Composable
fun DeclareBlockView(content: BlockContent.Declare, block: Block){
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember(content.name ?: "Variable") { mutableStateOf(content.name ?: "Variable") }

    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (isEditing) {
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
                            isEditing = false
                        }
                    ) {
                        Text("Отмена")
                    }

                    TextButton(
                        onClick = {
                            content.name = editedName
                            isEditing = false
                        }
                    ) {
                        Text("Сохранить")
                    }
                }

            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "int ", color = Color.Black)

                    Box(
                        modifier = Modifier
                            .background(Color.LightGray)
                            .clickable { isEditing = true }
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