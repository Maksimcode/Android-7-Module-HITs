package com.example.android_7_module_hits.interpreter
import com.example.android_7_module_hits.data.model.*

class Interpreter {

    fun interpret(blocks: List<Block>, context: ExecutionContext) {
        blocks.forEach { execute(it, context) }
    }

    private fun execute(block: Block, context: ExecutionContext) {
        when (block) {
            is DeclareVariableBlock -> handleDeclare(block, context)
            is AssignValueBlock -> handleAssign(block, context)
            else -> throw IllegalArgumentException("Unknown block type: ${block.javaClass}")
        }
    }

    private fun handleDeclare(block: DeclareVariableBlock, context: ExecutionContext) {
        // Проверяем, существует ли уже такая переменная
        if (context.hasVariable(block.variableName)) {
            throw IllegalStateException("Variable ${block.variableName} already declared")
        }

        // Инициализируем значение по умолчанию (0 для int)
        context.setVariable(block.variableName, 0)
        println("Declared variable: ${block.variableName} as ${block.variableType}")
    }

    private fun handleAssign(block: AssignValueBlock, context: ExecutionContext) {
        // Проверяем, существует ли переменная
        if (!context.hasVariable(block.variableName)) {
            throw IllegalStateException("Variable ${block.variableName} not declared")
        }

        val value = block.expression.evaluate(context)
        context.setVariable(block.variableName, value)
        println("Assigned ${block.variableName} = $value")
    }
}

