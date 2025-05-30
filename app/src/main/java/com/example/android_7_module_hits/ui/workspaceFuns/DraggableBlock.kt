package com.example.android_7_module_hits.ui.workspaceFuns

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.android_7_module_hits.R
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockHasBody
import com.example.android_7_module_hits.blocks.ForBlock
import com.example.android_7_module_hits.blocks.WhileBlock
import com.example.android_7_module_hits.ui.theme.AssignmentColor
import com.example.android_7_module_hits.ui.theme.ConditionColor
import com.example.android_7_module_hits.ui.theme.CycleColor
import com.example.android_7_module_hits.utils.weightBlock
import com.example.android_7_module_hits.utils.weightBody
import com.example.android_7_module_hits.viewModel.BlockViewModel
import kotlin.math.roundToInt

@Composable
fun DraggableBlock(
    block: Block,
    viewModel: BlockViewModel,
    onPositionChange: (String, Offset) -> Unit,
    onDelete: (String) -> Unit,
    onAttach: ((Block, Block, Boolean) -> Unit)? = null
) {
    var offset by remember { mutableStateOf(block.position) }
    val showDeleteIcon = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .combinedClickable(
                onClick = { showDeleteIcon.value = false },
                onLongClick = { showDeleteIcon.value = true })
            .pointerInput(Unit) {
                detectDragGestures(onDrag = { change, dragAmount ->
                    change.consume()
                    showDeleteIcon.value = false
                    offset += dragAmount
                }, onDragEnd = {
                    val potentialParent =
                        viewModel.findAttachableParent(block, offset, asNested = false)
                    val asNested = if (potentialParent != null) {
                        val threshold = 100f
                        (offset.x - potentialParent.position.x) >= threshold
                    } else false

                    val attachableParent = viewModel.findAttachableParent(block, offset, asNested)

                    if (attachableParent is BlockHasBody) {
                        if (asNested) {
                            val wasEmpty = attachableParent.nestedChildren.isEmpty()
                            onAttach?.invoke(attachableParent, block, true)

                            offset = if (wasEmpty) {
                                Offset(
                                    attachableParent.position.x + 70.dp.toPx(),
                                    attachableParent.position.y + weightBlock(attachableParent).toPx()
                                )
                            } else {
                                val lastChild = attachableParent.nestedChildren.last()
                                Offset(
                                    lastChild.position.x,
                                    lastChild.position.y + weightBlock(lastChild).toPx()
                                )
                            }
                        } else {
                            onAttach?.invoke(attachableParent, block, false)
                            offset = Offset(
                                attachableParent.position.x,
                                attachableParent.position.y + weightBody(attachableParent.nestedChildren).toPx() + weightBlock(
                                    attachableParent
                                ).toPx()
                            )
                        }
                    } else if (attachableParent != null) {
                        onAttach?.invoke(attachableParent, block, false)
                        offset = Offset(
                            attachableParent.position.x,
                            attachableParent.position.y + weightBlock(attachableParent).toPx()
                        )
                    }

                    onPositionChange(block.id, offset)
                }, onDragCancel = {
                    onPositionChange(block.id, offset)
                })
            }) {

        if (offset != block.position) {
            val potentialParent = viewModel.findAttachableParent(block, offset, asNested = false)
            val asNested = if (potentialParent != null) {
                val threshold = 150f
                (offset.x - potentialParent.position.x) >= threshold
            } else false
            potentialParent?.let { parent ->
                if (asNested) {
                    when (potentialParent) {
                        is WhileBlock, is ForBlock -> AttachmentHighlight(offset, CycleColor)
                        else -> AttachmentHighlight(offset, ConditionColor)
                    }

                } else {
                    AttachmentHighlight(offset, AssignmentColor)
                }

            }
        }
        BlockView(block)


        if (showDeleteIcon.value) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(id = R.string.delete_block_description),
                tint = Color.Red,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .clickable {
                        onDelete(block.id)
                    })
        }
    }
}
