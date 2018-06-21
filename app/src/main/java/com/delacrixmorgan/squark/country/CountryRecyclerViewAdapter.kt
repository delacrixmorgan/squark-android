package com.delacrixmorgan.squark.country

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.data.model.Country
import kotlinx.android.synthetic.main.cell_country.view.*

class CountryRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var countries: List<Country> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_country, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val country = countries[position]

        if (holder is CountryViewHolder) {
            holder.updateData(country)
        }
    }

    override fun getItemCount(): Int {
        return this.countries.size
    }

    fun updateDataSet(countries: List<Country>) {
        this.countries = countries
        notifyDataSetChanged()
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            this.itemView.setOnClickListener {
            }
        }

        fun updateData(country: Country) {
            this.itemView.codeTextView.text = country.code
            this.itemView.descriptionTextView.text = country.name
        }
    }
}