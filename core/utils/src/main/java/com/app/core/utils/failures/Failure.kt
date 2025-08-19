package com.app.core.utils.failures

sealed class Failure : Throwable() {
    data class NetworkConnection(override val message: String? = null) : Failure()
    data class ServerError(override val message: String? = null) : Failure()
    data class Unauthorized(override val message: String? = null) : Failure()
    data class NotFound(override val message: String? = null) : Failure()
    data class UnknownError(override val message: String? = null) : Failure()
    data class AuthenticationError(override val message: String? = null) : Failure()
    data class AuthenticationCancelled(override val message: String? = null) : Failure()
    data class DataError(override val message: String? = null) : Failure()
}