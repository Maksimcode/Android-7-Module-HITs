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
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent
import com.example.android_7_module_hits.ui.theme.ConditionColor

@Composable
fun ConditionBlockView(content: BlockContent.Condition, block: Block){
    var isEditingExpression by remember { mutableStateOf(false) }

    var editedExpression by remember(content.expression ?: "true") { mutableStateOf(content.expression ?: "true") }


    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = ConditionColor)
    ){
        Column(modifier = Modifier.padding(8.dp)){
            if (isEditingExpression)
            {
                TextField(
                    value = editedExpression,
                    onValueChange = {newText ->
                        editedExpression = newText
                    },
                    label = { Text(text = "Logical expression:") },
                    modifier = Modifier.width(150.dp)
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
            } else{
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text="if ( ")
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .clickable { isEditingExpression = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(text = editedExpression, color = Color.Gray)
                    }

                    Text(text = " ) {", color = Color.Black)
                }
            }
        }
    }
}