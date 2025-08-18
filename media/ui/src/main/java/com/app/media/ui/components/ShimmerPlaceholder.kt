package com.app.media.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.core.ui.composables.shimmer
import com.app.media.ui.R


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


@Composable
fun ShimmerPoster(
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxHeight(0.8f)
            .aspectRatio(0.65f),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            4.dp
        )
    ) {
        if (!isError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer()
            )
        } else {
            Box(
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
        }
    }
}
