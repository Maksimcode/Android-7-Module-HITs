package com.example.android_7_module_hits.blocks

class EndBlock() : BaseBlock(
    type = BlockType.END,
    content = BlockContent.End()
) {
    override fun canAcceptNestedChildren(): Boolean {
        return false
    }
}