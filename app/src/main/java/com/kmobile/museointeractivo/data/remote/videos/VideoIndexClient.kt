package com.kmobile.museointeractivo.data.remote.videos

import com.kmobile.museointeractivo.data.remote.TimingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VideoIndexClient {
    fun create (): VideoIndexApi{
        val client = OkHttpClient.Builder()
            .addInterceptor(
                VideoIndexInterceptor()
            )
            .addInterceptor(TimingInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.pexels.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VideoIndexApi::class.java)

    }
}