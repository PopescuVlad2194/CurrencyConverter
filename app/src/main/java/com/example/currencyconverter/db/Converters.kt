package com.example.currencyconverter.db

import androidx.room.TypeConverter
import com.example.currencyconverter.models.news.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}