package com.example.currencyconverter.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.currencyconverter.models.news.Article

@Dao
interface ArticleDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertArticle(article: Article): Article

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

}