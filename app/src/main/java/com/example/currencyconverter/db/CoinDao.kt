package com.example.currencyconverter.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.currencyconverter.models.exchange.Coin
import com.example.currencyconverter.models.exchange.Favorite

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(coin: Favorite)

    @Query("SELECT * FROM favorites")
    fun getFavorites(): LiveData<MutableList<Favorite>>

    @Delete
    suspend fun deleteFavorite(coin: Favorite)

}