package com.example.android_7_module_hits.Blocks

class DeclarationBlock(
    val variableName: String,
    val initialValue: String? = null,
    val isMutable: Boolean = false
) : BaseBlock(
    type = BlockType.DECLARE,
    content = BlockContent.Declare(
        name = variableName
    ),

)