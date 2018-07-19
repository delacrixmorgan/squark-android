package com.delacrixmorgan.squark.country

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.data.model.Country
import kotlinx.android.synthetic.main.cell_country.view.*

/**
 * CountryRecyclerViewAdapter
 * squark-android
 *
 * Created by Delacrix Morgan on 19/07/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CountryRecyclerViewAdapter(
        private val listener: CountryListListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var countries: List<Country> = ArrayList()
    private var isSearchMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_country, parent, false), listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val country = countries[position]

        if (holder is CountryViewHolder) {
            holder.updateData(country, position, this.countries.size, this.isSearchMode)
        }
    }

    override fun getItemCount(): Int {
        return this.countries.size
    }

    fun updateDataSet(countries: List<Country>, searchMode: Boolean) {
        this.countries = countries
        this.isSearchMode = searchMode
        notifyDataSetChanged()
    }

    class CountryViewHolder(itemView: View, private val listener: CountryListListener) : RecyclerView.ViewHolder(itemView) {
        private lateinit var country: Country

        init {
            this.itemView.cellViewGroup.setOnClickListener {
                this.listener.onCountrySelected(this.country)
            }
        }

        fun updateData(country: Country, position: Int, size: Int, searchMode: Boolean) {
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

            when (position) {
                0 -> {
                    this.itemView.headerTextView.text = this.itemView.context.getString(R.string.fragment_country_list_title_header_selected_currency)
                    this.itemView.headerTextView.visibility = View.VISIBLE
                }

                1 -> {
                    this.itemView.headerTextView.text = this.itemView.context.getString(R.string.fragment_country_list_title_header_available_currencies, size)
                    this.itemView.headerTextView.visibility = View.VISIBLE
                }

                else -> {
                    this.itemView.headerTextView.visibility = View.GONE
                }
            }

            if (searchMode) {
                this.itemView.headerTextView.visibility = View.GONE
            }
        }
    }
}