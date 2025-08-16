package com.app.media.data.data_source

import android.util.Log
import com.app.core.network.api.TMDBApiService
import com.app.core.network.util.NetworkResult
import com.app.media.data.mapper.toCast
import com.app.media.data.mapper.toMediaDetails
import com.app.media.data.mapper.toSeasonDetails
import com.app.media.data.mapper.toWatchProviders
import com.app.media.domain.model.Cast
import com.app.media.domain.model.MediaDetails
import com.app.media.domain.model.SeasonDetails
import com.app.media.domain.model.WatchProviders

class MediaRemoteDataSource(
    private val tmdbApiService: TMDBApiService
) {

    suspend fun getMediaDetails(
        mediaId: Int,
        mediaType: String
    ): NetworkResult<MediaDetails> {
        return try {
            val response = when (mediaType.lowercase()) {
                "movie" -> {
                    val movieDetails = tmdbApiService.getMovieDetails(mediaId)
                    movieDetails.toMediaDetails()
                }

                "tv" -> {
                    val tvDetails = tmdbApiService.getTvShowDetails(mediaId)
                    tvDetails.toMediaDetails()
                }

                else -> throw IllegalArgumentException("Invalid media type: $mediaType")
            }
            Log.d("MediaRemoteDataSource", "getMediaDetails: $response")
            NetworkResult.Success(response)
        } catch (e: Exception) {
            Log.e("MediaRemoteDataSource", "Error getting media details", e)
            NetworkResult.Error(e)
        }
    }

    suspend fun getMediaCast(
        mediaId: Int,
        mediaType: String
    ): NetworkResult<Cast> {
        return try {
            val response = when (mediaType.lowercase()) {
                "movie" -> tmdbApiService.getMovieCredits(mediaId)
                "tv" -> tmdbApiService.getTvShowCredits(mediaId)
                else -> throw IllegalArgumentException("Invalid media type: $mediaType")
            }
            Log.d("MediaRemoteDataSource", "getMediaCast: $response")
            NetworkResult.Success(response.toCast())
        } catch (e: Exception) {
            Log.e("MediaRemoteDataSource", "Error getting media cast", e)
            NetworkResult.Error(e)
        }
    }

    suspend fun getWatchProviders(
        mediaId: Int,
        mediaType: String
    ): NetworkResult<WatchProviders> {
        return try {
            val response = when (mediaType.lowercase()) {
                "movie" -> tmdbApiService.getMovieWatchProviders(mediaId)
                "tv" -> tmdbApiService.getTvShowWatchProviders(mediaId)
                else -> throw IllegalArgumentException("Invalid media type: $mediaType")
            }
            Log.d("MediaRemoteDataSource", "getWatchProviders: $response")
            NetworkResult.Success(response.toWatchProviders())
        } catch (e: Exception) {
            Log.e("MediaRemoteDataSource", "Error getting watch providers", e)
            NetworkResult.Error(e)
        }
    }

    suspend fun getSeasonDetails(
        tvId: Int,
        seasonNumber: Int
    ): NetworkResult<SeasonDetails> {
        return try {
            val response = tmdbApiService.getTvSeasonDetails(tvId, seasonNumber)
            Log.d("MediaRemoteDataSource", "getSeasonDetails: $response")
            NetworkResult.Success(response.toSeasonDetails())
        } catch (e: Exception) {
            Log.e("MediaRemoteDataSource", "Error getting season details", e)
            NetworkResult.Error(e)
        }
    }
}