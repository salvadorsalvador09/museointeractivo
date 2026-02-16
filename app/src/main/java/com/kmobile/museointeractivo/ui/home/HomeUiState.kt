package com.kmobile.museointeractivo.ui.home

import com.kmobile.museointeractivo.data.remote.articles.ArticleDto
import com.kmobile.museointeractivo.data.remote.podcasts.EpisodeDto
import com.kmobile.museointeractivo.data.remote.videos.VideoDto


data class HomeUiState(
    val selectedTab: HomeTab = HomeTab.ART,
    val loading: Boolean = false,
    val error: String? = null,

    val podcastsByTab: Map<HomeTab, List<EpisodeDto>> = emptyMap(),
    val videosByTab: Map<HomeTab, List<VideoDto>> = emptyMap(),
    val articlesByTab: Map<HomeTab, List<ArticleDto>> = emptyMap()
)

enum class HomeTab(val category: String) {
    ART("Egypt art"),
    ARQ("Ancient Egypt Architecture"),
    VIDA("Life in Ancient Egypt")
}
