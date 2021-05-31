package com.example.currencyconverter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.models.Coin
import com.example.currencyconverter.models.ExchangeResponse
import com.example.currencyconverter.repository.ExchangeRepository
import com.example.currencyconverter.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ExchangeViewModel(
    private val exchangeRepository: ExchangeRepository
) : ViewModel() {

    private val _coins: MutableLiveData<Resource<MutableList<Coin>>> = MutableLiveData()
    val coins: LiveData<Resource<MutableList<Coin>>> = _coins

    init {
        getExchangeRates()
    }

    private fun getExchangeRates() = viewModelScope.launch {
        _coins.postValue(Resource.Loading())
        val response = exchangeRepository.getExchangeRates()
        _coins.postValue(handleNetworkResponse(response))
    }

    private fun handleNetworkResponse(response: Response<ExchangeResponse>) : Resource<MutableList<Coin>> {
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
           list.add(Coin(item.key, item.value))
        }
        return list
    }
}