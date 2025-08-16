package com.app.discover.ui.states

import com.app.discover.domain.entities.Media

sealed class DiscoverMoviesState {
    object Loading : DiscoverMoviesState()
    data class Success(
        val mediaList: List<Media>,
        val currentPage: Int = 1,
        val isLoadingMore: Boolean = false,
        val hasMorePages: Boolean = true
    ) : DiscoverMoviesState()

    data class Error(val message: String) : DiscoverMoviesState()
}

sealed class DiscoverTvShowsState {
    object Loading : DiscoverTvShowsState()
    data class Success(
        val mediaList: List<Media>,
        val currentPage: Int = 1,
        val isLoadingMore: Boolean = false,
        val hasMorePages: Boolean = true
    ) : DiscoverTvShowsState()

    data class Error(val message: String) : DiscoverTvShowsState()
}

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    data class Success(
        val mediaList: List<Media>,
        val currentPage: Int = 1,
        val isLoadingMore: Boolean = false,
        val hasMorePages: Boolean = true
    ) : SearchState()

    data class Error(val message: String) : SearchState()
}

data class DiscoverUiState(
    val discoverMoviesState: DiscoverMoviesState = DiscoverMoviesState.Loading,
    val discoverTvShowsState: DiscoverTvShowsState = DiscoverTvShowsState.Loading,
    val searchState: SearchState = SearchState.Idle,
    val searchQuery: String = "",
    val isSearchMode: Boolean = false,
    val selectedMediaType: Int = 0 // 0 for movies, 1 for TV shows
)
