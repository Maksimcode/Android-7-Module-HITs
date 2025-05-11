package com.example.android_7_module_hits.interpreter

class InterpreterState {
    private val variables = mutableMapOf<String, Int>()

    fun declareVariable(name: String) {
        if (name.isBlank()) throw IllegalArgumentException("Имя переменной не может быть пустым")
        if (variables.containsKey(name)) throw IllegalArgumentException("Переменная уже объявлена")

        variables[name] = 0
    }

    fun assignValue(name: String, expression: String?) {
        if (!variables.containsKey(name)) {
            throw IllegalStateException("Переменная $name не объявлена")
        }

        val result = try {
            evaluateExpression(expression ?: "0", variables)
        } catch (e: Exception) {
            throw e
        }

        variables[name] = result
    }

    fun getVariables(): Map<String, Int> = variables.toMap()

    private fun evaluateExpression(expr: String, variables: Map<String, Int>): Int {
        val tokens = tokenize(expr.replace("\\s+".toRegex(), ""))
        currentTokenIndex = 0
        return parseAddition(tokens, variables)
    }

    private fun tokenize(input: String): List<String> {
        return input.split(Regex("(?<=[+\\-*/%()])|(?=[+\\-*/%()])"))
            .filter { it.isNotBlank() }
    }

    private var currentTokenIndex = 0

    private fun parseAddition(tokens: List<String>, variables: Map<String, Int>): Int {
        var result = parseMultiplication(tokens, variables)

        while (currentTokenIndex < tokens.size && (tokens[currentTokenIndex] == "+" || tokens[currentTokenIndex] == "-")) {
            val op = tokens[currentTokenIndex]
            currentTokenIndex++
            val right = parseMultiplication(tokens, variables)
            result = when (op) {
                "+" -> result + right
                "-" -> result - right
                else -> result
            }
        }

        return result
    }

    private fun parseMultiplication(tokens: List<String>, variables: Map<String, Int>): Int {
        var result = parsePrimary(tokens, variables)

        while (currentTokenIndex < tokens.size && setOf("*", "/", "%").contains(tokens[currentTokenIndex])) { // ✅ Добавлен %
            val op = tokens[currentTokenIndex]
            currentTokenIndex++

            val right = parsePrimary(tokens, variables)
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

    private fun parsePrimary(tokens: List<String>, variables: Map<String, Int>): Int {
        val token = tokens[currentTokenIndex++]

        return when {
            token == "(" -> {
                val result = parseAddition(tokens, variables)
                if (currentTokenIndex >= tokens.size || tokens.getOrNull(currentTokenIndex) != ")") {
                    throw IllegalArgumentException("Не хватает закрывающей скобки")
                }
                currentTokenIndex++
                result
            }
            token.matches(Regex("-?\\d+")) -> token.toInt()
            variables.containsKey(token) -> variables[token]!!
            else -> throw IllegalArgumentException("Неизвестная переменная: $token")
        }
    }
}