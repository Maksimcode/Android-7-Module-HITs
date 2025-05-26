package com.example.android_7_module_hits.interpreter

import androidx.compose.runtime.mutableStateListOf

object InterpreterLogger {
    val errors = mutableStateListOf<String>()

    fun logError(message: String) {
        errors.add("❌ $message")
    }

    fun clear() {
        errors.clear()
    }
}