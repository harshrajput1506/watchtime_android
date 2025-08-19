package com.app.media.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.media.domain.repository.MediaRepository
import com.app.media.ui.state.SeasonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SeasonViewModel(
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _seasonsState = MutableStateFlow<SeasonState>(SeasonState.Loading)
    val seasonsState: StateFlow<SeasonState> get() = _seasonsState.asStateFlow()

    fun loadSeasonDetails(tvId: Int, seasonNumber: Int) {
        viewModelScope.launch {
            _seasonsState.value = SeasonState.Loading
            try {
                val seasonDetails = mediaRepository.getSeasonDetails(tvId, seasonNumber)
                _seasonsState.value = SeasonState.Success(seasonDetails)
            } catch (e: Exception) {
                _seasonsState.value = SeasonState.Error(
                    e.message ?: "Failed to load season details"
                )
            }
        }
    }
}
