package com.app.auth.ui.states

import com.app.auth.domain.entities.UserEntity

sealed class AuthState {
    data class Authenticated(val user: UserEntity) : AuthState()
    data class Error(val error: String) : AuthState()
    object Loading : AuthState()
    object Unauthenticated : AuthState()
}