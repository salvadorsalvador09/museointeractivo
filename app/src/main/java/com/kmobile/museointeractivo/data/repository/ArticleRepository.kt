package com.kmobile.museointeractivo.data.repository

import com.kmobile.museointeractivo.data.remote.articles.ArticleDto
import com.kmobile.museointeractivo.data.remote.articles.ArticleIndexApi

class ArticleRepository(private val api: ArticleIndexApi) {
    suspend fun searchArticles(term: String): List<ArticleDto> {
        val objectsId = api.searchArticles(term).objectIDs
        val articles = mutableListOf<ArticleDto>()

        objectsId
            .take(10)
            .forEach { id ->
                val article = api.getArticle(id)
                articles.add(article)
            }


        return articles
    }

    suspend fun getArticle(id: Int): ArticleDto {
        return api.getArticle(id)
    }

}