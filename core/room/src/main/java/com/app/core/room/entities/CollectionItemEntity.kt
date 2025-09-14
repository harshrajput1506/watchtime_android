package com.app.core.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "collection_items",
    foreignKeys = [
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["collection_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ContentMetadataEntity::class,
            parentColumns = ["id"],
            childColumns = ["content_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["collection_id"]), Index(value = ["content_id"])]
)
data class CollectionItemEntity(
    @PrimaryKey val id: UUID,
    val collection_id: UUID,
    val content_id: UUID,
    val tmdb_id: Int,
    val media_type: String,
    val added_at: Long,
    val notes: String?
)
