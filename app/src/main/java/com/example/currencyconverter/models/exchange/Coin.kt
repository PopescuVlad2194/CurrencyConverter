package com.example.currencyconverter.models.exchange

data class Coin(
    var name: String,
    var value: Double,
    var favorite: Boolean = false
) {
    var priority: Int = 0
        get() {
            priority = when(name) {
                "RON" -> 3
                "EUR" -> 2
                "USD" -> 1
                else -> 0
            }
            return field
        }
}