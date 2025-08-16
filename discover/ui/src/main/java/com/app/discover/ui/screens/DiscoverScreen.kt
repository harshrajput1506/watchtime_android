package com.app.discover.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.core.domain.entities.Media
import com.app.core.ui.composables.MediaChoiceRow
import com.app.discover.ui.R
import com.app.discover.ui.composables.DiscoverMediaShimmer
import com.app.discover.ui.composables.DiscoverSearchBar
import com.app.discover.ui.composables.MediaCard
import com.app.discover.ui.states.DiscoverMoviesState
import com.app.discover.ui.states.DiscoverTvShowsState
import com.app.discover.ui.states.SearchState
import com.app.discover.ui.viewModels.DiscoverViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()
    var isSearchExpanded by remember { mutableStateOf(false) }

    // Detect scroll position to determine if top bar should be collapsed
    val isScrolled by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 50
        }
    }

    // Detect when user reaches end of list for pagination
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            totalItemsNumber > 0 && lastVisibleItemIndex > (totalItemsNumber - 6)
        }
    }

    // Trigger pagination when user reaches end
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadNextPage()
        }
    }

    // Animation values
    val topBarHeight by animateDpAsState(
        targetValue = if (isScrolled && !isSearchExpanded) 60.dp else 140.dp,
        animationSpec = tween(1000),
        label = "topBarHeight"
    )

    val topBarElevation by animateDpAsState(
        targetValue = if (isScrolled && !isSearchExpanded) 8.dp else 0.dp,
        animationSpec = tween(1000),
        label = "topBarElevation"
    )

    val largeTitleAlpha by animateFloatAsState(
        targetValue = if (isScrolled && !isSearchExpanded) 0f else 1f,
        animationSpec = tween(1000),
        label = "titleAlpha"
    )
    val smallTitleAlpha by animateFloatAsState(
        targetValue = if (isScrolled && !isSearchExpanded) 1f else 0f,
        animationSpec = tween(1000),
        label = "titleAlpha"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = topBarElevation),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topBarHeight)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isScrolled && !isSearchExpanded) {
                        // Collapsed top bar
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Discover",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.alpha(smallTitleAlpha)
                            )

                            IconButton(
                                onClick = { isSearchExpanded = true }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_search),
                                    contentDescription = "Search",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    } else {
                        // Expanded top bar
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center

                        ) {
                            Text(
                                "Discover",
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .alpha(largeTitleAlpha)
                            )

                            AnimatedVisibility(
                                visible = !isScrolled || isSearchExpanded,
                            ) {
                                DiscoverSearchBar(
                                    query = uiState.searchQuery,
                                    onQueryChange = viewModel::updateSearchQuery,
                                    onSearch = viewModel::onKeyboardSearch,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Media choice row
            AnimatedVisibility(
                visible = !isScrolled || isSearchExpanded
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    MediaChoiceRow(
                        selectedIndex = uiState.selectedMediaType,
                    ) { index ->
                        viewModel.switchMediaType(index)
                    }
                }
            }

            // Content
            when {
                uiState.isSearchMode -> {
                    val searchState = uiState.searchState
                    when (searchState) {
                        is SearchState.Idle -> {
                            InfoMessage("Start typing to search for movies and TV shows")
                        }

                        is SearchState.Loading -> {
                            DiscoverMediaShimmer()
                        }

                        is SearchState.Success -> {
                            if (searchState.mediaList.isEmpty()) {
                                InfoMessage("No results found")
                            } else {
                                MediaGrid(
                                    items = searchState.mediaList,
                                    onItemClick = viewModel::onMediaClicked,
                                    gridState = gridState,
                                    isLoadingMore = searchState.isLoadingMore
                                )
                            }
                        }

                        is SearchState.Error -> {
                            InfoMessage("Error: ${searchState.message}", isError = true)
                        }
                    }
                }

                else -> {
                    when {
                        (uiState.selectedMediaType == 0 && uiState.discoverMoviesState is DiscoverMoviesState.Loading) ||
                                (uiState.selectedMediaType == 1 && uiState.discoverTvShowsState is DiscoverTvShowsState.Loading) -> {
                            DiscoverMediaShimmer()
                        }

                        (uiState.selectedMediaType == 0 && uiState.discoverMoviesState is DiscoverMoviesState.Success) ||
                                (uiState.selectedMediaType == 1 && uiState.discoverTvShowsState is DiscoverTvShowsState.Success) -> {
                            val (mediaList, isLoadingMore) = if (uiState.selectedMediaType == 0) {
                                val moviesState =
                                    uiState.discoverMoviesState as DiscoverMoviesState.Success
                                moviesState.mediaList to moviesState.isLoadingMore
                            } else {
                                val tvShowsState =
                                    uiState.discoverTvShowsState as DiscoverTvShowsState.Success
                                tvShowsState.mediaList to tvShowsState.isLoadingMore
                            }

                            MediaGrid(
                                items = mediaList,
                                onItemClick = viewModel::onMediaClicked,
                                gridState = gridState,
                                isLoadingMore = isLoadingMore
                            )
                        }

                        (uiState.selectedMediaType == 0 && uiState.discoverMoviesState is DiscoverMoviesState.Error) ||
                                (uiState.selectedMediaType == 1 && uiState.discoverTvShowsState is DiscoverTvShowsState.Error) -> {
                            val errorMessage = if (uiState.selectedMediaType == 0) {
                                (uiState.discoverMoviesState as DiscoverMoviesState.Error).message
                            } else {
                                (uiState.discoverTvShowsState as DiscoverTvShowsState.Error).message
                            }
                            InfoMessage("Error: $errorMessage", isError = true)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun InfoMessage(message: String, isError: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(32.dp)
        )
    }
}

@Composable
fun MediaGrid(
    items: List<Media>,
    onItemClick: (Int) -> Unit,
    gridState: LazyGridState = rememberLazyGridState(),
    isLoadingMore: Boolean = false
) {
    LazyVerticalGrid(
        state = gridState,
        modifier = Modifier.padding(bottom = 100.dp),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(items.size) { index ->
            MediaCard(
                media = items[index],
                onClick = onItemClick,
            )
        }

        // Show loading indicator when loading more content
        if (isLoadingMore) {
            items(2) {
                MediaCard(
                    isShimmer = true
                )
            }
        }
    }
}