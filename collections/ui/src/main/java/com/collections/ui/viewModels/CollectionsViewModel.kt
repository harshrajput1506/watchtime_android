package com.collections.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.auth.domain.entities.UserEntity
import com.app.auth.domain.repository.AuthRepository
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

    private val _collectionsState = MutableStateFlow(CollectionsState())
    val collectionsState: StateFlow<CollectionsState> = _collectionsState.asStateFlow()

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

    fun selectCollection(collectionId: String) {
        _collectionsState.value = _collectionsState.value.copy(
            selectedCollectionId = collectionId
        )
    }

    fun retryLoadCollections() {
        loadCollections()
    }
}
