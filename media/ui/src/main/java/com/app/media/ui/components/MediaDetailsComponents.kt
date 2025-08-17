package com.app.media.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.app.core.network.util.ImageUrlBuilder
import com.app.core.ui.composables.shimmer
import com.app.media.domain.model.CastMember
import com.app.media.domain.model.Genre
import com.app.media.domain.model.Provider
import kotlin.math.roundToInt

@Composable
fun UserScoreBar(
    rating: Double,
    modifier: Modifier = Modifier
) {
    val ratingPercentage = (rating * 10).roundToInt()
    val progressColor = when {
        ratingPercentage >= 70 -> MaterialTheme.colorScheme.primary
        ratingPercentage >= 40 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier.background(
                MaterialTheme.colorScheme.surfaceContainerHighest,
                CircleShape
            ),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    ratingPercentage.toString(),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    "%",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(4.dp)
                    .size(48.dp),
                progress = { (rating / 10).toFloat() },
                strokeCap = StrokeCap.Round,
                trackColor = progressColor.copy(alpha = 0.1f),
                color = progressColor
            )
        }
    }
}

@Composable
fun GenreChip(
    genre: Genre,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            RoundedCornerShape(100.dp)
        )
    ) {
        Text(
            genre.name,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp)
        )
    }
}

@Composable
fun CastSection(
    cast: List<CastMember>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "Cast",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(cast.take(16)) { member ->
                CastMemberCard(member)
            }
        }
    }
}

@Composable
fun CastMemberCard(
    member: CastMember,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .aspectRatio(0.7f)
    ) {
        AsyncImage(
            model = ImageUrlBuilder.buildImageUrl(
                member.profilePath,
                ImageUrlBuilder.ImageSize.W185
            ),
            contentDescription = member.name,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = member.name,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = member.character,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }

}

@Composable
fun WatchProvidersSection(
    providers: List<Provider>,
    title: String,
    modifier: Modifier = Modifier
) {
    if (providers.isNotEmpty()) {
        Column(modifier = modifier) {
            Text(
                title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(providers) { provider ->
                    ProviderCard(provider)
                }
            }
        }
    }
}

@Composable
fun ProviderCard(
    provider: Provider,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.size(60.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        AsyncImage(
            model = ImageUrlBuilder.buildImageUrl(provider.logoPath, ImageUrlBuilder.ImageSize.W92),
            contentDescription = provider.providerName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
fun ShimmerPlaceHolder() {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
    ) {
        ShimmerCard(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(0.6f)
        )
        Spacer(Modifier.height(8.dp))
        ShimmerCard(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth(0.4f)
        )
        Spacer(Modifier.height(24.dp))
        ShimmerCard(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
        )

    }

}

@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmer()
        )
    }
}
