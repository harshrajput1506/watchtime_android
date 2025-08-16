package com.app.discover.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.app.core.ui.composables.shimmer
import com.app.discover.domain.entities.Media


@Composable
fun MediaCard(
    modifier: Modifier = Modifier,
    media: Media? = null,
    onClick: (Int, String) -> Unit,
    isShimmer: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = media != null
            ) {
                media?.let {
                    onClick(it.id, it.type.name)
                }
            }
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(0.65f),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            if (isShimmer || media == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

    }
}
