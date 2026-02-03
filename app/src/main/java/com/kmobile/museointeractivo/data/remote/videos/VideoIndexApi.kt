package com.kmobile.museointeractivo.data.remote.videos

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoIndexApi {
    @GET("videos/search")
    suspend fun searchVideos(
        @Query("query") term: String,
        @Query("per_page") pageSize: Int
    ): VideoSearchResponse

    @GET("videos/videos/{id}")
    suspend fun getVideo(
        @Path("id") id: Int
    ): VideoDto
}