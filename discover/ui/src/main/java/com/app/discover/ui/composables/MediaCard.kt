package com.app.discover.ui.composables

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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.app.core.ui.R
import com.app.core.ui.composables.shimmer
import com.app.discover.domain.entities.Media


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MediaCard(
    modifier: Modifier = Modifier,
    media: Media,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: (Int, String, String?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(media.id, media.type.name, media.posterUrl)
            }
    ) {
        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(media.posterUrl)
                    .crossfade(true)
                    .placeholderMemoryCacheKey("poster_${media.id}")
                    .memoryCacheKey("poster_${media.id}")
                    .build(),
                contentDescription = media.title,
                modifier = with(sharedTransitionScope) {
                    modifier
                        .fillMaxWidth()
                        .aspectRatio(0.65f)
                        .clip(MaterialTheme.shapes.medium)
                        .sharedElement(
                            rememberSharedContentState("poster_${media.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                },
                contentScale = ContentScale.Crop
            ) {
                val state = painter.state.collectAsState().value
                when (state) {
                    AsyncImagePainter.State.Empty -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer()
                    )

                    is AsyncImagePainter.State.Error -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_broken_image),
                            contentDescription = "Broken Image",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }

                    is AsyncImagePainter.State.Loading -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer()
                    )

                    is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                }
            }
        }

        Spacer(Modifier.height(4.dp))
        Text(
            text = media.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun ShimmerMediaCard() {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
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
