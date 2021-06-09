package com.example.currencyconverter.api

import com.example.currencyconverter.models.exchange.ConverterResponse
import com.example.currencyconverter.models.exchange.ExchangeResponse
import com.example.currencyconverter.util.Constants.API_KEY_EXCHANGE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeAPI {

    @GET("{apiKey}/latest/RON")
    suspend fun getExchangeRates(
        @Path("apiKey")
        apiKey: String = API_KEY_EXCHANGE
    ): Response<ExchangeResponse>

    @GET("{apiKey}/pair/{fromCurrency}/{toCurrency}")
    suspend fun convert(
        @Path("apiKey")
        apiKey: String = API_KEY_EXCHANGE,
        @Path("fromCurrency")
        fromCurrency: String,
        @Path("toCurrency")
        toCurrency: String
    ): Response<ConverterResponse>

}