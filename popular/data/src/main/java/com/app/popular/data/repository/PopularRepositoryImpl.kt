package com.app.popular.data.repository

import android.util.Log
import com.app.core.domain.entities.Media
import com.app.core.network.util.NetworkResult
import com.app.core.utils.failures.Failure
import com.app.popular.data.data_source.RemoteDataSource
import com.app.popular.domain.repository.PopularRepository

class PopularRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : PopularRepository {
    companion object {
        private const val TAG = "PopularRepositoryImpl"
    }

    override suspend fun getPopularMovies(page: Int): List<Media> {
        try {
            val response = remoteDataSource.getPopularMovies(page)
            return when (response) {
                is NetworkResult.Success -> {
                    response.data
                }

                is NetworkResult.Error -> throw response.exception
                NetworkResult.Loading -> return emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getPopularMovies: ", e)
            throw Failure.ServerError("Unable to fetch popular movies") // Propagate the error
        }
    }

    override suspend fun getTopRatedMovies(page: Int): List<Media> {
        try {
            val response = remoteDataSource.getTopRatedMovies(page)
            return when (response) {
                is NetworkResult.Success -> response.data
                is NetworkResult.Error -> throw response.exception
                NetworkResult.Loading -> return emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTopRatedMovies: ", e)
            throw Failure.ServerError("Unable to fetch top-rated movies") // Propagate the error
        }
    }

    override suspend fun getTrendingWeekly(page: Int): List<Media> {
        try {
            val response = remoteDataSource.getTrendingByWeekly(page)
            return when (response) {
                is NetworkResult.Success -> response.data
                is NetworkResult.Error -> throw response.exception
                NetworkResult.Loading -> return emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTrendingWeekly: ", e)
            throw Failure.ServerError("Unable to fetch trending weekly") // Propagate the error
        }
    }

    override suspend fun getPopularTvShows(page: Int): List<Media> {
        try {
            val response = remoteDataSource.getPopularTvShows(page)
            return when (response) {
                is NetworkResult.Success -> response.data
                is NetworkResult.Error -> throw response.exception
                NetworkResult.Loading -> return emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getPopularTvShows: ", e)
            throw Failure.ServerError("Unable to fetch popular TV shows") // Propagate the error
        }
    }

    override suspend fun getTopRatedTvShows(page: Int): List<Media> {
        try {
            val response = remoteDataSource.getTopRatedTvShows(page)
            return when (response) {
                is NetworkResult.Success -> response.data
                is NetworkResult.Error -> throw response.exception
                NetworkResult.Loading -> return emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTopRatedTvShows: ", e)
            throw Failure.ServerError("Unable to fetch top-rated TV shows") // Propagate the error
        }
    }

    override suspend fun getTrendingDaily(page: Int): List<Media> {
        try {
            val response = remoteDataSource.getTrendingByDaily(page)
            return when (response) {
                is NetworkResult.Success -> response.data
                is NetworkResult.Error -> throw response.exception
                NetworkResult.Loading -> return emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTrendingDaily: ", e)
            throw Failure.ServerError("Unable to fetch trending daily") // Propagate the error
        }
    }


}
