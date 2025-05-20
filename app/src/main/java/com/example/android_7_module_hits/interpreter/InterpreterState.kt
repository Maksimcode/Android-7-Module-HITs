package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.Blocks.BlockContent
import com.example.android_7_module_hits.Blocks.DataType

class InterpreterState {
    private val variables = mutableMapOf<String, VariableContent>()

//    TODO: parsing variables by commas
    fun declareVariable(content: BlockContent.Declare) {
        if (content.name.isBlank()) throw IllegalArgumentException("Имя переменной не может быть пустым")

        if (variables.containsKey(content.name)) throw IllegalArgumentException("Переменная уже объявлена")
        variables[content.name] = VariableContent(
            type = content.type,
            value = content.value)
    }

    fun assignValue(content: BlockContent.Assignment) {
        if (!variables.containsKey(content.name)) {
            throw IllegalStateException("Переменная ${content.name} не объявлена")
        }

        val oldValue = variables[content.name]
        val type = oldValue?.type
        val newValue = content.value


        val result:Any? = when(type){
            DataType.INTEGER -> {
                try {
                    evaluateExpression(newValue ?: "0")
                } catch (e: Exception) {
                    throw e
                }
            }
            DataType.STRING -> {
                try {
                    evaluateStringExpression(newValue)
                } catch (e: Exception) {
                    throw IllegalArgumentException("Ошибка при обработке строкового выражения", e)
                }
            }
            DataType.BOOLEAN -> {
                try{
                    parseBooleanExpression(newValue)
                }
                catch (e: Exception){
                    throw IllegalArgumentException("Ошибка при обработке булевого выражения", e)
                }
            }
            else -> {
                println("i can't do it now")
            }
        }


        if (type != null && result != null){
            variables[content.name] = VariableContent(
                type = type,
                value = result)
        }
    }

    fun setCondition(expr: String): Boolean {
        val trimmedExpr = expr.trim()
        return parseBooleanExpression(trimmedExpr)
    }

    fun getVariables(): Map<String, VariableContent> = variables.toMap()

    private fun parseBooleanExpression(expr: String): Boolean {
        val trimmedExpr = expr.trim()

        val simpleValue = trimmedExpr.toBooleanStrictOrNull()
        if (simpleValue != null) return simpleValue

        if (variables.containsKey(trimmedExpr)) {
            val variable = variables[trimmedExpr]!!
            return when (variable.type) {
                DataType.BOOLEAN -> variable.value.toString().toBooleanStrict()
                DataType.INTEGER -> variable.value.toString().toInt() != 0
                else -> false
            }
        }

        return evaluateLogicalExpression(trimmedExpr)
    }

    private fun splitByLogicalOperators(expr: String): List<String> {
        val pattern = Regex("""\s+(and|\|\||or|&&)\s+""", RegexOption.IGNORE_CASE)
        val parts = pattern.split(expr)
        return parts.map { it.trim() }
    }

    private fun evaluateLogicalExpression(expr: String): Boolean {
        val tokens = splitByLogicalOperators(expr)
        if (tokens.size == 1) {
            return evaluateComparison(tokens[0])
        }

        var result = evaluateComparison(tokens[0])

        for (i in 1 until tokens.size step 2) {
            val operator = tokens[i]
            val right = evaluateComparison(tokens[i + 1])

            result = when (operator.lowercase()) {
                "and", "&&" -> result && right
                "or", "||" -> result || right
                else -> throw IllegalArgumentException("Неизвестный логический оператор: $operator")
            }
        }

        return result
    }

    private fun evaluateComparison(expr: String): Boolean {
        val comparisonRegex = Regex("""(.+?)\s*([=!><]=?|!=)\s*(.+)""")
        val match = comparisonRegex.matchEntire(expr.trim()) ?: throw IllegalArgumentException("Неверный формат условия: $expr")

        val (leftStr, op, rightStr) = match.destructured

        val left = resolveValue(leftStr.trim())
        val right = resolveValue(rightStr.trim())

        return compareValues(left, right, op)
    }

    private fun resolveValue(token: String): Any {
        val lower = token.lowercase()

        // Булевы литералы
        if (lower == "true" || lower == "1") return true
        if (lower == "false" || lower == "0") return false

        // Числа
        if (token.matches(Regex("-?\\d+"))) return token.toInt()

        // Переменные
        if (variables.containsKey(token)) {
            val variable = variables[token]!!
            return when (variable.type) {
                DataType.BOOLEAN -> variable.value.toString().toBooleanStrict()
                DataType.INTEGER -> variable.value.toString().toInt()
                else -> throw IllegalArgumentException("Неподдерживаемый тип переменной: ${variable.type}")
            }
        }

        throw IllegalArgumentException("Неизвестное значение: $token")
    }

