// HomeScreen.kt
package com.app.core.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToMediaDetails: (Int, String, String?, String) -> Unit
) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    // Determine currently selected tab via hasRoute<T>()
    val selectedTab = when {
        currentDestination?.hasRoute<HomeDestination.Discover>() == true -> HomeDestination.Discover
        currentDestination?.hasRoute<HomeDestination.Collections>() == true -> HomeDestination.Collections
        currentDestination?.hasRoute<HomeDestination.Profile>() == true -> HomeDestination.Profile
        else -> HomeDestination.Popular
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selected = selectedTab,
                onItemClick = { dest ->
                    if (!currentDestination?.hasRoute(dest::class.java)!!) {
                        navController.navigate(dest) {
                            popUpTo(HomeDestination.Popular) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
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

// Small helper so we can pass a HomeDestination instance to hasRoute() above.
private fun <T : HomeDestination> NavDestination.hasRoute(clazz: Class<T>): Boolean {
    return when {
        this.hasRoute<HomeDestination.Popular>() -> clazz == HomeDestination.Popular::class.java
        this.hasRoute<HomeDestination.Discover>() -> clazz == HomeDestination.Discover::class.java
        this.hasRoute<HomeDestination.Collections>() -> clazz == HomeDestination.Collections::class.java
        this.hasRoute<HomeDestination.Profile>() -> clazz == HomeDestination.Profile::class.java
        else -> false
    }
}
