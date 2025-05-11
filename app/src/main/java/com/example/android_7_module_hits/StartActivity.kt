package com.example.android_7_module_hits

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.android_7_module_hits.ui.theme.Android7ModuleHITsTheme

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1500)
        installSplashScreen()
        setContent {
            Android7ModuleHITsTheme {
                val intent = Intent(this, LibraryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}