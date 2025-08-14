package com.app.auth.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun AnimatedHeroSection(
    modifier: Modifier = Modifier,
    moviePosters: Map<Int, List<String>> = posters(), // Poster URLs
    rowHeight: Dp = 180.dp,
    posterWidth: Dp = 120.dp,
    posterSpacing: Dp = 16.dp
) {
    Box {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(3) { rowIndex ->
                AnimatedMovieRow(
                    posters = moviePosters[rowIndex + 1] ?: emptyList(),
                    rowIndex = rowIndex,
                    rowHeight = rowHeight,
                    posterWidth = posterWidth,
                    posterSpacing = posterSpacing
                )
            }
        }


        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(200.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        )
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "One place for all your watchlist dreams",
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }

}

@Composable
private fun AnimatedMovieRow(
    posters: List<String>,
    rowIndex: Int,
    rowHeight: Dp,
    posterWidth: Dp,
    posterSpacing: Dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "movie_row_$rowIndex")
    val imageBaseUrl = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/"

    // Different speeds per row for variety
    val durationMillis = when (rowIndex) {
        0 -> 25000 // Slow
        1 -> 22000 // Fast
        else -> 25000 // Medium
    }

    // Width of one set of posters
    val singleSetWidthPx = with(LocalDensity.current) {
        (posterWidth + posterSpacing).toPx() * posters.size
    }

    // Animate offset from 0 to one set width
    val rawOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = singleSetWidthPx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset_animation_$rowIndex"
    )

    // Alternate direction for rows
    val animatedOffset = if (rowIndex % 2 == 0) -rawOffset else rawOffset

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .clipToBounds()
    ) {
        Layout(
            content = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(posterSpacing)
                ) {
                    // Create enough duplicates to ensure smooth infinite scrolling
                    repeat(7) { setIndex ->
                        posters.forEach { posterRes ->
                            MoviePoster(
                                imageUrl = "$imageBaseUrl$posterRes",
                                width = posterWidth,
                                height = rowHeight
                            )
                        }
                    }
                }
            }
        ) { measurables, constraints ->
            // Give children infinite width to lay out fully
            val placeable =
                measurables.map { it.measure(constraints.copy(maxWidth = androidx.compose.ui.unit.Constraints.Infinity)) }

            val width = placeable.maxOf { it.width }
            val height = placeable.maxOf { it.height }

            layout(width, height) {
                placeable.forEach { it.placeRelative(animatedOffset.dp.toPx().toInt(), 0) }
            }
        }

        if (rowIndex % 2 == 0) {

            // Left edge fade effect
            FadeEffect(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                isLeft = true
            )
        } else {
            // Right edge fade effect
            FadeEffect(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                isLeft = false
            )

        }
    }
}

@Composable
fun FadeEffect(
    modifier: Modifier,
    isLeft: Boolean = false
) {
    Box(
        modifier = modifier
            .width(12.dp)
            .fillMaxHeight()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        if (isLeft) MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f) else Color.Transparent,
                        if (isLeft) Color.Transparent else MaterialTheme.colorScheme.scrim.copy(
                            alpha = 0.6f
                        )
                    )
                )
            )
    )
}

@Composable
private fun MoviePoster(
    imageUrl: String,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(width)
            .height(height),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Movie Poster",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}


private fun posters(): Map<Int, List<String>> {

    return mapOf(
        1 to listOf(
            "cpf7vsRZ0MYRQcnLWteD5jK9ymT.jpg",
            "aFRDH3P7TX61FVGpaLhKr6QiOC1.jpg",
            "cMD9Ygz11zjJzAovURpO75Qg7rT.jpg",
            "36xXlhEpQqVVPuiZhfoQuaY4OlA.jpg",
            "apUF4pkFmrzmUJvkxEmMCPnhd4t.jpg",
            "iyxwxDZCpIm0vIORaHpmgJv2BGF.jpg"
        ),
        2 to listOf(
            "bR8ISy1O9XQxqiy0fQFw2BX72RQ.jpg",
            "ombsmhYUqR4qqOLOxAyr5V8hbyv.jpg",
            "gh4cZbhZxyTbgxQPxD0dOudNPTn.jpg",
            "4kh9dxAiClS2GMUpkRyzGwpNWWX.jpg",
            "8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
            "KoYWXbnYuS3b0GyQPkbuexlVK9.jpg"
        ),
        3 to listOf(
            "iiZZdoQBEYBv6id8su7ImL0oCbD.jpg",
            "1RICxzeoNCAO5NpcRMIgg1XT6fm.jpg",
            "u5VJ1XmPRUWNz9l0mP2rHmkUuvQ.jpg",
            "jSziioSwPVrOy9Yow3XhWIBDjq1.jpg",
            "b1RBy3l297N0c7PHjlz35cClWju.jpg",
            "84LdrRRvpWk8g0EaaW7z3eKdfum.jpg"

        )
    )
}
