
package com.example.android_7_module_hits

import android.util.Log
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
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.android_7_module_hits.ui.theme.FolderButtonMain
import com.example.android_7_module_hits.ui.theme.RunButtonMain
import com.example.android_7_module_hits.ui.theme.SettingsButtonMain
import com.example.android_7_module_hits.ui.theme.StopButtonMain
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import com.example.android_7_module_hits.Blocks.Block
import com.example.android_7_module_hits.Blocks.BlockContent
//import com.example.android_7_module_hits.Blocks.attachChild
//import com.example.android_7_module_hits.Blocks.findAttachableParent
//import com.example.android_7_module_hits.Blocks.logAllBlocks
import com.example.android_7_module_hits.interpreter.runInterpreter
import com.example.android_7_module_hits.ui.theme.FolderButtonSub
import com.example.android_7_module_hits.ui.theme.RunButtonSub
import com.example.android_7_module_hits.ui.theme.SettingsButtonSub
import com.example.android_7_module_hits.ui.theme.StopButtonSub
import com.example.android_7_module_hits.ui.uiblocks.AssignBlockView
import com.example.android_7_module_hits.ui.uiblocks.ConditionBlockView
import com.example.android_7_module_hits.ui.uiblocks.DeclareBlockView
import com.example.android_7_module_hits.ui.uiblocks.EndBlockView
import kotlin.math.roundToInt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_7_module_hits.Blocks.BlockType
import com.example.android_7_module_hits.viewModel.BlockViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    projectViewModel: ProjectViewModel
) {
    val viewModel: BlockViewModel = viewModel()
    val context = LocalContext.current
    val blocks by viewModel.blocks.collectAsState()

    /*LaunchedEffect(Unit) {
        val savedJson = loadStateFromFile(context, "project_state.json")
        if (savedJson != null) {
            val loadedBlockStates = deserializeBlocks(savedJson)
            val loadedBlocks = loadedBlockStates.map { it.toBlock() }
            viewModel.setInitialBlocks(loadedBlocks)
            Log.d("Load", "Загружено ${loadedBlocks.size} блоков из сохранённого файла")
        }
    }*/
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
                    blocks.forEach{block ->
                        key(block.id) {
                            DraggableBlock(
                                block = block,
                                viewModel = viewModel,
                                onPositionChange = { id, pos ->
                                    viewModel.updateBlockPosition(id, pos)
                                },
                                onDelete = {
                                    viewModel.deleteBlock(block.id)
                                },
                                onAttach = { parent, child ->
                                    viewModel.attachChild(parent, child)
                                }
                            )
                        }
                    }
                }

                BlockPalette { newBlock ->
                    viewModel.addBlock(newBlock)
                }

            }
        },
        bottomBar = {
            BottomCircleButtons(allBlocks = blocks,
                onProjectSaved = { newProject ->
                    projectViewModel.addProject(newProject)
                }
            )
        }
    )
}


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
        is BlockContent.End -> {
            EndBlockView(content, block)
        }
        else -> {
            Text("Неизвестный тип блока")
        }
    }
}


@Composable
fun DraggableBlock(
    block: Block,
    viewModel: BlockViewModel,
    onPositionChange: (String, Offset) -> Unit,
    onDelete: (String) -> Unit,
    onAttach: ((Block, Block) -> Unit)? = null
) {
    var offset by remember { mutableStateOf(block.position) }
    val showDeleteIcon = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .combinedClickable(
                onClick = { showDeleteIcon.value = false },
                onLongClick = { showDeleteIcon.value = true }
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        showDeleteIcon.value = false
                        offset += dragAmount
                    },
                    onDragEnd = {
                        val attachableParent = viewModel.findAttachableParent(block, offset)
                        if (attachableParent != null) {
                            onAttach?.invoke(attachableParent, block)
                            offset = Offset(
                                attachableParent.position.x,
                                attachableParent.position.y + 150f
                            )
                        }
                        if (block.type == BlockType.END ||
                            block.type == BlockType.ELSE_IF ||
                            block.type == BlockType.ELSE){
                            block.parent?.let {viewModel.attachHasBodyBlock(block, it)}
                        }

                        onPositionChange(block.id, offset)
                    },
                    onDragCancel = {
                        onPositionChange(block.id, offset)
                    }
                )
            }
    ) {
        Column {
            BlockView(block)

            if (offset != block.position) {
                val attachableParent = viewModel.findAttachableParent(block, offset)
                if (attachableParent != null) {
                    Log.d("highlight", "type parent - ${attachableParent.type}, child - ${block.type}")
                    AttachmentHighlight(attachableParent.position)
                }
            }
        }

        if (showDeleteIcon.value) {
            Icon(
                imageVector = Icons.Filled.DeleteOutline,
                contentDescription = "Удалить блок",
                tint = Color.Red,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .clickable {
                        onDelete(block.id)
                    }
            )
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
fun BottomCircleButtons(
    allBlocks: List<Block>,
    onProjectSaved: (ProjectPreview) -> Unit
) {
    val context = LocalContext.current

    val buttonColors = listOf(
        FolderButtonMain,
        StopButtonSub,
        SettingsButtonMain,
        RunButtonMain
    )

    val iconList: List<ImageVector> = listOf(
        Icons.Filled.Folder,
        Icons.Filled.CheckBoxOutlineBlank,
        Icons.Filled.BugReport,
        Icons.Filled.PlayArrow
    )

    val iconTints = listOf(
        FolderButtonSub,
        StopButtonMain,
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
                                0 -> {
                                    /*// При нажатии на первую кнопку происходит сохранение
                                    val blockStates = allBlocks.map { it.toBlockState() }
                                    val jsonData = serializeBlocks(blockStates)
                                    saveStateToFile(context, "project_state.json", jsonData)
                                    Log.d("Save", "Проект сохранён в файл: project_state.json")

                                    // Создаем объект ProjectPreview с текущей датой
                                    val currentDate = java.text.SimpleDateFormat(
                                        "dd.MM.yyyy",
                                        java.util.Locale.getDefault()
                                    ).format(java.util.Date())
                                    val newProject = ProjectPreview(
                                        id = java.util.UUID.randomUUID().toString(),
                                        saveDate = currentDate
                                    )
                                    onProjectSaved(newProject)*/
                                }
                                1 -> {}
                                2 -> {}
                                3 -> {
//                                    logAllBlocks()
                                    runInterpreter(blocks = allBlocks)
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
