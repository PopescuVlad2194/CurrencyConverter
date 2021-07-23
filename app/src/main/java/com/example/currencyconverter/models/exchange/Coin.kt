package com.example.currencyconverter.models.exchange

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Coin(
    @PrimaryKey
    var name: String,
    var value: Double,
    var favorite: Boolean = false
)