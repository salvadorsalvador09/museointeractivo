package com.kmobile.museointeractivo.data.remote.podcasts

import com.kmobile.museointeractivo.data.remote.TimingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PodcastIndexClient {

    fun create(
    ): PodcastIndexApi {

        val client = OkHttpClient.Builder()
            .addInterceptor(
                PodcastIndexInterceptor()
            )
            .addInterceptor(TimingInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.podcastindex.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PodcastIndexApi::class.java)
    }
}
