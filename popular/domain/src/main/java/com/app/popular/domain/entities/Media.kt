package com.app.popular.domain.entities

data class Media(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val type: MediaType
)

enum class MediaType {
    MOVIE,
    TV
}