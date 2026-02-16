package com.kmobile.museointeractivo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kmobile.museointeractivo.ui.theme.DeepGold
import com.kmobile.museointeractivo.ui.theme.Desert
import com.kmobile.museointeractivo.ui.theme.Gold
import com.kmobile.museointeractivo.ui.theme.Ink
import com.kmobile.museointeractivo.ui.theme.Nile
import com.kmobile.museointeractivo.ui.theme.Papyrus


@Composable
fun GenericHeroHeader(
    title: String,
    imageUrl: String?,
    subtitle: String?,
    onImageClick: (String?) -> Unit ,
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = Papyrus),
        border = BorderStroke(1.dp, DeepGold.copy(alpha = 0.55f)),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            if (!imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clickable { onImageClick(imageUrl)}
                ) {
                    CoilImages(
                        imageUrl,
                        modifier = Modifier.matchParentSize(),
                        description = title,
                        onImageClick = { url ->
                            android.util.Log.d("IMG", "GenericHeroHeader forwarding url=$url")
                            onImageClick(url)
                        }
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Nile.copy(alpha = 0.35f)
                                    )
                                )
                            )
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Nile
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (!subtitle.isNullOrBlank()) {
                        EgyptChip(text = subtitle)
                    }
                    EgyptChip(text = "Museo Interactivo")
                }
            }
        }

    }

}
@Composable
private fun EgyptChip(text: String) {
    Surface(
        color = Gold.copy(alpha = 0.15f),
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(1.dp, Gold.copy(alpha = 0.55f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = DeepGold
        )
    }
}

@Composable
fun InfoHint(text: String) {
    Surface(
        color = Desert.copy(alpha = 0.35f),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, DeepGold.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Ink.copy(alpha = 0.85f)
        )
    }
}

@Composable
fun ErrorStateCard(message: String, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(containerColor = Papyrus),
        border = BorderStroke(1.dp, Color(0xFFB3261E).copy(alpha = 0.55f)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Error", style = MaterialTheme.typography.titleMedium, color = Color(0xFFB3261E))
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium,
                color = Ink.copy(alpha = 0.88f)
            )
        }
    }
}

@Composable
fun EmptyStateCard(text: String, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(containerColor = Papyrus),
        border = BorderStroke(1.dp, Gold.copy(alpha = 0.45f)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Ink.copy(alpha = 0.85f)
        )
    }
}


