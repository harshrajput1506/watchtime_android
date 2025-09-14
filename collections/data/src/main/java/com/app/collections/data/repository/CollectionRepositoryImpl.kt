package com.app.collections.data.repository

import com.app.core.room.dao.CollectionDao
import com.app.collections.data.mappers.toDomain
import com.app.collections.data.mappers.toEntity
import com.app.collections.domain.models.Collection
import com.app.collections.domain.models.CollectionItem
import com.app.collections.domain.models.ContentMetadata
import com.app.collections.domain.models.DefaultCollectionType
import com.app.collections.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

class CollectionRepositoryImpl(
    private val collectionDao: CollectionDao
) : CollectionRepository {

    override suspend fun createCollection(
        userId: String,
        name: String,
        description: String?,
        isPublic: Boolean
    ): Result<Collection> {
        return try {
            val currentTime = System.currentTimeMillis()
            val collection = Collection(
                id = UUID.randomUUID(),
                userId = userId,
                name = name,
                description = description,
                isDefault = false,
                isPublic = isPublic,
                createdAt = currentTime,
                updatedAt = currentTime
            )

            collectionDao.insertCollection(collection.toEntity())
            Result.success(collection)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCollection(collectionId: UUID): Flow<Collection?> {
        return collectionDao.getCollectionWithItems(collectionId)
            .map { it?.toDomain() }
    }

    override suspend fun getCollections(): Flow<List<Collection>> {
        return collectionDao.getAllCollections()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getDefaultCollection(
        type: DefaultCollectionType
    ): Flow<Collection?> {
        return collectionDao.getCollectionByName(type.displayName)
            .map { it?.toDomain() }
    }

    override suspend fun deleteCollection(collectionId: UUID): Result<Unit> {
        return try {
            collectionDao.deleteCollection(collectionId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCollection(
        collectionId: UUID,
        name: String?,
        description: String?,
        isPublic: Boolean?
    ): Result<Unit> {
        return try {
            val existingCollection = collectionDao.getCollectionById(collectionId).first()
                ?: return Result.failure(Exception("Collection not found"))

            val updatedCollection = existingCollection.copy(
                name = name ?: existingCollection.name,
                description = description ?: existingCollection.description,
                is_public = isPublic ?: existingCollection.is_public,
                updated_at = System.currentTimeMillis()
            )

            collectionDao.updateCollection(updatedCollection)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addToCollection(
        collectionId: UUID,
        tmdbId: Int,
        mediaType: String,
        contentMetadata: ContentMetadata,
        notes: String?
    ): Result<CollectionItem> {
        return try {
            // Save or update content metadata
            collectionDao.insertContent(contentMetadata.toEntity())

            val collectionItem = CollectionItem(
                id = UUID.randomUUID(),
                collectionId = collectionId,
                contentId = contentMetadata.id,
                tmdbId = tmdbId,
                mediaType = mediaType,
                addedAt = System.currentTimeMillis(),
                notes = notes,
                content = contentMetadata
            )

            collectionDao.insertItem(collectionItem.toEntity())
            Result.success(collectionItem)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFromCollection(
        collectionId: UUID,
        tmdbId: Int,
        mediaType: String
    ): Result<Unit> {
        return try {
            collectionDao.deleteCollectionItem(collectionId, tmdbId, mediaType)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isInCollection(
        collectionId: UUID,
        tmdbId: Int,
        mediaType: String
    ): Flow<Boolean> {
        return collectionDao.isInCollection(collectionId, tmdbId, mediaType)
    }

    override suspend fun addToWatchlist(
        userId: String,
        tmdbId: Int,
        mediaType: String,
        contentMetadata: ContentMetadata
    ): Result<CollectionItem> {
        return try {
            val watchlistCollection = getOrCreateDefaultCollection(userId, DefaultCollectionType.WATCHLIST)
            addToCollection(watchlistCollection.id, tmdbId, mediaType, contentMetadata)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addToAlreadyWatched(
        userId: String,
        tmdbId: Int,
        mediaType: String,
        contentMetadata: ContentMetadata
    ): Result<CollectionItem> {
        return try {
            val alreadyWatchedCollection = getOrCreateDefaultCollection(userId = userId,DefaultCollectionType.ALREADY_WATCHED)
            addToCollection(alreadyWatchedCollection.id, tmdbId, mediaType, contentMetadata)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFromWatchlist(
        userId: String,
        tmdbId: Int,
        mediaType: String
    ): Result<Unit> {
        return try {
            val watchlistCollection = getOrCreateDefaultCollection(userId,DefaultCollectionType.WATCHLIST)
            removeFromCollection(watchlistCollection.id, tmdbId, mediaType)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFromAlreadyWatched(
        userId: String,
        tmdbId: Int,
        mediaType: String
    ): Result<Unit> {
        return try {
            val alreadyWatchedCollection = getOrCreateDefaultCollection(userId,DefaultCollectionType.ALREADY_WATCHED)
            removeFromCollection(alreadyWatchedCollection.id, tmdbId, mediaType)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isInWatchlist(
        tmdbId: Int,
        mediaType: String
    ): Flow<Boolean> {
        return collectionDao.isInDefaultCollection(
            DefaultCollectionType.WATCHLIST.displayName,
            tmdbId,
            mediaType
        )
    }

    override suspend fun isAlreadyWatched(
        tmdbId: Int,
        mediaType: String
    ): Flow<Boolean> {
        return collectionDao.isInDefaultCollection(
            DefaultCollectionType.ALREADY_WATCHED.displayName,
            tmdbId,
            mediaType
        )
    }

    override suspend fun saveContentMetadata(contentMetadata: ContentMetadata): Result<Unit> {
        return try {
            collectionDao.insertContent(contentMetadata.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getContentMetadata(tmdbId: Int, mediaType: String): ContentMetadata? {
        return collectionDao.getContentMetadata(tmdbId, mediaType)?.toDomain()
    }

    override suspend fun initializeDefaultCollections(userId: String): Result<Unit> {
        return try {
            val currentTime = System.currentTimeMillis()

            val watchlistCollection = Collection(
                id = UUID.randomUUID(),
                userId = userId,
                name = DefaultCollectionType.WATCHLIST.displayName,
                description = "Movies and TV shows you want to watch",
                isDefault = true,
                isPublic = false,
                createdAt = currentTime,
                updatedAt = currentTime
            )

            val alreadyWatchedCollection = Collection(
                id = UUID.randomUUID(),
                userId = userId,
                name = DefaultCollectionType.ALREADY_WATCHED.displayName,
                description = "Movies and TV shows you have watched",
                isDefault = true,
                isPublic = false,
                createdAt = currentTime,
                updatedAt = currentTime
            )

            collectionDao.insertCollections(
                listOf(
                    watchlistCollection.toEntity(),
                    alreadyWatchedCollection.toEntity()
                )
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getOrCreateDefaultCollection(
        userId: String,
        type: DefaultCollectionType
    ): Collection {
        // Try to get existing default collection using the Flow method
        val existingCollection = collectionDao.getCollectionByName(type.displayName).first()

        return if (existingCollection != null) {
            existingCollection.toDomain()
        } else {
            val currentTime = System.currentTimeMillis()
            val collection = Collection(
                id = UUID.randomUUID(),
                userId = userId, // You'll need to handle user ID properly
                name = type.displayName,
                description = when (type) {
                    DefaultCollectionType.WATCHLIST -> "Movies and TV shows you want to watch"
                    DefaultCollectionType.ALREADY_WATCHED -> "Movies and TV shows you have watched"
                },
                isDefault = true,
                isPublic = false,
                createdAt = currentTime,
                updatedAt = currentTime
            )

            collectionDao.insertCollection(collection.toEntity())
            collection
        }
    }
}
