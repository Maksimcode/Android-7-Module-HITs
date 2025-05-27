package com.example.android_7_module_hits.saving.utils

fun ensureJsonExtension(fileName: String): String {
    return if (fileName.endsWith(".json")) fileName else "$fileName.json"
}
