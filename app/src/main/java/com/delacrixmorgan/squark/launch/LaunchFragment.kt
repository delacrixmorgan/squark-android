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

/**
 * Created by Delacrix Morgan on 03/07/2017.
 **/

class LaunchFragment : Fragment(), RowListener {

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
        activity?.let {
            SquarkEngine.initTable(
                    activity = it,
                    tableLayout = tableLayout,
                    rowList = rowList,
                    listener = this)
        }
    }

    override fun onSwipeLeft(position: Int) {
        rowList.first().setBackgroundColor(ContextCompat.getColor(context!!, R.color.black))
    }

    override fun onSwipeRight(position: Int) {
        rowList.last().setBackgroundColor(ContextCompat.getColor(context!!, R.color.black))
    }

    override fun onSwipingLeft(position: Int) {
        rowList.first().setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))
        rowList.last().setBackgroundColor(ContextCompat.getColor(context!!, R.color.black))
    }

    override fun onSwipingRight(position: Int) {
        rowList.first().setBackgroundColor(ContextCompat.getColor(context!!, R.color.black))
        rowList.last().setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))
    }

    override fun onClick(position: Int) {
        if (isExpanded) {
            onRowCollapse(position)
        } else {
            onRowExpand(position)
        }
        isExpanded = !isExpanded
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
