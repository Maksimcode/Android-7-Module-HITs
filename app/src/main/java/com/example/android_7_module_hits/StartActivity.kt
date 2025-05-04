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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_7_module_hits.ui.theme.Android7ModuleHITsTheme

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android7ModuleHITsTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.background(Color.Gray)
                            .align(Alignment.BottomCenter)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(this@StartActivity, MainActivity::class.java)
                                startActivity(intent)
                            },
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(text = "To codeblock")
                        }


                        Button(onClick = {
                            val intent = Intent(this@StartActivity, LibraryActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "To library")
                    }
                  }
                }
            }
        }
    }
}