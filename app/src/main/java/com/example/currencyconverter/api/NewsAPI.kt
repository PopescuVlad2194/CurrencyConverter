package com.example.currencyconverter.api

import com.example.currencyconverter.models.NewsResponse
import com.example.currencyconverter.util.Constans
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines/")
    suspend fun getLatestNews(
        @Query("country")
        countryCode: String = "ro",
        @Query("apiKey")
        apiKey: String = Constans.API_KEY_NEWS
    ): Response<NewsResponse>
}