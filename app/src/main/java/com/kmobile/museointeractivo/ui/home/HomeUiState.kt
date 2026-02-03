package com.kmobile.museointeractivo.ui.home

import com.kmobile.museointeractivo.data.remote.articles.ArticleDto
import com.kmobile.museointeractivo.data.remote.podcasts.EpisodeDto
import com.kmobile.museointeractivo.data.remote.videos.VideoDto


data class HomeUiState(
    val selectedTab: HomeTab = HomeTab.ART,
    val loading: Boolean = false,
    val error: String? = null,
    val podcasts: List<EpisodeDto> = emptyList(),
    val videos: List<VideoDto> = emptyList(),
    val articles: List<ArticleDto> = emptyList(),
)

enum class HomeTab(val category: String) {
    ART("Egypt art"),
    ARQ("Ancient Egypt Architecture"),
    VIDA("Life in Ancient Egypt")
}
