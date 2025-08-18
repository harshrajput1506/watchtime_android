package com.app.media.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
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