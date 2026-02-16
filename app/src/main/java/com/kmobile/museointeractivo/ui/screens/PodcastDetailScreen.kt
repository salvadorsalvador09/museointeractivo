package com.kmobile.museointeractivo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kmobile.museointeractivo.ui.PodcastDetailViewModel
import com.kmobile.museointeractivo.ui.common.DetailState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.sp
import com.kmobile.museointeractivo.ui.components.PodcastPlayerCard
import com.kmobile.museointeractivo.ui.theme.Gold
import com.kmobile.museointeractivo.ui.theme.Ink
import com.kmobile.museointeractivo.ui.theme.Nile
import com.kmobile.museointeractivo.ui.theme.Papyrus
import com.kmobile.museointeractivo.ui.theme.Sand
import com.kmobile.museointeractivo.ui.components.EmptyStateCard
import com.kmobile.museointeractivo.ui.components.ErrorStateCard
import com.kmobile.museointeractivo.ui.components.GenericHeroHeader
import com.kmobile.museointeractivo.ui.components.InfoHint
import com.kmobile.museointeractivo.ui.gestos.edgeSwipeToBack

@Composable
fun PodcastDetailScreen(
    id: Long,
    viewModel: PodcastDetailViewModel,
    onBack: ()-> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(id) { viewModel.loadPodcast(id) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .edgeSwipeToBack { onBack() }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Papyrus, Sand)
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        when (val state = uiState) {
            is DetailState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Gold
                )
            }


            is DetailState.Error -> {
                ErrorStateCard(
                    message = state.message ?: "Ocurrió un error",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            is DetailState.Empty -> {
                EmptyStateCard(
                    text = "No se encontró información del podcast.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            is DetailState.Success -> {
                val feed = state.data
                val audioUrl = feed.enclosureUrl

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    var viewUrl by remember{ mutableStateOf<String?>(null) }

                    GenericHeroHeader(
                        title = feed.title ?: "Sin título",
                        imageUrl = feed.image,
                        subtitle = feed.datePublishedPretty,
                        onImageClick = {url ->
                            viewUrl = url
                        }
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
                                color = Nile
                            )

                            if (!audioUrl.isNullOrBlank()) {
                                PodcastPlayerCard(
                                    audioUrl = audioUrl,
                                    imageUrl = feed.image,
                                )
                            } else {
                                InfoHint(text = "No hay URL de audio disponible para reproducir.")
                            }
                        }
                    }

                    if (!feed.description.isNullOrBlank()) {
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
                                    color = Nile
                                )
                                Text(
                                    text = feed.description,
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

