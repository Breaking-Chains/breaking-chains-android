package com.breakingchains.app.ui.navigation

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Home")
}
