package com.app.core.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.discover.ui.screens.DiscoverScreen
import com.app.popular.ui.screens.PopularScreen
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeNavigation(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    navigateToMediaDetails: (Int, String, String?) -> Unit
) {
    NavHost(navController = navController, startDestination = HomeDestination.Popular) {
        composable<HomeDestination.Popular> {
            PopularScreen(
                modifier = modifier,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onNavigateToMediaDetails = navigateToMediaDetails,
                onNavigateToDiscover = {
                    navController.navigate(HomeDestination.Discover) {
                        popUpTo(HomeDestination.Popular) {
                            inclusive = false
                        }
                    }
                }
            )
        }
        composable<HomeDestination.Discover> {

            DiscoverScreen(
                modifier = modifier,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onNavigateToMediaDetails = navigateToMediaDetails,
            )
        }
        composable<HomeDestination.Collections> {
            DemoScreen(modifier, "Collections Screen")
        }
        composable<HomeDestination.Profile> {
            DemoScreen(modifier, "Profile Screen")
        }
    }
}

@Serializable
sealed class HomeDestination() {
    @Serializable
    object Popular : HomeDestination()

    @Serializable
    object Discover : HomeDestination()

    @Serializable
    object Collections : HomeDestination()

    @Serializable
    object Profile : HomeDestination()

    override fun toString(): String {
        return when (this) {
            Popular -> "com.app.core.home.HomeDestination.Popular"
            Discover -> "com.app.core.home.HomeDestination.Discover"
            Collections -> "com.app.core.home.HomeDestination.Collections"
            Profile -> "com.app.core.home.HomeDestination.Profile"
        }
    }
}

@Composable
fun DemoScreen(
    modifier: Modifier = Modifier,
    name: String = "Demo Screen", // Default parameter for demonstration purposes
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}