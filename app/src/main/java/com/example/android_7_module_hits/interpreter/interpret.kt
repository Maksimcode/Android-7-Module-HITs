package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent
import com.example.android_7_module_hits.blocks.ConditionBlock
import com.example.android_7_module_hits.blocks.ElseIfBlock

fun interpret(block: Block, state: InterpreterState) {
    var currentBlock = block

    when (val content = currentBlock.content) {
        is BlockContent.Declare -> {
            state.declareVariable(BlockContent.Declare(content.type, content.name, content.value, content.length))
        }

        is BlockContent.Assignment -> {
            state.assignValue(BlockContent.Assignment(content.name, content.value))
        }

        is BlockContent.Condition -> {
            val logicalExpression = content.expression

            if (!currentBlock.hasEndBlock()){
                throw IllegalStateException("Отсутствие привязанного блока End")
            }

            state.enterScope()

            val conditionResult = state.setCondition(logicalExpression)

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

        is BlockContent.ElseIf -> {
            val logicalExpression = content.expression

            if (!currentBlock.hasEndBlock()){
                throw IllegalStateException("Отсутствие привязанного блока End")
            }

            if (!currentBlock.hasRootBlock()){
                throw IllegalStateException("Отсутствие if/else if до..")
            }

            var conditionResult: Boolean = state.setCondition(logicalExpression)
            var temp = currentBlock.rootBlock?.rootBlock
            var anyPrevTrue = false
            while(temp != null){
                when (temp) {
                    is ConditionBlock, is ElseIfBlock -> {
                        val tempContent = temp.content
                        if (tempContent is BlockContent.Condition){
                            val result = state.setCondition(tempContent.expression)
                            if (result) {
                                anyPrevTrue = true
                                break
                            }
                        }
                        if (tempContent is BlockContent.ElseIf){
                            val result = state.setCondition(tempContent.expression)
                            if (result) {
                                anyPrevTrue = true
                                break
                            }
                        }
                    }
                }
                temp = temp.rootBlock?.rootBlock
            }

            var current: Block? = currentBlock.child
            var insideElseIfBody = conditionResult && !anyPrevTrue
            if (insideElseIfBody) state.enterScope()

            while (current != null && current.content !is BlockContent.End && insideElseIfBody) {
                interpret(current, state)
                current = current.child
            }


            current?.child?.let { interpret(it, state) }
            return
        }

        is BlockContent.Else -> {
            if (!currentBlock.hasEndBlock()){
                throw IllegalStateException("Отсутствие привязанного блока End")
            }

            if (!currentBlock.hasRootBlock()){
                throw IllegalStateException("Отсутствие if/else if до..")
            }

            var temp = currentBlock.rootBlock?.rootBlock
            var anyPrevTrue = false
            while(temp != null){
                when (temp) {
                    is ConditionBlock, is ElseIfBlock -> {
                        val tempContent = temp.content
                        if (tempContent is BlockContent.Condition){
                            val result = state.setCondition(tempContent.expression)
                            if (result) {
                                anyPrevTrue = true
                                break
                            }
                        }
                        if (tempContent is BlockContent.ElseIf){
                            val result = state.setCondition(tempContent.expression)
                            if (result) {
                                anyPrevTrue = true
                                break
                            }
                        }
                    }
                }
                temp = temp.rootBlock?.rootBlock
            }

            var current: Block? = currentBlock.child
            var insideElseBody = !anyPrevTrue
            if (insideElseBody) state.enterScope()

            while (current != null && current.content !is BlockContent.End && insideElseBody) {
                interpret(current, state)
                current = current.child
            }


            current?.child?.let { interpret(it, state) }
            return
        }

        is BlockContent.While -> {
            val logicalExpression = content.expression

            if (!currentBlock.hasEndBlock()){
                throw IllegalStateException("Отсутствие привязанного блока End")
            }

            val conditionResult = state.setCondition(logicalExpression)

            var current: Block? = currentBlock.child
            var insideWhileBody = conditionResult
            if (insideWhileBody) state.enterScope()

            while (insideWhileBody && current != null) {
                interpret(current, state)
                current = current.child
                insideWhileBody = state.setCondition(logicalExpression)
                if (current == currentBlock.EndBlock && insideWhileBody){
                    current = currentBlock.child
                }
            }


            current?.child?.let { interpret(it, state) }
            return
        }

        is BlockContent.End -> {
            if (!currentBlock.hasRootBlock()){
                throw IllegalStateException("Отсутствие привязанного блока HasBody")
            }
            state.exitScope()
        }
        else -> {
            println("пока хз")
        }
    }

    currentBlock.child?.let { interpret(it, state) }
}