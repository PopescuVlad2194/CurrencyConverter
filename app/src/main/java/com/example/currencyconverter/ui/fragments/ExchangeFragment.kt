package com.example.currencyconverter.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.*
import com.example.currencyconverter.adapters.ExchangeAdapter
import com.example.currencyconverter.models.exchange.FavoriteCoin
import com.example.currencyconverter.util.Constants.TAG
import com.example.currencyconverter.util.Resource
import kotlinx.android.synthetic.main.fragment_exchange.*

class ExchangeFragment : Fragment(R.layout.fragment_exchange), TakeFavorites {

    lateinit var viewModel: ExchangeViewModel
    lateinit var exchangeAdapter: ExchangeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).exchangeViewModel
        setupRecyclerView()

        val favoritesResponse = viewModel.prepareFavorites()
        exchangeAdapter.onClick = {
            when (it.isFavorite) {
                true -> viewModel.addFavorite(it)
                false -> viewModel.deleteFavorite(it)
            }
        }

//        val resultList = takeFavorites(favoritesResponse)

        viewModel.coins.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success<*> -> {
                    response.data.let { coins ->

//                        exchangeAdapter.takeFavorites(resultList)
                        exchangeAdapter.differ.submitList(coins)
                    }
                }
                is Resource.Error<*> -> {
                    Log.d(TAG, "ExchangeFragment Resource Error")
                }
            }
        }
    }

    private fun setupRecyclerView() {
        exchangeAdapter = ExchangeAdapter()
        fragmentExchangeRV.apply {
            adapter = exchangeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun takeFavorites(list: List<FavoriteCoin>): List<String> {
        val newList: MutableList<String> = mutableListOf()
        for (element in list) {
            newList.add(element.name)
            Log.d(TAG, element.name)
        }
        return newList
    }
}