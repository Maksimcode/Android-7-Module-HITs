package com.example.android_7_module_hits.saving

import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent

fun Block.toBlockState(): BlockState {
    val contentState = when (this.content) {
        is BlockContent.Declare -> BlockContentState.Declare(
            type = (this.content as BlockContent.Declare).type,
            name = (this.content as BlockContent.Declare).name,
            value = (this.content as BlockContent.Declare).value,
            length = (this.content as BlockContent.Declare).length
        )
        is BlockContent.Assignment -> BlockContentState.Assignment(
            name = (this.content as BlockContent.Assignment).name,
            value = (this.content as BlockContent.Assignment).value
        )
        is BlockContent.Condition -> BlockContentState.Condition(
            expression = (this.content as BlockContent.Condition).expression
        )
        is BlockContent.ElseIf -> BlockContentState.ElseIf(
            expression = (this.content as BlockContent.ElseIf).expression
        )
        is BlockContent.End -> BlockContentState.End
        is BlockContent.Else -> BlockContentState.Else
        else -> throw IllegalArgumentException("Неизвестный тип содержимого")
    }

    return BlockState(
        id = id,
        type = type,
        content = contentState,
        positionX = position.x,
        positionY = position.y
    )
}