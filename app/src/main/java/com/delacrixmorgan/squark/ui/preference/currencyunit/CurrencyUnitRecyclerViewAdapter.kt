package com.delacrixmorgan.squark.ui.preference.currencyunit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.databinding.ItemCountryBinding
import com.delacrixmorgan.squark.models.Currency
import me.zhanghai.android.fastscroll.PopupTextProvider

class CurrencyUnitRecyclerViewAdapter(private val listener: Listener) :
    RecyclerView.Adapter<CurrencyUnitRecyclerViewAdapter.CountryViewHolder>(), PopupTextProvider {

    interface Listener {
        fun onCurrencySelected(currency: Currency)
    }

    private var currencies: List<Currency> = ArrayList()
    private var isSearchMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(
        ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = currencies[position]
        holder.bind(country, position, currencies.size, isSearchMode)
    }

    override fun getItemCount() = currencies.size

    fun updateDataSet(countries: List<Currency>, searchMode: Boolean) {
        this.currencies = countries
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

    inner class CountryViewHolder(private val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(country: Currency, position: Int, size: Int, searchMode: Boolean) = with(binding) {
            val context = root.context

            codeTextView.text = country.code
            descriptionTextView.text = country.name
            flagImageView.setImageResource(getFlagResource(country))

            when (position) {
                0 -> {
                    headerTextView.text = context.getString(
                        R.string.fragment_country_list_title_header_selected_currency
                    )
                    headerTextView.isVisible = true
                }

                1 -> {
                    headerTextView.text = context.resources.getQuantityString(
                        R.plurals.number_of_currencies,
                        size,
                        size
                    )
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