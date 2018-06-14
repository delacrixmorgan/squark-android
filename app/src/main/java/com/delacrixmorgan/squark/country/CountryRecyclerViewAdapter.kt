package com.delacrixmorgan.squark.country

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.data.model.Country

open class CountryRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var countries: ArrayList<Country> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CountryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.cell_country, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val country = countries[position]

        if (holder is CountryViewHolder) {
            holder.updateData(country)
        }
    }

    override fun getItemCount() = countries.count()

    open class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            this.itemView.setOnClickListener { }
        }

        fun updateData(country: Country) {

        }
    }
}