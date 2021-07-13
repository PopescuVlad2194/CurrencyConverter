package com.example.currencyconverter.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.currencyconverter.models.exchange.FavoriteCoin

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(coin: FavoriteCoin)

    @Query("SELECT * FROM favorites")
    fun getFavorites(): MutableLiveData<MutableList<FavoriteCoin>>

    @Delete
    suspend fun deleteFavorite(coin: FavoriteCoin)

}