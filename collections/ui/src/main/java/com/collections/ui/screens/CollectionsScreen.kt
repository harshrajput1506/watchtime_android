@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.collections.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.collections.ui.composables.CollectionsSection
import com.collections.ui.viewModels.CollectionsViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CollectionsViewModel = koinViewModel(),
    onNavigateToMediaDetails: (tmdbId: Int, name: String, posterUrl: String?, key: String) -> Unit,
    onNavigateToCreateCollection: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val state = viewModel.collectionsState.collectAsStateWithLifecycle()
    val collections = state.value.collections
    val isLoading = state.value.isLoading

    // Separate default and custom collections
    val defaultCollections = collections.filter { it.isDefault }
    val customCollections = collections.filter { !it.isDefault }

    // check isScrolled
    val isCollapsed by remember { derivedStateOf { scrollState.value != 0 } }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = isCollapsed,
                    label = "TopBarAnimation"
                ) { targetIsCollapsed ->
                    if (targetIsCollapsed) {
                        CollapsedTopBar(
                            animatedVisibilityScope = this@AnimatedContent,
                            navigationAnimatedVisibilityScope = animatedVisibilityScope,
                            navigationSharedTransitionScope = sharedTransitionScope,
                            onAddCollectionClick = {
                                onNavigateToCreateCollection()
                            })
                    } else {
                        TopBar(
                            animatedVisibilityScope = this@AnimatedContent,
                            onAddCollectionClick = {
                                onNavigateToCreateCollection()
                            })
                    }

                }
            }

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .verticalScroll(state = scrollState)
        ) {

            Spacer(Modifier.height(16.dp))

            // Default Collections Section (Watchlist, Already Watched)
            if (defaultCollections.isNotEmpty() || isLoading) {
                for (collection in defaultCollections) {
                    CollectionsSection(
                        label = collection.name,
                        collectionItems = collection.items,
                        isLoading = isLoading,
                        onCollectionClicked = onNavigateToMediaDetails,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }

            for (collection in customCollections) {
                if (collection.items.isNotEmpty()) {
                    CollectionsSection(
                        label = collection.name.replaceFirstChar { it.uppercase() },
                        collectionItems = collection.items,
                        isLoading = isLoading,
                        onCollectionClicked = onNavigateToMediaDetails,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }

            }

            // Custom Collections Section

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun SharedTransitionScope.CollapsedTopBar(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigationAnimatedVisibilityScope: AnimatedVisibilityScope,
    navigationSharedTransitionScope: SharedTransitionScope,
    onAddCollectionClick: () -> Unit
) {
    with(navigationAnimatedVisibilityScope) {
        with(navigationSharedTransitionScope) {
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
                shadowElevation = 8.dp,
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    with(this@CollapsedTopBar) {
                        Text(
                            "Collections",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .wrapContentWidth()
                                .sharedBounds(
                                    rememberSharedContentState(
                                        key = "discover_top_bar_title",
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    enter = fadeIn(),
                                    exit = fadeOut(),
                                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                )
                        )
                    }

                    IconButton(
                        onClick = onAddCollectionClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Collection",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

    }

}

@Composable
fun SharedTransitionScope.TopBar(
    animatedVisibilityScope: AnimatedVisibilityScope,
    onAddCollectionClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .weight(0.8f)
        ) {
            Text(
                "Collections",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .wrapContentWidth()
                    .sharedBounds(
                        rememberSharedContentState(
                            key = "discover_top_bar_title",
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                    )
            )
            Text(
                "Your personal library of movies and TV shows.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.wrapContentWidth(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }



        IconButton(
            modifier = Modifier.weight(0.2f),
            onClick = onAddCollectionClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Collection",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}