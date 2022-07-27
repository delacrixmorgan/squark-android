package com.delacrixmorgan.squark.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.TableLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.databinding.ItemRowBinding
import kotlin.math.absoluteValue

class ExpandablePanningView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val tableLayout: TableLayout by lazy { findViewById(R.id.tableLayout) }

    private var rowList = arrayListOf<ItemRowBinding>()
    private var expandedList = arrayListOf<ItemRowBinding>()

    init {
        LayoutInflater.from(context).inflate(R.layout.cv_expandable_panning, this, true)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupTable(
        activity: Activity,
        listener: RowListener,
        config: ExpandablePanningViewConfig
    ) {
        val thresholdTranslationWidth = activity.resources.displayMetrics.widthPixels / 6F
        val thresholdSwipeWidth = thresholdTranslationWidth / 1.5F
        val alphaRatio = 1F / thresholdTranslationWidth
        val gestureDetector = GestureDetector(activity, SingleTapConfirm())

        for (index in 0..9) {
            val tableRow = ItemRowBinding.inflate(
                LayoutInflater.from(activity), tableLayout, false
            )
            tableRow.quantifierTextView.text = calculateRowQuantifier(config.multiplier, index)
            tableRow.resultTextView.text = calculateRowResult(
                config.multiplier, index, config.conversionRate
            )

            tableRow.beforeQuantifierTextView.text = calculateRowQuantifier(config.multiplier / 10, index)
            tableRow.beforeResultTextView.text = calculateRowResult(
                config.multiplier / 10, index, config.conversionRate
            )

            tableRow.nextQuantifierTextView.text = calculateRowQuantifier(config.multiplier * 10, index)
            tableRow.nextResultTextView.text = calculateRowResult(
                config.multiplier * 10, index, config.conversionRate
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
                                    if (config.multiplier < 10000000000) {
                                        config.multiplier *= 10
                                    }
                                    listener.onSwipeLeft(config.multiplier)
                                } else {
                                    if (config.multiplier > 0.1) {
                                        config.multiplier /= 10
                                    }
                                    listener.onSwipeRight(config.multiplier)
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
                            val movingPixels = event.rawX - config.anchorPosition
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

                        android.view.MotionEvent.ACTION_DOWN -> {
                            tableRow.root.performHapticContextClick()
                            config.anchorPosition = event.rawX
                        }
                    }
                }
                true
            }

            rowList.add(tableRow)
            tableLayout.addView(tableRow.root)
        }
    }

    fun updateTable(
        config: ExpandablePanningViewConfig
    ) {
        performHapticContextClick()
        rowList.forEachIndexed { index, tableRow ->
            with(tableRow) {
                quantifierTextView.text = calculateRowQuantifier(config.multiplier, index)
                resultTextView.text = calculateRowResult(config.multiplier, index, config.conversionRate)

                nextQuantifierTextView.text = calculateRowQuantifier(config.multiplier / 10, index)
                nextResultTextView.text =
                    calculateRowResult(config.multiplier / 10, index, config.conversionRate)

                beforeQuantifierTextView.text = calculateRowQuantifier(config.multiplier * 10, index)
                beforeResultTextView.text =
                    calculateRowResult(config.multiplier * 10, index, config.conversionRate)

                quantifierTextView.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(root.context, com.delacrixmorgan.squark.R.anim.wobble)
                )
                resultTextView.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(root.context, com.delacrixmorgan.squark.R.anim.wobble)
                )
            }
        }
    }

    fun expandTable(
        activity: Activity,
        expandQuantifier: Int,
        listener: RowListener,
        config: ExpandablePanningViewConfig
    ) {
        for (index in 1..9) {
            val tableRow = ItemRowBinding.inflate(
                LayoutInflater.from(activity), tableLayout, false
            )

            tableRow.root.background = ContextCompat.getDrawable(activity, R.drawable.shape_cell_light)

            tableRow.quantifierTextView.text = calculateExpandQuantifier(
                expandQuantifier, config.multiplier, index
            )
            tableRow.resultTextView.text = calculateExpandResult(
                expandQuantifier,
                config.multiplier, index,
                config.conversionRate
            )

            tableRow.root.setOnClickListener { listener.onRowClicked(index) }
            expandedList.add(tableRow)
            tableLayout.addView(tableRow.root, (expandQuantifier + index))
        }
    }

    fun onRowExpand(
        activity: Activity,
        selectedRow: Int,
        listener: RowListener,
        config: ExpandablePanningViewConfig
    ) {
        performHapticContextClick()
        rowList.forEachIndexed { index, tableRow ->
            if (index != selectedRow && index != (selectedRow + 1)) {
                tableRow.root.isVisible = false
            } else {
                tableRow.root.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
            }
        }

        expandTable(
            activity = activity,
            expandQuantifier = selectedRow,
            listener = listener,
            config = config
        )
    }

    fun onRowCollapse() {
        performHapticContextClick()
        expandedList.forEach { tableLayout.removeView(it.root) }
        rowList.forEach {
            it.root.isVisible = true
            it.root.background = ContextCompat.getDrawable(context, R.drawable.shape_cell_dark)
        }
    }

    private class SingleTapConfirm : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent) = true
    }
}

class ExpandablePanningViewConfig(
    var multiplier: Double = 1.0,
    var conversionRate: Double = 1.0,
    var anchorPosition: Float = 0F
)