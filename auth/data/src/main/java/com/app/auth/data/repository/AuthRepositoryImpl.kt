package com.app.auth.data.repository

import android.util.Log
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

    override suspend fun logout(): Boolean {
        try {
            authService.signOut()
            return true
        } catch (e: Exception) {
            // Log the error or handle it as needed
            Log.e(TAG, "logout: ", e)
            return false
        }
    }

    override fun isLoggedIn(): Boolean {
        try {
            return authService.isLoggedIn()
        } catch (e: Exception) {
            // Log the error or handle it as needed
            Log.e(TAG, "isLoggedIn: e", e)
            return false
        }
    }

    override fun getCurrentUser(): UserEntity? {
        try {
            val user = authService.getCurrentUser()
            return if (user != null) {
                UserEntity(
                    id = user.uid,
                    name = user.displayName ?: "Unknown",
                    email = user.email ?: "No Email",
                    profilePictureUrl = user.photoUrl?.toString(),
                )
            } else {
                null
            }
        } catch (e: Exception) {
            // Log the error or handle it as needed
            Log.e(TAG, "getCurrentUser: e", e)
            return null
        }
    }


}