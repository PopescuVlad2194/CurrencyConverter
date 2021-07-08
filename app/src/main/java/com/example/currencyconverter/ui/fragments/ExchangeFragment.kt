package com.example.currencyconverter.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.adapters.ExchangeAdapter
import com.example.currencyconverter.ui.ExchangeViewModel
import com.example.currencyconverter.ui.ExchangeViewModelProviderFactory
import com.example.currencyconverter.util.Constants.TAG
import com.example.currencyconverter.util.Resource
import kotlinx.android.synthetic.main.fragment_exchange.*

class ExchangeFragment : Fragment(R.layout.fragment_exchange) {

    lateinit var viewModel: ExchangeViewModel
    lateinit var exchangeAdapter: ExchangeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).exchangeViewModel
        setupRecyclerView()
//        registerForContextMenu(fragmentExchangeRV)

        viewModel.coins.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { coins ->
                        exchangeAdapter.differ.submitList(coins)
                    }
                }
                is Resource.Error -> {
                    Log.d(TAG, "ExchangeFragment Resource Error")
                }
            }
        })
    }
//
//    override fun onCreateContextMenu(
//        menu: ContextMenu,
//        v: View,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//
//        val inflater: MenuInflater? = activity?.menuInflater
//        inflater?.inflate(R.menu.exchange_context_menu, menu)
//        menu.setHeaderTitle("Favorites")
//    }

//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.add_to_favorites -> {
//                Toast.makeText(activity, "Added to favorites", Toast.LENGTH_SHORT).show()
//                true
//            }
//            R.id.remove_from_favorites -> {
//                Toast.makeText(activity, "Removed from favorites", Toast.LENGTH_SHORT).show()
//                true
//            }
//            else -> false
//        }
//    }

    private fun setupRecyclerView() {
        exchangeAdapter = ExchangeAdapter()
        fragmentExchangeRV.apply {
            adapter = exchangeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}