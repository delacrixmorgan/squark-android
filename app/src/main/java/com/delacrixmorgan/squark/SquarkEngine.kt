package com.delacrixmorgan.squark

import android.app.Activity
import android.widget.TableLayout
import android.widget.TableRow
import kotlinx.android.synthetic.main.view_row.view.*
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by Delacrix Morgan on 03/07/2017.
 **/

object SquarkEngine {

    private var multiplier: Double = 0.0
    private var conversionRate: Double = 0.0
    private val decimalFormat: DecimalFormat
    private var bigQuantifier: BigDecimal? = null
    private var bigResult: BigDecimal? = null

    init {
        conversionRate = 4.0
        multiplier = 1.0

        decimalFormat = DecimalFormat("###,##0.00")
    }

    fun expandTable(activity: Activity, tableLayout: TableLayout, expandQuantifier: Int, expandedList: ArrayList<TableRow>) {
        for (index in 1..9) {
            val tableRow = activity.layoutInflater.inflate(R.layout.view_row, tableLayout, false) as TableRow

            val calculateQuantifier = (expandQuantifier + 1) * multiplier + multiplier / 10 * index
            val calculateResult = calculateQuantifier * conversionRate

            bigQuantifier = BigDecimal(calculateQuantifier).setScale(2, BigDecimal.ROUND_HALF_UP)
            bigResult = BigDecimal(calculateResult).setScale(2, BigDecimal.ROUND_HALF_UP)

            tableRow.quantifierTextView.text = decimalFormat.format(bigQuantifier).toString()
            tableRow.resultTextView.text = decimalFormat.format(bigResult).toString()

            expandedList.add(tableRow)
            tableLayout.addView(tableRow, (expandQuantifier + index))
        }
    }

    //    fun updateConversionRate(baseCurrency: Currency, quoteCurrency: Currency) {
//        mTableExpanded = false
//        conversionRate = quoteCurrency.rate / baseCurrency.rate
//    }
//
//
//
//    fun updateTable(activity: Activity, tableLayout: TableLayout) {
//        tableLayout.removeAllViews()
//
//        for (i in 0..9) {
//            val tableRow = activity.layoutInflater.inflate(R.layout.view_row, tableLayout, false) as TableRow
//            val quantifierTextView = tableRow.findViewById<View>(R.id.view_row_quantifier) as TextView
//            val resultTextView = tableRow.findViewById<View>(R.id.view_row_result) as TextView
//
//            val m1 = multiplier * (i + 1)
//            val m2 = m1 * conversionRate
//
//            bigQuantifier = BigDecimal(m1).setScale(2, BigDecimal.ROUND_HALF_UP)
//            bigResult = BigDecimal(m2).setScale(2, BigDecimal.ROUND_HALF_UP)
//
//            quantifierTextView.text = decimalFormat.format(bigQuantifier).toString()
//            resultTextView.text = decimalFormat.format(bigResult).toString()
//
//            quantifierTextView.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble))
//            resultTextView.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble))
//
//            setupRowListener(activity, tableLayout, quantifierTextView, i)
//            setupRowListener(activity, tableLayout, resultTextView, i)
//
//            tableLayout.addView(tableRow)
//        }
//    }
//
//    private fun setupRowListener(activity: Activity, tableLayout: TableLayout, textView: TextView, expandQuantifier: Int) {
//        textView.setOnTouchListener(object : OnSwipeTouch(activity) {
//            override fun onSingleTap() {
//                if (mTableExpanded!! && expandQuantifier == 0) {
//                    mTableExpanded = false
//                    updateTable(activity, tableLayout)
//
//                } else {
//                    if ((!mTableExpanded)!!) {
//                        mTableExpanded = true
//
//                        expandTable(activity, tableLayout, expandQuantifier)
//                        changeExpandedRow(activity, tableLayout)
//                    }
//                    tableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble))
//                }
//            }
//
//            override fun onSwipeLeft() {
//                if ((!mTableExpanded)!!) {
//                    multiplier = if (multiplier < 1000000) multiplier *= 10.0 else multiplier
//                    updateTable(activity, tableLayout)
//                } else {
//                    tableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble))
//                }
//            }
//
//            override fun onSwipeRight() {
//                if ((!mTableExpanded)!!) {
//                    multiplier = if (multiplier > 0.1) multiplier /= 10.0 else multiplier
//                    updateTable(activity, tableLayout)
//                } else {
//                    tableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble))
//                }
//            }
//
//            override fun onSwipeTop() {
//                if (mTableExpanded!!) {
//                    tableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble))
//                }
//            }
//
//            override fun onSwipeBottom() {
//                if (mTableExpanded!!) {
//                    tableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble))
//                }
//            }
//        })
//    }
//
//    private fun changeExpandedRow(activity: Activity, tableLayout: TableLayout) {
//        val m1 = tableLayout.getChildAt(0).findViewById(R.id.view_row_quantifier) as TextView
//        val m2 = tableLayout.getChildAt(0).findViewById(R.id.view_row_result) as TextView
//
//        ContextCompat.getColor(activity, R.color.amber).let {
//            m1.setBackgroundColor(it)
//            m2.setBackgroundColor(it)
//        }
//
//        ContextCompat.getColor(activity, R.color.black).let {
//            m1.setTextColor(it)
//            m2.setTextColor(it)
//        }
//    }
//
//    fun getmTableExpanded(): Boolean? {
//        return mTableExpanded
//    }
}