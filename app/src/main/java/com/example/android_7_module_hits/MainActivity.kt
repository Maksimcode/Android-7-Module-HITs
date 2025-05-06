package com.example.android_7_module_hits

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
                Blocks()
            }
        },
        bottomBar = { BottomCircleButtons() }
    )
}


@Composable
fun Blocks() {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DraggableBox("bloсk 1", VariablesColor)
        DraggableBox("bloсk 2", MathColor)
        DraggableBox("bloсk 3", Color.Green)
    }
}

// Перетаскиваемый блок
@Composable
fun DraggableBox(caption: String, bgColor: Color) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box (
        Modifier
            .offset {
                IntOffset(
                    x = offsetX.roundToInt(),
                    y = offsetY.roundToInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures {change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .background(color = bgColor)
            .padding(1.dp)
            .size(200.dp, 52.dp)
    ) {
        Text (
            modifier = Modifier.align(Alignment.Center),
            text = caption,
            fontSize = 30.sp,
            color = Color.White,
            textAlign = TextAlign.Right,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
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
                            // Обработчик нажатия для кнопки с индексом
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


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    Android7ModuleHITsTheme {
//        Greeting("Android")
//    }
//}