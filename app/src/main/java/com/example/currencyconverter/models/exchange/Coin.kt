package com.example.currencyconverter.models.exchange

data class Coin(
    var name: String,
    var value: Double,
    var favorite: Boolean = false
) {
    val priority: Int
        get() {
            if (favorite) {
                return 1
            }
            return 0
        }
}