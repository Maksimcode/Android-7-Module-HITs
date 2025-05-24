package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.blocks.BlockContent
import com.example.android_7_module_hits.blocks.DataType

class InterpreterState {
    private val variables = mutableMapOf<String, VariableContent>()

//    TODO: parsing variables by commas
    fun declareVariable(content: BlockContent.Declare) {
        if (content.name.isBlank()) throw IllegalArgumentException("Имя переменной не может быть пустым")

        if (variables.containsKey(content.name)) throw IllegalArgumentException("Переменная уже объявлена")
        variables[content.name] = VariableContent(
            type = content.type,
            value = content.value,
            arrayLength = evaluateExpression(content.length).toString())
    }

    fun assignValue(content: BlockContent.Assignment) {
        val name = content.name.trim()
        val arrayAccess = parseArrayAccess(name)

        var arrayName: String? = null
        var index: Int = 0
        var isIndexAccess = false

        // Если это обращение к элементу массива
        if (arrayAccess != null) {
            isIndexAccess = true
            arrayName = arrayAccess.arrayName
            val indexExpr = arrayAccess.indexExpr
            index = evaluateExpression(indexExpr)

            val arrayVar = variables[arrayName]
                ?: throw IllegalArgumentException("Массив $arrayName не объявлен")

            if (index < 0 || index >= (arrayVar.value as List<*>).size) {
                throw IndexOutOfBoundsException("Индекс $index вне диапазона")
            }
        }

        // Определяем настоящую переменную для работы
        val realName = arrayName ?: name
        val oldValue = variables[realName]
            ?: throw IllegalStateException("Переменная $realName не объявлена")

        val type = oldValue.type
        val newValue = content.value

        val result: Any? = when(type) {
            DataType.INTEGER -> evaluateExpression(newValue ?: "0")
            DataType.STRING -> evaluateStringExpression(newValue)
            DataType.BOOLEAN -> parseBooleanExpression(newValue)

            in listOf(DataType.ARR_INT, DataType.ARR_STR, DataType.ARR_BOOL) -> {
                if (isIndexAccess) {
                    val arrayValue = oldValue.value as? MutableList<*>
                        ?: throw IllegalArgumentException("Неверный тип массива")

                    when (type) {
                        DataType.ARR_INT -> {
                            val intValue = evaluateExpression(newValue ?: "0")
                            val mutableArray = arrayValue.toMutableList() as MutableList<Int>
                            mutableArray[index] = intValue
                            mutableArray
                        }
                        DataType.ARR_STR -> {
                            val strValue = evaluateStringExpression(newValue)
                            val mutableArray = arrayValue.toMutableList() as MutableList<String>
                            mutableArray[index] = strValue
                            mutableArray
                        }
                        DataType.ARR_BOOL -> {
                            val boolValue = parseBooleanExpression(newValue)
                            val mutableArray = arrayValue.toMutableList() as MutableList<Boolean>
                            mutableArray[index] = boolValue
                            mutableArray
                        }
                        else -> throw IllegalArgumentException("Тип массива не поддерживается")
                    }
                } else {
                    // Полное присвоение массива
                    when (type) {
                        DataType.ARR_INT -> {
                            val cleanedValue = removeOuterBraces(newValue)
                            val elements = splitArrayElements(cleanedValue)
                            val arrLen = oldValue.arrayLength.toInt()
                            val res = mutableListOf<Int>()
                            for (i in 0 until arrLen) {
                                res.add(evaluateExpression(elements.getOrNull(i) ?: "0"))
                            }
                            res
                        }
                        DataType.ARR_STR -> {
                            val elements = splitArrayElements(newValue)
                            val arrLen = oldValue.arrayLength.toInt()
                            val res = mutableListOf<String>()
                            for (i in 0 until arrLen) {
                                res.add(evaluateStringExpression(elements.getOrNull(i) ?: ""))
                            }
                            res
                        }
                        DataType.ARR_BOOL -> {
                            val elements = splitArrayElements(newValue)
                            val arrLen = oldValue.arrayLength.toInt()
                            val res = mutableListOf<Boolean>()
                            for (i in 0 until arrLen) {
                                res.add(parseBooleanExpression(elements.getOrNull(i) ?: "false"))
                            }
                            res
                        }
                        else -> throw IllegalArgumentException("Тип массива не поддерживается")
                    }
                }
            }

            else -> throw IllegalArgumentException("Тип переменной не поддерживается")
        }

        if (type != null && result != null) {
            variables[realName] = VariableContent(
                type = type,
                value = result,
                arrayLength = oldValue.arrayLength
            )
        }
    }


    fun setCondition(expr: String): Boolean {
        val trimmedExpr = expr.trim()
        return parseBooleanExpression(trimmedExpr)
    }

    fun getVariables(): Map<String, VariableContent> = variables.toMap()

