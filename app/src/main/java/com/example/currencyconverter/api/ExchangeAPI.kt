package com.example.currencyconverter.api

import com.example.currencyconverter.models.exchange.ConverterResponse
import com.example.currencyconverter.models.exchange.ExchangeResponse
import com.example.currencyconverter.util.Constants.API_KEY_EXCHANGE
import com.example.currencyconverter.util.Constants.EXCHANGE_COIN_REFERENCE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeAPI {

    @GET("{apiKey}/latest/{coinReference}")
    suspend fun getExchangeRates(
        @Path("apiKey")
        apiKey: String = API_KEY_EXCHANGE,
        @Path("coinReference")
        coinReference: String = EXCHANGE_COIN_REFERENCE
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