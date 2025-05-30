package com.example.android_7_module_hits.blocks

class ElseBlock : BlockHasBody(
    type = BlockType.ELSE,
    content = BlockContent.Else(),
){
    override fun canAcceptNestedChildren(): Boolean {
        return true
    }
}