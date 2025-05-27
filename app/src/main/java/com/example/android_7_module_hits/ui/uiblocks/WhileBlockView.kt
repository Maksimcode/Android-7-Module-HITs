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
fun WhileBlockView(content: BlockContent.While, block: Block){
    var isEditingExpression by remember { mutableStateOf(false) }

    var editedExpression by remember(content.expression ?: "false") { mutableStateOf(content.expression ?: "false") }


    Card(
        modifier = Modifier
            .width(210.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = CycleColor)
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
                                        editedExpression = content.expression ?: "true"
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
            } else{
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text="repeat while ( ")
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(color = BlockInputBackgroundColor)
                            .clickable { isEditingExpression = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = editedExpression, color = BlockInputTextColor)
                    }

                    Text(text = " )", color = Color.Black)
                }
            }
        }
    }
}