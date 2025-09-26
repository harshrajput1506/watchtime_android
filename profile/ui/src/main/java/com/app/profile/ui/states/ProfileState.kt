package com.app.profile.ui.states

import com.app.profile.domain.entities.UserProfile

sealed class ProfileState {
    data object Loading : ProfileState()
    data class Success(val userProfile: UserProfile) : ProfileState()
    data class Error(val message: String) : ProfileState()
    data object LoggedOut : ProfileState()
}
