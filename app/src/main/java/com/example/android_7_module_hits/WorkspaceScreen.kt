
package com.example.android_7_module_hits

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.android_7_module_hits.ui.theme.FolderButtonMain
import com.example.android_7_module_hits.ui.theme.RunButtonMain
import com.example.android_7_module_hits.ui.theme.SettingsButtonMain
import com.example.android_7_module_hits.ui.theme.StopButtonMain
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.android_7_module_hits.Blocks.AssignmentBlock
import com.example.android_7_module_hits.Blocks.Block
import com.example.android_7_module_hits.Blocks.BlockContent
import com.example.android_7_module_hits.Blocks.BlockType
import com.example.android_7_module_hits.Blocks.ConditionBlock
import com.example.android_7_module_hits.Blocks.DeclarationBlock
import com.example.android_7_module_hits.Blocks.EndBlock
import com.example.android_7_module_hits.Blocks.attachChild
import com.example.android_7_module_hits.Blocks.findAttachableParent
import com.example.android_7_module_hits.Blocks.logAllBlocks
import com.example.android_7_module_hits.interpreter.runInterpreter
import com.example.android_7_module_hits.ui.theme.FolderButtonSub
import com.example.android_7_module_hits.ui.theme.RunButtonSub
import com.example.android_7_module_hits.ui.theme.SettingsButtonSub
import com.example.android_7_module_hits.ui.theme.StopButtonSub
import com.example.android_7_module_hits.ui.uiblocks.AssignBlockView
import com.example.android_7_module_hits.ui.uiblocks.BlockTemplate
import com.example.android_7_module_hits.ui.uiblocks.ConditionBlockView
import com.example.android_7_module_hits.ui.uiblocks.DeclareBlockView
import com.example.android_7_module_hits.ui.uiblocks.EndBlockView
import com.example.android_7_module_hits.ui.uiblocks.availableBlocks
import kotlin.math.roundToInt



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController
) {
    val allBlocks = remember { mutableStateOf(listOf<Block>()) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Enter project name") },
                navigationIcon = {
                    IconButton(onClick = { /* логика открытия меню */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Меню"
                        )
                    }
                },
                actions = {
                    IconButton(onClick ={navController.navigate(route = Screen.Library.route)}
                        ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                InfiniteCanvas {
                    CreateBlock(allBlocks)
                }

                BlockPalette { newBlock ->
                    allBlocks.value += newBlock
                }

            }
        },
        bottomBar = {
            BottomCircleButtons(allBlocks)
        }
    )
}

@Composable
fun CreateBlock(allBlocks: MutableState<List<Block>>) {
    val latestAllBlocks by rememberUpdatedState(allBlocks)
    latestAllBlocks.value.forEach { block ->
        key(block.id) {
            DraggableBlock(block = block, latestAllBlocks, onPositionChange = { newPosition ->
                block.position = newPosition
            })
        }
    }

}

@Composable
fun BlockView(block: Block) {
    when (val content = block.content) {
        is BlockContent.Declare -> DeclareBlockView(content, block)
        is BlockContent.Assignment -> AssignBlockView(content, block)
        is BlockContent.Condition -> ConditionBlockView(content, block)
        is BlockContent.End -> EndBlockView(content, block)
        else -> {
            Text("Неизвестный тип блока")
        }
    }
}


@Composable
fun DraggableBlock(block: Block, allBlocks: MutableState<List<Block>>, onPositionChange: (Offset) -> Unit) {
    var offset by remember { mutableStateOf(block.position) }
    val latestAllBlocks by rememberUpdatedState(allBlocks.value)

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount


                    },
                    onDragEnd = {

                        val attachableParent = findAttachableParent(latestAllBlocks, block, offset)

                        if (attachableParent != null) {
                            attachChild(parent = attachableParent, child = block)
                            offset = Offset(attachableParent.position.x, attachableParent.position.y + 150f)
                            onPositionChange(offset)
                        } else {
                            onPositionChange(offset)
                        }
                    },
                    onDragCancel = {
                        onPositionChange(offset)
                    }
                )
            }
    ) {
        BlockView(block)
        if (offset != block.position){
            val attachableParent = findAttachableParent(latestAllBlocks, block, offset)

            if (attachableParent != null)
            {
                Log.d("highlight", "type parent - ${attachableParent.type}, child - ${block.type}")
                AttachmentHighlight(attachableParent.position)
            }
        }

    }
}

@Composable
fun AttachmentHighlight(position: Offset) {
    Box(
        modifier = Modifier
            .background(Color.Green.copy(alpha = 0.3f))
            .size(width = 200.dp, height = 16.dp)
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt())}
    )
}

@Composable
fun InfiniteCanvas(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTransformGestures(
                    panZoomLock = false,
                    onGesture = { centroid, pan, zoom, rotation ->
                        offset += pan
                    }
                )
            }
            .graphicsLayer(
                translationX = offset.x,
                translationY = offset.y
            )
    ) {
        content()
    }
}


@Composable
fun BottomCircleButtons(allBlocks: MutableState<List<Block>>) {

    val buttonColors = listOf(
        FolderButtonMain,
        StopButtonMain,
        SettingsButtonMain,
        RunButtonMain
    )

    val iconList: List<ImageVector> = listOf(
        Icons.Filled.Star, // Папки почему-то не было
        Icons.Filled.Close,
        Icons.Filled.Settings,
        Icons.Filled.Check
    )

    val iconTints = listOf(
        FolderButtonSub,
        StopButtonSub,
        SettingsButtonSub,
        RunButtonSub
    )

    Box(modifier = Modifier
        .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 36.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Top,
        ) {
            buttonColors.forEachIndexed { index, color ->
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 2.dp,
                            spotColor = Color(0x0D101828),
                            ambientColor = Color(0x0D101828),
                            shape = RoundedCornerShape(32.dp)
                        )
                        .size(56.dp)
                        .background(
                            color = color,
                            shape = RoundedCornerShape(32.dp)
                        )
                        .clickable {
                            when (index) {
                                0 -> {}
                                1 -> {}
                                2 -> {}
                                3 -> {
                                    logAllBlocks(allBlocks.value)
                                    runInterpreter(allBlocks.value)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconList[index],
                        contentDescription = "Иконка ${index + 1}",
                        tint = iconTints[index],
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WorkspaceScreenPreview(){
    MainScreen(
        navController = rememberNavController()
    )
}