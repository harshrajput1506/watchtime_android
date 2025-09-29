package com.collections.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.collections.domain.models.CollectionItem

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CollectionsSection(
    modifier: Modifier = Modifier,
    label: String = "My Collections",
    collectionItems: List<CollectionItem> = emptyList(),
    onCollectionClicked: (tmdbId: Int, name: String, posterUrl: String?, key: String) -> Unit,
    onLongClick: (item: CollectionItem) -> Unit,
    isLoading: Boolean = false,
    selectedItem: CollectionItem?,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        // Display content based on current state
        when {
            isLoading -> {
                CollectionShimmerPlaceholder()
            }

            collectionItems.isNotEmpty() -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(collectionItems.size) { index ->
                        AnimatedVisibility(
                            visible = collectionItems[index] != selectedItem,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut(),
                            modifier = Modifier.animateItem()
                        ) {
                            CollectionItemCard(
                                collectionItem = collectionItems[index],
                                onClick = onCollectionClicked,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                extendedSharedTransitionScope = this@CollectionsSection,
                                extendedAnimatedVisibilityScope = this,
                                onLongTap = onLongClick
                            )
                        }
                    }
                }
            }

            else -> {
                EmptyCollectionsView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                )
            }
        }
    }
}
