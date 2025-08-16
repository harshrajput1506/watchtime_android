package com.app.media.domain.model

data class MediaDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val genres: List<Genre>,
    val runtime: Int? = null, // for movies
    val numberOfSeasons: Int? = null, // for TV shows
    val numberOfEpisodes: Int? = null, // for TV shows
    val status: String,
    val tagline: String?,
    val homepage: String?,
    val seasons: List<Season> = emptyList(), // for TV shows
    val networks: List<Network> = emptyList(), // for TV shows
    val createdBy: List<Creator> = emptyList() // for TV shows
)

data class Genre(
    val id: Int,
    val name: String
)

data class Season(
    val id: Int,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val seasonNumber: Int,
    val episodeCount: Int,
    val airDate: String?,
    val voteAverage: Double
)

data class Network(
    val id: Int,
    val name: String,
    val logoPath: String?
)

data class Creator(
    val id: Int,
    val name: String,
    val profilePath: String?
)

data class Cast(
    val id: Int,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)

data class CastMember(
    val id: Int,
    val name: String,
    val profilePath: String?,
    val character: String,
    val order: Int
)

data class CrewMember(
    val id: Int,
    val name: String,
    val profilePath: String?,
    val job: String,
    val department: String
)

data class WatchProviders(
    val id: Int,
    val countryProviders: Map<String, CountryProviders>
)

data class CountryProviders(
    val link: String?,
    val streaming: List<Provider>,
    val buy: List<Provider>,
    val rent: List<Provider>
)

data class Provider(
    val logoPath: String,
    val providerId: Int,
    val providerName: String
)

data class SeasonDetails(
    val id: String,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val seasonNumber: Int,
    val airDate: String?,
    val episodes: List<Episode>
)

data class Episode(
    val id: Int,
    val name: String,
    val overview: String,
    val episodeNumber: Int,
    val airDate: String?,
    val runtime: Int?,
    val stillPath: String?,
    val voteAverage: Double,
    val voteCount: Int
)
