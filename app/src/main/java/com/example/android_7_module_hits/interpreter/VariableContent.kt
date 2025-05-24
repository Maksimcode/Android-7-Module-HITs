package com.example.android_7_module_hits.interpreter

import com.example.android_7_module_hits.blocks.DataType

data class VariableContent(
    var type: DataType,
    var value: Any,
    var arrayLength: String = "0"
) {}