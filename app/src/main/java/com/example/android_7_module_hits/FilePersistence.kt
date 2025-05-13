package com.example.android_7_module_hits

import android.content.Context
import java.io.IOException

/**
 * Сохраняет строку JSON в файл во внутренней памяти.
 *
 * @param context Контекст приложения.
 * @param filename Имя файла (например, "project_state.json").
 * @param jsonData Сериализованные данные в формате JSON.
 */
fun saveStateToFile(context: Context, filename: String, jsonData: String) {
    try {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(jsonData.toByteArray())
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

/**
 * Загружает строку JSON из файла.
 *
 * @param context Контекст приложения.
 * @param filename Имя файла.
 * @return Строка с данными JSON или null в случае ошибки.
 */
fun loadStateFromFile(context: Context, filename: String): String? {
    return try {
        context.openFileInput(filename).bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
