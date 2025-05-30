package com.example.android_7_module_hits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.android_7_module_hits.navigation.SetupNavGraph
import com.example.android_7_module_hits.ui.theme.Android7ModuleHITsTheme

class StartActivity : ComponentActivity() {

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Android7ModuleHITsTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}