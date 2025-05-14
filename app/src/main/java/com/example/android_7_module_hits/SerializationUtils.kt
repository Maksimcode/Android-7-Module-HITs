package com.example.android_7_module_hits


import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun serializeBlocks(blockStates: List<BlockState>): String {
    return Json.encodeToString(blockStates)
}

fun deserializeBlocks(jsonData: String): List<BlockState> {
    return Json.decodeFromString(jsonData)
}
