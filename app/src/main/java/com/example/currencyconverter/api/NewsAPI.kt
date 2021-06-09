package com.example.currencyconverter.api

import com.example.currencyconverter.models.news.NewsResponse
import com.example.currencyconverter.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines/")
    suspend fun getLatestNews(
        @Query("country")
        countryCode: String = "ro",
        @Query("apiKey")
        apiKey: String = Constants.API_KEY_NEWS
    ): Response<NewsResponse>
}