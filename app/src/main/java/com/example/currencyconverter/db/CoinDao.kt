package com.example.currencyconverter.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.currencyconverter.models.exchange.Coin

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(coin: Coin)

    @Query("SELECT * FROM favorites")
    fun getFavorites(): LiveData<List<Coin>>

    @Delete
    suspend fun deleteFavorite(coin: Coin)

}