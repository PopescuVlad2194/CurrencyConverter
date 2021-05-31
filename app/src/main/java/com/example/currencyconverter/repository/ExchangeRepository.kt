package com.example.currencyconverter.repository

import com.example.currencyconverter.api.RetrofitInstance

class ExchangeRepository() {

    suspend fun getExchangeRates() = RetrofitInstance.api.getExchangeRates()
}