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
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    ratingPercentage.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "%",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
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

        Spacer(Modifier.width(4.dp))

        Column {
            Text(
                "User",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "Score",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
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
            MaterialTheme.colorScheme.surfaceVariant,
            RoundedCornerShape(100.dp)
        )
    ) {
        Text(
            genre.name,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(cast.take(10)) { member ->
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
    Card(
        modifier = modifier.width(120.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = ImageUrlBuilder.buildImageUrl(
                    member.profilePath,
                    ImageUrlBuilder.ImageSize.W185
                ),
                contentDescription = member.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.75f)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = member.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = member.character,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
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
