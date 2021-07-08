package com.example.currencyconverter.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyconverter.models.exchange.Coin

@Database(
    entities = [Coin::class],
    version = 1
)
abstract class CoinDatabase : RoomDatabase(){

    abstract fun getCoinDao(): CoinDao

    companion object {
        @Volatile
        private var instance: CoinDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CoinDatabase::class.java,
                "coins_db.db"
            ).build()
    }
}