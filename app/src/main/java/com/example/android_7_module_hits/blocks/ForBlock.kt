package com.example.android_7_module_hits.blocks

class ForBlock (
    val varCount: String,
    val startPoint: String,
    val logicalExpression: String,
    val update: String
) : BlockHasBody(
    type = BlockType.FOR,
    content = BlockContent.For(
        variable = varCount,
        initValue = startPoint,
        expression = logicalExpression,
        construct = update
    )
)