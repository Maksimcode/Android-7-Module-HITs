package com.example.android_7_module_hits

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material3.Icon
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import com.example.android_7_module_hits.blocks.Block
import kotlin.math.roundToInt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_7_module_hits.blocks.BlockHasBody
import com.example.android_7_module_hits.blocks.ConditionBlock
import com.example.android_7_module_hits.blocks.DeclarationBlock
import com.example.android_7_module_hits.blocks.ForBlock
import com.example.android_7_module_hits.blocks.FunsBlock
import com.example.android_7_module_hits.ui.workspaceFuns.BlockView
import com.example.android_7_module_hits.interpreter.InterpreterLogger
import com.example.android_7_module_hits.navigation.Screen
import com.example.android_7_module_hits.saving.utils.ensureJsonExtension
import com.example.android_7_module_hits.ui.workspaceFuns.AttachmentHighlight
import com.example.android_7_module_hits.ui.workspaceFuns.ConsoleMenu
import com.example.android_7_module_hits.ui.workspaceFuns.InfiniteCanvas
import com.example.android_7_module_hits.ui.notifications.InfoNotification
import com.example.android_7_module_hits.viewModel.BlockViewModel
import com.example.android_7_module_hits.ui.notifications.UiNotification
import com.example.android_7_module_hits.ui.notifications.NotificationHost
import com.example.android_7_module_hits.ui.notifications.showNotification
import com.example.android_7_module_hits.ui.uiblocks.BlockPalette
import com.example.android_7_module_hits.ui.workspaceFuns.BottomCircleButtons
import com.example.android_7_module_hits.utils.weightBlock
import com.example.android_7_module_hits.utils.weightBody
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    fileName: String
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val drawerWidth = configuration.screenWidthDp.dp * 0.6f

    var currentNotification by remember { mutableStateOf<UiNotification?>(null) }
    var isConsoleOpen by remember { mutableStateOf(false) }
    var projectName by remember { mutableStateOf("Enter project name") }
    var showNameDialog by remember { mutableStateOf(false) }

    val viewModel: BlockViewModel = viewModel()
    val blocks by viewModel.blocks.collectAsState()

    LaunchedEffect(key1 = fileName) {
        if (fileName != "Enter project name" && fileName.isNotBlank()) {
            projectName = fileName.removeSuffix(".json")
            viewModel.loadBlocks(fileName)
        } else {
            viewModel.clearBlocks()
        }
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
                        title = {
                            Text(
                                text = projectName,
                                modifier = Modifier.clickable {
                                    showNameDialog = true
                                }
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(route = Screen.Library.route) {
                                    popUpTo(Screen.Library.route) {
                                        inclusive = true
                                    }
                                }
                            }
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
                                        onPositionChange = { id, pos ->
                                            viewModel.updateBlockAndDescendantsPosition(id, pos)
                                        },
                                        onDelete = { id -> viewModel.deleteBlock(id) },
                                        onAttach = { parent, child, asNested ->
                                            viewModel.attachBlock(parent, child, asNested)
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
                                    viewModel.saveBlocks(ensureJsonExtension(projectName))
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

            if (showNameDialog) {
                var tempName by remember { mutableStateOf(if (projectName == "Enter project name") "" else projectName) }
                AlertDialog(
                    onDismissRequest = { showNameDialog = false },
                    text = {
                        OutlinedTextField(
                            value = tempName,
                            onValueChange = { tempName = it },
                            label = { Text("Project name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                projectName =
                                    if (tempName.isNotBlank()) tempName else "Enter project name"
                                showNameDialog = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showNameDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
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
    onAttach: ((Block, Block, Boolean) -> Unit)? = null
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
                        val potentialParent =
                            viewModel.findAttachableParent(block, offset, asNested = false)
                        val asNested = if (potentialParent != null) {
                            val threshold = 100f
                            (offset.x - potentialParent.position.x) >= threshold
                        } else false

                        val attachableParent =
                            viewModel.findAttachableParent(block, offset, asNested)

                        if (attachableParent is BlockHasBody) {
                            if (asNested) {
                                val wasEmpty = attachableParent.nestedChildren.isEmpty()
                                onAttach?.invoke(attachableParent, block, true)

                                offset = if (wasEmpty) {
                                    // Если был пустой, то позиция относительно родителя
                                    Offset(
                                        attachableParent.position.x + 70.dp.toPx(),
                                        attachableParent.position.y + weightBlock(attachableParent).toPx()
                                    )
                                } else {
                                    // Если не первый, то позиция относительно последнего в теле
                                    val lastChild = attachableParent.nestedChildren.last()
                                    Offset(
                                        lastChild.position.x,
                                        lastChild.position.y + weightBlock(lastChild).toPx()
                                    )
                                }
                            } else {
                                onAttach?.invoke(attachableParent, block, false)
                                offset = Offset(
                                    attachableParent.position.x,
                                    attachableParent.position.y + weightBody(attachableParent.nestedChildren).toPx() + weightBlock(attachableParent).toPx()
                                )
                            }
                        } else if (attachableParent != null) {
                            onAttach?.invoke(attachableParent, block, false)
                            offset = Offset(
                                attachableParent.position.x,
                                attachableParent.position.y + weightBlock(attachableParent).toPx()
                            )
                        }

                        onPositionChange(block.id, offset)
                    },
                    onDragCancel = {
                        onPositionChange(block.id, offset)
                    }
                )
            }
    ) {

        if (offset != block.position) {
            val potentialParent =
                viewModel.findAttachableParent(block, offset, asNested = false)
            potentialParent?.let { parent ->
                AttachmentHighlight(parent.position)
            }
        }
        BlockView(block)


        if (showDeleteIcon.value) {
            Icon(
                imageVector = Icons.Filled.Close,
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
