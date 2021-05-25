package com.example.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.currencyconverter.repository.ExchangeRepository
import com.example.currencyconverter.ui.ExchangeViewModel
import com.example.currencyconverter.ui.ExchangeViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ExchangeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = ExchangeRepository()
        val viewModelProviderFactory = ExchangeViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ExchangeViewModel::class.java)
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
    }
}