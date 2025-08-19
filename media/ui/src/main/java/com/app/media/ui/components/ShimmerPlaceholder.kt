package com.app.media.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.core.ui.composables.shimmer


@Composable
fun MediaShimmerPlaceHolder() {
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
fun SeasonShimmerPlaceHolder() = Box(
    modifier = Modifier
        .height(240.dp)
        .fillMaxWidth()
        .padding(16.dp)
        .clip(MaterialTheme.shapes.medium)
        .shimmer()
)

@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .shimmer()
    )
}