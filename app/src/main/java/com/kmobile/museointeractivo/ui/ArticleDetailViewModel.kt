package com.kmobile.museointeractivo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmobile.museointeractivo.data.repository.ArticleRepository
import com.kmobile.museointeractivo.data.remote.articles.ArticleDto
import com.kmobile.museointeractivo.ui.common.DetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val articleRepo: ArticleRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailState<ArticleDto>>(DetailState.Loading)
    val uiState: StateFlow<DetailState<ArticleDto>> = _uiState

    fun loadArticle(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailState.Loading

            runCatching { articleRepo.getArticle(id) }
                .onSuccess { response ->
                    val article = response
                    _uiState.value = if (article.title == null) {
                        DetailState.Empty
                    } else {
                        DetailState.Success(article)
                    }
                }
                .onFailure { e ->
                    _uiState.value = DetailState.Error(e.message ?: "Error cargando artículo")
                }
        }
    }
}