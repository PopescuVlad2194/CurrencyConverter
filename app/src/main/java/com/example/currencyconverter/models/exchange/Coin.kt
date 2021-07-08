package com.example.currencyconverter.models.exchange

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "coins"
)
data class Coin(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val name: String,
    val value: Double
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

    var favorite: Boolean = false
}