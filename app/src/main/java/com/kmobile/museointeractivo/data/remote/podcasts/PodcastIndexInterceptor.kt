package com.kmobile.museointeractivo.data.remote.podcasts


import android.util.Log
import com.kmobile.museointeractivo.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest
class PodcastIndexInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val time = (System.currentTimeMillis() / 1000).toString()
        val authHash = sha1(BuildConfig.PODCASTINDEX_KEY + BuildConfig.PODCASTINDEX_SECRET + time)
        Log.d("PODCAST", "Interceptor running. time=$time keyLen=${BuildConfig.PODCASTINDEX_KEY.length} secretLen=${BuildConfig.PODCASTINDEX_SECRET.length}")

        val request = chain.request().newBuilder()
            .addHeader("X-Auth-Date", time)
            .addHeader("X-Auth-Key", BuildConfig.PODCASTINDEX_KEY)
            .addHeader("Authorization", authHash)
            .addHeader("User-Agent", BuildConfig.PODCASTINDEX_UA)
            .build()

        return chain.proceed(request)
    }
    private fun sha1(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-1").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
