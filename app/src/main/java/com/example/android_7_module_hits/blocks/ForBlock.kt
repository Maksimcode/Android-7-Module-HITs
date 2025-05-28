package com.example.android_7_module_hits.blocks

class ForBlock (
    val counter: String,
    val startValue: String,
    val logicalExpression: String,
    val update: String
) : BlockHasBody(
    type = BlockType.FOR,
    content = BlockContent.For(
        variable = counter,
        initValue = startValue,
        expression = logicalExpression,
        construct = update
    )
)