package com.example.android_7_module_hits.data.model

import android.util.Log
import com.example.android_7_module_hits.ui.Blocks.UiBlock
import androidx.compose.ui.geometry.Offset
import kotlin.math.hypot

fun tryConnectBlockToOthers(
    currentBlock: UiBlock,
    allBlocks: List<UiBlock>,
    onConnect: (String, String) -> Unit
) {
    val currentPos = currentBlock.position
    Log.d("DEBUG", "Проверяем связи для блока ${currentBlock.id} на позиции $currentPos")

    var closest: UiBlock? = null
    var minDistance = Double.MAX_VALUE

    for (other in allBlocks) {
        if (other.id == currentBlock.id) continue

        val dx = currentPos.x - other.position.x
        val dy = currentPos.y - other.position.y
        val distance = hypot(dx.toDouble(), dy.toDouble())

        Log.d("DEBUG", "Расстояние до ${other.id} = $distance")

        if (distance < 400 && distance < minDistance) {
            minDistance = distance
            closest = other
        }
    }

    if (closest != null) {
        Log.d("DEBUG", "Соединяю ${currentBlock.id} с ${closest!!.id}")
        onConnect(currentBlock.id, closest!!.id)
    } else {
        Log.d("DEBUG", "Нет подходящих блоков")
    }
}

private fun isNear(pos1: Offset, pos2: Offset): Boolean {
    val dx = pos1.x - pos2.x
    val dy = pos1.y - pos2.y
    val distance = hypot(dx.toDouble(), dy.toDouble())
    return distance < 50
}