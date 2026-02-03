package com.kmobile.museointeractivo.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmobile.museointeractivo.data.remote.podcasts.EpisodeDto
import com.kmobile.museointeractivo.data.repository.PodcastRepository
import com.kmobile.museointeractivo.ui.common.DetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PodcastDetailViewModel(
    private val podcastRepo: PodcastRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailState<EpisodeDto>>(DetailState.Loading)
    val uiState: StateFlow<DetailState<EpisodeDto>> = _uiState

    fun loadPodcast(id: Long) = viewModelScope.launch {
        _uiState.value = DetailState.Loading
        Log.d("PodcastDetailViewModel", "loadPodcast: $id")
        runCatching { podcastRepo.getEpisode(id) }
            .onSuccess { response ->
                val feed = response
                _uiState.value = if (feed == null) {
                    DetailState.Empty
                } else {
                    DetailState.Success(feed)
                }
            }
            .also {
                Log.d("PodcastDetailViewModel", "loadPodcast: ${it.exceptionOrNull()?.message}")
            }
            .onFailure { e ->
                _uiState.value = DetailState.Error(e.message ?: "Error cargando podcast")
            }
    }
}
