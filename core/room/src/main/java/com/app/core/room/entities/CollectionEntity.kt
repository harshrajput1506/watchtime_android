package com.app.core.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey val id: UUID,
    val user_id: String,
    val name: String,
    val description: String?,
    val is_default: Boolean,
    val is_public: Boolean,
    val created_at: Long,
    val updated_at: Long
)
