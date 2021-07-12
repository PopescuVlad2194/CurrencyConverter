package com.example.currencyconverter.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.currencyconverter.ExchangeViewModel
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.util.Constants.TAG
import com.example.currencyconverter.util.Resource
import kotlinx.android.synthetic.main.fragment_converter.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.NumberFormatException


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
                        val arrayAdapter = ArrayAdapter(
                            requireContext(),
                            R.layout.dropdown_item,
                            coinList
                        )
                        autoCompleteTextView.setAdapter(arrayAdapter)
                        secondAutoCompleteTextView.setAdapter(arrayAdapter)

                        autoCompleteTextView.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, position, _ ->
                                tvCurrentCoin.text = coinList[position]
                            }
                        secondAutoCompleteTextView.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, position, _ ->
                                tvResultCoin.text = coinList[position]
                            }

                        var job: Job? = null
                        etAmountInput.addTextChangedListener { editable ->
                            job?.cancel()
                            job = MainScope().launch {
                                delay(500L)
                                try {
                                    val number = editable.toString()
                                    val selectedCoin = autoCompleteTextView.text.toString()
                                    val desireCoin = secondAutoCompleteTextView.text.toString()
                                    if (checkRequirements(number)) {
                                        initializeConvertCall(number, selectedCoin, desireCoin)
                                    }
                                } catch (exception: NumberFormatException) {
                                    Log.e(TAG, exception.toString())
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "ConverterFragment error call.")
                }
            }
        })
    }

    private fun initializeConvertCall(
        number: String,
        selectedCoin: String,
        desireCoin: String
    ) {
        viewModel.convert(selectedCoin, desireCoin)
        viewModel.converter.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { value ->
                        val callValue = number.toFloat()
                        val result = callValue * value.toFloat()
                        tvResultConversion.text = result.toString()
                    }
                }
            }
        })
    }

    private fun checkRequirements(number: String): Boolean {
        return autoCompleteTextView.text.toString().isNotEmpty() &&
                secondAutoCompleteTextView.text.toString().isNotEmpty() &&
                number.isNotEmpty()
    }
}