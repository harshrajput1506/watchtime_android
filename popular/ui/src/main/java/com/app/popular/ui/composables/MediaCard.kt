package com.app.popular.ui.composables

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
import com.app.core.ui.composables.NetworkImageLoader
import com.app.core.ui.composables.shimmer
import com.app.popular.domain.entities.Media

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MediaCard(
    modifier: Modifier = Modifier,
    label: String,
    media: Media,
    width: Dp = 120.dp,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: (Int, String, String?, String) -> Unit,
) {
    val posterKey = "${label.lowercase()}_poster_${media.id}"
    with(sharedTransitionScope) {
        Column(
            modifier = modifier
                .width(width)
                .aspectRatio(0.52f)
                .clickable {
                    onClick(media.id, media.type.name, media.posterUrl, posterKey)
                }

        ) {
            NetworkImageLoader(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.65f)
                    .sharedBounds(
                        rememberSharedContentState(posterKey),
                        animatedVisibilityScope = animatedVisibilityScope,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                    )
                    .shadow(4.dp, MaterialTheme.shapes.medium, clip = true),
                imageUrl = media.posterUrl,
                imageKey = posterKey,
                contentDescription = media.title,
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = media.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

        }
    }

}

@Composable
fun ShimmerMediaCard(
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