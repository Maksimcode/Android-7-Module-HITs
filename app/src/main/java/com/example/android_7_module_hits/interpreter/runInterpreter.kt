package com.example.android_7_module_hits.interpreter

import androidx.compose.runtime.MutableState
import com.example.android_7_module_hits.Blocks.Block

fun runInterpreter(allBlocks: List<Block>) {
    val state = InterpreterState()

    val rootBlocks = allBlocks.filter { it.parent == null }

    rootBlocks.forEach { rootBlock ->
        interpret(rootBlock, state)
    }

    println("Результат выполнения:")
    for ((name, value) in state.getVariables()) {
        println("$name = $value")
    }
}