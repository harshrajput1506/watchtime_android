package com.app.core.home

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    selected: HomeDestination,
    onItemClick: (HomeDestination) -> Unit = {},
) {
    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = selected::class == HomeDestination.Popular::class,
            onClick = {
                onItemClick(HomeDestination.Popular)
            },
            label = { Text("Popular") },
            icon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_popular),
                    contentDescription = "Popular"
                )
            }
        )

        NavigationBarItem(
            selected = selected::class == HomeDestination.Discover::class,
            onClick = { onItemClick(HomeDestination.Discover) },
            label = { Text("Discover") },
            icon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_discover),
                    contentDescription = "Discover"
                )
            }
        )
        NavigationBarItem(
            selected = selected::class == HomeDestination.Collections::class,
            onClick = { onItemClick(HomeDestination.Collections) },
            label = { Text("Collections") },
            icon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_collection),
                    contentDescription = "Collections"
                )
            }
        )
        NavigationBarItem(
            selected = selected::class == HomeDestination.Profile::class,
            onClick = { onItemClick(HomeDestination.Profile) },
            label = { Text("Profile") },
            icon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = "Profile"
                )
            }
        )
    }
}