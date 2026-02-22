package com.kmobile.museointeractivo.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmobile.museointeractivo.data.repository.ArticleRepository
import com.kmobile.museointeractivo.data.repository.PodcastRepository
import com.kmobile.museointeractivo.data.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val podcastRepo: PodcastRepository,
    private val videoRepo: VideoRepository,
    private val articleRepo: ArticleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private var tabJob: Job? = null

    init {
        onTabSelected(HomeTab.ART)
    }

    fun onTabSelected(tab: HomeTab, force: Boolean = false) {
        val current = _uiState.value

        val alreadyLoaded =
            current.podcastsByTab[tab]?.isNotEmpty() == true ||
                    current.videosByTab[tab]?.isNotEmpty() == true ||
                    current.articlesByTab[tab]?.isNotEmpty() == true

        if (alreadyLoaded && !force) {
            _uiState.update { it.copy(loading = false) }
            return
        }
        tabJob?.cancel()

        _uiState.update {
            it.copy(
                selectedTab = tab,
                loading = true, // si ya hay cache, no pongas loading
                error = null
            )
        }

        // Si ya estaba cargado, no vuelvas a pedir
        tabJob = viewModelScope.launch {
            val category = tab.category

            coroutineScope {
                val podcastsDeferred = async(Dispatchers.IO) { runCatching { podcastRepo.search(category) } }
                val videosDeferred = async(Dispatchers.IO) { runCatching { videoRepo.search(category) } }
                val articlesDeferred = async(Dispatchers.IO) { runCatching { articleRepo.searchArticles(category) } }

                val podcastsResult = podcastsDeferred.await()
                val videosResult = videosDeferred.await()
                val articlesResult = articlesDeferred.await()

                val errorGlobal =
                    podcastsResult.exceptionOrNull()?.message
                        ?: videosResult.exceptionOrNull()?.message
                        ?: articlesResult.exceptionOrNull()?.message

                _uiState.update { state ->
                    state.copy(
                        podcastsByTab = state.podcastsByTab + (tab to podcastsResult.getOrNull().orEmpty()),
                        videosByTab = state.videosByTab + (tab to videosResult.getOrNull().orEmpty()),
                        articlesByTab = state.articlesByTab + (tab to articlesResult.getOrNull().orEmpty()),
                        loading = false,
                        error = errorGlobal
                    )
                }
            }
        }
    }

}

