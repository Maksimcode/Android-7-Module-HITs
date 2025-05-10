package com.example.android_7_module_hits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_7_module_hits.ui.theme.Android7ModuleHITsTheme
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.android_7_module_hits.ui.theme.FolderButtonMain
import com.example.android_7_module_hits.ui.theme.RunButtonMain
import com.example.android_7_module_hits.ui.theme.SettingsButtonMain
import com.example.android_7_module_hits.ui.theme.StopButtonMain
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.example.android_7_module_hits.Blocks.Block
import com.example.android_7_module_hits.Blocks.BlockContent
import com.example.android_7_module_hits.Blocks.BlockType
import com.example.android_7_module_hits.Blocks.DeclarationBlock
import com.example.android_7_module_hits.Blocks.attachChild
import com.example.android_7_module_hits.Blocks.findAttachableParent
import com.example.android_7_module_hits.Blocks.getAllBlocks
import com.example.android_7_module_hits.ui.theme.FolderButtonSub
import com.example.android_7_module_hits.ui.theme.RunButtonSub
import com.example.android_7_module_hits.ui.theme.SettingsButtonSub
import com.example.android_7_module_hits.ui.theme.StopButtonSub
import com.example.android_7_module_hits.ui.uiblocks.BlockTemplate
import com.example.android_7_module_hits.ui.uiblocks.availableBlocks
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android7ModuleHITsTheme {
                MainScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
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
                    IconButton(onClick = { /* логика кнопки назад */}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
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
                val allBlocks = remember { mutableStateListOf<Block>() }

                BlockPalette { newBlock ->
                    allBlocks.add(newBlock)
                }

                CreateBlock(allBlocks)
            }
        },
        bottomBar = {
            BottomCircleButtons()
        }
    )
}

@Composable
fun CreateBlock(allBlocks: MutableList<Block>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Постоянные блоки
        allBlocks.forEach { block ->
            key(block.id) {
                RenderSingleBlock(block, allBlocks)
            }
        }
    }
}


@Composable
fun RenderAllBlocks(blocks: List<Block>, allBlocks: MutableList<Block>) {
    blocks.forEach { block ->
        key(block.id) {
            RenderSingleBlock(block, allBlocks)
        }
    }
}

@Composable
fun RenderSingleBlock(block: Block, allBlocks: MutableList<Block>) {
    DraggableBlock(block = block, allBlocks = allBlocks)

    val child = block.child
    if (child != null && allBlocks.contains(child)) {
        key(child.id) {
            RenderSingleBlock(child, allBlocks)
        }
    }
}


@Composable
fun BlockView(block: Block) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            when (val content = block.content) {
                is BlockContent.Declare -> {
                    Text(
                        text = "int ${content.name};",
                        color = Color.Black)
                }
                else -> Text("...")
            }
        }
    }
}


@Composable
fun DraggableBlock(block: Block, allBlocks: List<Block>) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        block.position = Offset(offsetX, offsetY)
                    },
                    onDragEnd = {

                        val attachableParent = findAttachableParent(allBlocks, draggedBlock = block)

                        if (attachableParent != null) {
                            attachChild(parent = attachableParent, child = block)
                            offsetX = attachableParent.position.x
                            offsetY = attachableParent.position.y + 100f
                            block.position = Offset(offsetX, offsetY)
                        } else {
                            block.position = Offset(offsetX, offsetY)
                        }
                    }
                )
            }
    ) {
        BlockView(block)
    }
}

@Composable
fun BlockPalette(onBlockSelected: (Block) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Блоки")
        Spacer(modifier = Modifier.height(8.dp))
        availableBlocks.forEach { template ->
            BlockPaletteItem(template = template, onBlockSelected = onBlockSelected)
        }
    }
}

@Composable
fun BlockPaletteItem(template: BlockTemplate, onBlockSelected: (Block) -> Unit) {
    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .clickable {
                val newBlock = when (template.type) {
                    BlockType.DECLARE->
                        DeclarationBlock(variableName = "x", initialValue = "0")
                    else -> throw IllegalArgumentException("Unknown block type")
                }
                onBlockSelected(newBlock)
            }
            .padding(8.dp)
            .width(100.dp)
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = template.title)
    }
}

@Composable
fun BottomCircleButtons() {

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
