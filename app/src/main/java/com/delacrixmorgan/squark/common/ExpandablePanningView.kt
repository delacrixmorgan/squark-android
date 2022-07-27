package com.delacrixmorgan.squark.common

import android.annotation.SuppressLint
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
    private lateinit var config: ExpandablePanningViewConfig

    private var rowList = mutableListOf<ItemRowBinding>()
    private var expandedList = mutableListOf<ItemRowBinding>()

    private var isExpanded = false
    private var listener: RowListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.cv_expandable_panning, this, true)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupTable(
        thresholdTranslationWidth: Float,
        listener: RowListener,
        config: ExpandablePanningViewConfig
    ) {
        val thresholdSwipeWidth = thresholdTranslationWidth / 1.5F
        val alphaRatio = 1F / thresholdTranslationWidth
        val gestureDetector = GestureDetector(context, SingleTapConfirm())
        this.listener = listener
        this.config = config

        for (index in 0..9) {
            val tableRow = ItemRowBinding.inflate(LayoutInflater.from(context), tableLayout, false)

            tableRow.quantifierTextView.text = calculateRowQuantifier(config.multiplier, index)
            tableRow.resultTextView.text = calculateRowResult(config.multiplier, index, config.conversionRate)

            tableRow.beforeQuantifierTextView.text = calculateRowQuantifier(config.multiplier / 10, index)
            tableRow.beforeResultTextView.text = calculateRowResult(config.multiplier / 10, index, config.conversionRate)

            tableRow.nextQuantifierTextView.text = calculateRowQuantifier(config.multiplier * 10, index)
            tableRow.nextResultTextView.text = calculateRowResult(config.multiplier * 10, index, config.conversionRate)

            tableRow.root.setOnTouchListener { _, event ->
                if (gestureDetector.onTouchEvent(event)) {
                    onRowClicked(index)
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
                                    onSwipeLeft(config.multiplier)
                                } else {
                                    if (config.multiplier > 0.1) {
                                        config.multiplier /= 10
                                    }
                                    onSwipeRight(config.multiplier)
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
                        MotionEvent.ACTION_DOWN -> {
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

    fun updateTable(config: ExpandablePanningViewConfig) {
        performHapticContextClick()
        this.config = config
        rowList.forEachIndexed { index, tableRow ->
            with(tableRow) {
                quantifierTextView.text = calculateRowQuantifier(config.multiplier, index)
                resultTextView.text = calculateRowResult(config.multiplier, index, config.conversionRate)

                nextQuantifierTextView.text = calculateRowQuantifier(config.multiplier / 10, index)
                nextResultTextView.text = calculateRowResult(config.multiplier / 10, index, config.conversionRate)

                beforeQuantifierTextView.text = calculateRowQuantifier(config.multiplier * 10, index)
                beforeResultTextView.text = calculateRowResult(config.multiplier * 10, index, config.conversionRate)

                quantifierTextView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(root.context, R.anim.wobble))
                resultTextView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(root.context, R.anim.wobble))
            }
        }
    }

    private fun expandTable(
        expandQuantifier: Int,
        listener: RowListener,
        config: ExpandablePanningViewConfig
    ) {
        for (index in 1..9) {
            val tableRow = ItemRowBinding.inflate(LayoutInflater.from(context), tableLayout, false)
            tableRow.root.background = ContextCompat.getDrawable(context, R.drawable.shape_cell_light)
            tableRow.quantifierTextView.text = calculateExpandQuantifier(
                expandQuantifier, config.multiplier, index
            )
            tableRow.resultTextView.text = calculateExpandResult(
                expandQuantifier,
                config.multiplier, index,
                config.conversionRate
            )
            tableRow.root.setOnClickListener { onRowClicked(index) }
            expandedList.add(tableRow)
            tableLayout.addView(tableRow.root, (expandQuantifier + index))
        }
    }

    fun onRowExpand(
        selectedRow: Int,
        listener: RowListener
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
            expandQuantifier = selectedRow,
            listener = listener,
            config = config
        )
    }

    fun onRowCollapse() {
        if (!isExpanded) return
        performHapticContextClick()
        expandedList.forEach { tableLayout.removeView(it.root) }
        rowList.forEach {
            it.root.isVisible = true
            it.root.background = ContextCompat.getDrawable(context, R.drawable.shape_cell_dark)
        }
    }

    private fun onSwipeLeft(multiplier: Double) {
        if (!isExpanded) {
            config.multiplier = multiplier
            if (SharedPreferenceHelper.isPersistentMultiplierEnabled) {
                SharedPreferenceHelper.multiplier = config.multiplier.toInt()
            }
            updateTable(config)
            listener?.onSwiped(config.multiplier)
        }
    }

    private fun onSwipeRight(multiplier: Double) {
        if (!isExpanded) {
            config.multiplier = multiplier
            if (SharedPreferenceHelper.isPersistentMultiplierEnabled) {
                SharedPreferenceHelper.multiplier = config.multiplier.toInt()
            }
            updateTable(config)
            listener?.onSwiped(config.multiplier)
        }
    }

    private fun onRowClicked(position: Int) {
        if (isExpanded) {
            onRowCollapse()
        } else {
            onRowExpand(
                selectedRow = position,
                listener = requireNotNull(listener)
            )
        }
        isExpanded = !isExpanded
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