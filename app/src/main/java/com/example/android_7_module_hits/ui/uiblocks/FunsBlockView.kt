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
import com.example.android_7_module_hits.blocks.FunsType
import com.example.android_7_module_hits.ui.theme.BlockInputBackgroundColor
import com.example.android_7_module_hits.ui.theme.BlockInputTextColor
import com.example.android_7_module_hits.ui.theme.DeclareColor

@Composable
fun FunsBlockView(content: BlockContent.Functions, block: Block){
    var isEditingFun by remember { mutableStateOf(false) }
    var isEditingComParam by remember { mutableStateOf(false) }
    var isEditingFirstSwap by remember { mutableStateOf(false) }
    var isEditingSecondSwap by remember { mutableStateOf(false) }

    var editedFun by remember(content.func ?: FunsType.PRINT) { mutableStateOf(content.func ?: FunsType.PRINT) }
    var editedComParam by remember(content.comParam ?: "Variable") { mutableStateOf(content.comParam ?: "Variable") }
    var editedFirstSwap by remember(content.firstSw ?: "a") { mutableStateOf(content.firstSw ?: "a") }
    var editedSecondSwap by remember(content.secondSw ?: "b") { mutableStateOf(content.secondSw ?: "b") }

    val options = listOf(FunsType.PRINT, FunsType.SWAP)


    Card(
        modifier = Modifier
            .width(210.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = DeclareColor)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (isEditingComParam) {
                Dialog(onDismissRequest = { isEditingComParam = false }) {
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
                                value = editedComParam,
                                onValueChange = { newText ->
                                    editedComParam = newText
                                },
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        editedComParam = content.comParam ?: "Variable"
                                        isEditingComParam = false
                                    }
                                ) {
                                    Text("Cancel")
                                }
                                TextButton(
                                    onClick = {
                                        content.comParam = editedComParam
                                        isEditingComParam = false
                                    }
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            } else if(isEditingFun){
                DropdownMenu(
                    expanded = isEditingFun,
                    onDismissRequest = { isEditingFun = false }) {
                    options.forEach { func ->
                        DropdownMenuItem(
                            text = {
                                Text(text = displayTextFuns(func))
                            },
                            onClick = {
                                editedFun = func
                                content.func = editedFun
                                isEditingFun = false
                            }
                        )

                    }
                }

            } else {
                if(editedFun == FunsType.SWAP){
                    if (isEditingFirstSwap) {
                        Dialog(onDismissRequest = { isEditingFirstSwap = false }) {
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
                                        value = editedFirstSwap,
                                        onValueChange = { newText ->
                                            editedFirstSwap = newText
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
                                                editedFirstSwap = content.firstSw ?: "0"
                                                isEditingFirstSwap = false
                                            }
                                        ) {
                                            Text("Cancel")
                                        }

                                        TextButton(
                                            onClick = {
                                                content.firstSw = editedFirstSwap
                                                isEditingFirstSwap = false
                                            }
                                        ) {
                                            Text("Save")
                                        }
                                    }
                                }
                            }
                        }
                    } else if (isEditingSecondSwap){
                            Dialog(onDismissRequest = { isEditingSecondSwap = false }) {
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
                                            value = editedSecondSwap,
                                            onValueChange = { newText ->
                                                editedSecondSwap = newText
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
                                                    editedSecondSwap = content.secondSw ?: "0"
                                                    isEditingSecondSwap = false
                                                }
                                            ) {
                                                Text("Cancel")
                                            }

                                            TextButton(
                                                onClick = {
                                                    content.secondSw = editedSecondSwap
                                                    isEditingSecondSwap = false
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
                                Text(text = "do", color = Color.Black)
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(color = BlockInputBackgroundColor)
                                        .clickable { isEditingFun = true }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = displayTextFuns(editedFun), color = BlockInputTextColor)
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = "of 2 Variables", color = Color.Black)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Variable 1:", color = Color.Black)
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(color = BlockInputBackgroundColor)
                                        .clickable { isEditingFirstSwap = true }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = editedFirstSwap, color = BlockInputTextColor)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Variable 2:", color = Color.Black)
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(color = BlockInputBackgroundColor)
                                        .clickable { isEditingSecondSwap = true }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = editedSecondSwap, color = BlockInputTextColor)
                                }
                            }
                        }
                    }

                }else {
                    Column (modifier = Modifier.height(80.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "do", color = Color.Black)
                            Spacer(modifier = Modifier.width(5.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color = BlockInputBackgroundColor)
                                    .clickable { isEditingFun = true }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(text = displayTextFuns(editedFun), color = BlockInputTextColor)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color = BlockInputBackgroundColor)
                                    .clickable { isEditingComParam = true }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(text = editedComParam, color = BlockInputTextColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun displayTextFuns(selectedFun: FunsType): String{
    return when(selectedFun){
        FunsType.PRINT -> "print value of "
        FunsType.SWAP -> "swap"
        else -> "pupu"
    }
}