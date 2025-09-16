@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.app.discover.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.app.discover.ui.states.DiscoverUiState
import com.app.discover.ui.states.SearchState
import com.app.discover.ui.viewModels.DiscoverViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: DiscoverViewModel = koinViewModel(),
    onNavigateToMediaDetails: (Int, String, String?, String) -> Unit
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            // Top Bar
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = isScrolled && !isSearchExpanded,
                    transitionSpec = {
                        fadeIn(tween(1000)) togetherWith
                                fadeOut(tween(1000)) using
                                SizeTransform { _, _ ->
                                    tween(durationMillis = 1000)
                                }
                    },
                    label = "TopBarAnimation"
                ) { isCollapsed ->
                    if (isCollapsed) {
                        CollapsedTopBar(
                            onSearchClick = { isSearchExpanded = true },
                            isSearchVisible = !isScrolled || isSearchExpanded,
                            textModifier = Modifier
                                .wrapContentWidth()
                                .sharedBounds(
                                    rememberSharedContentState(
                                        key = "discover_top_bar_title",
                                    ),
                                    animatedVisibilityScope = this@AnimatedContent,
                                    enter = fadeIn(),
                                    exit = fadeOut(),
                                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                ),
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    } else {
                        ExpandedTopBar(
                            uiState = uiState,
                            isSearchVisible = !isScrolled || isSearchExpanded,
                            onQueryChange = viewModel::updateSearchQuery,
                            onSearch = viewModel::onKeyboardSearch,
                            textModifier = Modifier
                                .wrapContentWidth()
                                .sharedBounds(
                                    rememberSharedContentState(
                                        key = "discover_top_bar_title",
                                    ),
                                    animatedVisibilityScope = this@AnimatedContent,
                                    enter = fadeIn(),
                                    exit = fadeOut(),
                                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                )
                        )
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
private fun CollapsedTopBar(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
    isSearchVisible: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope

) {
    // Collapsed top bar
    with(animatedVisibilityScope) {
        with(sharedTransitionScope) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .renderInSharedTransitionScopeOverlay(
                        zIndexInOverlay = 1f,
                    )
                    .animateEnterExit(
                        enter = fadeIn(),
                        exit = fadeOut()
                    ),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Discover",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = textModifier
                    )

                    AnimatedVisibility(
                        visible = !isSearchVisible
                    ) {
                        IconButton(
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
            }
        }
    }


}

@Composable
private fun ExpandedTopBar(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    uiState: DiscoverUiState,
    isSearchVisible: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
) {
    // Expanded top bar
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),

        ) {
        Text(
            "Discover",
            style = MaterialTheme.typography.headlineLarge,
            modifier = textModifier
        )

        Spacer(Modifier.height(8.dp))

        AnimatedVisibility(
            visible = isSearchVisible
        ) {
            DiscoverSearchBar(
                label = if (uiState.selectedMediaType == 0) "Search Movies" else "Search TV Shows",
                query = uiState.searchQuery,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),

                )
        }
    }
}


@Composable
private fun InfoMessage(message: String, isError: Boolean = false) {
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
    onItemClick: (Int, String, String?, String) -> Unit,
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