package com.kmobile.museointeractivo.data.repository

import android.util.Log
import com.google.gson.Gson
import com.kmobile.museointeractivo.data.remote.videos.VideoDto
import com.kmobile.museointeractivo.data.remote.videos.VideoIndexApi

class VideoRepository (
    private  val api: VideoIndexApi
)
{
    suspend fun search(term: String): List<VideoDto> {
        return api.searchVideos(term, 10).videos
    }
    suspend fun getVideo(id: Int): VideoDto {
        Log.d("VideoRepository", "Fetching video with ID: $id")
        Log.d("VideoRepository", "API URL: ${Gson().toJson(api.getVideo(id))}")
        return api.getVideo(id)
    }
}