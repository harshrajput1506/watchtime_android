package com.app.media.domain.repository

import com.app.media.domain.model.Cast
import com.app.media.domain.model.MediaDetails
import com.app.media.domain.model.SeasonDetails
import com.app.media.domain.model.WatchProviders

interface MediaRepository {
    suspend fun getMediaDetails(mediaId: Int, mediaType: String): MediaDetails
    suspend fun getMediaCast(mediaId: Int, mediaType: String): Cast
    suspend fun getWatchProviders(mediaId: Int, mediaType: String): WatchProviders
    suspend fun getSeasonDetails(tvId: Int, seasonNumber: Int): SeasonDetails
}
