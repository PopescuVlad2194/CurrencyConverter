package com.example.currencyconverter.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.ui.ExchangeViewModel
import com.example.currencyconverter.util.Resource
import kotlinx.android.synthetic.main.fragment_converter.*

class ConverterFragment : Fragment(R.layout.fragment_converter) {

    lateinit var viewModel: ExchangeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).exchangeViewModel

        viewModel.coins.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { coins ->
                        val coinList = coins.map { coin -> coin.name }
                        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, coinList)
                        autoCompleteTextView.setAdapter(arrayAdapter)
                        secondAutoCompleteTextView.setAdapter(arrayAdapter)

                        autoCompleteTextView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                tvResult.text = parent?.getItemAtPosition(position).toString()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                        }
                    }
                }
                is Resource.Loading -> { }
                is Resource.Error -> {
                    // implement error textView -> View.visible
                }
            }
        })
    }
}