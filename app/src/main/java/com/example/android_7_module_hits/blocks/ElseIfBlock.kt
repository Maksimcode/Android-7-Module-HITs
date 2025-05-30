package com.example.android_7_module_hits.blocks

class ElseIfBlock(
    val logicalExpression: String
) : BlockHasBody(
    type = BlockType.ELSE_IF,
    content = BlockContent.ElseIf(expression = logicalExpression)
) {
    override fun canAcceptNestedChildren(): Boolean {
        return true
    }
}