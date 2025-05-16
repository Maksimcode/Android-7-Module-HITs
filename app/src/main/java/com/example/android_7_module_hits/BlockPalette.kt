package com.example.android_7_module_hits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_7_module_hits.Blocks.AssignmentBlock
import com.example.android_7_module_hits.Blocks.Block
import com.example.android_7_module_hits.Blocks.BlockType
import com.example.android_7_module_hits.Blocks.ConditionBlock
import com.example.android_7_module_hits.Blocks.DataType
import com.example.android_7_module_hits.Blocks.DeclarationBlock
import com.example.android_7_module_hits.Blocks.EndBlock
import com.example.android_7_module_hits.ui.uiblocks.BlockTemplate
import com.example.android_7_module_hits.ui.uiblocks.availableBlocks

@Composable
fun BlockPalette(onBlockSelected: (Block) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Блоки")
        Spacer(modifier = Modifier.height(8.dp))
        availableBlocks.forEach { template ->
            BlockPaletteItem(template = template, onBlockSelected = onBlockSelected)
        }
    }
}

@Composable
fun BlockPaletteItem(template: BlockTemplate, onBlockSelected: (Block) -> Unit) {
    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .clickable {
                val newBlock = when (template.type) {
                    BlockType.DECLARE ->
                        DeclarationBlock(variableType = DataType.INTEGER, variableName = "Variable")
                    BlockType.ASSIGN ->
                        AssignmentBlock(variableName = "Variable", initialValue = "0")
                    BlockType.CONDITION ->
                        ConditionBlock(firstExpression = "0", operator = "==", secondExpression = "0")
                    BlockType.END ->
                        EndBlock()
                    else -> throw IllegalArgumentException("Unknown block type")
                }
                onBlockSelected(newBlock)
            }
            .padding(8.dp)
            .width(100.dp)
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = template.title)
    }
}