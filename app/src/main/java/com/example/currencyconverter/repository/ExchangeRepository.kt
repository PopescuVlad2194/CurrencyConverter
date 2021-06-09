package com.example.currencyconverter.repository

import com.example.currencyconverter.api.RetrofitInstance
import com.example.currencyconverter.util.Constants

class ExchangeRepository() {

    suspend fun getExchangeRates() = RetrofitInstance.exchangeAPI.getExchangeRates()

    suspend fun convert(selectedCoin: String, desiredCoin: String) =
        RetrofitInstance.exchangeAPI.convert(
            Constants.API_KEY_EXCHANGE,
            selectedCoin,
            desiredCoin
    )
}