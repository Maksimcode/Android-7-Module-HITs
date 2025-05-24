package com.example.android_7_module_hits.ui.uiblocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.blocks.BlockContent

@Composable
fun ElseBlockView(content: BlockContent.Else, block: Block) {

    Card(
    modifier = Modifier
        .width(200.dp)
        .padding(4.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ){
        Column(modifier = Modifier.padding(8.dp)){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "else {", color = Color.Black)
            }
        }
    }
}