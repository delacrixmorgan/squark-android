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

    private var totalRows = 10
    private var isRowExpanded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiplierViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.view_row, parent, false)
        val height = parent.measuredHeight / totalRows
        val width = parent.measuredWidth

        rootView.layoutParams = RecyclerView.LayoutParams(width, height)

        return MultiplierViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: MultiplierViewHolder, position: Int) {
        val itemViewPosition = holder.adapterPosition + 1

        with(holder) {
            itemView.quantifierTextView.text = itemViewPosition.toString()
            itemView.resultTextView.text = (4 * itemViewPosition).toString()

            itemView.setOnClickListener {
                if (isRowExpanded) {
                    rowListener.onRowCollapse(itemViewPosition)
                } else {
                    rowListener.onRowExpand(itemViewPosition)
                }

                isRowExpanded = !isRowExpanded
            }
        }
    }

    override fun getItemCount(): Int {
        return totalRows
    }

    fun expandRow(position: Int) {
        notifyItemRangeRemoved(0, position - 1)
        notifyItemRangeRemoved(position, 10)

        totalRows = 1
    }

    fun collapseRow(position: Int) {
        notifyItemRangeInserted(0, position - 1)
        notifyItemRangeInserted(position, 10)

        totalRows = 10
    }

    class MultiplierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}