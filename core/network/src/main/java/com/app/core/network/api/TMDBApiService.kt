package com.app.core.network.api

import com.app.core.network.model.CreditsResponse
import com.app.core.network.model.MediaListResponse
import com.app.core.network.model.MovieDetailsResponse
import com.app.core.network.model.MoviesListResponse
import com.app.core.network.model.SeasonDetailsResponse
import com.app.core.network.model.TvShowDetailsResponse
import com.app.core.network.model.TvShowsListResponse
import com.app.core.network.model.WatchProvidersResponse
import com.app.core.utils.constants.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MoviesListResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MoviesListResponse

    @GET("trending/all/week")
    suspend fun getTrendingByWeekly(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MediaListResponse

    @GET("trending/all/day")
    suspend fun getTrendingByDaily(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MediaListResponse

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): TvShowsListResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): TvShowsListResponse

    // Discover endpoints
    @GET("discover/movie")
    suspend fun discoverMovies(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("with_genres") withGenres: String? = null,
        @Query("year") year: Int? = null,
        @Query("vote_average.gte") voteAverageGte: Float? = null,
        @Query("vote_average.lte") voteAverageLte: Float? = null
    ): MoviesListResponse

    @GET("discover/tv")
    suspend fun discoverTvShows(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("with_genres") withGenres: String? = null,
        @Query("first_air_date_year") year: Int? = null,
        @Query("vote_average.gte") voteAverageGte: Float? = null,
        @Query("vote_average.lte") voteAverageLte: Float? = null
    ): TvShowsListResponse

    // Search endpoints
    @GET("search/multi")
    suspend fun searchMulti(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("include_adult") includeAdult: Boolean = false
    ): MediaListResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("year") year: Int? = null
    ): MoviesListResponse

    @GET("search/tv")
    suspend fun searchTvShows(
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("first_air_date_year") year: Int? = null
    ): TvShowsListResponse

    // Media Details endpoints
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("language") language: String = "en-US"
    ): MovieDetailsResponse

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvId: Int,
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("language") language: String = "en-US"
    ): TvShowDetailsResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("language") language: String = "en-US"
    ): CreditsResponse

    @GET("tv/{tv_id}/credits")
    suspend fun getTvShowCredits(
        @Path("tv_id") tvId: Int,
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("language") language: String = "en-US"
    ): CreditsResponse

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieWatchProviders(
        @Path("movie_id") movieId: Int,
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}"
    ): WatchProvidersResponse

    @GET("tv/{tv_id}/watch/providers")
    suspend fun getTvShowWatchProviders(
        @Path("tv_id") tvId: Int,
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}"
    ): WatchProvidersResponse

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getTvSeasonDetails(
        @Path("tv_id") tvId: Int,
        @Path("season_number") seasonNumber: Int,
        @Header("Authorization") authorization: String = "Bearer ${Constants.TMDB_API_KEY}",
        @Query("language") language: String = "en-US"
    ): SeasonDetailsResponse

}