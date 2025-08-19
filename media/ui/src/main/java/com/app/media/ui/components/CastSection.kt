package com.app.media.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.core.network.util.ImageUrlBuilder
import com.app.core.ui.composables.NetworkImageLoader
import com.app.media.domain.model.CastMember


@Composable
fun CastSection(
    cast: List<CastMember>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "Cast",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(cast.take(16)) { member ->
                CastMemberCard(member)
            }
        }
    }
}

@Composable
private fun CastMemberCard(
    member: CastMember,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .aspectRatio(0.7f)
    ) {
        NetworkImageLoader(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            imageUrl = ImageUrlBuilder.buildImageUrl(
                member.profilePath,
                ImageUrlBuilder.ImageSize.W185
            ),
            contentDescription = member.name,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = member.name,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = member.character,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }

}
