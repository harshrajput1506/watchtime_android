package com.app.auth.domain.entities

data class UserEntity(
    val id: String,
    val email: String,
    val name: String? = null,
    val profilePictureUrl: String? = null,
)
