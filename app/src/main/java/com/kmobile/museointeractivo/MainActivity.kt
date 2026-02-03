package com.kmobile.museointeractivo

import com.kmobile.museointeractivo.ui.home.HomeViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.kmobile.museointeractivo.data.repository.ArticleRepository
import com.kmobile.museointeractivo.data.repository.PodcastRepository
import com.kmobile.museointeractivo.data.repository.VideoRepository
import com.kmobile.museointeractivo.data.remote.articles.ArticleIndexClient
import com.kmobile.museointeractivo.data.remote.podcasts.PodcastIndexClient
import com.kmobile.museointeractivo.data.remote.videos.VideoIndexClient
import com.kmobile.museointeractivo.ui.home.HomeViewModelFactory
import com.kmobile.museointeractivo.ui.navigation.AppNavGraph
import com.kmobile.museointeractivo.ui.theme.MuseoInteractivoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val podcastApi = PodcastIndexClient.create()
        val podcastRepo = PodcastRepository(podcastApi)

        val videoApi = VideoIndexClient.create()
        val videoRepo = VideoRepository(videoApi)

        val articleApi = ArticleIndexClient.create()
        val articleRepo = ArticleRepository(articleApi)

        setContent {
            val navController = rememberNavController()

            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(podcastRepo, videoRepo, articleRepo)
            )
            MuseoInteractivoTheme()
            {
                AppNavGraph(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    podcastRepo = podcastRepo,
                    videoRepo = videoRepo,
                    articleRepo = articleRepo
                )
            }
        }
    }
}
