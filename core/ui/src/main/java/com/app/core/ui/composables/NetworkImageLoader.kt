package com.app.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.app.core.ui.R

@Composable
fun NetworkImageLoader(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    contentDescription: String,
    imageKey: String? = null,
    contentScale: ContentScale = ContentScale.Crop

) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .placeholderMemoryCacheKey(imageKey)
            .memoryCacheKey(imageKey)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    ) {
        val state = painter.state.collectAsState().value
        when (state) {
            AsyncImagePainter.State.Empty -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .shimmer()
            )

            is AsyncImagePainter.State.Error -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
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
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .shimmer()
            )

            is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
        }
    }
}