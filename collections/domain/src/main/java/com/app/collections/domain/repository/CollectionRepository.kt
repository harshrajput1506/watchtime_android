package com.app.collections.domain.repository

import com.app.collections.domain.models.Collection
import com.app.collections.domain.models.CollectionItem
import com.app.collections.domain.models.ContentMetadata
import com.app.collections.domain.models.DefaultCollectionType
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CollectionRepository {

    // Collection operations
    suspend fun createCollection(
        userId: String,
        name: String,
        description: String? = null,
        isPublic: Boolean = false
    ): Result<Collection>

    suspend fun getCollection(collectionId: UUID): Flow<Collection?>

    suspend fun getCollections(): Flow<List<Collection>>

    suspend fun getDefaultCollection(
        type: DefaultCollectionType
    ): Flow<Collection?>

    suspend fun deleteCollection(collectionId: UUID): Result<Unit>

    suspend fun updateCollection(
        collectionId: UUID,
        name: String? = null,
        description: String? = null,
        isPublic: Boolean? = null
    ): Result<Unit>

    // Collection item operations
    suspend fun addToCollection(
        collectionId: UUID,
        tmdbId: Int,
        mediaType: String,
        contentMetadata: ContentMetadata,
        notes: String? = null
    ): Result<CollectionItem>

    suspend fun removeFromCollection(
        collectionId: UUID,
        tmdbId: Int,
        mediaType: String
    ): Result<Unit>

    suspend fun isInCollection(
        collectionId: UUID,
        tmdbId: Int,
        mediaType: String
    ): Flow<Boolean>

    // Default collection convenience methods
    suspend fun addToWatchlist(
        userId: String,
        tmdbId: Int,
        mediaType: String,
        contentMetadata: ContentMetadata
    ): Result<CollectionItem>

    suspend fun addToAlreadyWatched(
        userId: String,
        tmdbId: Int,
        mediaType: String,
        contentMetadata: ContentMetadata
    ): Result<CollectionItem>

    suspend fun removeFromWatchlist(
        userId: String,
        tmdbId: Int,
        mediaType: String
    ): Result<Unit>

    suspend fun removeFromAlreadyWatched(
        userId: String,
        tmdbId: Int,
        mediaType: String
    ): Result<Unit>

    suspend fun isInWatchlist(
        tmdbId: Int,
        mediaType: String
    ): Flow<Boolean>

    suspend fun isAlreadyWatched(
        tmdbId: Int,
        mediaType: String
    ): Flow<Boolean>

    // Content metadata operations
    suspend fun saveContentMetadata(contentMetadata: ContentMetadata): Result<Unit>

    suspend fun getContentMetadata(tmdbId: Int, mediaType: String): ContentMetadata?

    // Initialize default collections for user
    suspend fun initializeDefaultCollections(userId: String): Result<Unit>
}
