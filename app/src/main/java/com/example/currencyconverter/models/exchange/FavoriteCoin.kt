package com.example.currencyconverter.models.exchange

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteCoin(
    @PrimaryKey
    var name: String,
    var isFavorite: Boolean
)
