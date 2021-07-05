package com.example.currencyconverter.ui

import android.util.Log
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
                list.add(Coin(item.key, item.value))
            }
        }
        val sortedList = list.sortedWith(
            compareByDescending<Coin> { it.priority }.thenBy{it.name}
        )


        return sortedList as MutableList<Coin>
    }

}