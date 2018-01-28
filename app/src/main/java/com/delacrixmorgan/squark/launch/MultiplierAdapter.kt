package com.delacrixmorgan.squark.launch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.delacrixmorgan.squark.R
import kotlinx.android.synthetic.main.view_row.view.*

/**
 * Created by Delacrix Morgan on 22/01/2018.
 **/

class MultiplierAdapter(private val rowListener: RowListener) : RecyclerView.Adapter<MultiplierAdapter.MultiplierViewHolder>() {
    
    private val totalRows = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiplierViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.view_row, parent, false)
        val height = parent.measuredHeight / totalRows
        val width = parent.measuredWidth

        rootView.layoutParams = RecyclerView.LayoutParams(width, height)

        return MultiplierViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: MultiplierViewHolder, position: Int) {
        with(holder.itemView) {
            quantifierTextView.text = (position + 1).toString()
            resultTextView.text = "4.0"

            setOnClickListener {
                rowListener.onRowSelected(position + 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return totalRows
    }

    class MultiplierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}