package com.app.discover.data.repository

import com.app.core.domain.entities.Media
import com.app.core.network.util.NetworkResult
import com.app.discover.data.data_source.DiscoverRemoteDataSource
import com.app.discover.domain.entities.DiscoverFilters
import com.app.discover.domain.repository.DiscoverRepository

class DiscoverRepositoryImpl(
    private val remoteDataSource: DiscoverRemoteDataSource
) : DiscoverRepository {

    override suspend fun discoverMovies(
        page: Int,
        filters: DiscoverFilters
    ): List<Media> {
        return when (val result = remoteDataSource.discoverMovies(page, filters)) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> emptyList()
            is NetworkResult.Loading -> emptyList()
        }
    }

    override suspend fun discoverTvShows(
        page: Int,
        filters: DiscoverFilters
    ): List<Media> {
        return when (val result = remoteDataSource.discoverTvShows(page, filters)) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> emptyList()
            is NetworkResult.Loading -> emptyList()
        }
    }

    override suspend fun searchMulti(
        query: String,
        page: Int
    ): List<Media> {
        return when (val result = remoteDataSource.searchMulti(query, page)) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> emptyList()
            is NetworkResult.Loading -> emptyList()
        }
    }

    override suspend fun searchMovies(
        query: String,
        page: Int,
        year: Int?
    ): List<Media> {
        return when (val result = remoteDataSource.searchMovies(query, page, year)) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> emptyList()
            is NetworkResult.Loading -> emptyList()
        }
    }

    override suspend fun searchTvShows(
        query: String,
        page: Int,
        year: Int?
    ): List<Media> {
        return when (val result = remoteDataSource.searchTvShows(query, page, year)) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> emptyList()
            is NetworkResult.Loading -> emptyList()
        }
    }
}
