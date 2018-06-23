package com.delacrixmorgan.squark

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.TableLayout
import android.widget.TableRow
import com.delacrixmorgan.squark.data.model.Currency
import com.delacrixmorgan.squark.launch.OnSwipeTouch
import com.delacrixmorgan.squark.launch.RowListener
import kotlinx.android.synthetic.main.view_row.view.*
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * SquarkEngine
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

object SquarkEngine {

    private var multiplier: Double = 1.0
    private var conversionRate: Double = 1.0
    private var bigResult: BigDecimal? = null
    private var bigQuantifier: BigDecimal? = null
    private val decimalFormat: DecimalFormat = DecimalFormat("###,##0.00")

    fun updateConversionRate(baseCurrency: Currency, quoteCurrency: Currency) {
        conversionRate = quoteCurrency.rate / baseCurrency.rate
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupTable(
            activity: Activity,
            tableLayout: TableLayout,
            rowList: ArrayList<TableRow>,
            listener: RowListener
    ) {
        for (index in 0..9) {
            val tableRow = activity.layoutInflater.inflate(R.layout.view_row, tableLayout, false) as TableRow

            val calculateQuantifier = (this.multiplier * (index + 1))
            val calculateResult = calculateQuantifier * this.conversionRate

            this.bigQuantifier = BigDecimal(calculateQuantifier).setScale(2, BigDecimal.ROUND_HALF_UP)
            this.bigResult = BigDecimal(calculateResult).setScale(2, BigDecimal.ROUND_HALF_UP)

            tableRow.quantifierTextView.text = this.decimalFormat.format(this.bigQuantifier).toString()
            tableRow.resultTextView.text = this.decimalFormat.format(this.bigResult).toString()

            tableRow.setOnTouchListener(object : OnSwipeTouch(activity) {
                override fun onSwipeLeft() {
                    if (this@SquarkEngine.multiplier < 1000000) {
                        this@SquarkEngine.multiplier *= 10
                    }
                    listener.onSwipeLeft(index)
                }

                override fun onSwipeRight() {
                    if (this@SquarkEngine.multiplier > 0.1) {
                        this@SquarkEngine.multiplier /= 10
                    }
                    listener.onSwipeRight(index)
                }

                override fun onSwipingLeft() {
                    listener.onSwipingLeft(index)
                }

                override fun onSwipingRight() {
                    listener.onSwipingRight(index)
                }

                override fun onSingleTap() {
                    listener.onClick(index)
                }
            })

            rowList.add(tableRow)
            tableLayout.addView(tableRow)
        }
    }

    fun updateTable(rowList: ArrayList<TableRow>) {
        rowList.forEachIndexed { index, tableRow ->
            val calculateQuantifier = (this.multiplier * (index + 1))
            val calculateResult = calculateQuantifier * this.conversionRate

            this.bigQuantifier = BigDecimal(calculateQuantifier).setScale(2, BigDecimal.ROUND_HALF_UP)
            this.bigResult = BigDecimal(calculateResult).setScale(2, BigDecimal.ROUND_HALF_UP)

            tableRow.quantifierTextView.text = this.decimalFormat.format(this.bigQuantifier).toString()
            tableRow.resultTextView.text = this.decimalFormat.format(this.bigResult).toString()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun expandTable(
            activity: Activity,
            tableLayout: TableLayout,
            expandQuantifier: Int,
            expandedList: ArrayList<TableRow>,
            listener: RowListener
    ) {
        for (index in 1..9) {
            val tableRow = activity.layoutInflater.inflate(R.layout.view_row, tableLayout, false) as TableRow

            val calculateQuantifier = (expandQuantifier + 1) * this.multiplier + (this.multiplier / 10 * index)
            val calculateResult = calculateQuantifier * this.conversionRate

            this.bigQuantifier = BigDecimal(calculateQuantifier).setScale(2, BigDecimal.ROUND_HALF_UP)
            this.bigResult = BigDecimal(calculateResult).setScale(2, BigDecimal.ROUND_HALF_UP)

            tableRow.quantifierTextView.text = this.decimalFormat.format(this.bigQuantifier).toString()
            tableRow.resultTextView.text = this.decimalFormat.format(this.bigResult).toString()

            tableRow.setOnClickListener {
                listener.onClick(index)
            }

            expandedList.add(tableRow)
            tableLayout.addView(tableRow, (expandQuantifier + index))
        }
    }
}