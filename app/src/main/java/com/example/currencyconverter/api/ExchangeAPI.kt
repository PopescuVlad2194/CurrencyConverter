package com.example.currencyconverter.api

import com.example.currencyconverter.models.ConverterResponse
import com.example.currencyconverter.models.ExchangeResponse
import com.example.currencyconverter.util.Constans.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeAPI {

    @GET("{apiKey}/latest/EUR")
    suspend fun getExchangeRates(
        @Path("apiKey")
        apiKey: String = API_KEY
    ): Response<ExchangeResponse>

    @GET("{apiKey}/pair/{fromCurrency}/{toCurrency}")
    suspend fun convert(
        @Path("apiKey")
        apiKey: String = API_KEY,
        @Path("fromCurrency")
        fromCurrency: String = "EUR",
        @Path("toCurrency")
        toCurrency: String = "RON"
    ): Response<ConverterResponse>
}