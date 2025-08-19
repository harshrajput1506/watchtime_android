package com.app.media.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.core.network.util.ImageUrlBuilder
import com.app.core.ui.composables.NetworkImageLoader
import com.app.core.utils.DateTimeUtils
import com.app.media.domain.model.Season


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SeasonsSection(
    modifier: Modifier = Modifier,
    seasons: List<Season>,
    onSeasonClick: (String?, Int, String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(modifier = modifier) {
        Text(
            "Seasons",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(seasons.filter { it.seasonNumber > 0 }) { season ->
                SeasonCard(
                    season = season,
                    onClick = {
                        onSeasonClick(
                            season.posterPath,
                            season.seasonNumber,
                            season.name
                        )
                    },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SeasonCard(
    modifier: Modifier = Modifier,
    season: Season,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val posterUrl = ImageUrlBuilder.buildImageUrl(
        season.posterPath,
        ImageUrlBuilder.ImageSize.W185
    )
    with(sharedTransitionScope) {
        Column(
            modifier = modifier
                .width(120.dp)
                .clickable(onClick = onClick),
            verticalArrangement = Arrangement.spacedBy(4.dp),

            ) {
            NetworkImageLoader(
                imageUrl = posterUrl,
                imageKey = "season_poster_${season.name}_${season.posterPath}",
                contentDescription = season.name,
                modifier = Modifier
                    .width(120.dp)
                    .aspectRatio(0.65f)
                    .sharedBounds(
                        rememberSharedContentState("season_poster_${season.name}_${season.posterPath}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .clip(MaterialTheme.shapes.medium),
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                text = season.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            season.airDate?.let { airDate ->
                Text(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    text = DateTimeUtils.formatDate(airDate),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = "${season.episodeCount} Episodes",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

}