package com.kmobile.museointeractivo.data.remote.articles

import com.kmobile.museointeractivo.data.remote.TimingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ArticleIndexClient {

    fun create(): ArticleIndexApi{
        val client = OkHttpClient.Builder()
            .addInterceptor(TimingInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl("https://collectionapi.metmuseum.org")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArticleIndexApi::class.java)
    }

}