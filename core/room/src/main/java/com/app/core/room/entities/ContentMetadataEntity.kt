package com.app.core.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "content_metadata")
data class ContentMetadataEntity(
    @PrimaryKey val id: UUID,
    val tmdb_id: Int,
    val media_type: String,
    val title: String,
    val original_title: String?,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?, // ISO string (or convert to Long timestamp)
    val genres: String?,       // Could store JSON as String
    val vote_average: Double?,
    val vote_count: Int?,
    val popularity: Double?,
    val adult: Boolean,
    val created_at: Long,
    val updated_at: Long
)
