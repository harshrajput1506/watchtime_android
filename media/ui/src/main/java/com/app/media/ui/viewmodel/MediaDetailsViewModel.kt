package com.app.media.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.media.domain.repository.MediaRepository
import com.app.media.ui.state.MediaDetailsState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaDetailsViewModel(
    private val mediaId: Int,
    private val mediaType: String,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MediaDetailsState>(MediaDetailsState.Loading)
    val state: StateFlow<MediaDetailsState> = _state.asStateFlow()

    init {
        loadMediaDetails(mediaId, mediaType)
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
