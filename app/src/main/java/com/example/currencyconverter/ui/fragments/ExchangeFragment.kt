package com.example.currencyconverter.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.ExchangeViewModel
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.adapters.ExchangeAdapter
import com.example.currencyconverter.models.exchange.Coin
import com.example.currencyconverter.util.Constants.TAG
import com.example.currencyconverter.util.Resource
import kotlinx.android.synthetic.main.fragment_exchange.*
import kotlinx.coroutines.*

class ExchangeFragment : Fragment(R.layout.fragment_exchange) {

    lateinit var viewModel: ExchangeViewModel
    lateinit var exchangeAdapter: ExchangeAdapter
    val job = Job()
    private val fragmentScope = CoroutineScope(Dispatchers.Main + job)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).exchangeViewModel
        setupRecyclerView()

        exchangeAdapter.onClick = {
            when (it.favorite) {
                true -> viewModel.addFavorite(it)
                false -> viewModel.deleteFavorite(it)
            }
            updateCoins(it)
        }

        viewModel.getFavorites().observe(viewLifecycleOwner, { favoriteCoins ->
            fragmentScope.launch {
                delay(500L)
                val currentCoins = viewModel.coins.value?.data
                exchangeAdapter.takeFavorites(favoriteCoins)
                if (currentCoins != null) {
                    val result = viewModel.sortListByFavorites(currentCoins, favoriteCoins)
                    exchangeAdapter.differ.submitList(result)
                    viewModel.updateFavoriteListConverter(result)
                }
            }
        })

        viewModel.coins.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success<*> -> {
                    response.data.let { coins ->
                        exchangeAdapter.differ.submitList(coins)
                    }
                }
                is Resource.Error<*> -> {
                    Log.d(TAG, "ExchangeFragment Resource Error")
                }
            }
        }
    }

    private fun updateCoins(coin: Coin) {
        viewModel.updateCoinStatus(coin)
    }


    private fun setupRecyclerView() {
        exchangeAdapter = ExchangeAdapter()
        fragmentExchangeRV.apply {
            adapter = exchangeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}