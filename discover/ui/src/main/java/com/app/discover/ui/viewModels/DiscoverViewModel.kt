package com.app.discover.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.discover.domain.entities.DiscoverFilters
import com.app.discover.domain.repository.DiscoverRepository
import com.app.discover.ui.states.DiscoverMoviesState
import com.app.discover.ui.states.DiscoverTvShowsState
import com.app.discover.ui.states.DiscoverUiState
import com.app.discover.ui.states.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val discoverRepository: DiscoverRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        loadDiscoverContent()
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            isSearchMode = query.isNotBlank()
        )

        // Cancel previous search job
        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchState = SearchState.Idle)
            return
        }

        // Debounce search by 500ms
        searchJob = viewModelScope.launch {
            delay(500)
            performSearch(query)
        }
    }

    fun onKeyboardSearch(query: String) {
        if (query.isNotBlank()) {
            searchJob?.cancel()
            performSearch(query)
        }
    }

    fun switchMediaType(index: Int) {
        _uiState.value = _uiState.value.copy(selectedMediaType = index)
        if (!_uiState.value.isSearchMode) {
            loadDiscoverContent()
        } else {
            // If in search mode, perform search with the current query
            performSearch(_uiState.value.searchQuery)
        }
    }

    fun loadNextPage() {
        if (_uiState.value.isSearchMode) {
            loadMoreSearchResults()
        } else {
            when (_uiState.value.selectedMediaType) {
                0 -> loadMoreMovies()
                1 -> loadMoreTvShows()
            }
        }
    }

    private fun loadDiscoverContent() {
        loadDiscoverMovies()
        loadDiscoverTvShows()
    }

    private fun loadDiscoverMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                discoverMoviesState = DiscoverMoviesState.Loading
            )

            try {
                val movies = discoverRepository.discoverMovies(
                    page = 1,
                    filters = DiscoverFilters()
                )
                _uiState.value = _uiState.value.copy(
                    discoverMoviesState = DiscoverMoviesState.Success(
                        mediaList = movies,
                        currentPage = 1,
                        hasMorePages = movies.isNotEmpty()
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    discoverMoviesState = DiscoverMoviesState.Error(
                        e.message ?: "Failed to load movies"
                    )
                )
            }
        }
    }

    private fun loadDiscoverTvShows() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                discoverTvShowsState = DiscoverTvShowsState.Loading
            )

            try {
                val tvShows = discoverRepository.discoverTvShows(
                    page = 1,
                    filters = DiscoverFilters()
                )
                _uiState.value = _uiState.value.copy(
                    discoverTvShowsState = DiscoverTvShowsState.Success(
                        mediaList = tvShows,
                        currentPage = 1,
                        hasMorePages = tvShows.isNotEmpty()
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    discoverTvShowsState = DiscoverTvShowsState.Error(
                        e.message ?: "Failed to load TV shows"
                    )
                )
            }
        }
    }

    private fun loadMoreMovies() {
        val currentState = _uiState.value.discoverMoviesState
        if (currentState !is DiscoverMoviesState.Success ||
            currentState.isLoadingMore ||
            !currentState.hasMorePages
        ) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                discoverMoviesState = currentState.copy(isLoadingMore = true)
            )

            try {
                val nextPage = currentState.currentPage + 1
                val newMovies = discoverRepository.discoverMovies(
                    page = nextPage,
                    filters = DiscoverFilters()
                )

                _uiState.value = _uiState.value.copy(
                    discoverMoviesState = DiscoverMoviesState.Success(
                        mediaList = currentState.mediaList + newMovies,
                        currentPage = nextPage,
                        isLoadingMore = false,
                        hasMorePages = newMovies.isNotEmpty()
                    )
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    discoverMoviesState = currentState.copy(isLoadingMore = false)
                )
            }
        }
    }

    private fun loadMoreTvShows() {
        val currentState = _uiState.value.discoverTvShowsState
        if (currentState !is DiscoverTvShowsState.Success ||
            currentState.isLoadingMore ||
            !currentState.hasMorePages
        ) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                discoverTvShowsState = currentState.copy(isLoadingMore = true)
            )

            try {
                val nextPage = currentState.currentPage + 1
                val newTvShows = discoverRepository.discoverTvShows(
                    page = nextPage,
                    filters = DiscoverFilters()
                )

                _uiState.value = _uiState.value.copy(
                    discoverTvShowsState = DiscoverTvShowsState.Success(
                        mediaList = currentState.mediaList + newTvShows,
                        currentPage = nextPage,
                        isLoadingMore = false,
                        hasMorePages = newTvShows.isNotEmpty()
                    )
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    discoverTvShowsState = currentState.copy(isLoadingMore = false)
                )
            }
        }
    }

    private fun loadMoreSearchResults() {
        val currentState = _uiState.value.searchState
        if (currentState !is SearchState.Success ||
            currentState.isLoadingMore ||
            !currentState.hasMorePages
        ) return

        val query = _uiState.value.searchQuery
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                searchState = currentState.copy(isLoadingMore = true)
            )

            try {
                val nextPage = currentState.currentPage + 1
                val newResults = when (_uiState.value.selectedMediaType) {
                    0 -> discoverRepository.searchMovies(query, page = nextPage)
                    1 -> discoverRepository.searchTvShows(query, page = nextPage)
                    else -> discoverRepository.searchMulti(query, page = nextPage)
                }

                _uiState.value = _uiState.value.copy(
                    searchState = SearchState.Success(
                        mediaList = currentState.mediaList + newResults,
                        currentPage = nextPage,
                        isLoadingMore = false,
                        hasMorePages = newResults.isNotEmpty()
                    )
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    searchState = currentState.copy(isLoadingMore = false)
                )
            }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(searchState = SearchState.Loading)

            try {
                val results = when (_uiState.value.selectedMediaType) {
                    0 -> discoverRepository.searchMovies(query)
                    1 -> discoverRepository.searchTvShows(query)
                    else -> discoverRepository.searchMulti(query)
                }


                _uiState.value = _uiState.value.copy(
                    searchState = SearchState.Success(
                        mediaList = results,
                        currentPage = 1,
                        hasMorePages = results.isNotEmpty()
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    searchState = SearchState.Error(
                        e.message ?: "Search failed"
                    )
                )
            }
        }
    }

    fun onMediaClicked(mediaId: Int) {
        // Handle media click - navigate to details screen
        // This can be implemented based on your navigation setup
    }
}
