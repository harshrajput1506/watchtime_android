package com.app.popular.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.app.core.ui.composables.shimmer
import com.app.popular.domain.entities.Media

@Composable
fun MediaCard(
    modifier: Modifier = Modifier,
    media: Media? = null,
    height: Dp = 180.dp,
    width: Dp = 120.dp,
    onClick: (Int) -> Unit = {},
    isShimmer: Boolean = false
) {
    Column(
        modifier = Modifier
            .width(width)
            .aspectRatio(0.52f)
            .clickable(
                enabled = media != null
            ) {
                onClick(media?.id ?: -1)
            }
    ) {
        Card(
            modifier = modifier
                .width(width)
                .height(height),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            if (isShimmer || media == null) {
                Box(
                    modifier = Modifier
                        .width(width)
                        .height(height)
                        .shimmer()
                )

            } else {
                AsyncImage(
                    model = media.posterUrl,
                    contentDescription = media.title,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        if (!isShimmer && media != null) {
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