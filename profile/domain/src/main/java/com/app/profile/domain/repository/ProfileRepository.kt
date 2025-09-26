package com.app.profile.domain.repository

import com.app.profile.domain.entities.UserProfile

interface ProfileRepository {
    fun getCurrentUserProfile(): UserProfile?
    suspend fun logout(): Boolean
}
