package com.androsmith.wiflow.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.androsmith.wiflow.ui.navigation.Screens
import com.androsmith.wiflow.ui.screens.home.HomeScreen
import com.androsmith.wiflow.ui.screens.settings.SettingsScreen
import com.androsmith.wiflow.ui.screens.welcome.WelcomeScreen


@Composable
fun WiFlowApp(
    startDestination: Screens,
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable<Screens.Welcome> {
            WelcomeScreen(onNavigateToHome = {
                navController.popBackStack()
                navController.navigate(Screens.Home)
            })
        }
        composable<Screens.Home> {
            HomeScreen(onNavigateToSettings = { navController.navigate(Screens.Settings) })
        }
        composable<Screens.Settings> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onInstructionPageClicked = {
                    navController.popBackStack()

                    navController.navigate(Screens.Welcome)}
            )
        }
    }

}