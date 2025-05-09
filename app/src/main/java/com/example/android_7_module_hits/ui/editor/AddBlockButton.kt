package com.example.android_7_module_hits.ui.editor

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.UUID

import com.example.android_7_module_hits.ui.Blocks.UiBlockType


@Composable
fun AddBlockButton(
    type: UiBlockType,
    onClick: (String) -> Unit
) {
    Button(
        onClick = {
            val uniqueId = generateRandomId()
            onClick(uniqueId)
        },
        modifier = Modifier.padding(8.dp)
    ) {
        Text(getButtonText(type))
    }
}

private fun getButtonText(type: UiBlockType): String = when (type) {
    UiBlockType.DECLARE_VARIABLE -> "Declare Variable"
    UiBlockType.ASSIGN_VALUE -> "Assign Value"
}

private fun generateRandomId(): String {
    return UUID.randomUUID().toString()
}