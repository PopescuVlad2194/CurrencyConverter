package com.example.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.currencyconverter.repository.ExchangeRepository
import com.example.currencyconverter.repository.NewsRepository
import com.example.currencyconverter.ui.ExchangeViewModel
import com.example.currencyconverter.ui.ExchangeViewModelProviderFactory
import com.example.currencyconverter.ui.NewsViewModel
import com.example.currencyconverter.ui.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var exchangeViewModel: ExchangeViewModel
    lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val exchangeRepository = ExchangeRepository()
        val newsRepository = NewsRepository()
        val exchangeViewModelProviderFactory = ExchangeViewModelProviderFactory(exchangeRepository)
        val newsViewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        exchangeViewModel = ViewModelProvider(this, exchangeViewModelProviderFactory).get(ExchangeViewModel::class.java)
        newsViewModel = ViewModelProvider(this, newsViewModelProviderFactory).get(NewsViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentHolder) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }
}