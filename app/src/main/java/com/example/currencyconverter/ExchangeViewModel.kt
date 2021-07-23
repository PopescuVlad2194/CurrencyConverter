package com.example.currencyconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.models.exchange.Coin
import com.example.currencyconverter.models.exchange.ConverterResponse
import com.example.currencyconverter.models.exchange.ExchangeResponse
import com.example.currencyconverter.repository.ExchangeRepository
import com.example.currencyconverter.util.Constants.EXCHANGE_COIN_REFERENCE
import com.example.currencyconverter.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ExchangeViewModel(
    private val exchangeRepository: ExchangeRepository
) : ViewModel() {
    private val _coins: MutableLiveData<Resource<MutableList<Coin>>> = MutableLiveData()
    val coins: LiveData<Resource<MutableList<Coin>>> = _coins
    val converter: MutableLiveData<Resource<String>> = MutableLiveData()
    val favoriteListConverter : MutableLiveData<MutableList<Coin>> = MutableLiveData()

    init {
        getExchangeRates()
    }

    private fun getExchangeRates() = viewModelScope.launch {
        _coins.postValue(Resource.Loading())
        val response = exchangeRepository.getExchangeRates()
        _coins.postValue(handleExchangeResponse(response))
    }

    fun convert(selectedCoin: String, desiredCoin: String) = viewModelScope.launch {
        converter.postValue(Resource.Loading())
        val response = exchangeRepository.convert(selectedCoin, desiredCoin)
        converter.postValue(handleConvertResponse(response))
    }

    private fun handleConvertResponse(response: Response<ConverterResponse>): Resource<String> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it.conversion_rate.toString())
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleExchangeResponse(response: Response<ExchangeResponse>) : Resource<MutableList<Coin>> {
        if(response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(extractExchangeRates(it))
            }
        }
        return Resource.Error(response.message())
    }

    private fun extractExchangeRates(response: ExchangeResponse) : MutableList<Coin> {
        val list: MutableList<Coin> = arrayListOf()

        for(item in response.conversion_rates) {
            if(item.key != EXCHANGE_COIN_REFERENCE) {
                list.add(Coin(item.key, item.value, false))
            }
        }

        val sortedList = sortListByFavorites(list)
        return sortedList as MutableList<Coin>
    }

    private fun sortListByFavorites(list: MutableList<Coin>): List<Coin> {
        return list.sortedWith(
            compareByDescending<Coin> { it.favorite }.thenBy { it.name }
        )
    }

    fun updateFavoriteListConverter(list: List<Coin>) {
        favoriteListConverter.postValue(list as MutableList<Coin>)
    }

    fun sortListByFavorites(
        list: MutableList<Coin>,
        favorites: List<Coin>
    ) : List<Coin> {
        for (element in list) {
            for (favoriteElement in favorites) {
                if (element.name == favoriteElement.name) {
                    element.favorite = favoriteElement.favorite
                }
            }
        }
        return list.sortedWith(
            compareByDescending<Coin> { it.favorite }.thenBy { it.name }
        )
    }

    fun updateCoinStatus(favoriteCoin: Coin) {
        val currentCoins = _coins.value?.data!!
        for (element in currentCoins) {
            if (element.name == favoriteCoin.name) {
                element.favorite = favoriteCoin.favorite
            }
        }
    }

    fun addFavorite(coin: Coin) = viewModelScope.launch {
        exchangeRepository.addFavorite(coin)
    }

    fun getFavorites() = exchangeRepository.getFavorites()

    fun deleteFavorite(coin: Coin) = viewModelScope.launch {
        exchangeRepository.deleteFavorite(coin)
    }
}