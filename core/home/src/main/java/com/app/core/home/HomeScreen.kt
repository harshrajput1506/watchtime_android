package com.app.core.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToMediaDetails: (Int, String, String?) -> Unit
) {
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selected = currentRoute ?: HomeDestination.Popular.toString(),
                onItemClick = { destination ->
                    navController.navigate(destination) {
                        popUpTo(HomeDestination.Popular) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true

                    }
                })
        }
    ) { innerPadding ->

        HomeNavigation(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            navigateToMediaDetails = navigateToMediaDetails
        )
    }
}