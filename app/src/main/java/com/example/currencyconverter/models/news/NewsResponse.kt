package com.example.currencyconverter.models.news

import com.example.currencyconverter.models.news.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)