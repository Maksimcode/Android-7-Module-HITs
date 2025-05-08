package com.example.android_7_module_hits.data.model

import com.example.android_7_module_hits.ui.Blocks.UiBlock
import androidx.compose.ui.geometry.Offset
import kotlin.math.hypot

fun tryConnectBlockToOthers(
    currentBlock: UiBlock,
    allBlocks: List<UiBlock>,
    onConnect: (fromId: String, toId: String) -> Unit
) {
    val nearbyBlock = allBlocks.find { other ->
        other.id != currentBlock.id && isNear(currentBlock.position, other.position)
    }

    nearbyBlock?.let {
        onConnect(currentBlock.id, it.id)
    }
}

private fun isNear(pos1: Offset, pos2: Offset): Boolean {
    val dx = pos1.x - pos2.x
    val dy = pos1.y - pos2.y
    val distance = hypot(dx.toDouble(), dy.toDouble())
    return distance < 50
}