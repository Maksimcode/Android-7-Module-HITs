package com.example.android_7_module_hits

import androidx.compose.ui.geometry.Offset
import com.example.android_7_module_hits.Blocks.AssignmentBlock
import com.example.android_7_module_hits.Blocks.Block
import com.example.android_7_module_hits.Blocks.BlockContent
import com.example.android_7_module_hits.Blocks.ConditionBlock
import com.example.android_7_module_hits.Blocks.DeclarationBlock
import com.example.android_7_module_hits.Blocks.EndBlock

fun Offset.toOffsetState(): OffsetState = OffsetState(x = this.x, y = this.y)

fun BlockContent.toBlockContentState(): BlockContentState = when (this) {
    is BlockContent.Declare -> BlockContentState.Declare(name = this.name, value = this.value)
    is BlockContent.Assignment -> BlockContentState.Assignment(name = this.name, value = this.value)
    is BlockContent.Condition -> BlockContentState.Condition(
        firstPart = this.firstPart,
        operator = this.operator,
        secondPart = this.secondPart
    )
    is BlockContent.End -> BlockContentState.End
    // При необходимости можно добавить обработку дополнительных типов
}

fun Block.toBlockState(): BlockState {
    return BlockState(
        id = this.id,
        type = BlockTypeState.valueOf(this.type.name),
        content = this.content.toBlockContentState(),
        position = this.position.toOffsetState(),
        parentId = this.parent?.id,
        childId = this.child?.id,
        associatedBlockId = null // При необходимости можно расширить
    )
}

fun BlockState.toBlock(): Block {
    // Преобразуем сохранённую позицию обратно в Offset
    val position = Offset(this.position.x, this.position.y)
    // В зависимости от типа блока создаём соответствующий объект
    return when (this.type) {
        BlockTypeState.DECLARE -> {
            // Приводим содержимое к нужному типу
            val contentState = this.content as? BlockContentState.Declare
            DeclarationBlock(
                variableName = contentState?.name ?: "unknown",
                initialValue = contentState?.value ?: "0"
            ).apply { this.position = position }
        }
        BlockTypeState.ASSIGN -> {
            val contentState = this.content as? BlockContentState.Assignment
            AssignmentBlock(
                variableName = contentState?.name ?: "unknown",
                initialValue = contentState?.value ?: "0"
            ).apply { this.position = position }
        }
        BlockTypeState.CONDITION -> {
            val contentState = this.content as? BlockContentState.Condition
            ConditionBlock(
                firstExpression = contentState?.firstPart ?: "",
                operator = contentState?.operator ?: "",
                secondExpression = contentState?.secondPart ?: ""
            ).apply { this.position = position }
        }
        BlockTypeState.END -> {
            EndBlock().apply { this.position = position }
        }
    }
}