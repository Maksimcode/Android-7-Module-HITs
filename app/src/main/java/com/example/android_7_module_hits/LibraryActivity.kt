package com.example.android_7_module_hits

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.android_7_module_hits.ui.theme.Android7ModuleHITsTheme


class LibraryActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Android7ModuleHITsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MainContent(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Header()
        Spacer(modifier = Modifier.height(16.dp))
        Buttons()
        Spacer(modifier = Modifier.height(16.dp))
        GreetProjectBlocks()
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),

    ){
        Text (
            text= "Library",
            fontSize = 48.sp,
        )

        Text (
            text = "All projects here!",
            fontSize = 24.sp,
        )
    }
}

@Composable
fun Buttons() {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Button(
            onClick = {
                val intent = Intent(context, LibraryActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("My projects")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, LibraryActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("Examples")
        }
    }
}

@Composable
fun GreetProjectBlocks(){
    //val unknownProjectBlock = ProjectBlock("Unknown", "1 Jan, 1970")
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items((1..5).toList()) { index ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProjectBlock("Unknown", "1 Jan, 1970")
                ProjectBlock("Unknown", "1 Jan, 1970")
            }
        }
    }
}

@Composable
fun ProjectBlock(
    title: String,
    date: String,
    modifier: Modifier = Modifier,
    gradientBrush: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFF456CE8), Color(0xFF0033DA)),
        start = Offset(0f, 0f),            // Начало градиента.
        end = Offset(300f, 600f)            // Конец градиента. Подберите по размеру компонента.
    ),
    contentColor: Color = Color.White,
//    onClick: () -> Unit = {}
){
    val context = LocalContext.current
    val onClick ={
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
    Card(
        modifier = modifier
            .size(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(brush = gradientBrush)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBrush)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                color = contentColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = date,
                color = contentColor,
                fontSize = 16.sp
            )
        }
    }
}

//  TODO: classes for headings, subheadings...
//  another file for text-classes
//  eliminate the use of spacer (replace with indents inside the component parameters)
//  class for project