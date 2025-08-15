package com.app.popular.domain.repository

import com.app.popular.domain.entities.Media

interface PopularRepository {
    suspend fun getPopularMovies(page: Int = 1): List<Media>
    suspend fun getTopRatedMovies(page: Int = 1): List<Media>
    suspend fun getTrendingWeekly(page: Int = 1): List<Media>
    suspend fun getPopularTvShows(page: Int = 1): List<Media>
    suspend fun getTopRatedTvShows(page: Int = 1): List<Media>
    suspend fun getTrendingDaily(page: Int = 1): List<Media>
}