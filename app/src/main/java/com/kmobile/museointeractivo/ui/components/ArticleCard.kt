package com.kmobile.museointeractivo.ui.components

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import android.util.Log
import androidx.media3.common.util.UnstableApi
import com.kmobile.museointeractivo.data.remote.articles.ArticleDto
import com.kmobile.museointeractivo.ui.theme.DeepGold
import com.kmobile.museointeractivo.ui.theme.Ink
import com.kmobile.museointeractivo.ui.theme.Nile
import com.kmobile.museointeractivo.ui.theme.Papyrus

@OptIn(UnstableApi::class)
@Composable
fun ArticleCard(
    article: ArticleDto,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onImageClick: (String?) -> Unit,
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
        colors = CardDefaults.cardColors(containerColor = Papyrus),
        //onClick = { onClick?.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Papyrus)
                .padding(10.dp)
                .clickable {
                    Log.d("IMG", "FORCED click on header area")
                    onImageClick(article.primaryImageSmall)
                }
        ) {
            GenericHeroHeader(
                title = article.title ?: "Sin título",
                imageUrl = article.primaryImageSmall,
                subtitle = article.artistDisplayName ?: "Sin subtítulo",
                onImageClick = { url ->
                    Log.d("IMG-tap", "ArticleCard image click url=$url  primary=${article.primaryImage} small=${article.primaryImageSmall}")
                    onImageClick(url)
                }
            )

            Text(
                text = article.title ?: "Sin título",
                style = MaterialTheme.typography.titleMedium
            )

            if (!article.artistDisplayName.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = article.artistDisplayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Ink
                )
            }

            if (!article.artistSuffix.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = article.artistSuffix,
                    style = MaterialTheme.typography.bodySmall,
                    color = Nile.copy(alpha = 0.75f),
                    maxLines = 3
                )
            }
        }
    }
}