    private fun compareValues(left: Any, right: Any, op: String): Boolean {
        val a = convertToComparable(left)
        val b = convertToComparable(right)

        return when(op) {
            "==" -> a == b
            "!=" -> a != b
            ">" -> a > b
            ">=" -> a >= b
            "<" -> a < b
            "<=" -> a <= b
            else -> throw IllegalArgumentException("Неизвестный оператор: $op")
        }
    }

    private fun convertToComparable(value: Any): Int {
        return when(value) {
            is Boolean -> if (value) 1 else 0
            is Int -> value
            else -> throw IllegalArgumentException("Невозможно привести к числу: $value")
        }
    }

    fun String.toBooleanStrictOrNull(): Boolean? =
        when(this.trim().lowercase()) {
            "true", "1" -> true
            "false", "0" -> false
            else -> null
        }

    fun String.toBooleanStrict(): Boolean =
        toBooleanStrictOrNull() ?: throw IllegalArgumentException("Невозможно преобразовать '$this' в Boolean")

    private fun evaluateStringExpression(expr: String): String {
        val tokens = expr.split(Regex("(?<!\\\\)\\+"))
        var result = ""

        for (token in tokens) {
            val trimmedToken = token.trim()

            if (trimmedToken.startsWith("\"") && trimmedToken.endsWith("\"")) {
                result += trimmedToken.substring(1, trimmedToken.length - 1)
            }
            else if (variables.containsKey(trimmedToken)) {
                val value = variables[trimmedToken]?.value ?: ""
                val variableType = variables[trimmedToken]?.type

                if (variableType == DataType.STRING) {
                    result += value
                } else if (variableType == DataType.INTEGER) {
                    result += value.toString().toIntOrNull() ?: 0
                }
            }
            else {
                result += trimmedToken
            }

        }

        return result
    }

    private fun evaluateExpression(expr: String): Int {
        val tokens = tokenize(expr.replace("\\s+".toRegex(), ""))
        currentTokenIndex = 0
        return parseAddition(tokens)
    }

    private fun tokenize(input: String): List<String> {
        return input.split(Regex("(?<=[+\\-*/%()])|(?=[+\\-*/%()])"))
            .filter { it.isNotBlank() }
    }

    private var currentTokenIndex = 0

    private fun parseAddition(tokens: List<String>): Int {
        var result = parseMultiplication(tokens)

        while (currentTokenIndex < tokens.size && (tokens[currentTokenIndex] == "+" || tokens[currentTokenIndex] == "-")) {
            val op = tokens[currentTokenIndex]
            currentTokenIndex++
            val right = parseMultiplication(tokens)
            result = when (op) {
                "+" -> result + right
                "-" -> result - right
                else -> result
            }
        }

        return result
    }

    private fun parseMultiplication(tokens: List<String>): Int {
        var result = parsePrimary(tokens)

        while (currentTokenIndex < tokens.size && setOf("*", "/", "%").contains(tokens[currentTokenIndex])) { // ✅ Добавлен %
            val op = tokens[currentTokenIndex]
            currentTokenIndex++

            val right = parsePrimary(tokens)
            result = when (op) {
                "*" -> result * right
                "/" -> {
                    if (right == 0) throw ArithmeticException("Деление на ноль")
                    result / right
                }
                "%" -> {
                    if (right == 0) throw ArithmeticException("Деление по модулю на ноль")
                    result % right
                }
                else -> result
            }
        }

        return result
    }

    private fun parsePrimary(tokens: List<String>): Int {
        val token = tokens[currentTokenIndex++]

        var variableToken: Int
        if (variables.containsKey(token)){
            val value = variables[token]
            val type = when (value){
                is BlockContent.Declare -> value.type
                else -> null
            }
            if(type != null && (type == DataType.INTEGER)){
                variableToken = when(value){
                    is BlockContent.Declare -> value.value.toString().toInt()
                    else -> 0
                }
            }
        }

        return when {
            token == "(" -> {
                val result = parseAddition(tokens)
                if (currentTokenIndex >= tokens.size || tokens.getOrNull(currentTokenIndex) != ")") {
                    throw IllegalArgumentException("Не хватает закрывающей скобки")
                }
                currentTokenIndex++
                result
            }
            token.matches(Regex("-?\\d+")) -> token.toInt()
            variables.containsKey(token) -> {
                val value = variables[token]
                val type = when (value){
                    is BlockContent.Declare -> value.type
                    else -> null
                }
                if(type != null && (type == DataType.INTEGER)){
                    when(value){
                        is BlockContent.Declare -> value.value.toString().toInt()
                        else -> 0
                    }
                } else{
                    0
                }
            }
            else -> throw IllegalArgumentException("Неизвестная переменная: $token")
        }
    }
}