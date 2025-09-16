package com.collections.ui.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.collections.domain.models.CollectionItem
import com.app.core.network.util.ImageUrlBuilder
import com.app.core.ui.composables.NetworkImageLoader
import com.app.core.ui.composables.shimmer

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CollectionItemCard(
    modifier: Modifier = Modifier,
    collectionItem: CollectionItem,
    width: Dp = 120.dp,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: (tmdbId: Int, mediaType: String, posterUrl: String?, key: String) -> Unit,
) {
    val cardKey = "collection_card_${collectionItem.id}"
    val posterUrl = ImageUrlBuilder.buildPosterUrl(collectionItem.content.posterPath)

    with(sharedTransitionScope) {
        Column(
            modifier = modifier
                .width(width)
                .aspectRatio(0.52f)
                .clickable {
                    onClick(
                        collectionItem.tmdbId,
                        collectionItem.mediaType,
                        posterUrl,
                        cardKey
                    )
                }

        ) {
            NetworkImageLoader(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.65f)
                    .sharedBounds(
                        rememberSharedContentState(cardKey),
                        animatedVisibilityScope = animatedVisibilityScope,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                    )
                    .shadow(4.dp, MaterialTheme.shapes.medium, clip = true),
                imageUrl = posterUrl,
                imageKey = cardKey,
                contentDescription = collectionItem.content.title,
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = collectionItem.content.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

        }
    }
}

@Composable
fun ShimmerCollectionItemCard(
    width: Dp = 120.dp,
) {
    Card(
        modifier = Modifier
            .width(width)
            .aspectRatio(0.65f),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmer()
        )
    }
}
