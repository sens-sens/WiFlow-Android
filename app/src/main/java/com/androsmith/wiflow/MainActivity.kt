package com.androsmith.wiflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.androsmith.wiflow.ui.WiFlowApp
import com.androsmith.wiflow.ui.theme.WiFlowTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>(
        factoryProducer = { MainViewModel.Factory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isInitialized
            }
        }

        enableEdgeToEdge()
        setContent {
            WiFlowTheme {
                Surface {

                    val startDestination = viewModel.startDestination.collectAsState()

                    startDestination.value?.let {
                        WiFlowApp(
                            startDestination = it
                        )
                    }

                }
            }
        }
    }
}

