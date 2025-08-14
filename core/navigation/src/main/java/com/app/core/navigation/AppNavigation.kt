package com.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.auth.ui.screens.AuthScreen
import com.app.core.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home) {

        composable<Screen.Home> {
            HomeScreen()
        }

        composable<Screen.Auth> {
            AuthScreen()
        }
    }
}
