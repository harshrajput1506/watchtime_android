package com.app.popular.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.auth.domain.entities.UserEntity
import com.app.popular.ui.R
import com.app.popular.ui.composables.MediaSection
import com.app.popular.ui.states.PopularMovieState
import com.app.popular.ui.states.PopularTvShowState
import com.app.popular.ui.states.TopRatedMovieState
import com.app.popular.ui.states.TopRatedTvShowState
import com.app.popular.ui.states.TrendingDailyState
import com.app.popular.ui.states.TrendingWeeklyState
import com.app.popular.ui.viewModels.PopularViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PopularScreen(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: PopularViewModel = koinViewModel(),
    onNavigateToDiscover: () -> Unit,
    onNavigateToMediaDetails: (Int, String, String?) -> Unit
) {
    val scrollState = rememberScrollState()
    val state = viewModel.popularState.collectAsState()
    val trendingDailyState = state.value.trendingDailyState
    val trendingWeeklyState = state.value.trendingWeeklyState
    val popularMoviesState = state.value.popularMovieState
    val popularTvState = state.value.popularTvShowState
    val topRatedMoviesState = state.value.topRatedMovieState
    val topRatedTvState = state.value.topRatedTvShowState
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .verticalScroll(state = scrollState)
        ) {


            // App Bar
            viewModel.user?.let {
                UserDisplayBar(it) {
                    onNavigateToDiscover()
                }
            }

            Spacer(Modifier.height(16.dp))

            // Trending Section
            MediaSection(
                label = "Trending",
                options = listOf("Today", "This Week"),
                mediaList1 = when (trendingDailyState) {
                    is TrendingDailyState.Success -> trendingDailyState.mediaList
                    else -> emptyList()
                },
                mediaList2 = when (trendingWeeklyState) {
                    is TrendingWeeklyState.Success -> trendingWeeklyState.mediaList
                    else -> emptyList()
                },
                isOption1Loading = when (trendingDailyState) {
                    is TrendingDailyState.Loading -> true
                    else -> false
                },
                isOption2Loading = when (trendingWeeklyState) {
                    is TrendingWeeklyState.Loading -> true
                    else -> false
                },
                onMediaClicked = onNavigateToMediaDetails,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            )

            // Popular Section
            MediaSection(
                label = "Popular",
                options = listOf("Movies", "TV"),
                mediaList1 = when (popularMoviesState) {
                    is PopularMovieState.Success -> popularMoviesState.mediaList
                    else -> emptyList()
                },
                mediaList2 = when (popularTvState) {
                    is PopularTvShowState.Success -> popularTvState.mediaList
                    else -> emptyList()
                },
                isOption1Loading = when (popularMoviesState) {
                    is PopularMovieState.Loading -> true
                    else -> false
                },
                isOption2Loading = when (popularTvState) {
                    is PopularTvShowState.Loading -> true
                    else -> false
                },
                onMediaClicked = onNavigateToMediaDetails,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            )


            // Upcoming Section
            MediaSection(
                label = "Top Rated",
                options = listOf("Movies", "TV"),
                mediaList1 = when (topRatedMoviesState) {
                    is TopRatedMovieState.Success -> topRatedMoviesState.mediaList
                    else -> emptyList()
                },
                mediaList2 = when (topRatedTvState) {
                    is TopRatedTvShowState.Success -> topRatedTvState.mediaList
                    else -> emptyList()
                },
                isOption1Loading = when (topRatedMoviesState) {
                    is TopRatedMovieState.Loading -> true
                    else -> false
                },
                isOption2Loading = when (topRatedTvState) {
                    is TopRatedTvShowState.Loading -> true
                    else -> false
                },
                onMediaClicked = onNavigateToMediaDetails,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            )

        }
    }
}

@Composable
fun UserDisplayBar(
    user: UserEntity,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(0.8f)
        ) {
            Text(
                "Hello \uD83D\uDC4B", style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = user.name ?: "User",
                style = MaterialTheme.typography.displaySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

        }


        IconButton(
            modifier = Modifier.weight(0.2f),
            onClick = onSearchClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search",
                modifier = Modifier.size(24.dp)
            )
        }


    }

}
