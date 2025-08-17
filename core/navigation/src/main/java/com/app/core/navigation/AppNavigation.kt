package com.app.core.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.app.auth.ui.screens.AuthScreen
import com.app.auth.ui.states.AuthState
import com.app.auth.ui.viewmodels.AuthViewModel
import com.app.core.home.HomeScreen
import com.app.media.ui.MediaDetailsScreen
import org.koin.compose.viewmodel.koinViewModel

@ExperimentalSharedTransitionApi
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

    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = startDestination) {

            composable<Screen.Home> {
                HomeScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    navigateToMediaDetails = { mediaId, mediaType, posterUrl ->
                        navController.navigate(Screen.MediaDetails(mediaId, mediaType, posterUrl))
                    }
                )
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

            composable<Screen.MediaDetails> { backStackEntry ->
                val screen = backStackEntry.toRoute<Screen.MediaDetails>()
                MediaDetailsScreen(
                    mediaId = screen.id,
                    mediaType = screen.type,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    posterUrl = screen.posterUrl,
                    onNavigateBack = {
                        navController.popBackStack()
                    })
            }
        }
    }
}
