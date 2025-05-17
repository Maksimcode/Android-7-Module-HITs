package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.Blocks.Block
import com.example.android_7_module_hits.Blocks.BlockContent
import com.example.android_7_module_hits.Blocks.DataType
import javax.xml.xpath.XPathExpression

class InterpreterState {
    private val variables = mutableMapOf<String, BlockContent.Declare>()

//    TODO: parsing variables by commas
    fun declareVariable(content: BlockContent.Declare) {
        if (content.name.isBlank()) throw IllegalArgumentException("Имя переменной не может быть пустым")

        if (variables.containsKey(content.name)) throw IllegalArgumentException("Переменная уже объявлена")
        variables[content.name] = content

    }

    fun assignValue(content: BlockContent.Assignment) {
        if (!variables.containsKey(content.name)) {
            throw IllegalStateException("Переменная ${content.name} не объявлена")
        }

        val oldValue = variables[content.name]
        val type = when (oldValue){
            is BlockContent.Declare -> oldValue.type
            else -> DataType.INTEGER
        }
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
            else -> {
                println("i can't do it now")
            }
        }



        variables[content.name] = BlockContent.Declare(type, content.name, result.toString())
    }

    fun setCondition(firstExpression: String, operator: String, secondExpression: String) : Boolean{

        val firstResult = try {
            evaluateExpression(firstExpression ?: "0")
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка при вычислении первого выражения", e)
        }

        val secondResult = try{
            evaluateExpression(secondExpression ?: "0")
        } catch (e:Exception) {
            throw IllegalArgumentException("Ошибка при вычислении второго выражения", e)
        }

        val cleanedOperator = operator.replace("\\s+".toRegex(), "")

        return when (cleanedOperator) {
            "==" -> firstResult == secondResult
            "!=" -> firstResult != secondResult
            "<" -> firstResult < secondResult
            ">" -> firstResult > secondResult
            "<=" -> firstResult <= secondResult
            ">=" -> firstResult >= secondResult
            else -> throw IllegalArgumentException("Неизвестный оператор сравнения: $operator")
        }

    }

    fun getVariables(): Map<String, BlockContent.Declare> = variables.toMap()

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
                    result += value.toIntOrNull() ?: 0
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
                    is BlockContent.Declare -> value.value.toInt()
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
                        is BlockContent.Declare -> value.value.toInt()
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