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
import androidx.compose.ui.window.Dialog
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent

@Composable
fun ElseIfBlockView(content: BlockContent.ElseIf, block: Block){
    var isEditingExpression by remember { mutableStateOf(false) }

    var editedExpression by remember(content.expression ?: "true") { mutableStateOf(content.expression ?: "true") }


    Card(
        modifier = Modifier
            .width(210.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ){
        Column(modifier = Modifier.padding(8.dp)){
            if (isEditingExpression)
            {
                Dialog(onDismissRequest = { isEditingExpression = false }) {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Введите логическое выражение:")
                            Spacer(modifier = Modifier.height(6.dp))
                            TextField(
                                value = editedExpression,
                                onValueChange = { newText ->
                                    editedExpression = newText
                                },
                                modifier = Modifier
                                    .height(60.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        editedExpression = content.expression ?: "true"
                                        isEditingExpression = false
                                    }
                                ) {
                                    Text("Отмена")
                                }

                                TextButton(
                                    onClick = {
                                        content.expression = editedExpression
                                        isEditingExpression = false
                                    }
                                ) {
                                    Text("Сохранить")
                                }
                            }
                        }
                    }
                }
            } else{
                Column (modifier = Modifier.height(60.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "otherwise, if ( ")
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .clickable { isEditingExpression = true }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(text = editedExpression, color = Color.Blue)
                        }

                        Text(text = " )", color = Color.Black)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "is true", color = Color.Black)
                    }
                }
            }
        }
    }
}