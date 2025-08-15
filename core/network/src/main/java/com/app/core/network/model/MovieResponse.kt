package com.app.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val id: Int,
    val title: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    @SerialName("video")
    val video: Boolean
)

@Serializable
data class TvShowResponse(
    val id: Int,
    val name: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("first_air_date")
    val firstAirDate: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    @SerialName("origin_country")
    val originCountry: List<String>
)

@Serializable
data class MediaResponse(
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    val id: Int,
    val name: String? = null, // For TV shows
    @SerialName("original_name")
    val originalName: String? = null,
    val title: String? = null, // For Movies
    @SerialName("original_title")
    val originalTitle: String? = null,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("media_type")
    val mediaType: String,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    val popularity: Double,
    @SerialName("first_air_date")
    val firstAirDate: String? = null, // TV
    @SerialName("release_date")
    val releaseDate: String? = null, // Movies
    @SerialName("video")
    val video: Boolean? = null, // Movies only
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("origin_country")
    val originCountry: List<String>? = null // TV only
)

@Serializable
data class MoviesListResponse(
    val page: Int,
    val results: List<MovieResponse>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class MediaListResponse(
    val page: Int,
    val results: List<MediaResponse>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)


@Serializable
data class TvShowsListResponse(
    val page: Int,
    val results: List<TvShowResponse>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)
