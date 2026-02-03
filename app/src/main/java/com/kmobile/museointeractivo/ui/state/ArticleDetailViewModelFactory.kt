package com.kmobile.museointeractivo.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kmobile.museointeractivo.data.repository.ArticleRepository
import com.kmobile.museointeractivo.ui.ArticleDetailViewModel

class ArticleDetailViewModelFactory (
    private val articleRepo: ArticleRepository
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleDetailViewModel::class.java)) {
            return ArticleDetailViewModel(articleRepo) as T
    }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}