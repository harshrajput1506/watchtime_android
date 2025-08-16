package com.app.popular.ui.composables

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.core.ui.composables.MediaChoiceRow
import com.app.popular.domain.entities.Media


@Composable
fun MediaSection(
    modifier: Modifier = Modifier,
    label: String = "Popular",
    options: List<String> = listOf("Movies", "TV"),
    mediaList1: List<Media> = emptyList(),
    mediaList2: List<Media> = emptyList(),
    onMediaClicked: (Int, String) -> Unit,
    isOption1Loading: Boolean = true,
    isOption2Loading: Boolean = true
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    // Determine current media list and loading state based on selection
    val currentMediaList = if (selectedIndex == 0) mediaList1 else mediaList2
    val isCurrentLoading = if (selectedIndex == 0) isOption1Loading else isOption2Loading

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
                selectedIndex = selectedIndex,
                onOptionSelected = { index ->
                    selectedIndex = index
                }
            )
        }

        // Display content based on current state
        when {
            isCurrentLoading -> {
                MediaShimmerPlaceHolder()
            }

            currentMediaList.isNotEmpty() -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(currentMediaList.size) { index ->
                        MediaCard(
                            media = currentMediaList[index],
                            onClick = onMediaClicked
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

