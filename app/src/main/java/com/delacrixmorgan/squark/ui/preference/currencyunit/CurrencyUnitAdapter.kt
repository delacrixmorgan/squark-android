package com.delacrixmorgan.squark.ui.preference.currencyunit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.databinding.ItemCurrencyBinding
import com.delacrixmorgan.squark.models.Currency
import me.zhanghai.android.fastscroll.PopupTextProvider

// TODO (Try ListAdapter)
//https://github.com/google-developer-training/android-kotlin-fundamentals-apps/blob/master/RecyclerViewHeaders/app/src/main/java/com/example/android/trackmysleepquality/sleeptracker/SleepNightAdapter.kt
class CurrencyUnitAdapter(private val listener: Listener) :
    RecyclerView.Adapter<CurrencyUnitAdapter.CountryViewHolder>(), PopupTextProvider {

    interface Listener {
        fun onCurrencySelected(currency: Currency)
    }

    private var currencies: List<Currency> = ArrayList()
    private var isSearchMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(
        ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = currencies[position]
        holder.bind(country, position, isSearchMode)
    }

    override fun getItemCount() = currencies.size

    fun updateDataSet(selectedCurrency: Currency?, countries: List<Currency>, searchMode: Boolean) {
        val filteredCurrencies = countries.toMutableList()
        selectedCurrency?.let { country ->
            filteredCurrencies.remove(country)
            filteredCurrencies.sortBy { it.code }
            filteredCurrencies.add(0, country)
        }

        this.currencies = filteredCurrencies
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

    inner class CountryViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(country: Currency, position: Int, searchMode: Boolean) = with(binding) {
            val context = root.context

            codeTextView.text = country.code
            descriptionTextView.text = country.name
            flagImageView.setImageResource(getFlagResource(country))

            when (position) {
                0 -> {
                    headerTextView.text = context.getString(R.string.fragment_country_list_title_header_selected_currency)
                    headerTextView.isVisible = true
                }

                1 -> {
                    headerTextView.text = context.resources.getQuantityString(R.plurals.number_of_currencies, currencies.size, currencies.size)
                    headerTextView.isVisible = true
                }

                else -> headerTextView.isVisible = false
            }

            if (searchMode) {
                headerTextView.isVisible = false
            }

            cellViewGroup.setOnClickListener {
                listener.onCurrencySelected(country)
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