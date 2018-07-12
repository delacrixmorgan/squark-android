package com.delacrixmorgan.squark

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.TableLayout
import android.widget.TableRow
import com.delacrixmorgan.squark.common.RowListener
import kotlinx.android.synthetic.main.fragment_launch.*
import kotlinx.android.synthetic.main.view_row.view.*
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.absoluteValue

/**
 * SquarkEngine
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

object SquarkEngine {

    private var anchorPosition = 0F
    private var multiplier: Double = 1.0
    private var conversionRate: Double = 1.0
    private var bigResult: BigDecimal? = null
    private var bigQuantifier: BigDecimal? = null
    private val decimalFormat: DecimalFormat = DecimalFormat("###,##0.00")

    fun updateConversionRate(baseRate: Double? = 1.0, quoteRate: Double? = 1.0) {
        this.conversionRate = quoteRate!! / baseRate!!
    }

    fun setupTable(
            activity: Activity,
            tableLayout: TableLayout,
            rowList: ArrayList<TableRow>,
            listener: RowListener
    ) {
        val thresholdWidth = activity.resources.displayMetrics.widthPixels / 6F
        val alphaRatio = 1F / thresholdWidth

        activity.currencyTableLayout.setOnTouchListener { _, event ->
            activity.currencyTableLayout.onTouchEvent(event)

            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    val currentPosition = rowList.firstOrNull()?.translationX ?: 0F
                    if (currentPosition > 0) {
                        if (this.multiplier < 1000000) {
                            this.multiplier *= 10
                        }
                        listener.onSwipeLeft()
                    } else {
                        if (this.multiplier > 0.1) {
                            this.multiplier /= 10
                        }
                        listener.onSwipeRight()
                    }

                    rowList.map {
                        it.translationX = 0F
                        it.quantifierTextView.alpha = 1F
                        it.resultTextView.alpha = 1F

                        it.nextQuantifierTextView.alpha = 0F
                        it.nextResultTextView.alpha = 0F

                        it.beforeQuantifierTextView.alpha = 0F
                        it.beforeResultTextView.alpha = 0F
                    }
                }

                MotionEvent.ACTION_DOWN -> {
                    this.anchorPosition = event.rawX
                }

                MotionEvent.ACTION_MOVE -> {
                    val movingPixels = event.rawX - this.anchorPosition
                    if (movingPixels.absoluteValue < thresholdWidth) {
                        val alpha = movingPixels.absoluteValue * alphaRatio
                        rowList.map {
                            it.translationX = movingPixels

                            it.quantifierTextView.alpha = Math.round((1F - alpha) * 10F) / 10F
                            it.resultTextView.alpha = Math.round((1F - alpha) * 10F) / 10F

                            if (movingPixels > 0) {
                                it.nextQuantifierTextView.alpha = Math.round(alpha * 10F) / 10F
                                it.nextResultTextView.alpha = Math.round(alpha * 10F) / 10F
                            } else {
                                it.beforeQuantifierTextView.alpha = Math.round(alpha * 10F) / 10F
                                it.beforeResultTextView.alpha = Math.round(alpha * 10F) / 10F
                            }
                        }
                    } else {
                        rowList.map {
                            it.quantifierTextView.alpha = 0F
                            it.resultTextView.alpha = 0F

                            if (movingPixels > 0) {
                                it.nextQuantifierTextView.alpha = 1F
                                it.nextResultTextView.alpha = 1F
                            } else {
                                it.beforeQuantifierTextView.alpha = 1F
                                it.beforeResultTextView.alpha = 1F
                            }
                        }
                    }
                }
            }
            true
        }

        for (index in 0..9) {
            val tableRow = activity.layoutInflater.inflate(R.layout.view_row, tableLayout, false) as TableRow


            // PRESENT
            var calculateQuantifier = (this.multiplier * (index + 1))
            var calculateResult = calculateQuantifier * this.conversionRate

            this.bigQuantifier = BigDecimal(calculateQuantifier).setScale(2, BigDecimal.ROUND_HALF_UP)
            this.bigResult = BigDecimal(calculateResult).setScale(2, BigDecimal.ROUND_HALF_UP)

            tableRow.quantifierTextView.text = this.decimalFormat.format(this.bigQuantifier).toString()
            tableRow.resultTextView.text = this.decimalFormat.format(this.bigResult).toString()


            // BEFORE
            calculateQuantifier = (this.multiplier / 10 * (index + 1))
            calculateResult = calculateQuantifier * this.conversionRate

            this.bigQuantifier = BigDecimal(calculateQuantifier).setScale(2, BigDecimal.ROUND_HALF_UP)
            this.bigResult = BigDecimal(calculateResult).setScale(2, BigDecimal.ROUND_HALF_UP)

            tableRow.beforeQuantifierTextView.text = this.decimalFormat.format(this.bigQuantifier).toString()
            tableRow.beforeResultTextView.text = this.decimalFormat.format(this.bigResult).toString()

            // NEXT

            calculateQuantifier = (this.multiplier * 10 * (index + 1))
            calculateResult = calculateQuantifier * this.conversionRate

            this.bigQuantifier = BigDecimal(calculateQuantifier).setScale(2, BigDecimal.ROUND_HALF_UP)
            this.bigResult = BigDecimal(calculateResult).setScale(2, BigDecimal.ROUND_HALF_UP)

            tableRow.nextQuantifierTextView.text = this.decimalFormat.format(this.bigQuantifier).toString()
            tableRow.nextResultTextView.text = this.decimalFormat.format(this.bigResult).toString()

            // TODO - Restore Click When Ready
//            tableRow.setOnClickListener {
//                listener.onClick(index)
//            }

            rowList.add(tableRow)
            tableLayout.addView(tableRow)
        }
    }

    fun updateTable(rowList: ArrayList<TableRow>) {
        rowList.forEachIndexed { index, tableRow ->
            // PRESENT
            var calculateQuantifier = (this.multiplier * (index + 1))
            var calculateResult = calculateQuantifier * this.conversionRate

            this.bigQuantifier = BigDecimal(calculateQuantifier).setScale(2, BigDecimal.ROUND_HALF_UP)
            this.bigResult = BigDecimal(calculateResult).setScale(2, BigDecimal.ROUND_HALF_UP)

            tableRow.quantifierTextView.text = this.decimalFormat.format(this.bigQuantifier).toString()
            tableRow.resultTextView.text = this.decimalFormat.format(this.bigResult).toString()

            // BEFORE
            calculateQuantifier = (this.multiplier / 10 * (index + 1))
            calculateResult = calculateQuantifier * this.conversionRate

            this.bigQuantifier = BigDecimal(calculateQuantifier).setScale(2, BigDecimal.ROUND_HALF_UP)
            this.bigResult = BigDecimal(calculateResult).setScale(2, BigDecimal.ROUND_HALF_UP)

            tableRow.beforeQuantifierTextView.text = this.decimalFormat.format(this.bigQuantifier).toString()
            tableRow.beforeResultTextView.text = this.decimalFormat.format(this.bigResult).toString()

            // NEXT
            calculateQuantifier = (this.multiplier * 10 * (index + 1))
            calculateResult = calculateQuantifier * this.conversionRate

            this.bigQuantifier = BigDecimal(calculateQuantifier).setScale(2, BigDecimal.ROUND_HALF_UP)
            this.bigResult = BigDecimal(calculateResult).setScale(2, BigDecimal.ROUND_HALF_UP)

            tableRow.nextQuantifierTextView.text = this.decimalFormat.format(this.bigQuantifier).toString()
            tableRow.nextResultTextView.text = this.decimalFormat.format(this.bigResult).toString()

            tableRow.quantifierTextView.startAnimation(AnimationUtils.loadAnimation(tableRow.context, R.anim.wobble))
            tableRow.resultTextView.startAnimation(AnimationUtils.loadAnimation(tableRow.context, R.anim.wobble))
        }
    }

    fun expandTable(
            activity: Activity,
            tableLayout: TableLayout,
            expandQuantifier: Int,
            expandedList: ArrayList<TableRow>,
            listener: RowListener
    ) {
        for (index in 1..9) {
            val tableRow = activity.layoutInflater.inflate(R.layout.view_row, tableLayout, false) as TableRow
            tableRow.setBackgroundColor(ContextCompat.getColor(activity, R.color.borderGrey))

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