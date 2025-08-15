package com.app.core.network.api

import com.app.core.network.model.MediaListResponse
import com.app.core.network.model.MoviesListResponse
import com.app.core.network.model.TvShowsListResponse
import com.app.core.network.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header
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

}
