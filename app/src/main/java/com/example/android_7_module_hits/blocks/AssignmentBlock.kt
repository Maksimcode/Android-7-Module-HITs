package com.example.android_7_module_hits.blocks

class AssignmentBlock(
    val variableName: String,
    val initialValue: String
) : BaseBlock(
    type = BlockType.ASSIGN,
    content = BlockContent.Assignment(
        name = variableName,
        value = initialValue
    )
) {
    override fun canAcceptNestedChildren(): Boolean {
        return false
    }
}