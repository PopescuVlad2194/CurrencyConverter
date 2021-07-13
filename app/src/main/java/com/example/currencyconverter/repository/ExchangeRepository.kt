package com.example.currencyconverter.repository

import com.example.currencyconverter.api.RetrofitInstance
import com.example.currencyconverter.db.CoinDatabase
import com.example.currencyconverter.models.exchange.FavoriteCoin
import com.example.currencyconverter.util.Constants

class ExchangeRepository(
    val db: CoinDatabase
) {

    suspend fun getExchangeRates() = RetrofitInstance.exchangeAPI.getExchangeRates()

    suspend fun convert(selectedCoin: String, desiredCoin: String) =
        RetrofitInstance.exchangeAPI.convert(
            Constants.API_KEY_EXCHANGE,
            selectedCoin,
            desiredCoin
    )

    suspend fun addFavorite(coin: FavoriteCoin) = db.getCoinDao().addFavorite(coin)

    fun getFavorites() = db.getCoinDao().getFavorites()

    suspend fun deleteFavorite(coin: FavoriteCoin) = db.getCoinDao().deleteFavorite(coin)
}