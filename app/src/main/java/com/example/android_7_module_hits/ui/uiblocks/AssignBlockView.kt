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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.android_7_module_hits.R
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent
import com.example.android_7_module_hits.ui.theme.AssignBlockDimens
import com.example.android_7_module_hits.ui.theme.AssignmentColor
import com.example.android_7_module_hits.ui.theme.BlockInputBackgroundColor
import com.example.android_7_module_hits.ui.theme.BlockInputTextColor

@Composable
fun AssignBlockView(content: BlockContent.Assignment, block: Block) {
    val defaultVariable = stringResource(id = R.string.default_variable)
    val defaultValue = stringResource(id = R.string.default_value)

    var isEditingName by remember { mutableStateOf(false) }
    var isEditingValue by remember { mutableStateOf(false) }
    var editedName by remember(content.name ?: defaultVariable) {
        mutableStateOf(content.name ?: defaultVariable)
    }
    var editedValue by remember(content.value ?: defaultValue) {
        mutableStateOf(content.value ?: defaultValue)
    }

    Card(
        modifier = Modifier
            .width(AssignBlockDimens.cardWidth)
            .padding(AssignBlockDimens.cardPadding),
        elevation = CardDefaults.cardElevation(defaultElevation = AssignBlockDimens.cardElevation),
        colors = CardDefaults.cardColors(containerColor = AssignmentColor)
    ) {
        Column(modifier = Modifier.padding(AssignBlockDimens.cardContentPadding)) {
            if (isEditingName) {
                Dialog(onDismissRequest = { isEditingName = false }) {
                    Card(
                        modifier = Modifier
                            .width(AssignBlockDimens.dialogCardWidth)
                            .padding(AssignBlockDimens.dialogPadding),
                        elevation = CardDefaults.cardElevation(defaultElevation = AssignBlockDimens.cardElevation)
                    ) {
                        Column(modifier = Modifier.padding(AssignBlockDimens.dialogPadding)) {
                            Text(text = stringResource(id = R.string.variable_name_label))
                            Spacer(modifier = Modifier.height(AssignBlockDimens.spacerSmall))
                            TextField(
                                value = editedName,
                                onValueChange = { newText ->
                                    editedName = newText
                                },
                            )
                            Spacer(modifier = Modifier.height(AssignBlockDimens.spacerLarge))
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        editedName = content.name ?: defaultVariable
                                        isEditingName = false
                                    }
                                ) {
                                    Text(text = stringResource(id = R.string.cancel))
                                }
                                TextButton(
                                    onClick = {
                                        content.name = editedName
                                        isEditingName = false
                                    }
                                ) {
                                    Text(text = stringResource(id = R.string.ok))
                                }
                            }
                        }
                    }
                }
            } else if (isEditingValue) {
                Dialog(onDismissRequest = { isEditingValue = false }) {
                    Card(
                        modifier = Modifier
                            .width(AssignBlockDimens.dialogCardWidth)
                            .padding(AssignBlockDimens.dialogPadding),
                        elevation = CardDefaults.cardElevation(defaultElevation = AssignBlockDimens.cardElevation)
                    ) {
                        Column(modifier = Modifier.padding(AssignBlockDimens.dialogPadding)) {
                            Text(text = stringResource(id = R.string.variable_value_label))
                            Spacer(modifier = Modifier.height(AssignBlockDimens.spacerSmall))
                            TextField(
                                value = editedValue,
                                onValueChange = { newText ->
                                    editedValue = newText
                                }
                            )
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        editedValue = content.value ?: defaultValue
                                        isEditingValue = false
                                    }
                                ) {
                                    Text(text = stringResource(id = R.string.cancel))
                                }
                                TextButton(
                                    onClick = {
                                        content.value = editedValue
                                        isEditingValue = false
                                    }
                                ) {
                                    Text(text = stringResource(id = R.string.ok))
                                }
                            }
                        }
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.set_keyword) + " ", color = Color.Black)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(AssignBlockDimens.boxCornerRadius))
                            .background(color = BlockInputBackgroundColor)
                            .clickable { isEditingName = true }
                            .padding(
                                horizontal = AssignBlockDimens.boxPaddingHorizontal,
                                vertical = AssignBlockDimens.boxPaddingVertical
                            )
                    ) {
                        Text(text = editedName, color = BlockInputTextColor)
                    }
                    Text(text = " " + stringResource(id = R.string.to_keyword) + " ", color = Color.Black)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(AssignBlockDimens.boxCornerRadius))
                            .background(color = BlockInputBackgroundColor)
                            .clickable { isEditingValue = true }
                            .padding(
                                horizontal = AssignBlockDimens.boxPaddingHorizontal,
                                vertical = AssignBlockDimens.boxPaddingVertical
                            )
                    ) {
                        Text(text = editedValue, color = BlockInputTextColor)
                    }
                }
            }
        }
    }
}
