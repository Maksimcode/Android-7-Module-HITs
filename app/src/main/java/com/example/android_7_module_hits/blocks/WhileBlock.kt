package com.example.android_7_module_hits.blocks

class WhileBlock (
    val logicalExpression: String
) : BlockHasBody(
    type = BlockType.WHILE,
    content = BlockContent.While(
        expression = logicalExpression
    )
)