package com.example.android_7_module_hits.Blocks

class ConditionBlock (
    val logicalExpression: String
) : BlockHasBody(
    type = BlockType.CONDITION,
    content = BlockContent.Condition(
        expression = logicalExpression
    )
)