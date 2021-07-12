package com.example.currencyconverter.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.*
import com.example.currencyconverter.adapters.ExchangeAdapter
import com.example.currencyconverter.models.exchange.Favorite
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

        val myResultCall = mutableListOf(Favorite("AUD"), Favorite("EUR"), Favorite("BAM"), Favorite("BHD"))
//        exchangeAdapter.onClick = { viewModel.method() }

        viewModel.coins.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success<*> -> {
                    response.data.let { coins ->
                        val resultList = takeFavorites(myResultCall)
                        exchangeAdapter.takeFavorites(resultList)
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

    override fun takeFavorites(list: List<Favorite>): List<String> {
        val newList: MutableList<String> = mutableListOf()
        for (element in list) {
            newList.add(element.name)
            Log.d(TAG, element.name)
        }
        return newList
    }
}