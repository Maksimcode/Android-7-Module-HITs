package com.example.android_7_module_hits.blocks

class FunsBlock (
    val function: FunsType,
    val uniParam: String,
    val firstSwap: String = "a",
    val secondSwap: String = "b"
): BaseBlock(
    type = BlockType.FUNCTIONS,
    content = BlockContent.Functions(
        func = function,
        comParam = uniParam,
        firstSw = firstSwap,
        secondSw = secondSwap
    )
){
    override fun canAcceptNestedChildren(): Boolean {
        return false
    }
}