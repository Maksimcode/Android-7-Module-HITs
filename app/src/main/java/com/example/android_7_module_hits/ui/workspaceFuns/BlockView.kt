package com.example.android_7_module_hits.ui.workspaceFuns

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent
import com.example.android_7_module_hits.ui.uiblocks.AssignBlockView
import com.example.android_7_module_hits.ui.uiblocks.ConditionBlockView
import com.example.android_7_module_hits.ui.uiblocks.DeclareBlockView
import com.example.android_7_module_hits.ui.uiblocks.ElseBlockView
import com.example.android_7_module_hits.ui.uiblocks.ElseIfBlockView
import com.example.android_7_module_hits.ui.uiblocks.EndBlockView
import com.example.android_7_module_hits.ui.uiblocks.ForBlockView
import com.example.android_7_module_hits.ui.uiblocks.WhileBlockView

@Composable
fun BlockView(block: Block) {
    when (val content = block.content) {
        is BlockContent.Declare -> {
            DeclareBlockView(content, block)
        }
        is BlockContent.Assignment -> {
            AssignBlockView(content, block)
        }
        is BlockContent.Condition -> {
            ConditionBlockView(content, block)
        }
        is BlockContent.ElseIf -> {
            ElseIfBlockView(content, block)
        }
        is BlockContent.Else -> {
            ElseBlockView(content, block)
        }
        is BlockContent.End -> {
            EndBlockView(content, block)
        }
        is BlockContent.While -> {
            WhileBlockView(content, block)
        }
        is BlockContent.For -> {
            ForBlockView(content, block)
        }
        else -> {
            Text("Unknown block type")
        }
    }
}