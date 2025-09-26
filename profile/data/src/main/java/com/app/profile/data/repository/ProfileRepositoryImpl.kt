package com.app.profile.data.repository

import com.app.auth.domain.repository.AuthRepository
import com.app.profile.domain.entities.UserProfile
import com.app.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val authRepository: AuthRepository
) : ProfileRepository {

    override fun getCurrentUserProfile(): UserProfile? {
        val user = authRepository.getCurrentUser()
        return user?.let {
            UserProfile(
                id = it.id,
                name = it.name ?: "Guest",
                email = it.email,
                profilePictureUrl = it.profilePictureUrl,
                isEmailVerified = false // Firebase user email verification can be added later
            )
        }
    }

    override suspend fun logout(): Boolean {
        return authRepository.logout()
    }
}
