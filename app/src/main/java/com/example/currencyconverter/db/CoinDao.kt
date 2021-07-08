package com.example.currencyconverter.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.currencyconverter.models.exchange.Coin

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(coin: Coin): Long

    @Query("SELECT * FROM coins WHERE favorite = 'true'")
    fun getFavoriteCoins(): LiveData<List<Coin>>

    @Delete
    suspend fun deleteFavoriteCoin(coin: Coin)

}