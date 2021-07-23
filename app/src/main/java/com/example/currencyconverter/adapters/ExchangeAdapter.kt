package com.example.currencyconverter.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.models.exchange.Coin
import com.example.currencyconverter.util.Constants.TAG
import kotlinx.android.synthetic.main.exchange_item_preview.view.*
import java.util.*

class ExchangeAdapter() : RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    var onClick: ((Coin) -> Unit)? = null

    inner class ExchangeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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

    private var favoritesList = mutableListOf<Coin>()

    fun takeFavorites(list: List<Coin>) {
        for (item in list) {
            favoritesList.add(item)
        }
    }


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
            val value = bank_currency_id.text.toString().lowercase(Locale.ENGLISH)
            val imageName = "flag_$value"
            for (element in favoritesList) {
                if (element == exchange) {
                    element.favorite = true
                }
            }

            setItemContent(imageName, exchange)

            favorites.setOnClickListener { view ->
                if (exchange.favorite) {
                    view.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_remove_favorite,
                        context?.theme
                    )
                    toastRemoveFavorite(exchange)
                    exchange.favorite = false
                } else {
                    view.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_add_favorite,
                        context?.theme
                    )
                    toastAddFavorite(exchange)
                    exchange.favorite = true
                }

                onClick?.let {
                    Log.d(TAG, "sending FavoriteCoin")
                    it.invoke(exchange)
                }
            }

            setOnCreateContextMenuListener { menu, _, _ ->
                if (exchange.favorite) {
                    menu.add(R.string.remove_from_favorites).setOnMenuItemClickListener {
                        toastRemoveFavorite(exchange)
                        exchange.favorite = false
                        setFavoriteImage(exchange)
                        onClick?.invoke(exchange)
                        true
                    }
                } else {
                    menu.add(R.string.add_to_favorites).setOnMenuItemClickListener {
                        toastAddFavorite(exchange)
                        exchange.favorite = true
                        setFavoriteImage(exchange)
                        onClick?.invoke(exchange)
                        true
                    }
                }
            }
        }
    }


    private fun View.toastAddFavorite(exchange: Coin) {
        Toast.makeText(
            context,
            "${exchange.name} added to favorites",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun View.toastRemoveFavorite(exchange: Coin) {
        Toast.makeText(
            context,
            "${exchange.name} removed from favorites",
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun View.setItemContent(
        imageName: String,
        exchange: Coin
    ) {
        val drawableId = resources.getIdentifier(imageName, "drawable", context.packageName)
        if (drawableId != 0) {
            ivFlagImage.background =
                ResourcesCompat.getDrawable(resources, drawableId, context?.theme)
        } else {
            ivFlagImage.background =
                ResourcesCompat.getDrawable(resources, R.drawable.flag_error, context?.theme)
        }
        val result = 1 / exchange.value.toFloat()
        val temp = "$result LEI"
        bank_currency_value.text = temp
        setFavoriteImage(exchange)
    }

    private fun View.setFavoriteImage(exchange: Coin) {
        if (exchange.favorite) {
            favorites.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_add_favorite,
                context?.theme
            )

        } else {
            favorites.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_remove_favorite,
                context?.theme
            )
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}