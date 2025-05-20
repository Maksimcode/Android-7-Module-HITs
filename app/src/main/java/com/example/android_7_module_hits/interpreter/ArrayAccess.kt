package com.example.android_7_module_hits.interpreter

data class ArrayAccess(val arrayName: String, val indexExpr: String)

fun parseArrayAccess(name: String): ArrayAccess? {
    val regex = Regex("""^(\w+)\[(.+)]$""")
    val match = regex.matchEntire(name.trim()) ?: return null
    val arrayName = match.groupValues[1]
    val indexExpr = match.groupValues[2]
    return ArrayAccess(arrayName, indexExpr)
}