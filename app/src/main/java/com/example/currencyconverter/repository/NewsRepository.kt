package com.example.currencyconverter.repository

import com.example.currencyconverter.api.RetrofitInstance

class NewsRepository() {
    suspend fun getBreakingNews(countryCode: String) =
        RetrofitInstance.newsAPI.getLatestNews(countryCode)

}