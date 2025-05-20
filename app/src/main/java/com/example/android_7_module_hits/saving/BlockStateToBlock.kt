package com.example.android_7_module_hits.saving

import com.example.android_7_module_hits.Blocks.AssignmentBlock
import com.example.android_7_module_hits.Blocks.Block
import com.example.android_7_module_hits.Blocks.BlockType
import com.example.android_7_module_hits.Blocks.ConditionBlock
import com.example.android_7_module_hits.Blocks.DeclarationBlock
import com.example.android_7_module_hits.Blocks.ElseBlock
import com.example.android_7_module_hits.Blocks.ElseIfBlock
import com.example.android_7_module_hits.Blocks.EndBlock

fun BlockState.toBlock(): Block {
    val position = androidx.compose.ui.geometry.Offset(positionX, positionY)

    return when (type) {
        BlockType.DECLARE -> {
            val contentState = content as BlockContentState.Declare
            DeclarationBlock(
                variableType = contentState.type,
                variableName = contentState.name,
                initialValue = contentState.value,
                arrayLength = contentState.length
            ).apply {
                this.position = position
            }
        }
        BlockType.ASSIGN -> {
            val contentState = content as BlockContentState.Assignment
            AssignmentBlock(
                variableName = contentState.name,
                initialValue = contentState.value
            ).apply {
                this.position = position
            }
        }
        BlockType.CONDITION -> {
            val contentState = content as BlockContentState.Condition
            ConditionBlock(
                logicalExpression = contentState.expression
            ).apply {
                this.position = position
            }
        }
        BlockType.ELSE_IF -> {
            val contentState = content as BlockContentState.ElseIf
            ElseIfBlock(
                logicalExpression = contentState.expression
            ).apply {
                this.position = position
            }
        }
        BlockType.ELSE -> {
            ElseBlock().apply {
                this.position = position
            }
        }
        BlockType.END -> {
            EndBlock().apply {
                this.position = position
            }
        }
        else -> throw IllegalArgumentException("Неизвестный тип блока: $type")
    }
}
