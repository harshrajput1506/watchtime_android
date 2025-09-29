package com.collections.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.auth.domain.entities.UserEntity
import com.app.auth.domain.repository.AuthRepository
import com.app.collections.domain.models.Collection
import com.app.collections.domain.models.ContentMetadata
import com.app.collections.domain.repository.CollectionRepository
import com.collections.ui.states.CollectionsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CollectionsViewModel(
    private val collectionRepository: CollectionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        private const val TAG = "CollectionsViewModel"
    }

    private val _collectionsState = MutableStateFlow(CollectionsState())
    val collectionsState: StateFlow<CollectionsState> = _collectionsState.asStateFlow()

    // Collections state
    private val _isInWatchlist = MutableStateFlow(false)
    val isInWatchlist: StateFlow<Boolean> = _isInWatchlist.asStateFlow()

    private val _isAlreadyWatched = MutableStateFlow(false)
    val isAlreadyWatched: StateFlow<Boolean> = _isAlreadyWatched.asStateFlow()

    // Bottom sheet state for collections
    private val _showCollectionBottomSheet = MutableStateFlow(false)
    val showCollectionBottomSheet: StateFlow<Boolean> = _showCollectionBottomSheet.asStateFlow()

    private val _availableCollections =
        MutableStateFlow<List<com.app.collections.domain.models.Collection>>(emptyList())
    val availableCollections: StateFlow<List<Collection>> = _availableCollections.asStateFlow()

    private val _selectedCollections = MutableStateFlow<Set<String>>(emptySet())
    val selectedCollections: StateFlow<Set<String>> = _selectedCollections.asStateFlow()

    private val _isCreatingCollection = MutableStateFlow(false)
    val isCreatingCollection: StateFlow<Boolean> = _isCreatingCollection.asStateFlow()


    var user: UserEntity? = null
        private set

    init {
        loadUser()
        loadCollections()
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                user = authRepository.getCurrentUser()
            } catch (e: Exception) {
                // Handle exception if needed
            }
        }
    }

    private fun loadCollections() {
        viewModelScope.launch {
            try {
                _collectionsState.value = _collectionsState.value.copy(isLoading = true)

                collectionRepository.getCollections()
                    .catch { exception ->
                        _collectionsState.value = _collectionsState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                    .collect { collections ->
                        _collectionsState.value = _collectionsState.value.copy(
                            collections = collections,
                            isLoading = false,
                            error = null
                        )
                        _availableCollections.value = collections.filter { !it.isDefault }
                    }

            } catch (e: Exception) {
                // Handle exception if needed
                _collectionsState.value = _collectionsState.value.copy(
                    isLoading = false,
                    error = "Something went wrong"
                )
            }

        }
    }

    fun loadCollectionStates(mediaId: Int, mediaType: String) {
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

    private fun loadSelectedCollectionsForMedia(mediaId: Int, mediaType: String) {
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
    fun toggleWatchlist(mediaId: Int, mediaType: String, contentMetadata: ContentMetadata) {
        viewModelScope.launch {
            try {
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

    fun toggleAlreadyWatched(mediaId: Int, mediaType: String, contentMetadata: ContentMetadata) {
        viewModelScope.launch {

            try {
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

    fun showAddToCollectionDialog(mediaId: Int, mediaType: String) {
        _showCollectionBottomSheet.value = true
        loadSelectedCollectionsForMedia(mediaId = mediaId, mediaType = mediaType)
    }

    fun hideCollectionBottomSheet() {
        _showCollectionBottomSheet.value = false
        _isCreatingCollection.value = false
    }

    fun toggleCollectionSelection(
        collectionId: String,
        mediaId: Int,
        mediaType: String,
        contentMetadata: ContentMetadata
    ) {
        viewModelScope.launch {
            Log.d(
                TAG,
                "toggleCollectionSelection: toggling collectionId=$collectionId for mediaId=$mediaId"
            )

            try {
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

    fun removeFromWatchlist(mediaId: Int, mediaType: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser()
                collectionRepository.removeFromWatchlist(
                    user?.id ?: "default_user",
                    mediaId,
                    mediaType
                )
            } catch (e: Exception) {
                Log.e(TAG, "removeFromWatchlist: exception", e)
                // Handle error
            }
        }
    }

    fun removeFromAlreadyWatched(mediaId: Int, mediaType: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser()
                collectionRepository.removeFromAlreadyWatched(
                    user?.id ?: "default_user",
                    mediaId,
                    mediaType
                )
            } catch (e: Exception) {
                Log.e(TAG, "removeFromAlreadyWatched: exception", e)
                // Handle error
            }
        }
    }

    fun removeFromCollection(collectionId: String, mediaId: Int, mediaType: String) {
        viewModelScope.launch {
            try {
                val collection =
                    _availableCollections.value.find { it.id.toString() == collectionId.toString() }

                if (collection != null) {
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
                    Log.w(
                        TAG,
                        "removeFromCollection: collection not found for id=$collectionId"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "removeFromCollection: exception", e)
                // Handle error
            }
        }
    }
}
