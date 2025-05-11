package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.Blocks.Block
import com.example.android_7_module_hits.Blocks.BlockContent

fun interpret(block: Block, state: InterpreterState) {
    when (val content = block.content) {
        is BlockContent.Declare -> {
            val variableName = content.name?.trim() ?: "unnamed"
            state.declareVariable(variableName)
        }
        is BlockContent.Assignment -> {
            val variableName = content.name?.trim() ?: "unnamed"
            val expr = content.value?.trim() ?: "0"

            state.assignValue(variableName, expr)
        }
        is BlockContent.Condition -> {
            val firstExpr = content.firstPart?.trim() ?: "0"
            val operate = content.operator?.trim() ?: "=="
            val secondExpr = content.secondPart?.trim() ?: "0"

            state.setCondition(firstExpr, operate, secondExpr)
        }
        is BlockContent.End -> {}
    }

    block.child?.let { child ->
        interpret(child, state)
    }
}