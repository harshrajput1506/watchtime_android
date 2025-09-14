package com.app.media.ui.utils

import com.app.collections.domain.models.ContentMetadata
import com.app.media.domain.model.MediaDetails
import java.util.UUID

fun MediaDetails.toContentMetadata(mediaType: String): ContentMetadata {
    return ContentMetadata(
        id = UUID.randomUUID(),
        tmdbId = this.id,
        title = this.title,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        mediaType = mediaType,
        releaseDate = this.releaseDate,
        adult = false,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        originalTitle = null,
        overview = null,
        genres = null,
        voteAverage = null,
        voteCount = null,
        popularity = null,
    )
}