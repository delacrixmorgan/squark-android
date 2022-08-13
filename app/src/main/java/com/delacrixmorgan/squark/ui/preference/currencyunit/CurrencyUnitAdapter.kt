package com.delacrixmorgan.squark.ui.preference.currencyunit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.databinding.ItemCurrencyBinding
import com.delacrixmorgan.squark.model.currency.Currency
import me.zhanghai.android.fastscroll.PopupTextProvider

// TODO (Try ListAdapter)
//https://github.com/google-developer-training/android-kotlin-fundamentals-apps/blob/master/RecyclerViewHeaders/app/src/main/java/com/example/android/trackmysleepquality/sleeptracker/SleepNightAdapter.kt
class CurrencyUnitAdapter(private val listener: Listener) :
    RecyclerView.Adapter<CurrencyUnitAdapter.CurrencyViewHolder>(), PopupTextProvider {

    interface Listener {
        fun onCurrencySelected(currency: Currency)
    }

    private var currencies: List<Currency> = ArrayList()
    private var isSearchMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CurrencyViewHolder(
        ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencies[position]
        holder.bind(currency, position, isSearchMode)
    }

    override fun getItemCount() = currencies.size

    fun updateDataSet(selectedCurrency: Currency?, filteredCurrencies: List<Currency>, searchMode: Boolean) {
        if (!isSearchMode && selectedCurrency != null) {
            this.currencies = filteredCurrencies.sortedWith(compareBy({ it.code != selectedCurrency.code }, { it.code }))
        } else {
            this.currencies = filteredCurrencies
        }
        isSearchMode = searchMode
        notifyDataSetChanged()
    }

    override fun getPopupText(position: Int): String {
        return if (position == 0) {
            currencies[position + 1].code[0].toString()
        } else {
            currencies[position].code[0].toString()
        }
    }

    inner class CurrencyViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currency: Currency, position: Int, isSearchMode: Boolean) = with(binding) {
            val context = root.context

            codeTextView.text = currency.code
            descriptionTextView.text = currency.name
            flagImageView.setImageResource(getFlagResource(currency))

            when (position) {
                0 -> {
                    headerTextView.text = context.getString(R.string.fragment_country_list_title_header_selected_currency)
                    headerTextView.isVisible = true && !isSearchMode
                }
                1 -> {
                    headerTextView.text = context.resources.getQuantityString(R.plurals.number_of_currencies, currencies.size, currencies.size)
                    headerTextView.isVisible = true && !isSearchMode
                }
                else -> headerTextView.isVisible = false
            }

            root.setOnClickListener {
                listener.onCurrencySelected(currency)
            }
        }

        private fun getFlagResource(currency: Currency): Int {
            val flagResource = itemView.context.resources.getIdentifier(
                "ic_flag_${currency.code.lowercase()}",
                "drawable",
                itemView.context.packageName
            )
            val fallbackFlagResource = itemView.context.resources.getIdentifier(
                "ic_flag_un", "drawable", itemView.context.packageName
            )

            return if (flagResource != 0) flagResource else fallbackFlagResource
        }
    }
}