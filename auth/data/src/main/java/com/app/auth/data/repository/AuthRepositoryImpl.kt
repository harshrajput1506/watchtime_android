package com.app.auth.data.repository

import com.app.auth.data.services.FirebaseAuthService
import com.app.auth.domain.entities.UserEntity
import com.app.auth.domain.repository.AuthRepository
import com.app.core.utils.failures.Failure

class AuthRepositoryImpl(
    private val authService: FirebaseAuthService
) : AuthRepository {

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }

    override suspend fun login(): UserEntity {
        try {
            // start google sign-in flow
            val idToken = authService.googleSignIn()
            // use the idToken to authenticate with Firebase
            val firebaseUser = authService.firebaseAuthWithGoogle(idToken)

            // Check if the user is null
            if (firebaseUser == null) {
                throw Failure.AuthenticationError("Firebase user is null")
            }

            // map FirebaseUser to UserEntity
            return UserEntity(
                id = firebaseUser.uid,
                name = firebaseUser.displayName ?: "Unknown",
                email = firebaseUser.email ?: "No Email",
                profilePictureUrl = firebaseUser.photoUrl?.toString(),
            )


        } catch (e: Throwable) {
            // Handle any exceptions that may occur during the login process
            throw e

        }

    }

    override fun logout(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): UserEntity? {
        TODO("Not yet implemented")
    }


}