package com.example.currencyconverter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.models.ExchangeResponse
import kotlinx.android.synthetic.main.exchange_item_row.view.*

class ExchangeAdapter() : RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    inner class ExchangeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<ExchangeResponse>() {
        override fun areItemsTheSame(
            oldItem: ExchangeResponse,
            newItem: ExchangeResponse
        ): Boolean {
            return oldItem.time_last_update_unix == newItem.time_last_update_unix
        }

        override fun areContentsTheSame(
            oldItem: ExchangeResponse,
            newItem: ExchangeResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        return ExchangeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.exchange_item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        val exchange = differ.currentList[position]
        holder.itemView.apply {
            bank_currency_title.text = exchange.base_code
            val str = bank_currency_title.text.toString()
            bank_currency_value.text = exchange.conversion_rates.RON.toString()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}