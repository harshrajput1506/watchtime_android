package com.collections.ui.states

import com.app.collections.domain.models.Collection

data class CollectionsState(
    val collections: List<Collection> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCollectionId: String? = null
)

sealed class CollectionsUiState {
    object Loading : CollectionsUiState()
    data class Success(val collections: List<Collection>) : CollectionsUiState()
    data class Error(val message: String) : CollectionsUiState()
}
