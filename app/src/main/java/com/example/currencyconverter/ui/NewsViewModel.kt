package com.example.currencyconverter.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.models.news.Article
import com.example.currencyconverter.models.news.NewsResponse
import com.example.currencyconverter.repository.NewsRepository
import com.example.currencyconverter.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val latestNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val storageNews: MutableLiveData<List<Article>> = MutableLiveData()

    init {
        getNewsRemotely("ro")
    }

    private fun getNewsRemotely(countryCode: String) = viewModelScope.launch {
        latestNews.postValue(Resource.Loading())
        val response = newsRepository.getNewsRemotely(countryCode)
        latestNews.postValue(handleLatestNewsResponse(response))
    }

    private fun handleLatestNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.saveArticle(article)
    }

    fun getNewsLocally() = newsRepository.getNewsLocally()

    fun updateArticle(articleURL: String, seen: Boolean) = viewModelScope.launch {
        newsRepository.updateArticle(articleURL, seen)
    }

}





