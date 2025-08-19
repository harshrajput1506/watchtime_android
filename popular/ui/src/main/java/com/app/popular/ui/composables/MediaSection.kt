package com.app.popular.ui.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.core.ui.composables.MediaChoiceRow
import com.app.popular.domain.entities.Media


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MediaSection(
    modifier: Modifier = Modifier,
    label: String = "Popular",
    options: List<String> = listOf("Movies", "TV"),
    mediaList: List<Media> = emptyList(),
    selectedType: Int = 0,
    onOptionSelected: (Int) -> Unit,
    onMediaClicked: (Int, String, String?, String) -> Unit,
    isLoading: Boolean = true,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope

) {

    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                label,
                style = MaterialTheme.typography.titleLarge,
            )

            MediaChoiceRow(
                options = options,
                selectedIndex = selectedType,
                onOptionSelected = onOptionSelected
            )
        }

        // Display content based on current state
        when {
            isLoading -> {
                MediaShimmerPlaceHolder()
            }

            mediaList.isNotEmpty() -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(mediaList.size) { index ->
                        MediaCard(
                            label = label,
                            media = mediaList[index],
                            onClick = onMediaClicked,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    }
                }
            }

            else -> {
                // Placeholder for empty state
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No media available",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

