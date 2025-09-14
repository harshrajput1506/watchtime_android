package com.app.collections.data.mappers

import com.app.core.room.entities.CollectionEntity
import com.app.core.room.entities.CollectionItemEntity
import com.app.core.room.entities.ContentMetadataEntity
import com.app.core.room.relation.CollectionItemWithContent
import com.app.core.room.relation.CollectionWithItems
import com.app.collections.domain.models.Collection
import com.app.collections.domain.models.CollectionItem
import com.app.collections.domain.models.ContentMetadata

// Entity to Domain mapping
fun CollectionEntity.toDomain(items: List<CollectionItem> = emptyList()): Collection {
    return Collection(
        id = id,
        userId = user_id,
        name = name,
        description = description,
        isDefault = is_default,
        isPublic = is_public,
        createdAt = created_at,
        updatedAt = updated_at,
        items = items
    )
}

fun CollectionItemEntity.toDomain(content: ContentMetadata): CollectionItem {
    return CollectionItem(
        id = id,
        collectionId = collection_id,
        contentId = content_id,
        tmdbId = tmdb_id,
        mediaType = media_type,
        addedAt = added_at,
        notes = notes,
        content = content
    )
}

fun ContentMetadataEntity.toDomain(): ContentMetadata {
    return ContentMetadata(
        id = id,
        tmdbId = tmdb_id,
        mediaType = media_type,
        title = title,
        originalTitle = original_title,
        overview = overview,
        posterPath = poster_path,
        backdropPath = backdrop_path,
        releaseDate = release_date,
        genres = genres,
        voteAverage = vote_average,
        voteCount = vote_count,
        popularity = popularity,
        adult = adult,
        createdAt = created_at,
        updatedAt = updated_at
    )
}

// Relations to Domain mapping
fun CollectionWithItems.toDomain(): Collection {
    val domainItems = items.map { it.toDomain() }
    return collection.toDomain(domainItems)
}

fun CollectionItemWithContent.toDomain(): CollectionItem {
    return item.toDomain(content.toDomain())
}

// Domain to Entity mapping
fun Collection.toEntity(): CollectionEntity {
    return CollectionEntity(
        id = id,
        user_id = userId,
        name = name,
        description = description,
        is_default = isDefault,
        is_public = isPublic,
        created_at = createdAt,
        updated_at = updatedAt
    )
}

fun CollectionItem.toEntity(): CollectionItemEntity {
    return CollectionItemEntity(
        id = id,
        collection_id = collectionId,
        content_id = contentId,
        tmdb_id = tmdbId,
        media_type = mediaType,
        added_at = addedAt,
        notes = notes
    )
}

fun ContentMetadata.toEntity(): ContentMetadataEntity {
    return ContentMetadataEntity(
        id = id,
        tmdb_id = tmdbId,
        media_type = mediaType,
        title = title,
        original_title = originalTitle,
        overview = overview,
        poster_path = posterPath,
        backdrop_path = backdropPath,
        release_date = releaseDate,
        genres = genres,
        vote_average = voteAverage,
        vote_count = voteCount,
        popularity = popularity,
        adult = adult,
        created_at = createdAt,
        updated_at = updatedAt
    )
}
