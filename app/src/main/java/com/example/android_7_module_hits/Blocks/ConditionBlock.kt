package com.example.android_7_module_hits.Blocks

class ConditionBlock (
    val conditionInto: String
) : BlockHasBody(
    type = BlockType.CONDITION,
    content = BlockContent.Condition(
        condition = conditionInto
    )
)