package com.kmobile.museointeractivo.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kmobile.museointeractivo.data.repository.VideoRepository
import com.kmobile.museointeractivo.ui.VideoDetailViewModel

class VideoDetailViewModelFactory(
    private val repo: VideoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoDetailViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}