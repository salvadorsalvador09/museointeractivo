package com.kmobile.museointeractivo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kmobile.museointeractivo.data.remote.podcasts.EpisodeDto
import com.kmobile.museointeractivo.ui.theme.DeepGold
import com.kmobile.museointeractivo.ui.theme.Ink
import com.kmobile.museointeractivo.ui.theme.Nile
import com.kmobile.museointeractivo.ui.theme.Papyrus

@Composable
fun PodcastCard(
    feed: EpisodeDto,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onImageClick: (String?) -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(shape)
            .border(
                width = 1.5.dp,
                color = DeepGold.copy(alpha = 0.55f),
                shape = shape
                ),
        colors = CardDefaults.cardColors(
            containerColor = Papyrus
        ),
        onClick = { onClick?.invoke() }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Papyrus)
                .padding(10.dp)
        ) {
            GenericHeroHeader(
                title = feed.title,
                imageUrl = feed.image,
                subtitle = feed.datePublishedPretty,
                onImageClick = onImageClick
            )




            feed.datePublishedPretty?.takeIf { it.isNotBlank() }?.let { date ->
                Spacer(Modifier.height(4.dp))
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Ink
                )
            }

            feed.description?.takeIf { it.isNotBlank() }?.let { desc ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = Nile.copy(alpha = 0.75f),
                    maxLines = 3
                )
            }
        }
    }
}


