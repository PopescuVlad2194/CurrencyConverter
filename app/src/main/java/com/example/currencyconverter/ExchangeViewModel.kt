package com.example.currencyconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.models.exchange.Coin
import com.example.currencyconverter.models.exchange.ConverterResponse
import com.example.currencyconverter.models.exchange.ExchangeResponse
import com.example.currencyconverter.models.exchange.FavoriteCoin
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
    private val _sortedListByFavorites: MutableLiveData<MutableList<Coin>> = MutableLiveData()
    val sortedListByFavorites: LiveData<MutableList<Coin>> = _sortedListByFavorites

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
                list.add(Coin(item.key, item.value ))
                }
            }

        val sortedList = sortListByFavorites(list)
        return sortedList as MutableList<Coin>
    }

    fun sortListByFavorites(list: MutableList<Coin>): List<Coin> {
        return list.sortedWith(
            compareByDescending<Coin> { it.priority }.thenBy { it.name }
        )
    }

    fun updateCoinStatus(favoriteCoin: FavoriteCoin) {
        val currentCoins = _coins.value?.data!!
            for (element in currentCoins) {
                if (element.name == favoriteCoin.name) {
                    element.favorite = favoriteCoin.isFavorite
                }
            }

    }

    fun updateLiveDataFavorites(list: MutableList<Coin>) = viewModelScope.launch {
        _sortedListByFavorites.postValue(list)
    }


    fun addFavorite(coin: FavoriteCoin) = viewModelScope.launch {
        exchangeRepository.addFavorite(coin)
    }

    fun getFavorites() = exchangeRepository.getFavorites()

    fun deleteFavorite(coin: FavoriteCoin) = viewModelScope.launch {
        exchangeRepository.deleteFavorite(coin)
    }
}