    fun splitArrayElements(value: String): List<String> {
        val result = mutableListOf<String>()
        val trimmedValue = value.trim()
        val processedValue = removeOuterBraces(trimmedValue)

        var current = StringBuilder()
        var inQuotes = false

        for (char in processedValue) {
            when {
                char == '"' -> {
                    inQuotes = !inQuotes
                    current.append(char)
                }

                char == ',' && !inQuotes -> {
                    result.add(current.toString().trim())
                    current.clear()
                }

                else -> current.append(char)
            }
        }

        if (current.isNotEmpty()) {
            result.add(current.toString().trim())
        }

        return result
    }


    private fun removeOuterBraces(value: String): String {
        var inQuotes = false
        var braceDepth = 0
        val result = StringBuilder()

        for (c in value) {
            when {
                c == '"' -> {
                    inQuotes = !inQuotes
                    result.append(c)
                }
                !inQuotes -> when (c) {
                    '{' -> {
                        if (braceDepth == 0) {
                        } else {
                            result.append(c)
                        }
                        braceDepth++
                    }
                    '}' -> {
                        braceDepth--
                        if (braceDepth == 0) {
                        } else {
                            result.append(c)
                        }
                    }
                    else -> result.append(c)
                }
                else -> result.append(c)
            }
        }

        return result.toString().trim()
    }

    private fun parseBooleanExpression(expr: String): Boolean {
        val trimmedExpr = expr.trim()
        val simpleValue = trimmedExpr.toBooleanStrictOrNull()
        if (simpleValue != null) return simpleValue

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
            val operator = tokens[i].lowercase()
            val right = evaluateComparison(tokens[i + 1])

            result = when (operator) {
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
        if (lower == "true" || lower == "1") return true
        if (lower == "false" || lower == "0") return false
        if (token.matches(Regex("-?\\d+"))) return token.toInt()

        val arrayAccess = parseArrayAccess(token)
        if (arrayAccess != null) {
            val arrayName = arrayAccess.arrayName
            val indexExpr = arrayAccess.indexExpr

            val arrayVar = variables[arrayName]
                ?: throw IllegalArgumentException("Массив $arrayName не найден")

            val index = evaluateExpression(indexExpr)

            return when (arrayVar.type) {
                DataType.ARR_INT -> {
                    val arr = arrayVar.value as? List<Int> ?: emptyList()
                    if (index in arr.indices) arr[index] else 0
                }
                DataType.ARR_STR -> {
                    val arr = arrayVar.value as? List<String> ?: emptyList()
                    if (index in arr.indices) arr[index] else ""
                }
                DataType.ARR_BOOL -> {
                    val arr = arrayVar.value as? List<Boolean> ?: emptyList()
                    if (index in arr.indices) arr[index] else false
                }
                else -> throw IllegalArgumentException("Невозможно получить значение из не-массива")
            }
        }

        if (variables.containsKey(token)) {
            val variable = variables[token]!!
            return when (variable.type) {
                DataType.BOOLEAN -> variable.value.toString().toBooleanStrict()
                DataType.INTEGER -> variable.value.toString().toInt()
                DataType.STRING -> variable.value.toString()
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
        var result = StringBuilder()

        for (token in tokens) {
            val trimmedToken = token.trim()
            if (trimmedToken.startsWith("\"") && trimmedToken.endsWith("\"")) {
                result.append(trimmedToken.substring(1, trimmedToken.length - 1))
            } else {
                try {
                    val value = resolveValue(trimmedToken)
                    result.append(value.toString())
                } catch (e: Exception) {
                    result.append(trimmedToken)
                }
            }
        }

        return result.toString()
    }

    private fun tokenize(input: String): List<String> {
        return input.split(Regex("(?<=[+\\-*/%()])|(?=[+\\-*/%()])"))
            .filter { it.isNotBlank() }
    }

    private var currentTokenIndex = 0

    private fun evaluateExpression(expr: String): Int {
        val tokens = tokenize(expr.replace("\\s+".toRegex(), ""))
        currentTokenIndex = 0
        return parseAddition(tokens)
    }

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
        while (currentTokenIndex < tokens.size && setOf("*", "/", "%").contains(tokens[currentTokenIndex])) {
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
        val token = tokens.getOrNull(currentTokenIndex++) ?: throw IllegalArgumentException("Пустое выражение")
        return when {
            token == "(" -> {
                val result = parseAddition(tokens)
                if (currentTokenIndex >= tokens.size || tokens[currentTokenIndex++] != ")") {
                    throw IllegalArgumentException("Не хватает закрывающей скобки")
                }
                result
            }
            token.matches(Regex("-?\\d+")) -> token.toInt()
            else -> {
                val value = resolveValue(token)
                when (value) {
                    is Boolean -> if (value) 1 else 0
                    is Int -> value
                    else -> throw IllegalArgumentException("Невозможно привести к числу: $value")
                }
            }
        }
    }
}