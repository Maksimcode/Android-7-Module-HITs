package com.example.android_7_module_hits.data.model
import com.example.android_7_module_hits.interpreter.ExecutionContext

public abstract class Block {
    abstract val id: String
}

// Блок объявления переменной (например: int x)
data class DeclareVariableBlock(
    override val id: String,
    val variableName: String,
    val variableType: VariableType
) : Block()

// Блок присваивания (например: x = 5)
data class AssignValueBlock(
    override val id: String,
    val variableName: String,
    val expression: Expression
) : Block()

// Выражение (может быть числом или арифметической операцией)
sealed class Expression {
    abstract fun evaluate(context: ExecutionContext): Int
}

// Числовая константа
data class NumberExpression(val value: Int) : Expression() {
    override fun evaluate(context: ExecutionContext): Int = value
}

// Операция сложения
data class AddExpression(val left: Expression, val right: Expression) : Expression() {
    override fun evaluate(context: ExecutionContext): Int =
        left.evaluate(context) + right.evaluate(context)
}

// Операция вычитания
data class SubtractExpression(val left: Expression, val right: Expression) : Expression() {
    override fun evaluate(context: ExecutionContext): Int =
        left.evaluate(context) - right.evaluate(context)
}

// Операция умножения
data class MultiplyExpression(val left: Expression, val right: Expression) : Expression() {
    override fun evaluate(context: ExecutionContext): Int =
        left.evaluate(context) * right.evaluate(context)
}

// Операция деления
data class DivideExpression(val left: Expression, val right: Expression) : Expression() {
    override fun evaluate(context: ExecutionContext): Int =
        left.evaluate(context) / right.evaluate(context)
}

// Использование переменной в выражении
data class VariableExpression(val name: String) : Expression() {
    override fun evaluate(context: ExecutionContext): Int =
        context.getVariable(name)
}

enum class VariableType {
    INT
}

enum class BlockType {
    DECLARE, ASSIGN, EXPRESSION
}