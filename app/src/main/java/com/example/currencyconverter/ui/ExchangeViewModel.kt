package com.example.currencyconverter.ui

import androidx.lifecycle.ViewModel
import com.example.currencyconverter.repository.ExchangeRepository

class ExchangeViewModel(
    val exchangeRepository: ExchangeRepository
) : ViewModel() {

}