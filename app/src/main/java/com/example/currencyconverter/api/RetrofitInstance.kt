package com.example.currencyconverter.api

import com.example.currencyconverter.util.Constans.BASE_URL_EXCHANGE
import com.example.currencyconverter.util.Constans.BASE_URL_NEWS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
        private val exchangeRetrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL_EXCHANGE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        private val newsRetrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL_NEWS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val newsAPI by lazy {
            newsRetrofit.create(NewsAPI::class.java)
        }

        val exchangeAPI by lazy {
            exchangeRetrofit.create(ExchangeAPI::class.java)
        }

}