package com.example.currencyconverter.repository

import com.example.currencyconverter.api.RetrofitInstance
import com.example.currencyconverter.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String) =
        RetrofitInstance.newsAPI.getLatestNews(countryCode)

}