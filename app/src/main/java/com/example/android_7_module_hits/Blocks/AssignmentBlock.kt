package com.example.android_7_module_hits.Blocks

class AssignmentBlock(
    val variableName: String,
    val initialValue: String
) : BaseBlock(
    type = BlockType.ASSIGN,
    content = BlockContent.Assignment(
        name = variableName,
        value = initialValue
    )
)