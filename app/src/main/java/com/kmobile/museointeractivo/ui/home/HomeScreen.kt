package com.kmobile.museointeractivo.ui.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.kmobile.museointeractivo.data.remote.articles.ArticleDto
import com.kmobile.museointeractivo.data.remote.podcasts.EpisodeDto
import com.kmobile.museointeractivo.data.remote.videos.VideoDto
import com.kmobile.museointeractivo.ui.components.VideoCard

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onTabClick: (HomeTab) -> Unit,
    onPodcastClick: (Long) -> Unit,
    onVideoClick: (Int) -> Unit,
    onArticleClick: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { msg ->
            snackbarHostState.showSnackbar(message = msg)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onTabSelected(HomeTab.ART)
    }

    val podcastsPreview = remember(uiState.podcasts) { uiState.podcasts.take(5) }
    val videosPreview = remember(uiState.videos) { uiState.videos.take(5) }
    val articlesPreview = remember(uiState.articles) { uiState.articles.take(5) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Box(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                EgyptTabs(
                    selectedTab = uiState.selectedTab,
                    onTabClick = { tab ->
                        viewModel.onTabSelected(tab)
                        onTabClick(tab)
                    }
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .padding(vertical = 20.dp)
        ) {


            if (uiState.loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = true) { },
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else{
                HomeContent(
                            podcastsPreview,
                            videosPreview,
                            articlesPreview,
                            onPodcastClick,
                            onVideoClick,
                            onArticleClick
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
    ){
    LazyColumn(
        verticalArrangement = spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { Text("Podcasts") }
        items(podcastsPreview, key = { it.id }) { podcast ->
            PodcastCard(
                feed = podcast,
                onClick = { onPodcastClick(podcast.id) }
            )
        }

        item { Text("Videos") }
        items(videosPreview, key = { it.id }) { video ->
            VideoCard(
                video = video,
                onClick = { onVideoClick(video.id) }
            )
        }

        item { Text("Artículos") }
        items(articlesPreview, key = { it.objectID }) { article ->
            ArticleCard(
                article = article,
                onClick = { onArticleClick(article.objectID) }
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
