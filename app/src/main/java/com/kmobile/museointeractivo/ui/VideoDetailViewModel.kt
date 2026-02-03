package com.kmobile.museointeractivo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmobile.museointeractivo.data.repository.VideoRepository
import com.kmobile.museointeractivo.data.remote.videos.VideoDto
import com.kmobile.museointeractivo.ui.common.DetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoDetailViewModel(
    private val videoRepo: VideoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailState<VideoDto>>(DetailState.Loading)
    val uiState: StateFlow<DetailState<VideoDto>> = _uiState

    fun loadVideo(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailState.Loading

            runCatching { videoRepo.getVideo(id) }
                .onSuccess { response ->
                    val video = response
                    _uiState.value = if (video.id == null) {
                        DetailState.Empty
                    } else {
                        DetailState.Success(video)
                    }
                }
                .onFailure { e ->
                    _uiState.value = DetailState.Error(e.message ?: "Error cargando video")
                }
        }
    }
}
