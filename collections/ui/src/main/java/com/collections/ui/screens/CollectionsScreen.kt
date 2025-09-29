@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.collections.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.collections.domain.models.Collection
import com.app.collections.domain.models.CollectionItem
import com.app.core.network.util.ImageUrlBuilder
import com.app.core.ui.composables.NetworkImageLoader
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
        var selectedItem by remember { mutableStateOf<CollectionItem?>(null) }
        var selectedCollection by remember { mutableStateOf<Collection?>(null) }
        SharedTransitionLayout(
            modifier = Modifier.fillMaxSize()
        ) {
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
                        val name = collection.name.replaceFirstChar { it.uppercase() }
                        CollectionsSection(
                            label = name,
                            collectionItems = collection.items.reversed(),
                            isLoading = isLoading,
                            selectedItem = selectedItem,
                            onCollectionClicked = onNavigateToMediaDetails,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            onLongClick = {
                                selectedItem = it
                                selectedCollection = collection
                            }
                        )
                    }
                }

                // Custom Collections Section
                for (collection in customCollections) {
                    if (collection.items.isNotEmpty()) {
                        val name = collection.name.replaceFirstChar { it.uppercase() }
                        CollectionsSection(
                            label = name,
                            collectionItems = collection.items.reversed(),
                            isLoading = isLoading,
                            selectedItem = selectedItem,
                            onCollectionClicked = onNavigateToMediaDetails,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            onLongClick = {
                                selectedCollection = collection
                                selectedItem = it
                            }
                        )
                    }

                }

                Spacer(Modifier.height(16.dp))
            }

            ExtendedEditCard(
                collectionName = selectedCollection?.name?.replaceFirstChar { it.uppercase() }
                    ?: "Collection",
                collectionItem = selectedItem,
                onDismiss = {
                    selectedItem = null
                },
                onRemoveFromCollection = {
                    selectedCollection?.let { collection ->
                        selectedItem?.let { item ->
                            if (collection.isDefault) {
                                when (collection.name.lowercase()) {
                                    "watchlist" -> {
                                        viewModel.removeFromWatchlist(
                                            item.tmdbId,
                                            item.mediaType
                                        )
                                    }

                                    "already watched" -> {
                                        viewModel.removeFromAlreadyWatched(
                                            item.tmdbId,
                                            item.mediaType
                                        )
                                    }
                                }
                            } else {
                                viewModel.removeFromCollection(
                                    collection.id.toString(),
                                    item.tmdbId,
                                    item.mediaType
                                )
                            }
                        }
                    }
                    selectedItem = null
                }
            )
        }

    }
}

@Composable
fun SharedTransitionScope.ExtendedEditCard(
    collectionName: String = "Collection",
    collectionItem: CollectionItem?,
    onDismiss: () -> Unit,
    onRemoveFromCollection: () -> Unit
) {
    AnimatedContent(
        targetState = collectionItem,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "ExtendedEditCardAnimation"

    ) { targetItem ->

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (targetItem != null) {
                val cardKey = "extended_collection_card${targetItem.id}"
                val posterUrl = ImageUrlBuilder.buildPosterUrl(targetItem.content.posterPath)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.9f))
                        .clickable {
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NetworkImageLoader(
                            modifier = Modifier
                                .width(200.dp)
                                .aspectRatio(0.65f)
                                .sharedBounds(
                                    rememberSharedContentState(cardKey),
                                    animatedVisibilityScope = this@AnimatedContent,
                                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                )
                                .shadow(20.dp, MaterialTheme.shapes.medium, clip = true),
                            imageUrl = posterUrl,
                            imageKey = cardKey,
                            contentDescription = targetItem.content.title,
                        )

                        TextButton(
                            onClick = onRemoveFromCollection
                        ) {
                            Text(
                                "Remove from $collectionName",
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        TextButton(
                            onClick = onDismiss
                        ) {
                            Text("Dismiss", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
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