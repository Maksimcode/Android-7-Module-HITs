//package com.example.android_7_module_hits.Blocks
//
//import android.util.Log
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.ui.geometry.Offset
//
//class BlockTree {
//    private val _blocks = mutableMapOf<String, Block>()
//    val blocks: Map<String, Block> get() = _blocks
//
//    val rootBlocks = mutableStateListOf<Block>()
//
//    fun add(block: Block) {
//        _blocks[block.id] = block
//        if (block.parent == null) {
//            rootBlocks.add(block)
//            Log.d("BlockTree", "Added block: ${block.id}, root blocks count: ${rootBlocks.size}")
//        } else{
//            Log.d("BlockTree", "NOOOO Added block: ${block.id}, root blocks count: ${rootBlocks.size}")
//        }
//    }
//
//    fun connect(parent: Block, child: Block) {
//        child.attachTo(parent)
//        if (child.parent == parent) {
//            add(child)
//        }
//    }
//
//    fun remove(block: Block) {
//        block.detach()
//        _blocks.remove(block.id)
//        rootBlocks.remove(block)
//    }
//
//    fun findAttachableParent(currentPosition: Offset, draggedBlock: Block): Block? {
//        return rootBlocks.firstOrNull { parent ->
//            val distance = distanceBetween(parent.position, currentPosition)
//            distance < 500f && parent.canAttachTo(draggedBlock)
//        }
//    }
//}