package com.delacrixmorgan.squark

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.view.animation.AnimationUtils
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

import com.delacrixmorgan.squark.deprecrated.listener.OnSwipeTouch
import com.delacrixmorgan.squark.deprecrated.model.Currency

import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by Delacrix Morgan on 03/07/2017.
 **/

class SquarkEngine {

    companion object {
        @Volatile private lateinit var SquarkEngineInstance: SquarkEngine

        fun newInstance(): SquarkEngine {
            SquarkEngineInstance = SquarkEngine()
            return SquarkEngineInstance
        }

        fun getInstance(): SquarkEngine = SquarkEngineInstance
    }

    private var mMultiplier: Double = 0.toDouble()
    private var mConversionRate: Double = 0.toDouble()
    private var mTableExpanded: Boolean? = null
    private val mDecimalFormat: DecimalFormat
    private var mBigQuantifier: BigDecimal? = null
    private var mBigResult: BigDecimal? = null

    init {
        mConversionRate = 1.0
        mMultiplier = 1.0

        mDecimalFormat = DecimalFormat("###,##0.00")
        mTableExpanded = false
    }

//    fun updateConversionRate(baseCurrency: Currency, quoteCurrency: Currency) {
//        mTableExpanded = false
//        mConversionRate = quoteCurrency.rate / baseCurrency.rate
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
//            val m1 = mMultiplier * (i + 1)
//            val m2 = m1 * mConversionRate
//
//            mBigQuantifier = BigDecimal(m1).setScale(2, BigDecimal.ROUND_HALF_UP)
//            mBigResult = BigDecimal(m2).setScale(2, BigDecimal.ROUND_HALF_UP)
//
//            quantifierTextView.text = mDecimalFormat.format(mBigQuantifier).toString()
//            resultTextView.text = mDecimalFormat.format(mBigResult).toString()
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
//    private fun expandTable(activity: Activity, tableLayout: TableLayout, expandQuantifier: Int) {
//        tableLayout.removeAllViews()
//
//        for (i in 0..10) {
//            val tableRow = activity.layoutInflater.inflate(R.layout.view_row, tableLayout, false) as TableRow
//            val quantifierTextView = tableRow.findViewById<View>(R.id.view_row_quantifier) as TextView
//            val resultTextView = tableRow.findViewById<View>(R.id.view_row_result) as TextView
//
//            val m1 = (expandQuantifier + 1) * mMultiplier + mMultiplier / 10 * i
//            val m2 = m1 * mConversionRate
//
//            mBigQuantifier = BigDecimal(m1).setScale(2, BigDecimal.ROUND_HALF_UP)
//            mBigResult = BigDecimal(m2).setScale(2, BigDecimal.ROUND_HALF_UP)
//
//            quantifierTextView.text = mDecimalFormat.format(mBigQuantifier).toString()
//            resultTextView.text = mDecimalFormat.format(mBigResult).toString()
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
//                    mMultiplier = if (mMultiplier < 1000000) mMultiplier *= 10.0 else mMultiplier
//                    updateTable(activity, tableLayout)
//                } else {
//                    tableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble))
//                }
//            }
//
//            override fun onSwipeRight() {
//                if ((!mTableExpanded)!!) {
//                    mMultiplier = if (mMultiplier > 0.1) mMultiplier /= 10.0 else mMultiplier
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