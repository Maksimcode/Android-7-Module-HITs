package com.example.android_7_module_hits.interpreter
import com.example.android_7_module_hits.data.model.*
import com.example.android_7_module_hits.ui.Blocks.*

class Interpreter {

    fun interpret(blocks: List<UiBlock>, context: ExecutionContext) {
        val blockMap = blocks.associateBy { it.id }

        var currentBlockId = findStartBlock(blocks)?.id

        while (currentBlockId != null) {
            val block = blockMap[currentBlockId] ?: break

            when (block.type) {
                BlockType.DECLARE -> handleDeclare(block, context)
                BlockType.ASSIGN -> handleAssign(block, context)
                else -> {}
            }

            currentBlockId = block.nextBlockId
        }
    }

    private fun handleDeclare(block: UiBlock, context: ExecutionContext) {
        val variableName = block.editableFields["variableName"] as? String ?: return
        if (context.hasVariable(variableName)) {
            throw IllegalStateException("Variable $variableName already declared")
        }
        context.addVariable(variableName)
        println("Declared variable: $variableName")
    }

    private fun handleAssign(block: UiBlock, context: ExecutionContext) {
        val variableName = block.editableFields["variableName"] as? String ?: return

        if (!context.hasVariable(variableName)) {
            throw IllegalStateException("Переменная '$variableName' не объявлена")
        }

        val expression = block.editableFields["expression"] as? String ?: return

        val value = evaluateExpression(expression, context)
        context.setVariable(variableName, value)
        println("Assigned $variableName = $expression → $value")
    }

    private fun evaluateExpression(expr: String, context: ExecutionContext): Int {
        return try {
            expr.toInt()
        } catch (e: NumberFormatException) {
            // Позже реализуем парсер выражений
            0
        }
    }

    private fun findStartBlock(blocks: List<UiBlock>): UiBlock? {
        // Ищем блоки, которые нигде не указаны как nextBlockId
        val childIds = blocks.mapNotNull { it.nextBlockId }.toSet()

        return blocks.firstOrNull { !childIds.contains(it.id) }
    }
}

