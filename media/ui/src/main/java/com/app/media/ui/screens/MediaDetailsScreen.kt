package com.app.media.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.app.core.network.util.ImageUrlBuilder
import com.app.core.ui.R
import com.app.core.ui.composables.NetworkImageLoader
import com.app.core.utils.DateTimeUtils
import com.app.media.domain.model.Cast
import com.app.media.domain.model.MediaDetails
import com.app.media.ui.components.AddToCollectionBottomSheet
import com.app.media.ui.components.CastSection
import com.app.media.ui.components.GenreChip
import com.app.media.ui.components.MediaShimmerPlaceHolder
import com.app.media.ui.components.SeasonsSection
import com.app.media.ui.components.UserScoreBar
import com.app.media.ui.state.MediaDetailsState
import com.app.media.ui.viewmodel.MediaDetailsViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MediaDetailsScreen(
    mediaId: Int,
    mediaType: String,
    posterUrl: String?,
    posterKey: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: MediaDetailsViewModel,
    onNavigateBack: () -> Unit = {},
    onNavigateToSeason: (posterPath: String?, tvId: Int, seasonNumber: Int, seasonName: String, tvName: String) -> Unit = { _, _, _, _, _ -> }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val showCollectionSheet by viewModel.showCollectionBottomSheet.collectAsStateWithLifecycle()
    val isCreatingCollection by viewModel.isCreatingCollection.collectAsStateWithLifecycle()
    val availableCollections by viewModel.availableCollections.collectAsStateWithLifecycle()
    val selectedCollections by viewModel.selectedCollections.collectAsStateWithLifecycle()

    // log all bottomsheet states
    LaunchedEffect(
        showCollectionSheet,
        isCreatingCollection,
        availableCollections,
        selectedCollections
    ) {
        Log.d("MediaDetailsScreen", "showCollectionSheet: $showCollectionSheet")
        Log.d("MediaDetailsScreen", "isCreatingCollection: $isCreatingCollection")
        Log.d("MediaDetailsScreen", "availableCollections: $availableCollections")
        Log.d("MediaDetailsScreen", "selectedCollections: $selectedCollections")
    }

    AddToCollectionBottomSheet(
        showBottomSheet = showCollectionSheet,
        availableCollections = availableCollections,
        selectedCollections = selectedCollections,
        isCreatingCollection = isCreatingCollection,
        onToggleCollection = { collectionId ->
            viewModel.toggleCollectionSelection(collectionId)
        },
        onDismiss = { viewModel.hideCollectionBottomSheet() }, // dismiss
        onCreateCollection = { name, description ->
            viewModel.createNewCollection(name, description, false)
        },
    )

    with(sharedTransitionScope) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .sharedBounds(
                    rememberSharedContentState("card_$posterKey"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                ),
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
                    posterKey = posterKey,
                    posterUrl = posterUrl,
                    onNavigateBack = onNavigateBack
                )

                // Content Section
                when (state) {
                    is MediaDetailsState.Loading -> {
                        MediaShimmerPlaceHolder()
                    }

                    is MediaDetailsState.Success -> {
                        val successState = state as MediaDetailsState.Success
                        MediaDetailsSection(
                            mediaDetails = successState.mediaDetails,
                            cast = successState.cast,
                            mediaType = mediaType,
                            onSeasonClick = { posterPath, seasonNumber, seasonName ->
                                onNavigateToSeason(
                                    posterPath,
                                    mediaId,
                                    seasonNumber,
                                    seasonName,
                                    successState.mediaDetails.title
                                )
                            },
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            viewModel = viewModel
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

}

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PosterSection(
    details: MediaDetails? = null,
    posterKey: String,
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
                    NetworkImageLoader(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight(0.8f)
                            .aspectRatio(0.65f)
                            .sharedBounds(
                                rememberSharedContentState(posterKey),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                            )
                            .shadow(4.dp, MaterialTheme.shapes.medium, clip = true),
                        imageUrl = posterUrl,
                        imageKey = posterKey,
                        contentDescription = details?.title ?: "Media Poster",
                    )

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


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MediaDetailsSection(
    modifier: Modifier = Modifier,
    mediaDetails: MediaDetails,
    cast: Cast?,
    mediaType: String,
    onSeasonClick: (String?, Int, String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: MediaDetailsViewModel
) {
    val isInWatchlist by viewModel.isInWatchlist.collectAsStateWithLifecycle()
    val isAlreadyWatched by viewModel.isAlreadyWatched.collectAsStateWithLifecycle()

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

        Spacer(Modifier.height(16.dp))

        // Actions
        MediaActionButtons(
            isInWatchlist = isInWatchlist,
            isAlreadyWatched = isAlreadyWatched,
            onToggleWatchlist = { viewModel.toggleWatchlist() },
            onToggleAlreadyWatched = { viewModel.toggleAlreadyWatched() },
            onShowAddToCollection = { viewModel.showAddToCollectionDialog() }
        )

        Spacer(Modifier.height(16.dp))

        // Overview
        if (mediaDetails.overview.isNotEmpty()) {
            Text(
                text = mediaDetails.overview,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
        }

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
                onSeasonClick = onSeasonClick,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaActionButtons(
    isInWatchlist: Boolean,
    isAlreadyWatched: Boolean,
    onToggleWatchlist: () -> Unit,
    onToggleAlreadyWatched: () -> Unit,
    onShowAddToCollection: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Add to Collection Button
        FilledIconButton(
            onClick = onShowAddToCollection,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {

            Icon(
                contentDescription = "Add to Collection",
                painter = painterResource(id = R.drawable.ic_bookmar_add),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        // Watchlist Button
        FilledIconButton(
            onClick = onToggleWatchlist,
            colors =
                IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (isInWatchlist) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer
                )
        ) {
            Icon(
                contentDescription = if (isInWatchlist) "Remove from Watchlist" else "Add to Watchlist",
                imageVector = if (isInWatchlist) Icons.Rounded.Check else Icons.Rounded.Add,
                tint = if (isInWatchlist)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }


        // Already Watched button
        Button(
            onClick = onToggleAlreadyWatched,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isAlreadyWatched) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer

            )
        ) {
            Text(
                text = if (isAlreadyWatched) "Already Watched" else "Already Watched?",
                color = if (isAlreadyWatched)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }

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
