package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent
import com.example.android_7_module_hits.blocks.BlockHasBody
import com.example.android_7_module_hits.blocks.ConditionBlock
import com.example.android_7_module_hits.blocks.DataType
import com.example.android_7_module_hits.blocks.ElseIfBlock
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

            val conditionResult = state.setCondition(logicalExpression)

            var insideIfBody = conditionResult

            if (insideIfBody) {
                state.enterScope()
                (currentBlock as BlockHasBody).nestedChildren.forEach { current ->
                    try {
                        interpret(current, state)
                    } catch (e: Exception) {
                        InterpreterLogger.logError("Ошибка в блоке ${current.type}: ${e.message}")
                    }
                }
                state.exitScope()
            }
            return
        }

        is BlockContent.ElseIf -> {
            val logicalExpression = content.expression
            var conditionResult: Boolean = state.setCondition(logicalExpression)
            var temp = currentBlock.parent
            var anyPrevTrue = false
            while(temp != null && (temp is ConditionBlock || temp is ElseIfBlock)){
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
                temp = temp.parent
            }

            var insideElseIfBody = conditionResult && !anyPrevTrue

            if (insideElseIfBody) {
                state.enterScope()
                (currentBlock as BlockHasBody).nestedChildren.forEach { current ->
                    try {
                        interpret(current, state)
                    } catch (e: Exception) {
                        InterpreterLogger.logError("Ошибка в блоке ${current.type}: ${e.message}")
                    }
                }
                state.exitScope()
            }
            return
        }

        is BlockContent.Else -> {
            var temp = currentBlock.parent
            var anyPrevTrue = false
            while(temp != null && (temp is ConditionBlock || temp is ElseIfBlock)){
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
                temp = temp.parent
            }

            var insideElseBody = !anyPrevTrue

            if (insideElseBody){
                state.enterScope()
                (currentBlock as BlockHasBody).nestedChildren.forEach { current ->
                    try {
                        interpret(current, state)
                    } catch (e: Exception) {
                        InterpreterLogger.logError("Ошибка в блоке ${current.type}: ${e.message}")
                    }
                }
                state.exitScope()
            }
            state.exitScope()
            return
        }

        is BlockContent.While -> {
            val logicalExpression = content.expression

            val conditionResult = state.setCondition(logicalExpression)

            var insideWhileBody = conditionResult
            state.enterScope()

            while (insideWhileBody) {
                state.enterScope()
                (currentBlock as BlockHasBody).nestedChildren.forEach { current ->
                    try{
                        interpret(current, state)
                    } catch (e : Exception) {
                        InterpreterLogger.logError("Ошибка в блоке ${block.type}: ${e.message}")
                    }
                }
                state.enterScope()
                insideWhileBody = state.setCondition(logicalExpression)
            }

            return
        }

        is BlockContent.For -> {
            val counter = content.variable
            val startValue = content.initValue
            val logicalExpression = content.expression
            val update = content.construct

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

            var insideForBody = conditionResult

            while (insideForBody) {
                state.enterScope()
                insideForBody = state.setCondition(logicalExpression)
                if (insideForBody) {
                    (currentBlock as BlockHasBody).nestedChildren.forEach { current ->
                        try {
                            interpret(current, state)
                        } catch (e: Exception) {
                            InterpreterLogger.logError("Ошибка в блоке ${block.type}: ${e.message}")
                        }
                    }
                }

                insideForBody = state.setCondition(logicalExpression)
                if (insideForBody) {
                    state.assignValue(
                        BlockContent.Assignment(
                            name = counter,
                            value = update
                        )
                    )
                }

                state.exitScope()
            }
            return
        }

        is BlockContent.Functions -> {

            when(content.func){
                FunsType.PRINT -> state.printValue(content.comParam)
                FunsType.SWAP -> {
                    state.swapping(content.firstSw, content.secondSw)
                    println("вызван swap")
                }
                else -> println("pupu")
            }
        }
        is BlockContent.End -> {
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