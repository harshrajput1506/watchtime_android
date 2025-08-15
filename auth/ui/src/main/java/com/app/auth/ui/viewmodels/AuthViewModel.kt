package com.app.auth.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.auth.domain.repository.AuthRepository
import com.app.auth.ui.states.AuthState
import com.app.core.utils.failures.Failure
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authState = mutableStateOf<AuthState>(AuthState.Unauthenticated)
    val authState get() = _authState

    init {
        val user = authRepository.getCurrentUser()
        if (user != null) {
            _authState.value = AuthState.Authenticated(user)
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun login() {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val user = authRepository.login()
                _authState.value = AuthState.Authenticated(user)
            } catch (e: Throwable) {
                when (e) {
                    is Failure.AuthenticationCancelled -> _authState.value =
                        AuthState.Unauthenticated

                    is Failure -> _authState.value = AuthState.Error("${e.message}")
                    else -> _authState.value = AuthState.Error("An unexpected error occurred")
                }

            }
        }
    }


}