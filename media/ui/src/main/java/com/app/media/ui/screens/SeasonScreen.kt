package com.app.media.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.core.network.util.ImageUrlBuilder
import com.app.core.ui.composables.NetworkImageLoader
import com.app.core.utils.DateTimeUtils
import com.app.media.domain.model.Episode
import com.app.media.domain.model.SeasonDetails
import com.app.media.ui.components.SeasonShimmerPlaceHolder
import com.app.media.ui.state.SeasonState
import com.app.media.ui.viewmodel.SeasonViewModel
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SeasonsScreen(
    tvId: Int,
    seasonNumber: Int,
    tvName: String,
    seasonName: String,
    posterPath: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateBack: () -> Unit,
    viewModel: SeasonViewModel = koinViewModel()
) {
    val state by viewModel.seasonsState.collectAsStateWithLifecycle()
    var isTopBarCollapsed by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()

    // fetch season details when the screen is launched
    LaunchedEffect(tvId, seasonNumber) {
        viewModel.loadSeasonDetails(tvId, seasonNumber)
    }

    // Handle scroll changes to toggle top bar visibility
    LaunchedEffect(scrollState) {
        snapshotFlow {
            scrollState.firstVisibleItemIndex > 0 ||
                    (scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset > 0)
        }.collect { collapsed ->
            isTopBarCollapsed = collapsed
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top Bar - Fixed at top
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = isTopBarCollapsed
                ) { targetState ->
                    when {
                        targetState -> TopBar(
                            seasonName = seasonName,
                            onNavigateBack = onNavigateBack,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent
                        )

                        else -> SeasonHeader(
                            seasonName = seasonName,
                            tvName = tvName,
                            seasonDetails = (state as? SeasonState.Success)?.seasonDetails,
                            posterPath = posterPath, // posterUrl,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent,
                            sharedTransitionScope1 = sharedTransitionScope,
                            animatedVisibilityScope1 = animatedVisibilityScope,
                            onNavigateBack = onNavigateBack
                        )
                    }
                }
            }

            // Content based on state
            when (state) {
                is SeasonState.Loading -> {
                    SeasonShimmerPlaceHolder()
                }

                is SeasonState.Success -> {
                    SeasonDetailsContent(
                        seasonDetails = (state as SeasonState.Success).seasonDetails,
                        scrollState = scrollState,
                        modifier = Modifier.weight(1f),
                    )
                }

                is SeasonState.Error -> {
                    SeasonShimmerPlaceHolder()
                    /*ErrorScreen(
                        message = state.message,
                        onNavigateBack = onNavigateBack,
                        modifier = Modifier.weight(1f)
                    )*/
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SeasonDetailsContent(
    seasonDetails: SeasonDetails,
    modifier: Modifier = Modifier,
    scrollState: LazyListState
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = scrollState
    ) {
        // Season Header
        item {
            SeasonOverview(seasonDetails.overview)
        }

        // Episodes Section
        item {
            Text(
                text = "Episodes (${seasonDetails.episodes.size})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(
            items = seasonDetails.episodes,
            key = { it.id }
        ) { episode ->
            EpisodeCard(
                episode = episode,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun TopBar(
    seasonName: String,
    onNavigateBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .sharedBounds(
                    rememberSharedContentState("season_top_bar"),
                    animatedVisibilityScope
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = seasonName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .sharedElement(
                        rememberSharedContentState("season_title"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SeasonHeader(
    seasonName: String,
    tvName: String,
    posterPath: String?,
    seasonDetails: SeasonDetails?,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope1: SharedTransitionScope,
    animatedVisibilityScope1: AnimatedVisibilityScope,
    onNavigateBack: () -> Unit
) {
    with(sharedTransitionScope) {
        Column(
            modifier = Modifier.sharedBounds(
                rememberSharedContentState("season_top_bar"),
                animatedVisibilityScope
            )
        ) {
            // Back button row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Season header content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Season Poster
                Card(
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    with(sharedTransitionScope1) {
                        NetworkImageLoader(
                            modifier = Modifier
                                .size(120.dp, 180.dp)
                                .sharedBounds(
                                    rememberSharedContentState("season_poster_${seasonName}_${posterPath}"),
                                    animatedVisibilityScope = animatedVisibilityScope1
                                )
                                .clip(MaterialTheme.shapes.medium),
                            imageUrl = ImageUrlBuilder.buildImageUrl(
                                posterPath,
                                ImageUrlBuilder.ImageSize.W342
                            ),
                            contentDescription = seasonName,
                            imageKey = "season_poster_${seasonName}_${posterPath}",
                        )
                    }
                }

                // Season Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = tvName,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = seasonName,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("season_title"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )

                    seasonDetails?.airDate?.let { airDate ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = DateTimeUtils.formatDate(airDate),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun SeasonOverview(
    overview: String,
    modifier: Modifier = Modifier
) {
    if (overview.isNotBlank()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = modifier.padding(horizontal = 16.dp),
            text = overview,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun EpisodeCard(
    episode: Episode,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Episode Still Image
        Card(
            modifier = Modifier.size(120.dp, 80.dp),
            shape = MaterialTheme.shapes.small
        ) {
            NetworkImageLoader(
                modifier = Modifier.fillMaxSize(),
                imageUrl = ImageUrlBuilder.buildImageUrl(
                    episode.stillPath,
                    ImageUrlBuilder.ImageSize.W342
                ),
                contentDescription = episode.name
            )
        }

        // Episode Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "${episode.episodeNumber}. ${episode.name}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (episode.voteAverage > 0) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = "★ ${
                                String.format(
                                    locale = Locale.ENGLISH,
                                    "%.1f",
                                    episode.voteAverage
                                )
                            }",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            episode.airDate?.let { airDate ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = DateTimeUtils.formatDate(airDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (DateTimeUtils.isDateInFuture(airDate)) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Upcoming",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }



            if (episode.runtime != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${episode.runtime} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (episode.overview.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = episode.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight
                )
            }
        }
    }
}
