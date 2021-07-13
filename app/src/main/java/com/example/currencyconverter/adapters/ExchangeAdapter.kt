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
import com.example.currencyconverter.models.exchange.FavoriteCoin
import com.example.currencyconverter.util.Constants.TAG
import kotlinx.android.synthetic.main.exchange_item_preview.view.*
import java.util.*


class ExchangeAdapter() : RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    var onClick: ((FavoriteCoin) -> Unit)? = null

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

    private var favoritesList = emptyList<String>()

    fun takeFavorites(list: List<String>) {
         favoritesList = list
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
            var isFavorite = favoritesList.contains(exchange.name)


            setItemData(imageName, exchange)

            checkFavoriteStatus(isFavorite)

            favorites.setOnClickListener { view ->
                if (isFavorite) {
                    view.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_add_favorites,
                        context?.theme)
                    isFavorite = false
                } else {
                    view.background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_remove_favorites,
                        context?.theme)
                    isFavorite = true
                }

                onClick?.let  {
                    Log.d(TAG, "sending FavoriteCoin")
                    it.invoke(FavoriteCoin(exchange.name, isFavorite))

                }
            }

            setOnCreateContextMenuListener { menu, _, _ ->
                menu.add(R.string.add_to_favorites).setOnMenuItemClickListener {
                     Toast.makeText(context, "${exchange.name} added to favorites", Toast.LENGTH_SHORT).show()
                     true
                }
                menu.add(R.string.remove_from_favorites).setOnMenuItemClickListener {
                     Toast.makeText(context, "${exchange.name} removed from favorites", Toast.LENGTH_SHORT).show()
                     true
                }
            }
        }
    }


    private fun View.setItemData(
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
    }

    private fun View.checkFavoriteStatus(isFavorite: Boolean) {
        if (isFavorite) {
            favorites.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_remove_favorites,
                context?.theme
            )
        } else {
            favorites.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_add_favorites,
                context?.theme
            )
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}