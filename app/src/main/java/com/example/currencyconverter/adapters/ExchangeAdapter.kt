package com.example.currencyconverter.adapters


import android.util.Log
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.models.exchange.Coin
import kotlinx.android.synthetic.main.exchange_item_preview.view.*
import java.util.*

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
            val value = bank_currency_id.text.toString().toLowerCase(Locale.ENGLISH)
            val imageName = "flag_$value"

            val drawableId = resources.getIdentifier(imageName, "drawable", context.packageName)
            if (drawableId != 0) {
                ivFlagImage.background = ResourcesCompat.getDrawable(resources, drawableId, context?.theme)
            } else {
                ivFlagImage.background = ResourcesCompat.getDrawable(resources, R.drawable.flag_error, context?.theme)
            }
            val result = 1 / exchange.value!!.toFloat()
            val temp = "$result LEI"
            bank_currency_value.text = temp

            setOnCreateContextMenuListener { menu, v, menuInfo ->
                menu.add(R.string.add_to_favorites).setOnMenuItemClickListener {
                    Log.d("CLICK", "Item added to favorites pos $position clicked")
                     true
                }
                menu.add(R.string.remove_from_favorites).setOnMenuItemClickListener {
                    Log.d("CLICK", "Item removed from favorites pos $position clicked")
                     true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}