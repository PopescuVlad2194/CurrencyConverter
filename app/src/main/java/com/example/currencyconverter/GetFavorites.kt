package com.example.currencyconverter

import com.example.currencyconverter.models.exchange.Favorite

interface TakeFavorites {

    fun takeFavorites(list: List<Favorite>) : List<String>
}