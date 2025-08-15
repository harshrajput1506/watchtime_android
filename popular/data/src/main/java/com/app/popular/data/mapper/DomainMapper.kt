package com.app.popular.data.mapper

import com.app.core.network.model.MediaResponse
import com.app.core.network.model.MovieResponse
import com.app.core.network.model.TvShowResponse
import com.app.core.network.util.ImageUrlBuilder
import com.app.popular.domain.entities.Media
import com.app.popular.domain.entities.MediaTYpe

fun MovieResponse.toDomain(): Media {
    return Media(
        id = id,
        title = title,
        overview = overview,
        posterUrl = ImageUrlBuilder.buildPosterUrl(posterPath),
        backdropUrl = ImageUrlBuilder.buildBackdropUrl(backdropPath),
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        type = MediaTYpe.MOVIE
    )
}

fun TvShowResponse.toDomain(): Media {
    return Media(
        id = id,
        title = name,
        overview = overview,
        posterUrl = ImageUrlBuilder.buildPosterUrl(posterPath),
        backdropUrl = ImageUrlBuilder.buildBackdropUrl(backdropPath),
        releaseDate = firstAirDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        type = MediaTYpe.TV
    )
}

fun MediaResponse.toDomain(): Media {
    return Media(
        id = id,
        title = if (mediaType == "movie") title ?: "Unknown" else name ?: "Unknown",
        overview = overview,
        posterUrl = ImageUrlBuilder.buildPosterUrl(posterPath),
        backdropUrl = ImageUrlBuilder.buildBackdropUrl(backdropPath),
        releaseDate = if (mediaType == "movie") releaseDate ?: "Unknown" else firstAirDate
            ?: "Unknown",
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        type = if (mediaType == "movie") MediaTYpe.MOVIE else MediaTYpe.TV
    )
}
