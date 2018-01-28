package com.delacrixmorgan.squark

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_row.view.*

/**
 * Created by Delacrix Morgan on 22/01/2018.
 **/

class MultiplierAdapter : RecyclerView.Adapter<MultiplierAdapter.MultiplierViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiplierViewHolder {
        return MultiplierViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_row, parent, false))
    }

    override fun onBindViewHolder(holder: MultiplierViewHolder, position: Int) {
        with(holder.itemView) {
            quantifierTextView.text = "1"
            resultTextView.text = "4.0"
        }
    }

    override fun getItemCount(): Int {
        return 10
    }

    class MultiplierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}