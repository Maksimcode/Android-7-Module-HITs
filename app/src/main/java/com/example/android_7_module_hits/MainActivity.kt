package com.example.android_7_module_hits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android7ModuleHITsTheme  {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Blocks()
                }
            }
        }
    }
}

@Composable
fun Blocks() {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DraggableBox("bloсk 1", Color.Red)
        DraggableBox("bloсk 2", Color.Yellow)
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
            .padding(25.dp)
            .size(250.dp, 40.dp)
    ) {
        Text (
            modifier = Modifier.align(Alignment.Center),
            text = caption,
            fontSize = 30.sp,
            color = Color(0xFFFFEEFF),
            textAlign = TextAlign.Right,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    Android7ModuleHITsTheme {
//        Greeting("Android")
//    }
//}