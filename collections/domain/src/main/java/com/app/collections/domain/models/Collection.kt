package com.app.collections.domain.models

import java.util.UUID

data class Collection(
    val id: UUID,
    val userId: String,
    val name: String,
    val description: String?,
    val isDefault: Boolean,
    val isPublic: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val items: List<CollectionItem> = emptyList()
)

data class CollectionItem(
    val id: UUID,
    val collectionId: UUID,
    val contentId: UUID,
    val tmdbId: Int,
    val mediaType: String,
    val addedAt: Long,
    val notes: String?,
    val content: ContentMetadata
)

data class ContentMetadata(
    val id: UUID,
    val tmdbId: Int,
    val mediaType: String,
    val title: String,
    val originalTitle: String?,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val genres: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val popularity: Double?,
    val adult: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

// Collection types for default collections
enum class DefaultCollectionType(val displayName: String) {
    WATCHLIST("Watchlist"),
    ALREADY_WATCHED("Already Watched")
}

// Media types
enum class MediaType(val value: String) {
    MOVIE("movie"),
    TV("tv")
}
