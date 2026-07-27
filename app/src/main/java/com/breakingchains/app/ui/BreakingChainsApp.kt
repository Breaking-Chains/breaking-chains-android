package com.breakingchains.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.breakingchains.app.ui.navigation.Screen
import com.breakingchains.app.ui.screens.home.HomeScreen
import com.breakingchains.app.ui.theme.BreakingChainsTheme

@Composable
fun BreakingChainsApp() {
    BreakingChainsTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
        }
    }
}
