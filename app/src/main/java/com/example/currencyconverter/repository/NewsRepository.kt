package com.example.currencyconverter.repository

import com.example.currencyconverter.api.RetrofitInstance
import com.example.currencyconverter.db.ArticleDatabase
import com.example.currencyconverter.models.news.Article

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getNewsRemotely(countryCode: String) =
        RetrofitInstance.newsAPI.getLatestNews(countryCode)

    suspend fun saveArticle(article: Article) = db.getArticlesDao().insertArticle(article)

    fun getNewsLocally() = db.getArticlesDao().getAllArticles()

    fun deleteNews() = db.getArticlesDao().deleteNews()

    suspend fun updateArticle(
        articleURL: String,
        seen: Boolean
    ) = db.getArticlesDao().updateArticle(articleURL, seen)
}