package com.example.android_7_module_hits.Blocks

class ConditionBlock (
    val firstExpression: String,
    val operator: String,
    val secondExpression: String
) : BlockHasBody(
    type = BlockType.CONDITION,
    content = BlockContent.Condition(
        firstPart = firstExpression,
        operator = operator,
        secondPart = secondExpression
    )
)