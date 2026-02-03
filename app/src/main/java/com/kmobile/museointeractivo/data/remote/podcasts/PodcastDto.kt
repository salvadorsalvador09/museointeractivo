package com.kmobile.museointeractivo.data.remote.podcasts

import org.intellij.lang.annotations.Language


data class PodcastSearchResponse(
    val status: String?,
    val feeds: List<FeedDto> = emptyList(),
    val count: Int? = null,
    val query: String? = null,
    val description: String? = null
)

data class FeedDto(
    val id: Long,
    val title: String?,
    val description: String?,
    val url: String,
    val image: String?,
    val author: String?,
    val podcastGuid: String,
    val language: String?
)

data class EpisodesResponse(
    val status: String?,
    val count: Int? = null,
    val description: String? = null,
    val query: String? = null,
    val feeds: List<EpisodeDto>? = null,
    val items: List<EpisodeDto>? = null
)


data class EpisodeDto(
    val chaptersUrl: Any,
    val dateCrawled: Int,
    val datePublished: Int,
    val datePublishedPretty: String,
    val description: String,
    val duration: Int,
    val enclosureLength: Int,
    val enclosureType: String,
    val enclosureUrl: String,
    val episode: Any,
    val episodeType: String,
    val feedDead: Int,
    val feedDuplicateOf: Any,
    val feedId: Int,
    val feedImage: String,
    val feedItunesId: Int,
    val feedLanguage: String,
    val feedUrl: String,
    val guid: String,
    val id: Long,
    val image: String,
    val link: String,
    val podcastGuid: String,
    val season: Int,
    val title: String,
    val transcriptUrl: Any
)




