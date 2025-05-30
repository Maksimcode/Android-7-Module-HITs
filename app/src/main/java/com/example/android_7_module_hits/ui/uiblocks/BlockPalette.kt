package com.example.android_7_module_hits.ui.uiblocks

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
import androidx.compose.ui.unit.dp
import com.example.android_7_module_hits.blocks.AssignmentBlock
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockType
import com.example.android_7_module_hits.blocks.ConditionBlock
import com.example.android_7_module_hits.blocks.DataType
import com.example.android_7_module_hits.blocks.DeclarationBlock
import com.example.android_7_module_hits.blocks.ElseBlock
import com.example.android_7_module_hits.blocks.ElseIfBlock
import com.example.android_7_module_hits.blocks.ForBlock
import com.example.android_7_module_hits.blocks.FunsBlock
import com.example.android_7_module_hits.blocks.FunsType
import com.example.android_7_module_hits.blocks.WhileBlock

@Composable
fun BlockPalette(onBlockSelected: (Block) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Select block")
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
            .clickable {
                val newBlock = when (template.type) {
                    BlockType.DECLARE ->
                        DeclarationBlock(variableType = DataType.INTEGER, variableName = "Variable")

                    BlockType.ASSIGN ->
                        AssignmentBlock(variableName = "Variable", initialValue = "0")

                    BlockType.CONDITION ->
                        ConditionBlock(logicalExpression = "true")

                    BlockType.ELSE_IF ->
                        ElseIfBlock(logicalExpression = "true")

                    BlockType.ELSE ->
                        ElseBlock()

                    // BlockType.END ->
                    //     EndBlock()

                    BlockType.WHILE ->
                        WhileBlock(logicalExpression = "false")

                    BlockType.FOR ->
                        ForBlock(
                            counter = "i",
                            startValue = "0",
                            logicalExpression = "i < 10",
                            update = "i + 1"
                        )

                    BlockType.FUNCTIONS ->
                        FunsBlock(
                            function = FunsType.PRINT,
                            uniParam = "Variable"
                        )

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