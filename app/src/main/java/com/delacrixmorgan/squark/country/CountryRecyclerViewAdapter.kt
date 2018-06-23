package com.delacrixmorgan.squark.country

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.data.model.Country
import kotlinx.android.synthetic.main.cell_country.view.*

class CountryRecyclerViewAdapter(
        private val listener: CountryListListener,
        private val countryCode: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var countries: List<Country> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_country, parent, false), listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val country = countries[position]

        if (holder is CountryViewHolder) {
            holder.updateData(country, countryCode)
        }
    }

    override fun getItemCount(): Int {
        return this.countries.size
    }

    fun updateDataSet(countries: List<Country>) {
        this.countries = countries
        notifyDataSetChanged()
    }

    class CountryViewHolder(itemView: View, private val listener: CountryListListener) : RecyclerView.ViewHolder(itemView) {
        private lateinit var country: Country

        init {
            this.itemView.setOnClickListener {
                this.listener.onCountrySelected(this.country)
            }
        }

        fun updateData(country: Country, countryCode: String) {
            this.country = country

            this.itemView.context.let {
                val flagResource = it.resources.getIdentifier("ic_flag_${this.country.code.toLowerCase()}", "drawable", it.packageName)
                val fallbackFlagResource = it.resources.getIdentifier("ic_flag_un", "drawable", it.packageName)

                if (flagResource != 0) {
                    this.itemView.flagImageView.setImageResource(flagResource)
                } else {
                    this.itemView.flagImageView.setImageResource(fallbackFlagResource)
                }
            }

            this.itemView.codeTextView.text = this.country.code
            this.itemView.descriptionTextView.text = this.country.name
            this.itemView.selectedCountryImageView.visibility = if (this.country.code == countryCode) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}