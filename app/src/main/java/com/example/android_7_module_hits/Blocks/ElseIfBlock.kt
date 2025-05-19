package com.example.android_7_module_hits.Blocks

class ElseIfBlock(
    val logicalExpression: String
) : BlockHasBody(
    type = BlockType.ELSE_IF,
    content = BlockContent.Condition(
        expression = logicalExpression
    )
)