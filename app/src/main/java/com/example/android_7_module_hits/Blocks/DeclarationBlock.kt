package com.example.android_7_module_hits.Blocks

class DeclarationBlock(
    val variableType: DataType,
    val variableName: String,
    val initialValue: String? = "0"
) : BaseBlock(
    type = BlockType.DECLARE,
    content = BlockContent.Declare(
        type = variableType,
        name = variableName
    )
)