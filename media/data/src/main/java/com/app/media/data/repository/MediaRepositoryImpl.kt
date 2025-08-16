package com.app.media.data.repository

import com.app.core.network.util.NetworkResult
import com.app.media.data.data_source.MediaRemoteDataSource
import com.app.media.domain.model.Cast
import com.app.media.domain.model.MediaDetails
import com.app.media.domain.model.SeasonDetails
import com.app.media.domain.model.WatchProviders
import com.app.media.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val remoteDataSource: MediaRemoteDataSource
) : MediaRepository {

    override suspend fun getMediaDetails(
        mediaId: Int,
        mediaType: String
    ): MediaDetails {
        return when (val result = remoteDataSource.getMediaDetails(mediaId, mediaType)) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> throw Exception(
                result.exception.message ?: "Unknown error occurred"
            )

            is NetworkResult.Loading -> throw Exception("Loading state not handled")
        }
    }

    override suspend fun getMediaCast(
        mediaId: Int,
        mediaType: String
    ): Cast {
        return when (val result = remoteDataSource.getMediaCast(mediaId, mediaType)) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> throw Exception(
                result.exception.message ?: "Unknown error occurred"
            )

            is NetworkResult.Loading -> throw Exception("Loading state not handled")
        }
    }

    override suspend fun getWatchProviders(
        mediaId: Int,
        mediaType: String
    ): WatchProviders {
        return when (val result = remoteDataSource.getWatchProviders(mediaId, mediaType)) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> throw Exception(
                result.exception.message ?: "Unknown error occurred"
            )

            is NetworkResult.Loading -> throw Exception("Loading state not handled")
        }
    }

    override suspend fun getSeasonDetails(
        tvId: Int,
        seasonNumber: Int
    ): SeasonDetails {
        return when (val result = remoteDataSource.getSeasonDetails(tvId, seasonNumber)) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> throw Exception(
                result.exception.message ?: "Unknown error occurred"
            )

            is NetworkResult.Loading -> throw Exception("Loading state not handled")
        }
    }
}
