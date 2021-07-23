package com.example.currencyconverter.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.currencyconverter.models.news.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("DELETE FROM articles")
    fun deleteNews()

    @Query("UPDATE articles SET seen =:seen WHERE url =:articleURL")
    suspend fun updateArticle(articleURL: String, seen: Boolean)
}