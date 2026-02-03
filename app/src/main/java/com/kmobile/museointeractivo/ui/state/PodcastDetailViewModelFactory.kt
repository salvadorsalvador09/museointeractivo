package com.kmobile.museointeractivo.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kmobile.museointeractivo.data.repository.PodcastRepository
import com.kmobile.museointeractivo.ui.PodcastDetailViewModel

class PodcastDetailViewModelFactory(
    private val podcastRepo: PodcastRepository
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PodcastDetailViewModel::class.java)) {
            return PodcastDetailViewModel(podcastRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}