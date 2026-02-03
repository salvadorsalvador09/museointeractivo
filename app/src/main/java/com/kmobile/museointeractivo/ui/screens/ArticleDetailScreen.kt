package com.kmobile.museointeractivo.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.kmobile.museointeractivo.ui.ArticleDetailViewModel
import com.kmobile.museointeractivo.ui.common.DetailState
import com.kmobile.museointeractivo.ui.components.EmptyStateCard
import com.kmobile.museointeractivo.ui.components.ErrorStateCard
import com.kmobile.museointeractivo.ui.components.GenericHeroHeader
import com.kmobile.museointeractivo.ui.gestos.edgeSwipeToBack
import com.kmobile.museointeractivo.ui.theme.Gold
import com.kmobile.museointeractivo.ui.theme.Ink
import com.kmobile.museointeractivo.ui.theme.Nile
import com.kmobile.museointeractivo.ui.theme.Papyrus
import com.kmobile.museointeractivo.ui.theme.Sand

@Composable
fun ArticleDetailScreen(
    id: Int,
    viewModel: ArticleDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(id) { viewModel.loadArticle(id) }

    Log.d("ArticleDetailScreen", "id = $id")
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
                val article = state.data

                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    GenericHeroHeader(
                        title = article.title ?: "Sin título",
                        imageUrl = null,
                        subtitle = article.tags
                            ?.mapNotNull { it.term }
                            ?.joinToString(", ")
                            ?: "Sin etiquetas"

                    )
                    Text(text = article.title ?: "Sin título")
                    Spacer(Modifier.height(8.dp))

                    if (!article.objectWikidata_URL.isNullOrBlank()) {
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
                                    text = article.objectWikidata_URL + article.title,
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

