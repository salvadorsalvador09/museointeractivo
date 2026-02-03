package com.kmobile.museointeractivo.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmobile.museointeractivo.ui.VideoDetailViewModel
import com.kmobile.museointeractivo.ui.common.DetailState
import com.kmobile.museointeractivo.ui.components.EgyptVideoPlayer
import com.kmobile.museointeractivo.ui.components.EmptyStateCard
import com.kmobile.museointeractivo.ui.components.ErrorStateCard
import com.kmobile.museointeractivo.ui.components.InfoHint
import com.kmobile.museointeractivo.ui.components.GenericHeroHeader
import com.kmobile.museointeractivo.ui.gestos.edgeSwipeToBack
import com.kmobile.museointeractivo.ui.theme.Gold
import com.kmobile.museointeractivo.ui.theme.Ink
import com.kmobile.museointeractivo.ui.theme.Papyrus
import com.kmobile.museointeractivo.ui.theme.Sand

@Composable
fun VideoDetailScreen(
    id: Int,
    viewModel: VideoDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Log.d("VideoDetailScreen", "id: $id")
    LaunchedEffect(id) {
        viewModel.loadVideo(id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .edgeSwipeToBack { onBack() }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Papyrus, Sand)
                )
            )
    ) {
        when (val state = uiState) {
            is DetailState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Gold
                )
            }

            is DetailState.Error -> {
                Log.d("VideoDetailScreen", "Error: ${state.message}")
                ErrorStateCard(
                    message = state.message ?: "Ocurrió un error",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            is DetailState.Empty -> {
                EmptyStateCard(
                    text = "No se encontró información del video.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            is DetailState.Success -> {
                val video = state.data
                val videourl = video.url

                Column(modifier = Modifier.padding(16.dp)) {
                    GenericHeroHeader(
                        title = video.user.name ?: "Sin título",
                        imageUrl = video.image,
                        subtitle = video.user.name ?: "Sin subtítulo"
                    )

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(containerColor = Sand),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Reproductor",
                                style = MaterialTheme.typography.titleMedium,
                                color = Sand
                            )

                            if (!videourl.isNullOrBlank()) {
                                EgyptVideoPlayer(
                                    url = videourl,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16 / 9f)
                                )

                            } else {
                                InfoHint(text = "No hay URL de audio disponible para reproducir.")
                            }
                        }
                    }

                    if (!video.user.name.isNullOrBlank()) {
                        OutlinedCard(
                            colors = CardDefaults.outlinedCardColors(containerColor = Papyrus),
                            border = BorderStroke(1.dp, Gold.copy(alpha = 0.55f)),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Descripción",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Sand
                                )
                                Text(
                                    text = video.user.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Ink.copy(alpha = 0.88f),
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}


