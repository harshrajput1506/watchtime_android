@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.app.media.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.app.core.network.util.ImageUrlBuilder
import com.app.core.ui.composables.shimmer
import com.app.core.utils.DateTimeUtils
import com.app.media.domain.model.Cast
import com.app.media.domain.model.MediaDetails
import com.app.media.domain.model.Season
import com.app.media.domain.model.SeasonDetails
import com.app.media.ui.components.CastSection
import com.app.media.ui.components.GenreChip
import com.app.media.ui.components.ShimmerPlaceHolder
import com.app.media.ui.components.UserScoreBar
import com.app.media.ui.state.MediaDetailsState
import com.app.media.ui.viewmodel.MediaDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun MediaDetailsScreen(
    mediaId: Int,
    mediaType: String,
    posterUrl: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: MediaDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // load data
    LaunchedEffect(mediaId, mediaType) {
        viewModel.loadMediaDetails(mediaId, mediaType)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(scrollState)
        ) {


            PosterSection(
                isLoading = state is MediaDetailsState.Loading,
                details = (state as? MediaDetailsState.Success)?.mediaDetails,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                mediaId = mediaId,
                posterUrl = posterUrl,
                onNavigateBack = onNavigateBack
            )

            // Content Section
            when (state) {
                is MediaDetailsState.Loading -> {
                    ShimmerPlaceHolder()
                }

                is MediaDetailsState.Success -> {
                    val successState = state as MediaDetailsState.Success
                    MediaDetailsSection(
                        mediaDetails = successState.mediaDetails,
                        cast = successState.cast,
                        mediaType = mediaType,
                        seasonDetails = successState.seasonDetails,
                        onSeasonClick = { seasonNumber ->
                            //viewModel.loadSeasonDetails(mediaId, seasonNumber, mediaType)
                        }
                    )
                }

                is MediaDetailsState.Error -> {
                    ErrorScreen(
                        message = (state as MediaDetailsState.Error).message,
                        onNavigateBack = onNavigateBack
                    )
                }
            }

        }

    }


}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PosterSection(
    details: MediaDetails? = null,
    mediaId: Int,
    isLoading: Boolean,
    posterUrl: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateBack: () -> Unit
) {

    val screenHeight =
        LocalConfiguration.current.screenHeightDp.dp
    with(sharedTransitionScope) {

        Card(
            shape = MaterialTheme.shapes.extraLarge.copy(
                topStart = CornerSize(0.dp),
                topEnd = CornerSize(0.dp)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.5f),
                contentAlignment = Alignment.Center
            ) {
                // Backdrop Image
                if (!isLoading && details != null) {
                    AsyncImage(
                        model = ImageUrlBuilder.buildImageUrl(
                            details.backdropPath,
                            ImageUrlBuilder.ImageSize.W780
                        ),
                        contentDescription = details.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.2f
                    )
                }

                // Poster and Back Button
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.Center),

                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(
                                    posterUrl
                                )
                                .crossfade(true)
                                .placeholderMemoryCacheKey("poster_${mediaId}")
                                .memoryCacheKey("poster_${mediaId}")
                                .diskCacheKey("poster_$mediaId")
                                .build(),
                            modifier = Modifier
                                .fillMaxHeight(0.8f)
                                .aspectRatio(0.65f)
                                .clip(
                                    MaterialTheme.shapes.medium
                                )
                                .sharedElement(
                                    rememberSharedContentState("poster_$mediaId"),
                                    animatedVisibilityScope = animatedVisibilityScope

                                ),
                            contentDescription = details?.title,
                            contentScale = ContentScale.Crop
                        ) {
                            val state = painter.state.collectAsState().value
                            Log.d("MediaDetailsScreen", "PosterSection: $state")
                            when (state) {
                                AsyncImagePainter.State.Empty -> ShimmerPoster()
                                is AsyncImagePainter.State.Error -> ShimmerPoster(isError = true)
                                is AsyncImagePainter.State.Loading -> ShimmerPoster()
                                is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                            }


                        }

                    }

                    FilledIconButton(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(horizontal = 16.dp, vertical = 48.dp),
                        onClick = onNavigateBack,
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

            }

        }
    }
}


@Composable
fun ShimmerPoster(
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxHeight(0.8f)
            .aspectRatio(0.65f),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            4.dp
        )
    ) {
        if (!isError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer()
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_broken_image),
                    contentDescription = "Broken Image",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun MediaDetailsSection(
    modifier: Modifier = Modifier,
    mediaDetails: MediaDetails,
    cast: Cast?,
    seasonDetails: List<SeasonDetails>,
    mediaType: String,
    onSeasonClick: (Int) -> Unit
) {

    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize()
    ) {
        // Title and Rating
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    mediaDetails.title,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = DateTimeUtils.formatDate(mediaDetails.releaseDate),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Additional info for TV shows
                if (mediaType.lowercase() == "tv") {
                    Text(
                        "${mediaDetails.numberOfSeasons} Seasons • ${mediaDetails.numberOfEpisodes} Episodes",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    mediaDetails.runtime?.let { runtime ->
                        Text(
                            "${runtime}min",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            UserScoreBar(rating = mediaDetails.voteAverage)
        }

        Spacer(Modifier.height(24.dp))

        // Overview
        Text(
            mediaDetails.overview,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        // Genres
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            mediaDetails.genres.forEach { genre ->
                GenreChip(genre)
            }
        }

        Spacer(Modifier.height(24.dp))

        // Cast Section
        cast?.let { castData ->
            if (castData.cast.isNotEmpty()) {
                CastSection(cast = castData.cast)
                Spacer(Modifier.height(24.dp))
            }
        }

        // TV Show Seasons and Episodes
        if (mediaType.lowercase() == "tv" && mediaDetails.seasons.isNotEmpty()) {
            SeasonsSection(
                seasons = mediaDetails.seasons,
                seasonDetails = seasonDetails,
                onSeasonClick = onSeasonClick
            )
        }
    }
}

@Composable
private fun SeasonsSection(
    seasons: List<Season>,
    seasonDetails: List<SeasonDetails>,
    onSeasonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "Seasons",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(seasons.filter { it.seasonNumber > 0 }) { season ->
                SeasonCard(
                    season = season,
                    onClick = { onSeasonClick(season.seasonNumber) }
                )
            }
        }
    }
}

@Composable
private fun SeasonCard(
    season: Season,

    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(4.dp),

        ) {
        AsyncImage(
            model = ImageUrlBuilder.buildImageUrl(
                season.posterPath,
                ImageUrlBuilder.ImageSize.W185
            ),
            contentDescription = season.name,
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(0.65f)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = season.name,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        season.airDate?.let { airDate ->
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = DateTimeUtils.formatDate(airDate),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = "${season.episodeCount} Episodes",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onNavigateBack) {
                Text("Go Back")
            }
        }
    }
}
