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
import com.example.android_7_module_hits.ui.theme.BlockInputBackgroundColor
import com.example.android_7_module_hits.ui.theme.BlockInputTextColor
import com.example.android_7_module_hits.ui.theme.CycleColor

@Composable
fun ForBlockView(content: BlockContent.For, block: Block) {
    var isEditingVariable by remember { mutableStateOf(false) }
    var isEditingStartValue by remember { mutableStateOf(false) }
    var isEditingExpression by remember { mutableStateOf(false) }
    var isEditingUpdating by remember { mutableStateOf(false) }

    var editedVariable by remember(content.variable ?: "i") {
        mutableStateOf(
            content.variable ?: "i"
        )
    }
    var editedStartValue by remember(content.initValue ?: "0") {
        mutableStateOf(
            content.initValue ?: "0"
        )
    }
    var editedExpression by remember(
        content.expression ?: "i < 10"
    ) { mutableStateOf(content.expression ?: "i < 10") }
    var editedUpdating by remember(content.construct ?: "${editedVariable} + 1") {
        mutableStateOf(
            content.construct ?: "i + 1"
        )
    }

    Card(
        modifier = Modifier
            .width(280.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = CycleColor)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (isEditingVariable) {
                Dialog(onDismissRequest = { isEditingVariable = false }) {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Counter name:")
                            Spacer(modifier = Modifier.height(6.dp))
                            TextField(
                                value = editedVariable,
                                onValueChange = { newText ->
                                    editedVariable = newText
                                },
                                modifier = Modifier
                            )
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        editedVariable = content.variable ?: "0"
                                        isEditingVariable = false
                                    }
                                ) {
                                    Text("Cancel")
                                }

                                TextButton(
                                    onClick = {
                                        content.variable = editedVariable
                                        isEditingVariable = false
                                    }
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            } else if (isEditingStartValue) {
                Dialog(onDismissRequest = { isEditingStartValue = false }) {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Counter value:")
                            Spacer(modifier = Modifier.height(6.dp))
                            TextField(
                                value = editedStartValue,
                                onValueChange = { newText ->
                                    editedStartValue = newText
                                },
                                modifier = Modifier
                            )
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        editedStartValue = content.initValue ?: "0"
                                        isEditingStartValue = false
                                    }
                                ) {
                                    Text("Cancel")
                                }

                                TextButton(
                                    onClick = {
                                        content.initValue = editedStartValue
                                        isEditingStartValue = false
                                    }
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            } else if (isEditingExpression) {
                Dialog(onDismissRequest = { isEditingExpression = false }) {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Logical expression:")
                            Spacer(modifier = Modifier.height(6.dp))
                            TextField(
                                value = editedExpression,
                                onValueChange = { newText ->
                                    editedExpression = newText
                                },
                                modifier = Modifier
                            )
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        editedExpression = content.expression ?: "i < 10"
                                        isEditingExpression = false
                                    }
                                ) {
                                    Text("Cancel")
                                }

                                TextButton(
                                    onClick = {
                                        content.expression = editedExpression
                                        isEditingExpression = false
                                    }
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            } else if (isEditingUpdating) {
                Dialog(onDismissRequest = { isEditingUpdating = false }) {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Logical expression:")
                            Spacer(modifier = Modifier.height(6.dp))
                            TextField(
                                value = editedUpdating,
                                onValueChange = { newText ->
                                    editedUpdating = newText
                                },
                                modifier = Modifier
                            )
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        editedUpdating = content.construct ?: "${editedVariable}+1"
                                        isEditingUpdating = false
                                    }
                                ) {
                                    Text("Cancel")
                                }

                                TextButton(
                                    onClick = {
                                        content.construct = editedUpdating
                                        isEditingUpdating = false
                                    }
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            } else {
                Column (){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "for ")
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(color = BlockInputBackgroundColor)
                                .clickable { isEditingVariable = true }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(text = editedVariable, color = BlockInputTextColor)
                        }
                        Text(text = " = ")
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(color = BlockInputBackgroundColor)
                                .clickable { isEditingStartValue = true }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(text = editedStartValue, color = BlockInputTextColor)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "while ")
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(color = BlockInputBackgroundColor)
                                .clickable { isEditingExpression = true }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(text = editedExpression, color = BlockInputTextColor)
                        }
                        Text(text = " is true")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "step: ")
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(color = BlockInputBackgroundColor)
                                .clickable { isEditingUpdating = true }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(text = editedUpdating, color = BlockInputTextColor)
                        }
                    }
                }
            }
        }
    }
}
