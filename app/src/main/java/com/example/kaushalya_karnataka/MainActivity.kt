package com.example.kaushalya_karnataka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.example.kaushalya_karnataka.navigation.NavGraph
import com.example.kaushalya_karnataka.ui.theme.KaushalyaKarnatakaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Professional Edge-to-Edge System UI
        enableEdgeToEdge()
        
        setContent {
            val darkTheme = isSystemInDarkTheme()
            
            // Ensure system bars match our high-end design
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.Transparent.toArgb(),
                        Color.Transparent.toArgb()
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim
                    )
                )
                onDispose {}
            }

            KaushalyaKarnatakaTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}

private val lightScrim = Color(0xFFE2E8F0).toArgb() // Slate 80
private val darkScrim = Color(0xFF0F172A).toArgb()  // Slate 10
