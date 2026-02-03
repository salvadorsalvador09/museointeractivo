package com.kmobile.museointeractivo.data.remote.podcasts

import retrofit2.http.GET
import retrofit2.http.Query

interface PodcastIndexApi {

    @GET("api/1.0/search/byterm")
    suspend fun searchPodcast(
        @Query("q") term: String
    ): EpisodesResponse

    @GET("api/1.0/episodes/byfeedid")
    suspend fun getFirstEpisodeByFeedId(
        @Query("id") id: Long
    ): EpisodesResponse
}
