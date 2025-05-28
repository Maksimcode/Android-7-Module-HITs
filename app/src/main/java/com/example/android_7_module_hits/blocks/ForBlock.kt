package com.example.android_7_module_hits.blocks

class ForBlock (
    val init: String,
    val logicalExpression: String,
    val update: String
) : BlockHasBody(
    type = BlockType.FOR,
    content = BlockContent.For(
        variable = init,
        expression = logicalExpression,
        construct = update
    )
)