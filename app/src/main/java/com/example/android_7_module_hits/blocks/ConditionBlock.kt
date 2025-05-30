package com.example.android_7_module_hits.blocks

class ConditionBlock (
    val logicalExpression: String
) : BlockHasBody(
    type = BlockType.CONDITION,
    content = BlockContent.Condition(expression = logicalExpression)
){
    override fun canAcceptNestedChildren(): Boolean {
        return true
    }
}