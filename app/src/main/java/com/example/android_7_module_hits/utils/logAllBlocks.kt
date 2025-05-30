package com.example.android_7_module_hits.utils

import com.example.android_7_module_hits.blocks.Block
import kotlin.collections.forEach

fun logAllBlocks(blocks: List<Block>) {
    blocks.forEach {
        println("ID: ${it.id}, parent: ${it.parent?.id}, child: ${it.child?.id}")
    }
}