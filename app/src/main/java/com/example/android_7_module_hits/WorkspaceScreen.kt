package com.example.android_7_module_hits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.android_7_module_hits.interpreter.InterpreterLogger
import com.example.android_7_module_hits.navigation.Screen
import com.example.android_7_module_hits.saving.utils.ensureJsonExtension
import com.example.android_7_module_hits.ui.notifications.InfoNotification
import com.example.android_7_module_hits.ui.notifications.NotificationHost
import com.example.android_7_module_hits.ui.notifications.UiNotification
import com.example.android_7_module_hits.ui.notifications.showNotification
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.bottomBoxPadding
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.drawerColumnSpacing
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.drawerHorizontalPadding
import com.example.android_7_module_hits.ui.theme.WorkspaceFunctionsDimens.drawerWidthMultiplier
import com.example.android_7_module_hits.ui.uiblocks.BlockPalette
import com.example.android_7_module_hits.ui.workspaceFuns.BottomCircleButtons
import com.example.android_7_module_hits.ui.workspaceFuns.ConsoleMenu
import com.example.android_7_module_hits.ui.workspaceFuns.DraggableBlock
import com.example.android_7_module_hits.ui.workspaceFuns.InfiniteCanvas
import com.example.android_7_module_hits.viewModel.BlockViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController, fileName: String
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val drawerWidth = configuration.screenWidthDp.dp * drawerWidthMultiplier

    var currentNotification by remember { mutableStateOf<UiNotification?>(null) }
    var isConsoleOpen by remember { mutableStateOf(false) }
    val initialProjectName = stringResource(id = R.string.enter_project_name)
    var projectName by remember { mutableStateOf(initialProjectName) }
    var showNameDialog by remember { mutableStateOf(false) }

    val viewModel: BlockViewModel = viewModel()
    val blocks by viewModel.blocks.collectAsState()

    LaunchedEffect(key1 = fileName) {
        if (fileName != initialProjectName && fileName.isNotBlank()) {
            projectName = fileName.removeSuffix(".json")
            viewModel.loadBlocks(fileName)
        } else {
            viewModel.clearBlocks()
        }
    }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet(
            modifier = Modifier.width(drawerWidth)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(drawerHorizontalPadding),
                verticalArrangement = Arrangement.spacedBy(drawerColumnSpacing)
            ) {
                Text(
                    text = stringResource(id = R.string.block_creation),
                    style = MaterialTheme.typography.titleMedium
                )
                BlockPalette { newBlock ->
                    viewModel.addBlock(newBlock)
                    scope.launch { drawerState.close() }
                }
            }
        }
    }, content = {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = projectName, modifier = Modifier.clickable {
                        showNameDialog = true
                    })
            }, navigationIcon = {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = stringResource(id = R.string.menu)
                    )
                }
            }, actions = {
                IconButton(onClick = {
                    navController.navigate(route = Screen.Library.route) {
                        popUpTo(Screen.Library.route) {
                            inclusive = true
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            })
        }, content = { innerPadding ->
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
                                })
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = bottomBoxPadding)
                ) {
                    BottomCircleButtons(allBlocks = blocks, onConsoleClick = {
                        isConsoleOpen = true
                    }, onSaveClick = {
                        viewModel.saveBlocks(ensureJsonExtension(projectName))
                        showNotification(
                            scope,
                            { currentNotification = it },
                            InfoNotification(onDismiss = { currentNotification = null })
                        )
                    })
                }
            }
        })

        NotificationHost(notification = currentNotification)

        if (isConsoleOpen) {
            ConsoleMenu(
                onDismissRequest = { isConsoleOpen = false }) {
                Column {
                    Text(text = stringResource(id = R.string.console_output))
                    Text(text = stringResource(id = R.string.interpreter_running))
                    if (InterpreterLogger.errors.isEmpty()) {
                        Text(text = stringResource(id = R.string.no_errors))
                    } else {
                        for (error in InterpreterLogger.errors) {
                            Text(text = error)
                        }
                    }
                }
            }
        }

        if (showNameDialog) {
            var tempName by remember { mutableStateOf(if (projectName == initialProjectName) "" else projectName) }
            AlertDialog(onDismissRequest = { showNameDialog = false }, text = {
                OutlinedTextField(
                    value = tempName,
                    onValueChange = { tempName = it },
                    label = { Text(stringResource(id = R.string.project_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }, confirmButton = {
                TextButton(
                    onClick = {
                        projectName = if (tempName.isNotBlank()) tempName else initialProjectName
                        showNameDialog = false
                    }) {
                    Text(stringResource(id = R.string.ok))
                }
            }, dismissButton = {
                TextButton(onClick = { showNameDialog = false }) {
                    Text(stringResource(id = R.string.cancel))
                }
            })
        }
    })
}

