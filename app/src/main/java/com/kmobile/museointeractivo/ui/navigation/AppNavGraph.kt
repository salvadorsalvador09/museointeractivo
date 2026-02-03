package com.kmobile.museointeractivo.ui.navigation

import android.os.SystemClock
import androidx.annotation.OptIn
import com.kmobile.museointeractivo.ui.home.HomeViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kmobile.museointeractivo.data.repository.ArticleRepository
import com.kmobile.museointeractivo.data.repository.PodcastRepository
import com.kmobile.museointeractivo.data.repository.VideoRepository
import com.kmobile.museointeractivo.ui.home.HomeScreen
import com.kmobile.museointeractivo.ui.screens.ArticleDetailScreen
import com.kmobile.museointeractivo.ui.screens.PodcastDetailScreen
import com.kmobile.museointeractivo.ui.screens.VideoDetailScreen
import com.kmobile.museointeractivo.ui.ArticleDetailViewModel
import com.kmobile.museointeractivo.ui.PodcastDetailViewModel
import com.kmobile.museointeractivo.ui.VideoDetailViewModel
import com.kmobile.museointeractivo.ui.state.ArticleDetailViewModelFactory
import com.kmobile.museointeractivo.ui.state.PodcastDetailViewModelFactory
import com.kmobile.museointeractivo.ui.state.VideoDetailViewModelFactory

@OptIn(UnstableApi::class)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    podcastRepo: PodcastRepository,
    videoRepo: VideoRepository,
    articleRepo: ArticleRepository,
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            val start = remember { SystemClock.elapsedRealtime() }

            LaunchedEffect(Unit) {
                Log.d("UI-Home", "HomeScreen composed in ${SystemClock.elapsedRealtime() - start}ms")
            }
            HomeScreen(
                viewModel = homeViewModel,
                onTabClick = { tab -> homeViewModel.onTabSelected(tab) },
                onPodcastClick = { id -> navController.navigate("podcast/$id") },
                onVideoClick = { id -> navController.navigate("video/$id") },
                onArticleClick = { id -> navController.navigate("article/$id") }
            )
        }

        composable("podcast/{id}") { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString("id") ?: return@composable
            val id = idStr.toLongOrNull() ?: return@composable

            val detailVm: PodcastDetailViewModel = viewModel(
                factory = PodcastDetailViewModelFactory(podcastRepo)
            )

            PodcastDetailScreen(id = id, viewModel = detailVm, onBack = { navController.popBackStack() })
        }

        composable("video/{id}") { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString("id") ?: return@composable
            val id = idStr.toIntOrNull() ?: return@composable

            val detailVm: VideoDetailViewModel = viewModel(
                factory = VideoDetailViewModelFactory(videoRepo)
            )

            VideoDetailScreen(id = id, viewModel = detailVm, onBack = { navController.popBackStack() })
        }

        composable("article/{id}") { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString("id") ?: return@composable
            val id = idStr.toIntOrNull() ?: return@composable

            val detailVm: ArticleDetailViewModel = viewModel(
                factory = ArticleDetailViewModelFactory(articleRepo)
            )

            ArticleDetailScreen(id = id, viewModel = detailVm, onBack = { navController.popBackStack() } )
        }
    }
}
