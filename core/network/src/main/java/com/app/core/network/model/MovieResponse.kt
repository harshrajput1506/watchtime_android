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

// Media Details Response Models
@Serializable
data class GenreResponse(
    val id: Int,
    val name: String
)

@Serializable
data class ProductionCompany(
    val id: Int,
    val name: String,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class ProductionCountry(
    @SerialName("iso_3166_1")
    val iso31661: String,
    val name: String
)

@Serializable
data class SpokenLanguage(
    @SerialName("english_name")
    val englishName: String,
    @SerialName("iso_639_1")
    val iso6391: String,
    val name: String
)

@Serializable
data class MovieDetailsResponse(
    val id: Int,
    val title: String,
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
    val popularity: Double,
    val runtime: Int?,
    val budget: Long,
    val revenue: Long,
    val genres: List<GenreResponse>,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountry>,
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("original_language")
    val originalLanguage: String,
    val adult: Boolean,
    val video: Boolean,
    @SerialName("imdb_id")
    val imdbId: String?,
    val homepage: String?
)

@Serializable
data class NetworkResponse(
    val id: Int,
    val name: String,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class SeasonResponse(
    @SerialName("air_date")
    val airDate: String?,
    @SerialName("episode_count")
    val episodeCount: Int,
    val id: Int,
    val name: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("season_number")
    val seasonNumber: Int,
    @SerialName("vote_average")
    val voteAverage: Double
)

@Serializable
data class CreatedBy(
    val id: Int,
    @SerialName("credit_id")
    val creditId: String,
    val name: String,
    val gender: Int,
    @SerialName("profile_path")
    val profilePath: String?
)

@Serializable
data class TvShowDetailsResponse(
    val id: Int,
    val name: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("first_air_date")
    val firstAirDate: String,
    @SerialName("last_air_date")
    val lastAirDate: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    val popularity: Double,
    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int,
    @SerialName("number_of_seasons")
    val numberOfSeasons: Int,
    @SerialName("episode_run_time")
    val episodeRunTime: List<Int>,
    val genres: List<GenreResponse>,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountry>,
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("original_language")
    val originalLanguage: String,
    val adult: Boolean,
    val homepage: String?,
    @SerialName("in_production")
    val inProduction: Boolean,
    val networks: List<NetworkResponse>,
    @SerialName("created_by")
    val createdBy: List<CreatedBy>,
    val seasons: List<SeasonResponse>,
    @SerialName("origin_country")
    val originCountry: List<String>
)

// Cast and Crew Models
@Serializable
data class CastMemberResponse(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val profilePath: String?,
    val character: String,
    @SerialName("credit_id")
    val creditId: String,
    val order: Int,
    val gender: Int?,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    @SerialName("original_name")
    val originalName: String,
    val popularity: Double,
    val adult: Boolean
)

@Serializable
data class CrewMemberResponse(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val profilePath: String?,
    val job: String,
    val department: String,
    @SerialName("credit_id")
    val creditId: String,
    val gender: Int?,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    @SerialName("original_name")
    val originalName: String,
    val popularity: Double,
    val adult: Boolean
)

@Serializable
data class CreditsResponse(
    val id: Int,
    val cast: List<CastMemberResponse>,
    val crew: List<CrewMemberResponse>
)

// Watch Providers Models
@Serializable
data class WatchProviderResponse(
    @SerialName("logo_path")
    val logoPath: String,
    @SerialName("provider_id")
    val providerId: Int,
    @SerialName("provider_name")
    val providerName: String,
    @SerialName("display_priority")
    val displayPriority: Int
)

@Serializable
data class CountryWatchProvidersResponse(
    val link: String?,
    @SerialName("flatrate")
    val flatRate: List<WatchProviderResponse>? = null,
    val buy: List<WatchProviderResponse>? = null,
    val rent: List<WatchProviderResponse>? = null
)

@Serializable
data class WatchProvidersResponse(
    val id: Int,
    val results: Map<String, CountryWatchProvidersResponse>
)

// Season Details Models
@Serializable
data class EpisodeResponse(
    @SerialName("air_date")
    val airDate: String?,
    @SerialName("episode_number")
    val episodeNumber: Int,
    val id: Int,
    val name: String,
    val overview: String,
    @SerialName("production_code")
    val productionCode: String?,
    val runtime: Int?,
    @SerialName("season_number")
    val seasonNumber: Int,
    @SerialName("show_id")
    val showId: Int,
    @SerialName("still_path")
    val stillPath: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    val crew: List<CrewMemberResponse>,
    @SerialName("guest_stars")
    val guestStars: List<CastMemberResponse>
)

@Serializable
data class SeasonDetailsResponse(
    @SerialName("_id")
    val id: String,
    @SerialName("air_date")
    val airDate: String?,
    val episodes: List<EpisodeResponse>,
    val name: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("season_number")
    val seasonNumber: Int,
    @SerialName("vote_average")
    val voteAverage: Double
)
