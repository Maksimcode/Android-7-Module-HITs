package com.example.android_7_module_hits.ui.uiblocks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent
import com.example.android_7_module_hits.ui.theme.AssignmentColor
import com.example.android_7_module_hits.ui.theme.BlockInputBackgroundColor
import com.example.android_7_module_hits.ui.theme.BlockInputTextColor


@Composable
fun AssignBlockView(content: BlockContent.Assignment, block: Block){
    var isEditingName by remember { mutableStateOf(false) }
    var isEditingValue by remember { mutableStateOf(false) }
    var editedName by remember(content.name ?: "Variable") { mutableStateOf(content.name ?: "Variable") }
    var editedValue by remember ( content.value ?: "0" ) { mutableStateOf(content.value ?: "0") }

    Card(
        modifier = Modifier
            .width(210.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = AssignmentColor)
    ){
        Column(modifier = Modifier.padding(8.dp)){
            if (isEditingName) {
                Dialog(onDismissRequest = { isEditingName = false }) {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Variable name:")
                            Spacer(modifier = Modifier.height(6.dp))
                            TextField(
                                value = editedName,
                                onValueChange = { newText ->
                                    editedName = newText
                                },
                            )
                            Spacer(modifier = Modifier.height(16.dp))
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
                                    Text("Cancel")
                                }
                                TextButton(
                                    onClick = {
                                        content.name = editedName
                                        isEditingName = false
                                    }
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            } else if (isEditingValue) {
                Dialog(onDismissRequest = { isEditingValue = false }) {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Variable value:")
                            Spacer(modifier = Modifier.height(6.dp))
                            TextField(
                                value = editedValue,
                                onValueChange = { newText ->
                                    editedValue = newText
                                },
                                modifier = Modifier
                            )
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        editedValue = content.value ?: "0"
                                        isEditingValue = false
                                    }
                                ) {
                                    Text("Cancel")
                                }
                                TextButton(
                                    onClick = {
                                        content.value = editedValue
                                        isEditingValue = false
                                    }
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            } else{
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "set ", color = Color.Black)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(color = BlockInputBackgroundColor)
                            .clickable { isEditingName = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = editedName, color = BlockInputTextColor)
                    }
                    Text(text = " to ", color = Color.Black)

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(color = BlockInputBackgroundColor)
                            .clickable { isEditingValue = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = editedValue, color = BlockInputTextColor)
                    }
                }
            }
        }
    }
}
