package com.app.media.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.app.core.network.util.ImageUrlBuilder
import com.app.media.domain.model.Cast
import com.app.media.domain.model.MediaDetails
import com.app.media.domain.model.Network
import com.app.media.domain.model.Season
import com.app.media.domain.model.SeasonDetails
import com.app.media.domain.model.WatchProviders
import com.app.media.ui.components.CastSection
import com.app.media.ui.components.GenreChip
import com.app.media.ui.components.UserScoreBar
import com.app.media.ui.components.WatchProvidersSection
import com.app.media.ui.state.MediaDetailsState
import com.app.media.ui.viewmodel.MediaDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale


@Composable
fun MediaDetailsScreen(
    mediaId: Int,
    mediaType: String,
    viewModel: MediaDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(mediaId, mediaType) {
        viewModel.loadMediaDetails(mediaId, mediaType)
    }

    when (state) {
        is MediaDetailsState.Loading -> {
            LoadingScreen()
        }

        is MediaDetailsState.Success -> {
            MediaDetailsContent(
                state = state as MediaDetailsState.Success,
                mediaType = mediaType,
                onNavigateBack = onNavigateBack,
                onSeasonClick = { seasonNumber ->
                    viewModel.loadSeasonDetails(mediaId, seasonNumber)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaDetailsContent(
    state: MediaDetailsState.Success,
    mediaType: String,
    onNavigateBack: () -> Unit,
    onSeasonClick: (Int) -> Unit
) {
    val mediaDetails = state.mediaDetails

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
            MediaDetailsBottomSheet(
                mediaDetails = mediaDetails,
                cast = state.cast,
                watchProviders = state.watchProviders,
                seasonDetails = state.seasonDetails,
                mediaType = mediaType,
                onSeasonClick = onSeasonClick
            )
        },
        sheetPeekHeight = 300.dp
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Backdrop Image
            AsyncImage(
                model = ImageUrlBuilder.buildImageUrl(
                    mediaDetails.backdropPath,
                    ImageUrlBuilder.ImageSize.W780
                ),
                contentDescription = mediaDetails.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.2f
            )

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
                    AsyncImage(
                        model = ImageUrlBuilder.buildImageUrl(
                            mediaDetails.posterPath,
                            ImageUrlBuilder.ImageSize.W500
                        ),
                        contentDescription = mediaDetails.title,
                        modifier = Modifier.fillMaxHeight(0.8f),
                        contentScale = ContentScale.Fit
                    )
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MediaDetailsBottomSheet(
    mediaDetails: MediaDetails,
    cast: Cast?,
    watchProviders: WatchProviders?,
    seasonDetails: List<SeasonDetails>,
    mediaType: String,
    onSeasonClick: (Int) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 24.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Title and Rating
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    mediaDetails.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    mediaDetails.releaseDate,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Additional info for TV shows
                if (mediaType.lowercase() == "tv") {
                    Text(
                        "${mediaDetails.numberOfSeasons} Seasons • ${mediaDetails.numberOfEpisodes} Episodes",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    mediaDetails.runtime?.let { runtime ->
                        Text(
                            "${runtime}min",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            UserScoreBar(rating = mediaDetails.voteAverage)
        }

        Spacer(Modifier.height(16.dp))

        // Popularity and Status
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoCard(
                title = "Popularity",
                value = String.format(locale = Locale.ENGLISH, "%.1f", mediaDetails.popularity)
            )
            InfoCard(
                title = "Status",
                value = mediaDetails.status
            )
        }

        Spacer(Modifier.height(16.dp))

        // Overview
        Text(
            "Overview",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
            mediaDetails.overview,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
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

        // Watch Providers
        watchProviders?.let { providers ->
            val usProviders = providers.countryProviders["US"]
            usProviders?.let { countryProviders ->
                if (countryProviders.streaming.isNotEmpty()) {
                    WatchProvidersSection(
                        providers = countryProviders.streaming,
                        title = "Stream"
                    )
                    Spacer(Modifier.height(16.dp))
                }

                if (countryProviders.buy.isNotEmpty()) {
                    WatchProvidersSection(
                        providers = countryProviders.buy,
                        title = "Buy"
                    )
                    Spacer(Modifier.height(16.dp))
                }

                if (countryProviders.rent.isNotEmpty()) {
                    WatchProvidersSection(
                        providers = countryProviders.rent,
                        title = "Rent"
                    )
                    Spacer(Modifier.height(16.dp))
                }
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

        // Networks for TV shows
        if (mediaType.lowercase() == "tv" && mediaDetails.networks.isNotEmpty()) {
            Spacer(Modifier.height(24.dp))
            NetworksSection(networks = mediaDetails.networks)
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.height(300.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(seasons.filter { it.seasonNumber > 0 }) { season ->
                val details = seasonDetails.find { it.seasonNumber == season.seasonNumber }
                SeasonCard(
                    season = season,
                    seasonDetails = details,
                    onClick = { onSeasonClick(season.seasonNumber) }
                )
            }
        }
    }
}

@Composable
private fun SeasonCard(
    season: Season,
    seasonDetails: SeasonDetails?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = ImageUrlBuilder.buildImageUrl(
                    season.posterPath,
                    ImageUrlBuilder.ImageSize.W154
                ),
                contentDescription = season.name,
                modifier = Modifier
                    .width(60.dp)
                    .aspectRatio(0.67f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = season.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${season.episodeCount} Episodes",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                season.airDate?.let { airDate ->
                    Text(
                        text = airDate,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (season.overview.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = season.overview,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Show episodes if season details are loaded
                seasonDetails?.let { details ->
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Episodes loaded: ${details.episodes.size}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun NetworksSection(
    networks: List<Network>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "Networks",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(networks) { network ->
                NetworkCard(network)
            }
        }
    }
}

@Composable
private fun NetworkCard(
    network: Network,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            network.logoPath?.let { logoPath ->
                AsyncImage(
                    model = ImageUrlBuilder.buildImageUrl(logoPath, ImageUrlBuilder.ImageSize.W92),
                    contentDescription = network.name,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = network.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
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
