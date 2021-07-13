package com.example.currencyconverter

import com.example.currencyconverter.models.exchange.FavoriteCoin

interface TakeFavorites {

    fun takeFavorites(list: List<FavoriteCoin>) : List<String>
}