package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.blocks.BlockContent
import com.example.android_7_module_hits.blocks.DataType

class InterpreterState {
    private val scopes = mutableListOf<MutableMap<String, VariableContent>>()

    init {
        scopes.add(mutableMapOf())
    }

    fun getVariables(): Map<String, VariableContent> {
        val result = mutableMapOf<String, VariableContent>()
        for (scope in scopes) {
            result.putAll(scope)
        }
        return result
    }

    fun getCurrentScopeVariables(): Map<String, VariableContent> {
        return scopes.last().toMap()
    }

    fun enterScope() {
        scopes.add(mutableMapOf())
    }

    fun exitScope() {
        if (scopes.size > 1) {
            scopes.removeAt(scopes.lastIndex)
        }
    }

    private fun findVariable(name: String): VariableContent? {
        for (i in scopes.indices.reversed()) {
            scopes[i][name]?.let { return it }
        }
        return null
    }

    private fun declareInCurrentScope(name: String, value: VariableContent) {
        val currentScope = scopes.last()
        if (currentScope.containsKey(name)) throw IllegalArgumentException("Переменная $name уже объявлена в текущей области")
        currentScope[name] = value
    }

    fun declareVariable(content: BlockContent.Declare) {
        val rawNames = content.name.split(',').map { it.trim() }

        if (rawNames.isEmpty()) {
            throw IllegalArgumentException("Не указано имя переменной")
        }

        for ((index, name) in rawNames.withIndex()) {
            if (name.isBlank()) {
                throw IllegalArgumentException("Имя переменной не может быть пустым")
            }

            if (findVariable(name) != null) {
                throw IllegalArgumentException("Переменная $name уже объявлена в текущей области")
            }

            val evaluatedValue = evaluateExpression(content.value)

            val arrayLength = if (content.type in listOf(DataType.ARR_INT, DataType.ARR_STR, DataType.ARR_BOOL)) {
                evaluateExpression(content.length ?: "0").toString()
            } else {
                "0"
            }

            declareInCurrentScope(
                name,
                VariableContent(
                    type = content.type,
                    value = evaluatedValue,
                    arrayLength = arrayLength
                )
            )
        }
    }

