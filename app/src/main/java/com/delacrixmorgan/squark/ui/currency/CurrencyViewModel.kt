package com.delacrixmorgan.squark.ui.currency

import android.annotation.SuppressLint
import android.app.Activity
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.TableLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.*
import com.delacrixmorgan.squark.databinding.ItemRowBinding
import kotlin.math.absoluteValue

class CurrencyViewModel : ViewModel() {
    private var anchorPosition = 0F
    private var multiplier: Double = 1.0
    private var conversionRate: Double = 1.0

    fun updateConversionRate(baseRate: Double? = 1.0, quoteRate: Double? = 1.0) {
        conversionRate = (quoteRate ?: 1.0) / (baseRate ?: 1.0)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupTable(
        activity: Activity,
        tableLayout: TableLayout,
        rowList: ArrayList<ItemRowBinding>,
        listener: RowListener
    ) {
        val thresholdTranslationWidth = activity.resources.displayMetrics.widthPixels / 6F
        val thresholdSwipeWidth = thresholdTranslationWidth / 1.5F
        val alphaRatio = 1F / thresholdTranslationWidth
        val gestureDetector = GestureDetector(activity, SingleTapConfirm())

        for (index in 0..9) {
            val tableRow = ItemRowBinding.inflate(
                LayoutInflater.from(activity), tableLayout, false
            )
            tableRow.quantifierTextView.text = calculateRowQuantifier(multiplier, index)
            tableRow.resultTextView.text = calculateRowResult(
                multiplier, index, conversionRate
            )

            tableRow.beforeQuantifierTextView.text = calculateRowQuantifier(multiplier / 10, index)
            tableRow.beforeResultTextView.text = calculateRowResult(
                multiplier / 10, index, conversionRate
            )

            tableRow.nextQuantifierTextView.text = calculateRowQuantifier(multiplier * 10, index)
            tableRow.nextResultTextView.text = calculateRowResult(
                multiplier * 10, index, conversionRate
            )

            tableRow.root.setOnTouchListener { _, event ->
                if (gestureDetector.onTouchEvent(event)) {
                    listener.onRowClicked(index)
                    rowList.forEach {
                        it.root.translationX = 0F
                        it.quantifierTextView.alpha = 1F
                        it.resultTextView.alpha = 1F

                        it.nextQuantifierTextView.alpha = 0F
                        it.nextResultTextView.alpha = 0F

                        it.beforeQuantifierTextView.alpha = 0F
                        it.beforeResultTextView.alpha = 0F
                    }
                } else {
                    tableRow.root.onTouchEvent(event)
                    when (event.action) {
                        MotionEvent.ACTION_UP -> {
                            val currentPosition = rowList.firstOrNull()?.root?.translationX ?: 0F

                            if (currentPosition.absoluteValue > thresholdSwipeWidth) {
                                if (currentPosition < 0) {
                                    if (multiplier < 10000000000) {
                                        multiplier *= 10
                                    }
                                    listener.onSwipeLeft(multiplier)
                                } else {
                                    if (multiplier > 0.1) {
                                        multiplier /= 10
                                    }
                                    listener.onSwipeRight(multiplier)
                                }
                            }

                            rowList.forEach {
                                it.root.translationX = 0F
                                it.quantifierTextView.alpha = 1F
                                it.resultTextView.alpha = 1F

                                it.nextQuantifierTextView.alpha = 0F
                                it.nextResultTextView.alpha = 0F

                                it.beforeQuantifierTextView.alpha = 0F
                                it.beforeResultTextView.alpha = 0F
                            }
                        }

                        MotionEvent.ACTION_MOVE -> {
                            val movingPixels = event.rawX - anchorPosition
                            if (movingPixels.absoluteValue < thresholdTranslationWidth) {

                                val alpha = movingPixels.absoluteValue * alphaRatio
                                rowList.forEach {
                                    it.root.translationX = movingPixels

                                    it.quantifierTextView.alpha = (1F - alpha).roundUp()
                                    it.resultTextView.alpha = (1F - alpha).roundUp()

                                    if (movingPixels > 0) {
                                        it.nextQuantifierTextView.alpha = alpha.roundUp()
                                        it.nextResultTextView.alpha = alpha.roundUp()
                                    } else {
                                        it.beforeQuantifierTextView.alpha = alpha.roundUp()
                                        it.beforeResultTextView.alpha = alpha.roundUp()
                                    }
                                }
                            } else {
                                rowList.forEach {
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

                        MotionEvent.ACTION_DOWN -> {
                            tableRow.root.performHapticContextClick()
                            anchorPosition = event.rawX
                        }
                    }
                }
                true
            }

            rowList.add(tableRow)
            tableLayout.addView(tableRow.root)
        }
    }

    fun updateTable(rowList: ArrayList<ItemRowBinding>) {
        rowList.forEachIndexed { index, tableRow ->
            with(tableRow) {
                quantifierTextView.text = calculateRowQuantifier(multiplier, index)
                resultTextView.text = calculateRowResult(multiplier, index, conversionRate)

                nextQuantifierTextView.text = calculateRowQuantifier(multiplier / 10, index)
                nextResultTextView.text =
                    calculateRowResult(multiplier / 10, index, conversionRate)

                beforeQuantifierTextView.text = calculateRowQuantifier(multiplier * 10, index)
                beforeResultTextView.text =
                    calculateRowResult(multiplier * 10, index, conversionRate)

                quantifierTextView.startAnimation(
                    AnimationUtils.loadAnimation(root.context, R.anim.wobble)
                )
                resultTextView.startAnimation(
                    AnimationUtils.loadAnimation(root.context, R.anim.wobble)
                )
            }
        }
    }

    fun expandTable(
        activity: Activity,
        tableLayout: TableLayout,
        expandQuantifier: Int,
        expandedList: ArrayList<ItemRowBinding>,
        listener: RowListener
    ) {
        for (index in 1..9) {
            val tableRow = ItemRowBinding.inflate(
                LayoutInflater.from(activity), tableLayout, false
            )

            tableRow.root.background = ContextCompat.getDrawable(
                activity,
                R.drawable.shape_cell_light
            )

            tableRow.quantifierTextView.text = calculateExpandQuantifier(
                expandQuantifier, multiplier, index
            )
            tableRow.resultTextView.text = calculateExpandResult(
                expandQuantifier,
                multiplier, index,
                conversionRate
            )

            tableRow.root.setOnClickListener { listener.onRowClicked(index) }
            expandedList.add(tableRow)
            tableLayout.addView(tableRow.root, (expandQuantifier + index))
        }
    }

    fun updateMultiplier(multiplier: Int) {
        this.multiplier = multiplier.toDouble()
    }

    private class SingleTapConfirm : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent) = true
    }
}