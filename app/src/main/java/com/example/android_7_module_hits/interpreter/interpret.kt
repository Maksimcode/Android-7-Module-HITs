package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent
import com.example.android_7_module_hits.blocks.BlockType
import com.example.android_7_module_hits.blocks.ConditionBlock
import com.example.android_7_module_hits.blocks.DataType
import com.example.android_7_module_hits.blocks.ElseIfBlock
import com.example.android_7_module_hits.blocks.FunsBlock
import com.example.android_7_module_hits.blocks.FunsType

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

            while (current != null && current != currentBlock.EndBlock) {
                if (insideIfBody) {
                    try{
                        interpret(current, state)
                    } catch (e : Exception) {
                        InterpreterLogger.logError("Ошибка в блоке ${current.type}: ${e.message}")
                    }
                }
                current = current.child
            }


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

            while (current != null && current != currentBlock.EndBlock && insideElseIfBody) {
                try{
                    interpret(current, state)
                } catch (e : Exception) {
                    InterpreterLogger.logError("Ошибка в блоке ${current.type}: ${e.message}")
                }
                current = current.child
            }

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

            while (current != null && current != currentBlock.EndBlock && insideElseBody) {
                try{
                    interpret(current, state)
                } catch (e : Exception) {
                    InterpreterLogger.logError("Ошибка в блоке ${current.type}: ${e.message}")
                }
                current = current.child
            }

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
                try{
                    interpret(current, state)
                } catch (e : Exception) {
                    InterpreterLogger.logError("Ошибка в блоке ${block.type}: ${e.message}")
                    break
                }
                current = current.child
                insideWhileBody = state.setCondition(logicalExpression)
                if (current == currentBlock.EndBlock && insideWhileBody){
                    current = currentBlock.child
                }
            }


            return
        }

        is BlockContent.For -> {
            val counter = content.variable
            val startValue = content.initValue
            val logicalExpression = content.expression
            val update = content.construct

            if (!currentBlock.hasEndBlock()){
                throw IllegalStateException("Отсутствие привязанного блока End")
            }

            state.declareVariable(
                BlockContent.Declare(
                    type = DataType.INTEGER,
                    name = counter)
            )

            state.assignValue(
                BlockContent.Assignment(
                    name = counter,
                    value = startValue
                )
            )

            val conditionResult = state.setCondition(logicalExpression)

            var current: Block? = currentBlock.child
            var insideForBody = conditionResult
            if (insideForBody) state.enterScope()

            while (insideForBody && current != null) {
                try{
                    interpret(current, state)
                } catch (e : Exception) {
                    InterpreterLogger.logError("Ошибка в блоке ${block.type}: ${e.message}")
                    break
                }

                state.assignValue(
                    BlockContent.Assignment(
                        name = counter,
                        value = update
                    )
                )

                current = current.child
                insideForBody = state.setCondition(logicalExpression)
                if (current == currentBlock.EndBlock && insideForBody){
                    current = currentBlock.child
                }
            }

            return
        }

        is BlockContent.Functions -> {
            if (currentBlock is FunsBlock){
                when(currentBlock.function){
                    FunsType.PRINT -> state.printValue(content.comParam)
                    FunsType.SWAP -> state.swapping(content.firstSw, content.secondSw)
                    else -> println("pupu")
                }
            }
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

    currentBlock.child?.let {
        try{
            interpret(it, state)
        } catch (e : Exception) {
            InterpreterLogger.logError("Ошибка в блоке ${block.type}: ${e.message}")
        }
    }
}