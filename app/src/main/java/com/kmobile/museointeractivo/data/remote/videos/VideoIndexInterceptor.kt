package com.kmobile.museointeractivo.data.remote.videos

import com.kmobile.museointeractivo.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class VideoIndexInterceptor() : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", BuildConfig.VIDEO_KEY)
            .build()
        return chain.proceed(request)
    }

}