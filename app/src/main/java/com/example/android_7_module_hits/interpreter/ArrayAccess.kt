package com.example.android_7_module_hits.interpreter

data class ArrayAccess(val arrayName: String, val indexExpr: String)

fun parseArrayAccess(token: String): ArrayAccess? {
    val pattern = Regex("""^(.+)\[(.+)]$""")
    val match = pattern.matchEntire(token.trim()) ?: return null
    val arrayName = match.groupValues[1].trim()
    val indexExpr = match.groupValues[2].trim()
    return ArrayAccess(arrayName, indexExpr)
}