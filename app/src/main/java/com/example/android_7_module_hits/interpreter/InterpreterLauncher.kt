package com.example.android_7_module_hits.interpreter

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.android_7_module_hits.blocks.Block
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object InterpreterLauncher {
    fun launchInterpreter(
        lifecycleScope: CoroutineScope,
        blocks: List<Block>,
        onResult: () -> Unit
    ) {
        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                runInterpreter(blocks)
            }
            onResult()
        }
    }
}