package com.kmobile.museointeractivo.data.remote.videos

import com.google.gson.annotations.SerializedName

data class VideoSearchResponse(
    @SerializedName("next_page") val nextPage: String,
    val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("total_results") val totalResults: Int,
    val url: String,
    val videos: List<VideoDto>
)

data class VideoDto(
    @SerializedName("avg_color") val avgColor: Any,
    val duration: Int,
    @SerializedName("full_res") val fullRes: Any,
    val height: Int,
    val id: Int,
    val image: String,
    val tags: List<Any>,
    val url: String,
    val user: User,
    @SerializedName("video_files") val videoFiles: List<VideoFile>,
    @SerializedName("video_pictures") val videoPictures: List<VideoPicture>,
    val width: Int
)


data class VideoPicture(
    val id: Int,
    val nr: Int,
    val picture: String
)



data class User(
    val id: Int,
    val name: String,
    val url: String
)


data class VideoFile(
    @SerializedName("file_type") val fileType: String,
    val fps: Double,
    val height: Int,
    val id: Int,
    val link: String,
    val quality: String,
    val size: Int,
    val width: Int
)
