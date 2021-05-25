package com.example.currencyconverter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.repository.ExchangeRepository

class ExchangeViewModelProviderFactory(
    val exchangeRepository: ExchangeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExchangeViewModel(exchangeRepository) as T
    }
}