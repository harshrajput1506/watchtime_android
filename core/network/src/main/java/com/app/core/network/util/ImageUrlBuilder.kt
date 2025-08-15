package com.app.core.network.util

object ImageUrlBuilder {

    enum class ImageSize(val path: String) {
        W92("w92"),
        W154("w154"),
        W185("w185"),
        W342("w342"),
        W500("w500"),
        W780("w780"),
        ORIGINAL("original")
    }

    /**
     * Builds a complete image URL from TMDB image path
     * @param imagePath The image path from TMDB API response
     * @param size The desired image size
     * @return Complete image URL or null if imagePath is null or empty
     */
    fun buildImageUrl(imagePath: String?, size: ImageSize = ImageSize.W500): String? {
        return if (!imagePath.isNullOrEmpty()) {
            "${Constants.TMDB_IMAGE_BASE_URL}${size.path}$imagePath"
        } else {
            null
        }
    }

    /**
     * Builds a backdrop image URL (typically larger)
     */
    fun buildBackdropUrl(backdropPath: String?): String? {
        return buildImageUrl(backdropPath, ImageSize.W780)
    }

    /**
     * Builds a poster image URL
     */
    fun buildPosterUrl(posterPath: String?): String? {
        return buildImageUrl(posterPath, ImageSize.W342)
    }
}
