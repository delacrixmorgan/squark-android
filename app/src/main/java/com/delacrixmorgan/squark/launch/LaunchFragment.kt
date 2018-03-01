package com.delacrixmorgan.squark.launch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.SquarkEngine
import kotlinx.android.synthetic.main.fragment_launch.*
import kotlinx.android.synthetic.main.view_row.view.*

/**
 * Created by Delacrix Morgan on 03/07/2017.
 **/

class LaunchFragment : Fragment() {

    companion object {
        fun newInstance(): LaunchFragment {
            return LaunchFragment()
        }
    }

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
            val tableRow = layoutInflater.inflate(R.layout.view_row, this.tableLayout, false) as TableRow

            tableRow.quantifierTextView.text = (index + 1).toString()
            tableRow.resultTextView.text = ((index + 1) * 4).toString()

            tableRow.setOnClickListener {
                if (isExpanded) {
                    this@LaunchFragment.onRowCollapse(index)
                } else {
                    this@LaunchFragment.onRowExpand(index)
                }
                isExpanded = !isExpanded
            }

            rowList.add(tableRow)
            tableLayout.addView(tableRow)
        }
    }

    private fun onRowExpand(selectedRow: Int) {
        rowList.forEachIndexed { index, tableRow ->
            if (index != selectedRow && index != (selectedRow + 1)) {
                tableRow.visibility = View.GONE
            } else {
                tableRow.setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))
            }
        }

        activity?.let {
            SquarkEngine.expandTable(
                    activity = it,
                    tableLayout = tableLayout,
                    expandQuantifier = selectedRow,
                    expandedList = expandedList)
        }
    }

    private fun onRowCollapse(selectedRow: Int) {
        expandedList.map {
            tableLayout.removeView(it)
        }

        rowList.map {
            it.visibility = View.VISIBLE
            it.setBackgroundColor(ContextCompat.getColor(context!!, R.color.black))
        }
    }
}
