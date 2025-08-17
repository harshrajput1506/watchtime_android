package com.app.discover.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.core.ui.composables.MediaChoiceRow
import com.app.discover.domain.entities.Media
import com.app.discover.ui.R
import com.app.discover.ui.composables.DiscoverMediaShimmer
import com.app.discover.ui.composables.DiscoverSearchBar
import com.app.discover.ui.composables.MediaCard
import com.app.discover.ui.composables.ShimmerMediaCard
import com.app.discover.ui.states.DiscoverMoviesState
import com.app.discover.ui.states.DiscoverTvShowsState
import com.app.discover.ui.states.SearchState
import com.app.discover.ui.viewModels.DiscoverViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: DiscoverViewModel = koinViewModel(),
    onNavigateToMediaDetails: (Int, String, String?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    var isSearchExpanded by remember { mutableStateOf(false) }
    var showScrollToTopFab by remember { mutableStateOf(false) }
    var isScrolling by remember { mutableStateOf(false) }

    // Detect scroll position to determine if top bar should be collapsed
    val isScrolled by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 50
        }
    }

    // Detect when user is scrolling
    val isCurrentlyScrolling by remember {
        derivedStateOf {
            gridState.isScrollInProgress
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

    // Handle scroll state changes and FAB visibility
    LaunchedEffect(isScrolled, isCurrentlyScrolling) {
        isScrolling = isCurrentlyScrolling
        if (isScrolled && !isScrolling) {
            showScrollToTopFab = true
            isSearchExpanded = false // Collapse search bar when scrolling
            delay(3000)
            showScrollToTopFab = false
        } else {
            showScrollToTopFab = false
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
        targetValue = if (isScrolled && !isSearchExpanded) 60.dp else 160.dp,
        animationSpec = tween(1000),
        label = "topBarHeight"
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
        modifier = modifier.fillMaxSize(),
        topBar = {
            // Top Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(topBarHeight),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isScrolled) 4.dp else 0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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

                            Spacer(Modifier.height(8.dp))

                            AnimatedVisibility(
                                visible = !isScrolled || isSearchExpanded,
                                enter = scaleIn() + fadeIn(),
                                exit = scaleOut() + fadeOut()
                            ) {
                                DiscoverSearchBar(
                                    label = if (viewModel.uiState.value.selectedMediaType == 0) "Search Movies" else "Search TV Shows",
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
        },
        floatingActionButton = {

            AnimatedVisibility(
                visible = showScrollToTopFab && !isScrolling,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),

                ) {
                // FAB
                FloatingActionButton(
                    onClick = {
                        if (gridState.firstVisibleItemIndex > 0) {
                            coroutineScope.launch {
                                gridState.animateScrollToItem(0)
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

            }

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            // Media choice row
            AnimatedVisibility(
                visible = !isScrolled || isSearchExpanded
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
                                    onItemClick = onNavigateToMediaDetails,
                                    gridState = gridState,
                                    isLoadingMore = searchState.isLoadingMore,
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope
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
                                onItemClick = onNavigateToMediaDetails,
                                gridState = gridState,
                                isLoadingMore = isLoadingMore,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MediaGrid(
    items: List<Media>,
    onItemClick: (Int, String, String?) -> Unit,
    gridState: LazyGridState = rememberLazyGridState(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isLoadingMore: Boolean = false
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(items.size) { index ->
            MediaCard(
                media = items[index],
                onClick = onItemClick,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope

            )
        }

        // Show loading indicator when loading more content
        if (isLoadingMore) {
            items(2) {
                ShimmerMediaCard()
            }
        }
    }
}