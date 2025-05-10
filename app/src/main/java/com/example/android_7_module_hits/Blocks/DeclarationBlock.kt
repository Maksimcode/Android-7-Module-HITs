package com.example.android_7_module_hits.Blocks

class DeclarationBlock(
    val variableName: String,
    val initialValue: String? = "0"
) : BaseBlock(
    type = BlockType.DECLARE,
    content = BlockContent.Declare(
        name = variableName
    )
)