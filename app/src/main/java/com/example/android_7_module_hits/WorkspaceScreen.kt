
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import com.example.android_7_module_hits.blocks.Block
import com.example.android_7_module_hits.interpreter.runInterpreter
import com.example.android_7_module_hits.ui.theme.FolderButtonSub
import com.example.android_7_module_hits.ui.theme.RunButtonSub
import com.example.android_7_module_hits.ui.theme.SettingsButtonSub
import com.example.android_7_module_hits.ui.theme.StopButtonSub
import kotlin.math.roundToInt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_7_module_hits.ui.workspaceFuns.BlockView
import com.example.android_7_module_hits.blocks.BlockType
import com.example.android_7_module_hits.interpreter.InterpreterLauncher
import com.example.android_7_module_hits.interpreter.InterpreterLogger
import com.example.android_7_module_hits.navigation.Screen
import com.example.android_7_module_hits.ui.workspaceFuns.AttachmentHighlight
import com.example.android_7_module_hits.ui.workspaceFuns.ConsoleMenu
import com.example.android_7_module_hits.ui.workspaceFuns.InfiniteCanvas
import com.example.android_7_module_hits.ui.notifications.InfoNotification
import com.example.android_7_module_hits.viewModel.BlockViewModel
import com.example.android_7_module_hits.viewModel.logAllBlocks
import com.example.android_7_module_hits.ui.notifications.UiNotification
import com.example.android_7_module_hits.ui.notifications.NotificationHost
import com.example.android_7_module_hits.ui.notifications.showNotification
import com.example.android_7_module_hits.ui.uiblocks.BlockPalette
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val drawerWidth = configuration.screenWidthDp.dp * 0.6f

    var currentNotification by remember { mutableStateOf<UiNotification?>(null) }

    var isConsoleOpen by remember { mutableStateOf(false) }

    val viewModel: BlockViewModel = viewModel()
    val blocks by viewModel.blocks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBlocks()
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(drawerWidth)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Block creation",
                        style = MaterialTheme.typography.titleMedium
                    )
                    BlockPalette { newBlock ->
                        viewModel.addBlock(newBlock)
                        scope.launch { drawerState.close() }
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(text = "Enter project name") },
                        navigationIcon = {
                            IconButton(onClick = {scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick ={navController.navigate(route = Screen.Library.route){
                                popUpTo(Screen.Library.route){
                                    inclusive = true
                                }
                            } }
                                ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
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
                            blocks.forEach { block ->
                                key(block.id) {
                                    DraggableBlock(
                                        block = block,
                                        viewModel = viewModel,
                                        onPositionChange = {id, pos ->
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 0.dp)
                        ) {
                            BottomCircleButtons(
                                allBlocks = blocks,
                                onConsoleClick = {
                                    isConsoleOpen = true
                                },
                                onSaveClick = {
                                    viewModel.saveBlocks()
                                    showNotification(
                                        scope,
                                        { currentNotification = it },
                                        InfoNotification(onDismiss = { currentNotification = null })
                                    )
                                }
                            )
                        }
                    }
                }
            )

            NotificationHost(notification = currentNotification)

            if (isConsoleOpen) {
                ConsoleMenu(
                    onDismissRequest = { isConsoleOpen = false }
                ) {
                    // Сюда будем добавлять консольный вывод
                    Column {
                        Text(text = "Console Output:")
                        Text(text = ">> Interpreter is running...")
                        if (InterpreterLogger.errors.isEmpty()) {
                            Text(text = "Ошибок нет")
                        } else {
                            for (error in InterpreterLogger.errors) {
                                Text(text = error)
                            }
                        }
                    }
                }
            }
        }
    )
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
                            if (attachableParent.type == BlockType.ELSE_IF){
                                onAttach?.invoke(attachableParent, block)
                                offset = Offset(
                                    attachableParent.position.x,
                                    attachableParent.position.y + 230f
                                )
                            }
                            else if (attachableParent.type == BlockType.DECLARE){
                                onAttach?.invoke(attachableParent, block)
                                offset = Offset(
                                    attachableParent.position.x,
                                    attachableParent.position.y + 285f
                                )
                            }
                            else {
                                onAttach?.invoke(attachableParent, block)
                                offset = Offset(
                                    attachableParent.position.x,
                                    attachableParent.position.y + 150f
                                )
                            }
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
                contentDescription = "Delete Block",
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
fun BottomCircleButtons(
    allBlocks: List<Block>,
    onConsoleClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

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

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
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
                                0 -> onSaveClick()
                                1 -> {}
                                2 -> {}
                                3 -> {
                                    logAllBlocks(allBlocks)
                                    InterpreterLauncher.launchInterpreter(
                                        lifecycleScope = scope,
                                        blocks = allBlocks
                                    ) {
                                        onConsoleClick()
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconList[index],
                        contentDescription = "Icon ${index + 1}",
                        tint = iconTints[index],
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
