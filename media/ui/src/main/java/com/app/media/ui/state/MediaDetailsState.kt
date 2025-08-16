package com.app.media.ui.state

import com.app.media.domain.model.Cast
import com.app.media.domain.model.MediaDetails
import com.app.media.domain.model.SeasonDetails
import com.app.media.domain.model.WatchProviders

sealed class MediaDetailsState {
    object Loading : MediaDetailsState()
    data class Success(
        val mediaDetails: MediaDetails,
        val cast: Cast? = null,
        val watchProviders: WatchProviders? = null,
        val seasonDetails: List<SeasonDetails> = emptyList()
    ) : MediaDetailsState()

    data class Error(val message: String) : MediaDetailsState()
}
