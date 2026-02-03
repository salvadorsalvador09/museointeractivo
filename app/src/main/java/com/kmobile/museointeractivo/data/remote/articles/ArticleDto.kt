package com.kmobile.museointeractivo.data.remote.articles

import com.google.gson.annotations.SerializedName

data class ArticleSearchResponse(
    val objectIDs: List<Int>,
    val total: Int
)
data class ArticleDto(
    @SerializedName("GalleryNumber") val galleryNumber: String,
    val accessionNumber: String?,
    val accessionYear: String?,
    val additionalImages: List<String?>,
    val artistAlphaSort: String?,
    val artistBeginDate: String?,
    val artistDisplayBio: String?,
    val artistDisplayName: String?,
    val artistEndDate: String?,
    val artistGender: String?,
    val artistNationality: String?,
    val artistPrefix: String?,
    val artistRole: String?,
    val artistSuffix: String?,
    val artistULAN_URL: String?,
    val artistWikidata_URL: String?,
    val city: String?,
    val classification: String?,
    val constituents: List<Constituent?>,
    val country: String?,
    val county: String?,
    val creditLine: String?,
    val culture: String?,
    val department: String?,
    val dimensions: String?,
    val dynasty: String?,
    val excavation: String?,
    val geographyType: String?,
    val isHighlight: Boolean?,
    val isPublicDomain: Boolean?,
    val isTimelineWork: Boolean?,
    val linkResource: String?,
    val locale: String?,
    val locus: String?,
    val measurements: List<Measurement>?,
    val medium: String?,
    val metadataDate: String?,
    val objectBeginDate: Int?,
    val objectDate: String?,
    val objectEndDate: Int?,
    val objectID: Int,
    val objectName: String?,
    val objectURL: String?,
    val objectWikidata_URL: String?,
    val period: String?,
    val portfolio: String?,
    val primaryImage: String?,
    val primaryImageSmall: String?,
    val region: String?,
    val reign: String?,
    val repository: String?,
    val rightsAndReproduction: String?,
    val river: String?,
    val state: String?,
    val subregion: String?,
    val tags: List<Tag>?,
    val title: String?
)

data class Constituent(
    val constituentID: Int?,
    val constituentULAN_URL: String?,
    val constituentWikidata_URL: String?,
    val gender: String?,
    val name: String?,
    val role: String?
)

data class Measurement(
    val elementDescription: String,
    val elementMeasurements: ElementMeasurements,
    val elementName: String
)

data class Tag(
    val AAT_URL: String?,
    val Wikidata_URL: String?,
    val term: String?
)

data class ElementMeasurements(
    val Height: Double?,
    val Width: Double?
)