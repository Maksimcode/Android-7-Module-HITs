package com.example.android_7_module_hits

import android.content.Context
import java.io.IOException

fun saveStateToFile(context: Context, filename: String, jsonData: String) {
    try {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(jsonData.toByteArray())
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun loadStateFromFile(context: Context, filename: String): String? {
    return try {
        context.openFileInput(filename).bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