    fun assignValue(content: BlockContent.Assignment) {
        val name = content.name.trim()
        val arrayAccess = parseArrayAccess(name)
        var arrayName: String? = null
        var index: Int = 0
        var isIndexAccess = false

        if (arrayAccess != null) {
            isIndexAccess = true
            arrayName = arrayAccess.arrayName
            val indexExpr = arrayAccess.indexExpr
            index = evaluateExpression(indexExpr)

            val arrayVar = findVariable(arrayName)
                ?: throw IllegalArgumentException("Массив $arrayName не объявлен")

            if (index < 0 || index >= (arrayVar.value as List<*>).size) {
                throw IndexOutOfBoundsException("Индекс $index вне диапазона")
            }
        }

        val realName = arrayName ?: name
        val oldValue = findVariable(realName)
            ?: throw IllegalStateException("Переменная $realName не объявлена")

        val type = oldValue.type
        val newValue = content.value
        var resolvedValue: Any? = null
        try {
            resolvedValue = resolveValue(newValue)
        } catch (e: Exception){ }

        val result: Any? = when(type) {
            DataType.INTEGER -> {
                if (resolvedValue is Int) resolvedValue else evaluateExpression(newValue)
            }
            DataType.STRING -> {
                if (resolvedValue is String) resolvedValue else evaluateStringExpression(newValue)
            }
            DataType.BOOLEAN -> {
                if (resolvedValue is Boolean) resolvedValue else evaluateLogicalExpression(newValue)
            }
            in listOf(DataType.ARR_INT, DataType.ARR_STR, DataType.ARR_BOOL) -> {
                if (isIndexAccess) {
                    val arrayValue = oldValue.value as? MutableList<*>
                        ?: throw IllegalArgumentException("Неверный тип массива")

                    when (type) {
                        DataType.ARR_INT -> {
                            val intValue = if (resolvedValue is Int) resolvedValue else evaluateExpression(newValue)
                            val mutableArray = arrayValue.toMutableList() as MutableList<Int>
                            mutableArray[index] = intValue
                            mutableArray
                        }
                        DataType.ARR_STR -> {
                            val strValue = if (resolvedValue is String) resolvedValue else evaluateStringExpression(newValue)
                            val mutableArray = arrayValue.toMutableList() as MutableList<String>
                            mutableArray[index] = strValue
                            mutableArray
                        }
                        DataType.ARR_BOOL -> {
                            val boolValue = if (resolvedValue is Boolean) resolvedValue else parseBooleanExpression(newValue)
                            val mutableArray = arrayValue.toMutableList() as MutableList<Boolean>
                            mutableArray[index] = boolValue
                            mutableArray
                        }
                        else -> throw IllegalArgumentException("Тип массива не поддерживается")
                    }
                } else {
                    when (type) {
                        DataType.ARR_INT -> {
                            val arrLen = oldValue.arrayLength.toInt()
                            val res = mutableListOf<Int>()
                            for (i in 0 until arrLen) {
                                val element = splitArrayElements(newValue).getOrNull(i) ?: "0"
                                res.add(evaluateExpression(element))
                            }
                            res
                        }
                        DataType.ARR_STR -> {
                            val arrLen = oldValue.arrayLength.toInt()
                            val res = mutableListOf<String>()
                            for (i in 0 until arrLen) {
                                val element = splitArrayElements(newValue).getOrNull(i) ?: ""
                                res.add(evaluateStringExpression(element))
                            }
                            res
                        }
                        DataType.ARR_BOOL -> {
                            val arrLen = oldValue.arrayLength.toInt()
                            val res = mutableListOf<Boolean>()
                            for (i in 0 until arrLen) {
                                val element = splitArrayElements(newValue).getOrNull(i) ?: "false"
                                res.add(parseBooleanExpression(element))
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
            val currentScope = scopes.last { it.containsKey(realName) }
            currentScope[realName] = VariableContent(
                type = type,
                value = result,
                arrayLength = oldValue.arrayLength
            )
        }
    }

    fun swapping(firstSwap: String, secondSwap: String) {
        val access1 = parseArrayAccess(firstSwap)
        val varName1 = access1?.arrayName ?: firstSwap
        val indexExpr1 = access1?.indexExpr

        val access2 = parseArrayAccess(secondSwap)
        val varName2 = access2?.arrayName ?: secondSwap
        val indexExpr2 = access2?.indexExpr

        val var1 = findVariable(varName1) ?: throw IllegalArgumentException("Переменная $varName1 не найдена")
        val var2 = findVariable(varName2) ?: throw IllegalArgumentException("Переменная $varName2 не найдена")

        if (var1.type != var2.type) {
            throw IllegalArgumentException("Невозможно поменять местами разные типы: " +
                    "$varName1 - ${var1.type}, $varName2 - ${var2.type}")
        }

        val value1 = if (access1 != null) {
            val index1 = evaluateExpression(indexExpr1!!)
            val array = var1.value as? List<*> ?: throw IllegalArgumentException("$varName1 не является массивом")
            if (index1 < 0 || index1 >= array.size) throw IndexOutOfBoundsException("Индекс вне диапазона: $index1")
            resolveValue("${varName1}[$indexExpr1]")
        } else {
            var1.value
        }

        val value2 = if (access2 != null) {
            val index2 = evaluateExpression(indexExpr2!!)
            val array = var2.value as? List<*> ?: throw IllegalArgumentException("$varName2 не является массивом")
            if (index2 < 0 || index2 >= array.size) throw IndexOutOfBoundsException("Индекс вне диапазона: $index2")
            resolveValue("${varName2}[$indexExpr2]")
        } else {
            var2.value
        }


        assignValue(
            BlockContent.Assignment(
                name = firstSwap,
                value = value2.toString()
            )
        )

        assignValue(
            BlockContent.Assignment(
                name = secondSwap,
                value = value1.toString()
            )
        )

    }


    fun printValue(variable: String) {
        try {
            val value = resolveValue(variable)
            InterpreterLogger.errors.add("🖨️ $variable = $value")
        } catch (e: Exception) {
            InterpreterLogger.logError("Ошибка при печати '$variable': ${e.message}")
        }
    }



    fun setCondition(expr: String): Boolean {
        val trimmedExpr = expr.trim()
        var resolvedValue: Any? = null
        try {
            resolvedValue = resolveValue(trimmedExpr)
        } catch (e: Exception){ }
        return if (resolvedValue is Boolean) resolvedValue else parseBooleanExpression(trimmedExpr)
    }


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

    private fun evaluateExpressionIfPossible(token: String): Any {
        return try {
            resolveValue(token)
        } catch (e: Exception) {
            evaluateExpression(token)
        }
    }

    private fun evaluateComparison(expr: String): Boolean {
        val comparisonRegex = Regex("""(.+?)\s*([=!><]=?|!=)\s*(.+)""")
        val match = comparisonRegex.matchEntire(expr.trim()) ?: throw IllegalArgumentException("Неверный формат условия: $expr")
        val (leftStr, op, rightStr) = match.destructured

        val left = evaluateExpressionIfPossible(leftStr.trim())
        val right = evaluateExpressionIfPossible(rightStr.trim())

        return compareValues(left, right, op)
    }

    private fun resolveValue(token: String): Any {
        val trimmedToken = token.trim()

        val arrayAccess = parseArrayAccess(trimmedToken)
        if (arrayAccess != null) {
            val arrayName = arrayAccess.arrayName
            val indexExpr = arrayAccess.indexExpr

            val index = evaluateExpression(indexExpr)

            val arrayVar = findVariable(arrayName)
                ?: throw IllegalArgumentException("Массив $arrayName не найден")

            if (index < 0 || index >= (arrayVar.value as List<*>).size) {
                throw IndexOutOfBoundsException("Индекс $index вне диапазона")
            }
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

        val lower = trimmedToken.lowercase()
        if (lower == "true") return true
        if (lower == "false") return false

        if (trimmedToken.matches(Regex("-?\\d+"))) return trimmedToken.toInt()

        if (trimmedToken.startsWith("\"") && trimmedToken.endsWith("\"")) {
            return trimmedToken.substring(1, trimmedToken.length - 1)
        }

        if (trimmedToken.startsWith("{") && trimmedToken.endsWith("}")) {
            val elements = splitArrayElements(trimmedToken)
            return elements.map { element ->
                when {
                    element.toBooleanStrictOrNull() != null -> element.toBoolean()
                    element.matches(Regex("-?\\d+")) -> element.toInt()
                    else -> element
                }
            }
        }

        val variable = findVariable(trimmedToken)
            ?: throw IllegalArgumentException("Неизвестное значение: $trimmedToken")

        return when (variable.type) {
            DataType.BOOLEAN -> variable.value.toString().toBooleanStrict()
            DataType.INTEGER -> variable.value.toString().toInt()
            DataType.STRING -> variable.value.toString()
            in listOf(DataType.ARR_INT, DataType.ARR_STR, DataType.ARR_BOOL) -> variable.value
            else -> throw IllegalArgumentException("Неподдерживаемый тип переменной: ${variable.type}")
        }
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