package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.blocks.Block

fun runInterpreter(blocks: List<Block>) {
    val state = InterpreterState()

    val rootBlocks = blocks.filter { it.parent == null }

    rootBlocks.forEach { rootBlock ->
        interpret(rootBlock, state)
    }

    println("Результат выполнения:")
    for ((name, value) in state.getVariables()) {
        println("$name = $value")
    }
}