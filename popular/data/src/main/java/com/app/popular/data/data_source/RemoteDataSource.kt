package com.app.popular.data.data_source

import android.util.Log
import com.app.core.domain.entities.Media
import com.app.core.network.api.TMDBApiService
import com.app.core.network.util.NetworkResult
import com.app.popular.data.mapper.toDomain

class RemoteDataSource(
    private val tmdbApiService: TMDBApiService
) {

    suspend fun getPopularMovies(page: Int = 1): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.getPopularMovies(
                page = page
            )
            // log the response for debugging
            Log.d("RemoteDataSource", "getPopularMovies: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun getTopRatedMovies(page: Int = 1): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.getTopRatedMovies(
                page = page
            )
            // log the response for debugging
            Log.d("RemoteDataSource", "getTopRatedMovies: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun getTrendingByWeekly(page: Int = 1): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.getTrendingByWeekly(
                page = page
            )
            // log the response for debugging
            Log.d("RemoteDataSource", "getTrendingByWeekly: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun getPopularTvShows(page: Int = 1): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.getPopularTvShows(
                page = page
            )
            // log the response for debugging
            Log.d("RemoteDataSource", "getPopularTvShows: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun getTopRatedTvShows(page: Int = 1): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.getTopRatedTvShows(
                page = page
            )
            // log the response for debugging
            Log.d("RemoteDataSource", "getTopRatedTvShows: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun getTrendingByDaily(page: Int = 1): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.getTrendingByDaily(
                page = page
            )
            // log the response for debugging
            Log.d("RemoteDataSource", "getTrendingByDaily: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}