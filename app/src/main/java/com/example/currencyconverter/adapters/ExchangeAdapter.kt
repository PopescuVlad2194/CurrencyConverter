package com.example.currencyconverter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.models.Coin
import kotlinx.android.synthetic.main.exchange_item_preview.view.*

class ExchangeAdapter() : RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    inner class ExchangeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Coin>() {
        override fun areItemsTheSame(
            oldItem: Coin,
            newItem: Coin
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Coin,
            newItem: Coin
        ): Boolean {
            return oldItem.name == newItem.name && oldItem.value == newItem.value
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        return ExchangeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.exchange_item_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        val exchange = differ.currentList[position]
        holder.itemView.apply {
            bank_currency_id.text = exchange.name
            bank_currency_value.text = exchange.value.toString()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}