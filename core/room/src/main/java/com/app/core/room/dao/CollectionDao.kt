package com.app.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.app.core.room.entities.CollectionEntity
import com.app.core.room.entities.CollectionItemEntity
import com.app.core.room.entities.ContentMetadataEntity
import com.app.core.room.relation.CollectionItemWithContent
import com.app.core.room.relation.CollectionWithItems
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CollectionDao {
    // Insert operations
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertItem(item: CollectionItemEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertContent(content: ContentMetadataEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCollections(collections: List<CollectionEntity>)

    // Update operations
    @Update
    suspend fun updateCollection(collection: CollectionEntity)

    // Delete operations
    @Query("DELETE FROM collections WHERE id = :id")
    suspend fun deleteCollection(id: UUID)

    @Query("DELETE FROM collection_items WHERE collection_id = :collectionId AND tmdb_id = :tmdbId AND media_type = :mediaType")
    suspend fun deleteCollectionItem(collectionId: UUID, tmdbId: Int, mediaType: String)

    // Collection queries
    @Transaction
    @Query("SELECT * FROM collections WHERE id = :id")
    fun getCollectionWithItems(id: UUID): Flow<CollectionWithItems?>

    @Transaction
    @Query("SELECT * FROM collection_items WHERE id = :id")
    fun getItemWithContent(id: UUID): Flow<CollectionItemWithContent>

    @Transaction
    @Query("SELECT * FROM collections")
    fun getAllCollections(): Flow<List<CollectionWithItems>>

    // Get Collection
    @Transaction
    @Query("SELECT * FROM collections WHERE id = :id")
    fun getCollectionById(id: UUID): Flow<CollectionEntity?>

    // Get Default Collection by name (e.g., "Watch Later" collection)
    @Transaction
    @Query("SELECT * FROM collections WHERE name = :name LIMIT 1")
    fun getCollectionByName(name: String): Flow<CollectionWithItems?>

    // Collection item queries
    @Query("SELECT COUNT(*) > 0 FROM collection_items WHERE collection_id = :collectionId AND tmdb_id = :tmdbId AND media_type = :mediaType")
    fun isInCollection(collectionId: UUID, tmdbId: Int, mediaType: String): Flow<Boolean>

    @Query("""
        SELECT COUNT(*) > 0 FROM collection_items ci 
        JOIN collections c ON ci.collection_id = c.id 
        WHERE c.name = :collectionName AND c.is_default = 1 
        AND ci.tmdb_id = :tmdbId AND ci.media_type = :mediaType
    """)
    fun isInDefaultCollection(collectionName: String, tmdbId: Int, mediaType: String): Flow<Boolean>

    // Content metadata queries
    @Query("SELECT * FROM content_metadata WHERE tmdb_id = :tmdbId AND media_type = :mediaType LIMIT 1")
    suspend fun getContentMetadata(tmdbId: Int, mediaType: String): ContentMetadataEntity?

    @Query("SELECT * FROM content_metadata WHERE id = :contentId")
    suspend fun getContentMetadataById(contentId: UUID): ContentMetadataEntity?
}