package com.app.discover.domain.repository

import com.app.core.domain.entities.Media
import com.app.discover.domain.entities.DiscoverFilters

interface DiscoverRepository {
    suspend fun discoverMovies(
        page: Int = 1,
        filters: DiscoverFilters = DiscoverFilters()
    ): List<Media>

    suspend fun discoverTvShows(
        page: Int = 1,
        filters: DiscoverFilters = DiscoverFilters()
    ): List<Media>

    suspend fun searchMulti(
        query: String,
        page: Int = 1
    ): List<Media>

    suspend fun searchMovies(
        query: String,
        page: Int = 1,
        year: Int? = null
    ): List<Media>

    suspend fun searchTvShows(
        query: String,
        page: Int = 1,
        year: Int? = null
    ): List<Media>
}
