package com.androsmith.wiflow.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.androsmith.wiflow.ui.navigation.Home
import com.androsmith.wiflow.ui.navigation.Settings
import com.androsmith.wiflow.ui.screens.home.HomeScreen
import com.androsmith.wiflow.ui.screens.settings.SettingsScreen


@Composable
fun WiFlowApp(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(
                onNavigateToSettings = { navController.navigate(Settings)}
            )
        }
        composable<Settings> {
            SettingsScreen(
                onNavigateBack = {navController.popBackStack()}
            )
        }
    }

}