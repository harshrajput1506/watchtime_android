package com.app.popular.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun PopularScreen() {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(state = scrollState)
        ) {
            // App Bar
            UserDisplayBar()
            TitleSection(
                label = "Trending",
                options = listOf("Today", "This Week")
            )
            TitleSection(
                label = "Popular",
            )
            TitleSection(
                label = "Top Rated"
            )


            // Add some spacing at the bottom to ensure scrolling works
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun TrendingSection() {
    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Trending",
                style = MaterialTheme.typography.titleLarge,
            )

            TitleChoiceRow(
                options = listOf("Today", "This Week")
            )
        }
        // Add your trending items here
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            items(6) { index ->
                TitleCard()
            }
        }
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String = "Popular",
    options: List<String> = listOf("Movies", "TV"),
) {
    Column(
        modifier = modifier.padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                label,
                style = MaterialTheme.typography.titleLarge,
            )

            TitleChoiceRow(
                options
            )
        }
        // Add your trending items here
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            items(6) { index ->
                TitleCard()
            }
        }
    }
}

@Composable
fun TitleCard(
    modifier: Modifier = Modifier,
    imageUrl: String = "https://www.themoviedb.org/t/p/w1280/1RICxzeoNCAO5NpcRMIgg1XT6fm.jpg", // Placeholder URL
    title: String = "Jurassic World: Dominion",// Placeholder title
    height: Dp = 180.dp,
    width: Dp = 120.dp
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(width)
    ) {
        Card(
            modifier = modifier
                .width(width)
                .height(height),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TitleChoiceRow(
    options: List<String> = listOf("Movies", "TV")
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { selectedIndex = index },
                selected = index == selectedIndex,
                label = { Text(label, style = MaterialTheme.typography.labelMedium) }
            )
        }
    }

}

@Composable
fun UserDisplayBar() {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            "Hello \uD83D\uDC4B", style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            "Harsh Rajput", style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}
