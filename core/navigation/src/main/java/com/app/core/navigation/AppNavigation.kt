package com.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.auth.ui.screens.AuthScreen
import com.app.auth.ui.states.AuthState
import com.app.auth.ui.viewmodels.AuthViewModel
import com.app.core.home.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = koinViewModel(),
) {
    val navController = rememberNavController()
    val userAuthenticated = when (authViewModel.authState.value) {
        is AuthState.Authenticated -> true
        is AuthState.Unauthenticated -> false
        else -> false // Handle other states if necessary
    }
    val startDestination = if (userAuthenticated) Screen.Home else Screen.Auth

    NavHost(navController = navController, startDestination = startDestination) {

        composable<Screen.Home> {
            HomeScreen()
        }

        composable<Screen.Auth> {
            AuthScreen {
                navController.navigate(Screen.Home) {
                    popUpTo(Screen.Auth) {
                        inclusive = true
                    }
                }
            }
        }
    }
}
