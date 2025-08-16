package com.app.discover.data.data_source

import android.util.Log
import com.app.core.domain.entities.Media
import com.app.core.network.api.TMDBApiService
import com.app.core.network.util.NetworkResult
import com.app.discover.data.mapper.toDomain
import com.app.discover.domain.entities.DiscoverFilters

class DiscoverRemoteDataSource(
    private val tmdbApiService: TMDBApiService
) {

    suspend fun discoverMovies(
        page: Int = 1,
        filters: DiscoverFilters = DiscoverFilters()
    ): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.discoverMovies(
                page = page,
                sortBy = filters.sortBy,
                includeAdult = filters.includeAdult,
                withGenres = if (filters.genres.isNotEmpty()) filters.genres.joinToString(",") else null,
                year = filters.year,
                voteAverageGte = filters.voteAverageGte,
                voteAverageLte = filters.voteAverageLte
            )
            Log.d("DiscoverRemoteDataSource", "discoverMovies: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e("DiscoverRemoteDataSource", "Error discovering movies", e)
            NetworkResult.Error(e)
        }
    }

    suspend fun discoverTvShows(
        page: Int = 1,
        filters: DiscoverFilters = DiscoverFilters()
    ): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.discoverTvShows(
                page = page,
                sortBy = filters.sortBy,
                includeAdult = filters.includeAdult,
                withGenres = if (filters.genres.isNotEmpty()) filters.genres.joinToString(",") else null,
                year = filters.year,
                voteAverageGte = filters.voteAverageGte,
                voteAverageLte = filters.voteAverageLte
            )
            Log.d("DiscoverRemoteDataSource", "discoverTvShows: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e("DiscoverRemoteDataSource", "Error discovering TV shows", e)
            NetworkResult.Error(e)
        }
    }

    suspend fun searchMulti(
        query: String,
        page: Int = 1
    ): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.searchMulti(
                query = query,
                page = page
            )
            Log.d("DiscoverRemoteDataSource", "searchMulti: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e("DiscoverRemoteDataSource", "Error searching multi", e)
            NetworkResult.Error(e)
        }
    }

    suspend fun searchMovies(
        query: String,
        page: Int = 1,
        year: Int? = null
    ): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.searchMovies(
                query = query,
                page = page,
                year = year
            )
            Log.d("DiscoverRemoteDataSource", "searchMovies: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e("DiscoverRemoteDataSource", "Error searching movies", e)
            NetworkResult.Error(e)
        }
    }

    suspend fun searchTvShows(
        query: String,
        page: Int = 1,
        year: Int? = null
    ): NetworkResult<List<Media>> {
        return try {
            val response = tmdbApiService.searchTvShows(
                query = query,
                page = page,
                year = year
            )
            Log.d("DiscoverRemoteDataSource", "searchTvShows: $response")
            NetworkResult.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e("DiscoverRemoteDataSource", "Error searching TV shows", e)
            NetworkResult.Error(e)
        }
    }
}
