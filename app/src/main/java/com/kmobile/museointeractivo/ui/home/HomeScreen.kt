package com.kmobile.museointeractivo.ui.home

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kmobile.museointeractivo.data.repository.ArticleRepository
import com.kmobile.museointeractivo.data.repository.PodcastRepository
import com.kmobile.museointeractivo.data.repository.VideoRepository
import com.kmobile.museointeractivo.ui.components.ArticleCard
import com.kmobile.museointeractivo.ui.components.PodcastCard
import com.kmobile.museointeractivo.data.remote.articles.ArticleDto
import com.kmobile.museointeractivo.data.remote.podcasts.EpisodeDto
import com.kmobile.museointeractivo.data.remote.videos.VideoDto
import com.kmobile.museointeractivo.ui.components.VideoCard
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import com.kmobile.museointeractivo.ui.components.EgyptSectionHeader
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onTabClick: (HomeTab) -> Unit,
    onPodcastClick: (Long) -> Unit,
    onVideoClick: (Int) -> Unit,
    onArticleClick: (Int) -> Unit,
    onImageClick: (String?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = uiState.selectedTab.ordinal,
        pageCount = { HomeTab.entries.size }
    )

    val tab = HomeTab.entries[pagerState.currentPage]

    LaunchedEffect(pagerState.currentPage) {
        val tab = HomeTab.entries[pagerState.currentPage]
        viewModel.onTabSelected(tab)
        onTabClick(tab)
    }

    Scaffold(
        topBar = {
            EgyptTabs(
                selectedTab = HomeTab.entries[pagerState.currentPage],
                onTabClick = { tab ->
                    scope.launch { pagerState.animateScrollToPage(tab.ordinal) }
                }
            )
        }
    ) { padding ->

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(padding)
        ) { page ->
            val pageTab = HomeTab.entries[page]

            val pagePodcasts = uiState.podcastsByTab[pageTab].orEmpty()
            val pageVideos = uiState.videosByTab[pageTab].orEmpty()
            val pageArticles = uiState.articlesByTab[pageTab].orEmpty()

            val pageHasData = pagePodcasts.isNotEmpty() || pageVideos.isNotEmpty() || pageArticles.isNotEmpty()
            val pageShowLoader = uiState.loading && !pageHasData && pageTab == HomeTab.entries[pagerState.currentPage]

            if (pageShowLoader) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            } else {
                HomeContent(
                    podcastsPreview = pagePodcasts.take(10),
                    videosPreview = pageVideos.take(10),
                    articlesPreview = pageArticles.take(10),
                    onPodcastClick = onPodcastClick,
                    onVideoClick = onVideoClick,
                    onArticleClick = onArticleClick,
                    onImageClick = onImageClick
                )
            }
        }

    }
}


@Composable
private fun HomeContent(
    podcastsPreview: List<EpisodeDto>,
    videosPreview: List<VideoDto>,
    articlesPreview: List<ArticleDto>,
    onPodcastClick: (Long) -> Unit,
    onVideoClick: (Int) -> Unit,
    onArticleClick: (Int) -> Unit,
    onImageClick: (String?) -> Unit,
) {
    LazyColumn(
        verticalArrangement = spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {

        item { EgyptSectionHeader("Podcasts") }
        items(podcastsPreview, key = { it.id }) { podcast ->
            PodcastCard(
                feed = podcast,
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { onPodcastClick(podcast.id) },
                onImageClick = { onImageClick(podcast.image) }
            )
        }

        item { EgyptSectionHeader("Videos") }
        items(videosPreview, key = { it.id }) { video ->
            VideoCard(
                video = video,
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { onVideoClick(video.id) },
                onImageClick = { onImageClick(video.image) }
            )
        }

        item { EgyptSectionHeader("Artículos") }
        items(articlesPreview, key = { it.objectID }) { article ->
            ArticleCard(
                article = article,
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { onArticleClick(article.objectID) },
                onImageClick = {onImageClick(article.primaryImage)}

            )
        }


    }
}


class HomeViewModelFactory(
    private val podcastRepo: PodcastRepository,
    private val videoRepo: VideoRepository,
    private val articleRepo: ArticleRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(podcastRepo, videoRepo, articleRepo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun EgyptTabs(
    selectedTab: HomeTab,
    onTabClick: (HomeTab) -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                height = 3.dp
            )
        }
    ) {
        HomeTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            Tab(
                selected = selected,
                onClick = { onTabClick(tab) },
                text = {
                    Text(
                        text = when (tab) {
                            HomeTab.ART -> "Art"
                            HomeTab.VIDA -> "Life"
                            HomeTab.ARQ -> "Architecture"
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            )
        }
    }
}
