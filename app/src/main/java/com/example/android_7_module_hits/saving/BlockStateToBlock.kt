package com.example.android_7_module_hits.saving

import com.example.android_7_module_hits.blocks.AssignmentBlock
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockType
import com.example.android_7_module_hits.blocks.ConditionBlock
import com.example.android_7_module_hits.blocks.DeclarationBlock
import com.example.android_7_module_hits.blocks.ElseBlock
import com.example.android_7_module_hits.blocks.ElseIfBlock
import com.example.android_7_module_hits.blocks.EndBlock
import com.example.android_7_module_hits.blocks.ForBlock
import com.example.android_7_module_hits.blocks.FunsBlock
import com.example.android_7_module_hits.blocks.WhileBlock

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

        BlockType.WHILE -> {
            val contentState = content as BlockContentState.While
            WhileBlock(
                logicalExpression = contentState.expression
            ).apply {
                this.position = position
            }
        }
        BlockType.FOR -> {
            val contentState = content as BlockContentState.For
            ForBlock(
                counter = contentState.variable,
                startValue = contentState.initValue,
                logicalExpression = contentState.expression,
                update = contentState.construct
            ).apply {
                this.position = position
            }
        }

        BlockType.FUNCTIONS -> {
            val contentState = content as BlockContentState.Functions
            FunsBlock(
                function = contentState.func,
                uniParam = contentState.comParam,
                firstSwap = contentState.firstSw,
                secondSwap = contentState.secondSw,
            ).apply {
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
