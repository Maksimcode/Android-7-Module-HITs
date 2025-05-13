package com.example.android_7_module_hits


import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Сериализация списка состояний блоков в JSON-строку.
 */
fun serializeBlocks(blockStates: List<BlockState>): String {
    return Json.encodeToString(blockStates)
}

/**
 * Десериализация JSON-строки в список состояний блоков.
 */
fun deserializeBlocks(jsonData: String): List<BlockState> {
    return Json.decodeFromString(jsonData)
}
