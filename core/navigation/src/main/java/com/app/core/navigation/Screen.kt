package com.app.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    object Home : Screen()

    @Serializable
    object Auth : Screen()
}