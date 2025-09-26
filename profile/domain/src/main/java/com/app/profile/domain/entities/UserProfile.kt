package com.app.profile.domain.entities

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String?,
    val isEmailVerified: Boolean = false
)
