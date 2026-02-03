package com.kmobile.museointeractivo.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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

    fun onTabSelected(tab: HomeTab) {
        tabJob?.cancel()

        _uiState.update {
            it.copy(
                selectedTab = tab,
                loading = true,
                error = null,
                podcasts = emptyList(),
                videos = emptyList(),
                articles = emptyList()
            )
        }

        tabJob = viewModelScope.launch {
            val category = tab.category
            Log.d("HomeViewModel", "onTabSelected: $category")
            coroutineScope {
                val podcastsDeferred = async(Dispatchers.IO) {
                    runCatching {
                        podcastRepo.search(category)
                    }
                        .also { result ->
                            Log.d("HomeViewModel", "onTabSelected: ${result.exceptionOrNull()?.message}")
                        }
                }
                val videosDeferred = async(Dispatchers.IO) {
                    runCatching { videoRepo.search(category) }
                }
                val articlesDeferred = async(Dispatchers.IO) {
                    runCatching { articleRepo.searchArticles(category) }
                }

                val podcastsResult = podcastsDeferred.await()
                val videosResult = videosDeferred.await()
                val articlesResult = articlesDeferred.await()

                val errorGlobal =
                    podcastsResult.exceptionOrNull()?.message
                        ?: videosResult.exceptionOrNull()?.message
                        ?: articlesResult.exceptionOrNull()?.message

                _uiState.update { state ->
                    state.copy(
                        podcasts = podcastsResult.getOrNull().orEmpty(),
                        videos = videosResult.getOrNull().orEmpty(),
                        articles = articlesResult.getOrNull().orEmpty(),
                        loading = false,
                        error = errorGlobal
                    )
                }
            }
        }
    }
}

