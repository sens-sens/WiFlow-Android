package com.androsmith.wiflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.androsmith.wiflow.ui.WiFlowApp
import com.androsmith.wiflow.ui.screens.home.HomeScreen
import com.androsmith.wiflow.ui.theme.WiFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WiFlowTheme {
                Surface {
                    WiFlowApp()
                }
            }
        }
    }
}

