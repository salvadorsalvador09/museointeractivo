package com.kmobile.museointeractivo.data.remote.articles

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleIndexApi {

        @GET("public/collection/v1/search")
        suspend fun searchArticles(
            @Query("q") term: String,
            @Query("departmentId") departmentId: Int=10,
        ): ArticleSearchResponse

        @GET("public/collection/v1/objects/{id}")
        suspend fun getArticle(
            @Path("id") id: Int,
        ): ArticleDto
}
