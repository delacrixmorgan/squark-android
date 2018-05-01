package com.delacrixmorgan.squark.launch

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.SquarkEngine
import com.delacrixmorgan.squark.currency.CurrencyActivity
import kotlinx.android.synthetic.main.fragment_launch.*

/**
 * LaunchFragment
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

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
        this.activity?.let {
            SquarkEngine.setupTable(
                    activity = it,
                    tableLayout = tableLayout,
                    rowList = rowList,
                    listener = this)
        }

        this.baseCurrencyTextView.setOnClickListener {
            this.context?.let {
                val currencyIntent = CurrencyActivity.newLaunchIntent(
                        it,
                        baseCurrencyCode = "USD")
                this.startActivity(currencyIntent)
            }
        }

        this.quoteCurrencyTextView.setOnClickListener {
            this.context?.let {
                val currencyIntent = CurrencyActivity.newLaunchIntent(
                        it,
                        baseCurrencyCode = "MYR")
                this.startActivity(currencyIntent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onRowExpand(selectedRow: Int) {
        this.rowList.forEachIndexed { index, tableRow ->
            if (index != selectedRow && index != (selectedRow + 1)) {
                tableRow.visibility = View.GONE
            } else {
                tableRow.setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))
            }
        }

        this.activity?.let {
            SquarkEngine.expandTable(
                    activity = it,
                    tableLayout = tableLayout,
                    expandQuantifier = selectedRow,
                    expandedList = expandedList,
                    listener = this)
        }
    }

    private fun onRowCollapse(selectedRow: Int) {
        this.expandedList.map {
            tableLayout.removeView(it)
        }

        this.rowList.map {
            it.visibility = View.VISIBLE
            it.setBackgroundColor(ContextCompat.getColor(context!!, R.color.black))
        }
    }

    override fun onSwipeLeft(position: Int) {
        if (!this.isExpanded) {
            SquarkEngine.updateTable(rowList)
        }
    }

    override fun onSwipeRight(position: Int) {
        if (!this.isExpanded) {
            SquarkEngine.updateTable(rowList)
        }
    }

    override fun onClick(position: Int) {
        if (this.isExpanded) {
            onRowCollapse(position)
        } else {
            onRowExpand(position)
        }

        this.isExpanded = !this.isExpanded
    }

    override fun onSwipingLeft(position: Int) = Unit

    override fun onSwipingRight(position: Int) = Unit
}
