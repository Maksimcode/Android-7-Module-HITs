package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.blocks.Block

fun runInterpreter(blocks: List<Block>) {
    InterpreterLogger.clear()
    val state = InterpreterState()

    val rootBlocks = blocks.filter { it.parent == null }

    rootBlocks.forEach { block ->
        try {
            interpret(block, state)
        } catch (e: Exception) {
            InterpreterLogger.logError("Ошибка в блоке ${block.type}: ${e.message}")
        }
    }

    println("Результат выполнения:")
    for ((name, value) in state.getVariables()) {
        println("$name = $value")
    }
}