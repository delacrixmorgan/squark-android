package com.delacrixmorgan.squark.launch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import com.delacrixmorgan.squark.R
import kotlinx.android.synthetic.main.fragment_launch.*
import kotlinx.android.synthetic.main.view_row.view.*

/**
 * Created by Delacrix Morgan on 03/07/2017.
 **/

class LaunchFragment : Fragment(), RowListener {

    companion object {
        fun newInstance(): LaunchFragment {
            return LaunchFragment()
        }
    }

    private lateinit var rowAdapter: MultiplierAdapter

    private var rowList: ArrayList<TableRow> = ArrayList()
    private var expandedList: ArrayList<TableRow> = ArrayList()

    private var isExpanded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_launch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTableView(0, 9)
    }

    private fun setupTableView(start: Int, end: Int) {
        for (index in start..end) {
            val tableRow = layoutInflater.inflate(R.layout.view_row, this.tableView, false) as TableRow

            tableRow.quantifierTextView.text = (index + 1).toString()
            tableRow.resultTextView.text = ((index + 1) * 4).toString()

            if (index == 5 || index == 6) {
                tableRow.setOnClickListener {
                    triggerRow()
                }
            }

            rowList.add(tableRow)
            this.tableView.addView(tableRow)
        }
    }

    private fun triggerRow() {
        if (!isExpanded) {
            rowList.forEachIndexed { index, tableRow ->
                if (index != 5 && index != 6) {
                    tableRow.visibility = View.GONE
                } else {
                    tableRow.setBackgroundColor(ContextCompat.getColor(this.context!!, R.color.amber))
                }
            }

            for (index in 0..9) {
                val tableRow = layoutInflater.inflate(R.layout.view_row, this.tableView, false) as TableRow

                tableRow.quantifierTextView.text = (index + 1).toString()
                tableRow.resultTextView.text = ((index + 1) * 4).toString()

                expandedList.add(tableRow)
                this.tableView.addView(tableRow, 6)
            }
        } else {
            expandedList.map {
                this.tableView.removeView(it)
            }

            rowList.map {
                it.visibility = View.VISIBLE
                it.setBackgroundColor(ContextCompat.getColor(this.context!!, R.color.black))
            }
        }

        isExpanded = !isExpanded
    }

    override fun onRowExpand(position: Int) {
        rowAdapter.expandRow(position)
    }

    override fun onRowCollapse(position: Int) {
        rowAdapter.collapseRow(position)
    }
}
