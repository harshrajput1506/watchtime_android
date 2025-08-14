package com.app.auth.domain.repository

import com.app.auth.domain.entities.UserEntity

interface AuthRepository {
    suspend fun login(): UserEntity
    suspend fun logout(): Boolean
    fun isLoggedIn(): Boolean
    fun getCurrentUser(): UserEntity?
}