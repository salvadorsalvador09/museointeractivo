package com.kmobile.museointeractivo.data.remote

import androidx.media3.common.util.Log
import android.os.SystemClock
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import okhttp3.Interceptor
import okhttp3.Response

class TimingInterceptor: Interceptor {
    @OptIn(UnstableApi::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val start = SystemClock.elapsedRealtime()

        val response = chain.proceed(request)
        val took = SystemClock.elapsedRealtime() - start

        Log.d("API", "${request.method} ${request.url} took=${took}ms code=${response.code}")

        return response
    }
}