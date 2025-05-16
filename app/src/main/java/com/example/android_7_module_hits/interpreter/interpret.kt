package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.Blocks.Block
import com.example.android_7_module_hits.Blocks.BlockContent
import com.example.android_7_module_hits.Blocks.BlockType

fun interpret(block: Block, state: InterpreterState) {
    var currentBlock = block

    when (val content = currentBlock.content) {
        is BlockContent.Declare -> {
            state.declareVariable(BlockContent.Declare(content.type, content.name, content.value))
        }

        is BlockContent.Assignment -> {
            state.assignValue(BlockContent.Assignment(content.name, content.value))
        }

        is BlockContent.Condition -> {
            val firstExpr = content.firstPart?.trim() ?: "0"
            val operate = content.operator?.trim() ?: "=="
            val secondExpr = content.secondPart?.trim() ?: "0"

            if (!currentBlock.hasEndBlock()){
                throw IllegalStateException("Отсутствие привязанного блока End")
            }

            val conditionResult = state.setCondition(firstExpr, operate, secondExpr)

            var current: Block? = currentBlock.child
            var insideIfBody = conditionResult

            while (current != null && current.content !is BlockContent.End) {
                if (insideIfBody) {
                    interpret(current, state)
                }
                current = current.child
            }


            current?.child?.let { interpret(it, state) }
            return
        }

        is BlockContent.End -> {
            if (!currentBlock.hasRootBlock()){
                throw IllegalStateException("Отсутствие привязанного блока HasBody")
            }

        }
    }

    currentBlock.child?.let { interpret(it, state) }
}