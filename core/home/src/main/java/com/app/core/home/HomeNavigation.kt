package com.app.core.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Composable
fun HomeNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navController, startDestination = HomeDestination.Popular) {
        composable<HomeDestination.Popular> {
            DemoScreen(modifier, "Popular Screen")
        }
        composable<HomeDestination.Discover> {

            DemoScreen(modifier, "Discover Screen")
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