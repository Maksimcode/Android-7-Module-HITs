package com.example.android_7_module_hits

import androidx.lifecycle.viewmodel.compose.viewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import com.example.android_7_module_hits.ui.theme.Android7ModuleHITsTheme
import com.example.android_7_module_hits.ui.theme.MathColor
import com.example.android_7_module_hits.ui.theme.VariablesColor
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import com.example.android_7_module_hits.ui.theme.FolderButtonSub
import com.example.android_7_module_hits.ui.theme.RunButtonSub
import com.example.android_7_module_hits.ui.theme.SettingsButtonSub
import com.example.android_7_module_hits.ui.theme.StopButtonSub
import com.example.android_7_module_hits.ui.editor.EditorScreen
import com.example.android_7_module_hits.ui.editor.EditorViewModel
import com.example.android_7_module_hits.data.model.BlockType
import com.example.android_7_module_hits.ui.Blocks.*
import com.example.android_7_module_hits.ui.editor.*
import com.example.android_7_module_hits.interpreter.*


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
                val viewModel: EditorViewModel = viewModel() // Получаем ViewModel
                Column {
                    PaletteSection(viewModel) // Передаём в палитру
                    EditorScreen(viewModel)   // И в редактор }
                }
            }
        },
        bottomBar = {
            val viewModel: EditorViewModel = viewModel()
            BottomCircleButtons(viewModel)
        }
    )
}

@Composable
fun PaletteSection(viewModel: EditorViewModel) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AddBlockButton(UiBlockType.DECLARE_VARIABLE) { id ->
            val newBlock = UiBlock(
                id = id,
                content = "var x: int",
                position = Offset.Zero,
                type = BlockType.DECLARE,
                editableFields = mutableMapOf("variableName" to "x")
            )
            viewModel.addBlock(newBlock)
        }

        AddBlockButton(UiBlockType.ASSIGN_VALUE) { id ->
            val newBlock = UiBlock(
                id = id,
                content = "x = 0",
                position = Offset.Zero,
                type = BlockType.ASSIGN,
                editableFields = mutableMapOf("variableName" to "x", "expression" to "0")
            )
            viewModel.addBlock(newBlock)
        }
    }
}



@Composable
fun BottomCircleButtons(viewModel: EditorViewModel) {

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
                                    val interpreter = Interpreter()
                                    interpreter.interpret(viewModel.blocks.value, ExecutionContext())
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
