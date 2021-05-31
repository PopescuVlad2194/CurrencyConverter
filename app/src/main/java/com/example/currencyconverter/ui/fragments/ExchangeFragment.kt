package com.example.currencyconverter.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.adapters.ExchangeAdapter
import com.example.currencyconverter.ui.ExchangeViewModel
import com.example.currencyconverter.util.Resource
import kotlinx.android.synthetic.main.fragment_exchange.*

class ExchangeFragment : Fragment(R.layout.fragment_exchange) {

    lateinit var viewModel: ExchangeViewModel
    lateinit var exchangeAdapter: ExchangeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.coins.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { coins ->
                        exchangeAdapter.differ.submitList(coins)
                    }
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    // implement error textView -> View.visible
                }
            }
        })
    }

    private fun setupRecyclerView() {
        exchangeAdapter = ExchangeAdapter()
        fragmentExchangeRV.apply {
            adapter = exchangeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}