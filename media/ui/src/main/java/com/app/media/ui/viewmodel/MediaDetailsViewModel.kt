package com.app.media.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.collections.domain.repository.CollectionRepository
import com.app.media.domain.repository.MediaRepository
import com.app.media.ui.state.MediaDetailsState
import com.app.media.ui.utils.toContentMetadata
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaDetailsViewModel(
    private val mediaId: Int,
    private val mediaType: String,
    private val mediaRepository: MediaRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MediaDetailsState>(MediaDetailsState.Loading)
    val state: StateFlow<MediaDetailsState> = _state.asStateFlow()

    // Collections state
    private val _isInWatchlist = MutableStateFlow(false)
    val isInWatchlist: StateFlow<Boolean> = _isInWatchlist.asStateFlow()

    private val _isAlreadyWatched = MutableStateFlow(false)
    val isAlreadyWatched: StateFlow<Boolean> = _isAlreadyWatched.asStateFlow()

    init {
        loadMediaDetails(mediaId, mediaType)
        loadCollectionStates()
    }

    private fun loadMediaDetails(mediaId: Int, mediaType: String) {
        viewModelScope.launch {
            _state.value = MediaDetailsState.Loading

            try {
                // Load media details first
                val mediaDetails = mediaRepository.getMediaDetails(mediaId, mediaType)

                // Load additional data concurrently
                val castDeferred = async {
                    try {
                        mediaRepository.getMediaCast(mediaId, mediaType)
                    } catch (_: Exception) {
                        null
                    }
                }
                val watchProvidersDeferred = async {
                    try {
                        mediaRepository.getWatchProviders(mediaId, mediaType)
                    } catch (_: Exception) {
                        null
                    }
                }

                // Load season details for TV shows
                val seasonDetailsDeferred =
                    if (mediaType.lowercase() == "tv" && mediaDetails.seasons.isNotEmpty()) {
                        mediaDetails.seasons.take(3).map { season -> // Load first 3 seasons
                            async {
                                try {
                                    mediaRepository.getSeasonDetails(mediaId, season.seasonNumber)
                                } catch (_: Exception) {
                                    null
                                }
                            }
                        }
                    } else emptyList()

                val cast = castDeferred.await()
                val watchProviders = watchProvidersDeferred.await()
                val seasonDetails = seasonDetailsDeferred.mapNotNull { it.await() }

                _state.value = MediaDetailsState.Success(
                    mediaDetails = mediaDetails,
                    cast = cast,
                    watchProviders = watchProviders,
                    seasonDetails = seasonDetails
                )
            } catch (e: Exception) {
                _state.value = MediaDetailsState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun loadCollectionStates() {
        viewModelScope.launch {
            try {
                // Check if the media is in the watchlist
                collectionRepository.isInWatchlist(mediaId, mediaType)
                    .collect { isInWatchlist ->
                        _isInWatchlist.value = isInWatchlist
                    }
            } catch (_: Exception) {
                // Handle error silently
            }
        }

        viewModelScope.launch {
            try {
                // Check if the media is already watched
                collectionRepository.isAlreadyWatched(mediaId, mediaType)
                    .collect { isAlreadyWatched ->
                        _isAlreadyWatched.value = isAlreadyWatched
                    }
            } catch (_: Exception) {
                // Handle error silently
            }
        }
    }

    // Collection action methods
    fun toggleWatchlist() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is MediaDetailsState.Success) {
                try {
                    val contentMetadata = currentState.mediaDetails.toContentMetadata(mediaType)

                    if (_isInWatchlist.value) {
                        collectionRepository.removeFromWatchlist("default_user", mediaId, mediaType)
                    } else {
                        collectionRepository.addToWatchlist("default_user", mediaId, mediaType, contentMetadata)
                    }
                } catch (_: Exception) {
                    // Handle error - could show toast
                }
            }
        }
    }

    fun toggleAlreadyWatched() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is MediaDetailsState.Success) {
                try {
                    val contentMetadata = currentState.mediaDetails.toContentMetadata(mediaType)

                    if (_isAlreadyWatched.value) {
                        collectionRepository.removeFromAlreadyWatched("default_user", mediaId, mediaType)
                    } else {
                        collectionRepository.addToAlreadyWatched("default_user", mediaId, mediaType, contentMetadata)
                    }
                } catch (_: Exception) {
                    // Handle error - could show toast
                }
            }
        }
    }

    fun showAddToCollectionDialog() {
        // This method will be called when the "Add to Collection" button is clicked
        // You can implement a dialog to show custom collections here
        // For now, this is a placeholder
    }

    fun loadSeasonDetails(tvId: Int, seasonNumber: Int) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is MediaDetailsState.Success) {
                try {
                    val seasonDetails = mediaRepository.getSeasonDetails(tvId, seasonNumber)
                    val updatedSeasonDetails = currentState.seasonDetails.toMutableList()
                    val existingIndex = updatedSeasonDetails.indexOfFirst {
                        it.seasonNumber == seasonNumber
                    }

                    if (existingIndex >= 0) {
                        updatedSeasonDetails[existingIndex] = seasonDetails
                    } else {
                        updatedSeasonDetails.add(seasonDetails)
                    }

                    _state.value = currentState.copy(seasonDetails = updatedSeasonDetails)
                } catch (_: Exception) {
                    // Handle error silently or show toast
                }
            }
        }
    }
}
