package com.kmobile.museointeractivo.data.repository

import android.util.Log
import com.kmobile.museointeractivo.data.remote.podcasts.EpisodeDto
import com.kmobile.museointeractivo.data.remote.podcasts.PodcastIndexApi

class PodcastRepository(
    private val api: PodcastIndexApi
) {
    suspend fun search(term: String): List<EpisodeDto> {

        return (api.searchPodcast(term).feeds ?: emptyList()) as List<EpisodeDto>
    }

    suspend fun getEpisode(id: Long): EpisodeDto? {
        Log.d("PodcastRepository", "getEpisode: $id")
        return api.getFirstEpisodeByFeedId(id).items?.firstOrNull() as EpisodeDto?
    }
}

