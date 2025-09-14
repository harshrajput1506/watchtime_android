package com.app.media.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.auth.domain.repository.AuthRepository
import com.app.collections.domain.models.Collection
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
    private val collectionRepository: CollectionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        private const val TAG = "MediaDetailsViewModel"
    }

    private val _state = MutableStateFlow<MediaDetailsState>(MediaDetailsState.Loading)
    val state: StateFlow<MediaDetailsState> = _state.asStateFlow()

    // Collections state
    private val _isInWatchlist = MutableStateFlow(false)
    val isInWatchlist: StateFlow<Boolean> = _isInWatchlist.asStateFlow()

    private val _isAlreadyWatched = MutableStateFlow(false)
    val isAlreadyWatched: StateFlow<Boolean> = _isAlreadyWatched.asStateFlow()

    // Bottom sheet state for collections
    private val _showCollectionBottomSheet = MutableStateFlow(false)
    val showCollectionBottomSheet: StateFlow<Boolean> = _showCollectionBottomSheet.asStateFlow()

    private val _availableCollections = MutableStateFlow<List<Collection>>(emptyList())
    val availableCollections: StateFlow<List<Collection>> = _availableCollections.asStateFlow()

    private val _selectedCollections = MutableStateFlow<Set<String>>(emptySet())
    val selectedCollections: StateFlow<Set<String>> = _selectedCollections.asStateFlow()

    private val _isCreatingCollection = MutableStateFlow(false)
    val isCreatingCollection: StateFlow<Boolean> = _isCreatingCollection.asStateFlow()

    init {
        loadMediaDetails(mediaId, mediaType)
        loadCollectionStates()
        loadAvailableCollections()
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

    private fun loadAvailableCollections() {
        viewModelScope.launch {
            try {
                collectionRepository.getCollections().collect { collections ->
                    _availableCollections.value = collections.filter { !it.isDefault }
                }
            } catch (_: Exception) {
                // Handle error silently
            }
        }
    }

    private fun loadSelectedCollectionsForMedia() {
        viewModelScope.launch {
            try {
                Log.d(
                    TAG,
                    "loadSelectedCollectionsForMedia: start - mediaId=$mediaId mediaType=$mediaType available=${_availableCollections.value.size}"
                )
                val collections = _availableCollections.value
                Log.d(TAG, "Available collections: $collections")

                // Prepare a list of flows for each collection
                val flows = collections.map { collection ->
                    collectionRepository.isInCollection(collection.id, mediaId, mediaType)
                }

                // Combine all flows and update selected set
                kotlinx.coroutines.flow.combine(flows) { results ->
                    collections.mapIndexedNotNull { idx, collection ->
                        if (results[idx]) collection.id.toString() else null
                    }.toSet()
                }.collect { selectedIds ->
                    _selectedCollections.value = selectedIds
                    Log.d(TAG, "Updated selected collections: $selectedIds")
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadSelectedCollectionsForMedia: error", e)
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
                    val user = authRepository.getCurrentUser()

                    if (_isInWatchlist.value) {
                        collectionRepository.removeFromWatchlist("default_user", mediaId, mediaType)
                    } else {
                        collectionRepository.addToWatchlist(
                            user?.id ?: "default_user",
                            mediaId,
                            mediaType,
                            contentMetadata
                        )
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
                    val user = authRepository.getCurrentUser()
                    if (_isAlreadyWatched.value) {
                        collectionRepository.removeFromAlreadyWatched(
                            user?.id ?: "default_user",
                            mediaId,
                            mediaType
                        )
                    } else {
                        collectionRepository.addToAlreadyWatched(
                            user?.id ?: "default_user",
                            mediaId,
                            mediaType,
                            contentMetadata
                        )
                    }
                } catch (_: Exception) {
                    // Handle error - could show toast
                }
            }
        }
    }

    fun showAddToCollectionDialog() {
        _showCollectionBottomSheet.value = true
        loadSelectedCollectionsForMedia()
    }

    fun hideCollectionBottomSheet() {
        _showCollectionBottomSheet.value = false
        _isCreatingCollection.value = false
    }

    fun toggleCollectionSelection(collectionId: String) {
        viewModelScope.launch {
            Log.d(
                TAG,
                "toggleCollectionSelection: toggling collectionId=$collectionId for mediaId=$mediaId"
            )
            val currentState = _state.value
            if (currentState is MediaDetailsState.Success) {
                try {
                    val contentMetadata = currentState.mediaDetails.toContentMetadata(mediaType)
                    val collection =
                        _availableCollections.value.find { it.id.toString() == collectionId }

                    if (collection != null) {
                        val isCurrentlySelected = _selectedCollections.value.contains(collectionId)
                        Log.d(
                            TAG,
                            "toggleCollectionSelection: isCurrentlySelected=$isCurrentlySelected"
                        )

                        if (isCurrentlySelected) {
                            val result = collectionRepository.removeFromCollection(
                                collection.id,
                                mediaId,
                                mediaType
                            )
                            result.fold(
                                onSuccess = {
                                    Log.d(
                                        TAG,
                                        "Removed mediaId=$mediaId from collection=${collection.id} name=${collection.name}"
                                    )
                                },
                                onFailure = { err ->
                                    Log.e(
                                        TAG,
                                        "Failed to remove from collection ${collection.id}",
                                        err
                                    )
                                }
                            )
                        } else {
                            val result = collectionRepository.addToCollection(
                                collection.id,
                                mediaId,
                                mediaType,
                                contentMetadata
                            )
                            result.fold(
                                onSuccess = { item ->
                                    Log.d(
                                        TAG,
                                        "Added mediaId=$mediaId to collection=${collection.id} name=${collection.name} itemId=${item.id}"
                                    )
                                },
                                onFailure = { err ->
                                    Log.e(TAG, "Failed to add to collection ${collection.id}", err)
                                }
                            )
                        }
                    } else {
                        Log.w(
                            TAG,
                            "toggleCollectionSelection: collection not found for id=$collectionId"
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "toggleCollectionSelection: exception", e)
                    // Handle error
                }
            }
        }
    }

    fun createNewCollection(name: String, description: String? = null, isPublic: Boolean = false) {
        viewModelScope.launch {
            _isCreatingCollection.value = true
            try {
                val user = authRepository.getCurrentUser()
                val result = collectionRepository.createCollection(
                    userId = user?.id ?: "default_user", // Replace with actual user ID
                    name = name,
                    description = description,
                    isPublic = isPublic
                )

                result.fold(
                    onSuccess = { collection ->
                        _isCreatingCollection.value = false
                    },
                    onFailure = {
                        _isCreatingCollection.value = false
                        // Handle error
                    }
                )
            } catch (_: Exception) {
                _isCreatingCollection.value = false
                // Handle error
            }
        }
    }

}
