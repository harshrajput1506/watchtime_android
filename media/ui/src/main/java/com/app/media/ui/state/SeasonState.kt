package com.app.media.ui.state

import com.app.media.domain.model.SeasonDetails

sealed class SeasonState {
    object Loading : SeasonState()
    data class Success(
        val seasonDetails: SeasonDetails
    ) : SeasonState()

    data class Error(val message: String) : SeasonState()
}
