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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent
import com.example.android_7_module_hits.blocks.DataType
import com.example.android_7_module_hits.ui.theme.BlockInputBackgroundColor
import com.example.android_7_module_hits.ui.theme.BlockInputTextColor
import com.example.android_7_module_hits.ui.theme.DeclareColor

@Composable
fun DeclareBlockView(content: BlockContent.Declare, block: Block){
    var isEditingName by remember { mutableStateOf(false) }
    var isEditingType by remember { mutableStateOf(false) }
    var isEditingLength by remember { mutableStateOf(false) }

    var editedName by remember(content.name ?: "Variable") { mutableStateOf(content.name ?: "Variable") }
    var editedType by remember(content.type ?: DataType.INTEGER) { mutableStateOf(content.type ?: DataType.INTEGER) }
    var editedLength by remember(content.length ?: "0") { mutableStateOf(content.length ?: "0") }

    val options = listOf(DataType.INTEGER, DataType.STRING, DataType.BOOLEAN,
                         DataType.ARR_INT, DataType.ARR_STR, DataType.ARR_BOOL)


    Card(
        modifier = Modifier
            .width(210.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = DeclareColor)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
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
                            Spacer(modifier = Modifier.height(8.dp))
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
                if(editedType == DataType.ARR_BOOL ||
                    editedType == DataType.ARR_STR ||
                    editedType == DataType.ARR_INT){
                    if (isEditingLength) {
                        Dialog(onDismissRequest = { isEditingLength = false }) {
                            Card(
                                modifier = Modifier
                                    .width(300.dp)
                                    .padding(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = "Array Length:")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextField(
                                        value = editedLength,
                                        onValueChange = { newText ->
                                            editedLength = newText
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
                                                editedLength = content.name ?: "0"
                                                isEditingLength = false
                                            }
                                        ) {
                                            Text("Cancel")
                                        }

                                        TextButton(
                                            onClick = {
                                                content.length = editedLength
                                                isEditingLength = false
                                            }
                                        ) {
                                            Text("Save")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else{
                        Column (modifier = Modifier.height(80.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "create", color = Color.Black)
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(color = BlockInputBackgroundColor)
                                        .clickable { isEditingName = true }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = editedName, color = BlockInputTextColor)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = "as", color = Color.Black)
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(color = BlockInputBackgroundColor)
                                        .clickable { isEditingType = true }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = displayText(editedType), color = BlockInputTextColor)
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = ":", color = Color.Black)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(color = BlockInputBackgroundColor)
                                        .clickable { isEditingLength = true }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = editedLength, color = BlockInputTextColor)
                                }
                            }
                        }
                    }

                }else {
                    Column (modifier = Modifier.height(80.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "create", color = Color.Black)
                            Spacer(modifier = Modifier.width(5.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color = BlockInputBackgroundColor)
                                    .clickable { isEditingName = true }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(text = editedName, color = BlockInputTextColor)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "as", color = Color.Black)
                            Spacer(modifier = Modifier.width(5.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color = BlockInputBackgroundColor)
                                    .clickable { isEditingType = true }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(text = displayText(editedType), color = BlockInputTextColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun displayText(selectedType: DataType): String{
    return when(selectedType){
        DataType.INTEGER -> "integer"
        DataType.STRING -> "text"
        DataType.BOOLEAN -> "boolean"
        DataType.ARR_INT -> "array (integer)"
        DataType.ARR_STR -> "array (text)"
        DataType.ARR_BOOL -> "array (boolean)"
        else -> "pupu"
    }
}