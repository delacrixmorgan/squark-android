package com.delacrixmorgan.squark.ui.currency

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TableLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.RowListener
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.common.calculateExpandQuantifier
import com.delacrixmorgan.squark.common.calculateExpandResult
import com.delacrixmorgan.squark.common.calculateRowQuantifier
import com.delacrixmorgan.squark.common.calculateRowResult
import com.delacrixmorgan.squark.common.performHapticContextClick
import com.delacrixmorgan.squark.common.roundUp
import com.delacrixmorgan.squark.databinding.FragmentCurrencyBinding
import com.delacrixmorgan.squark.databinding.ItemRowBinding
import com.delacrixmorgan.squark.ui.preference.PreferenceNavigationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@AndroidEntryPoint
class CurrencyFragment : Fragment(R.layout.fragment_currency), RowListener {

    companion object {
        const val EXTRA_CURRENCY = "CurrencyFragment.currency"
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentCurrencyBinding? = null

    private var isExpanded = false

    private var rowList = arrayListOf<ItemRowBinding>()
    private var expandedList = arrayListOf<ItemRowBinding>()

    private val viewModel: CurrencyViewModel by viewModels()

    private val requestBaseCountryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val countryCode = result.data?.getStringExtra(EXTRA_CURRENCY)
                SharedPreferenceHelper.baseCurrency = countryCode

                if (isExpanded) onRowCollapse()
                updateTable()
            }
        }

    private val requestQuoteCountryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val countryCode = result.data?.getStringExtra(EXTRA_CURRENCY)
                SharedPreferenceHelper.quoteCurrency = countryCode

                if (isExpanded) onRowCollapse()
                updateTable()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (SharedPreferenceHelper.isPersistentMultiplierEnabled) {
            viewModel.multiplier = SharedPreferenceHelper.multiplier.toDouble()
        }

        setupTable(
            activity = requireActivity(),
            tableLayout = binding.currencyTableLayout,
            rowList = rowList,
            listener = this
        )

        binding.baseCurrencyTextView.setOnClickListener {
            val currencyIntent = PreferenceNavigationActivity.newLaunchIntent(
                view.context, requireNotNull(viewModel.baseCurrency)
            )
            requestBaseCountryLauncher.launch(currencyIntent)
        }

        binding.quoteCurrencyTextView.setOnClickListener {
            val currencyIntent = PreferenceNavigationActivity.newLaunchIntent(
                view.context, requireNotNull(viewModel.quoteCurrency)
            )
            requestQuoteCountryLauncher.launch(currencyIntent)
        }

        binding.swapButton.setOnClickListener {
            val baseCurrencyCode = viewModel.baseCurrency?.code
            val quoteCurrencyCode = viewModel.quoteCurrency?.code

            SharedPreferenceHelper.baseCurrency = quoteCurrencyCode
            SharedPreferenceHelper.quoteCurrency = baseCurrencyCode

            binding.swapButton.performHapticContextClick()
            updateTable()
        }

        lifecycleScope.launch {
            viewModel.onStart()
            viewModel.uiState.collect {
                when (it) {
                    CurrencyUiState.Start -> Unit
                    is CurrencyUiState.Success -> updateTable()
                    is CurrencyUiState.Failure -> Unit
                }
            }
        }
    }

    private fun updateTable() {
        binding.baseCurrencyTextView.text = viewModel.baseCurrency?.code
        binding.quoteCurrencyTextView.text = viewModel.quoteCurrency?.code

        if (viewModel.baseCurrency?.rate != 0.0 && viewModel.quoteCurrency?.rate != 0.0) {
            viewModel.updateConversionRate(viewModel.baseCurrency?.rate, viewModel.quoteCurrency?.rate)
            updateTable(rowList)
        }
    }

    // TODO (Move to Custom View)
    private fun onRowExpand(selectedRow: Int) {
        binding.currencyTableLayout.performHapticContextClick()
        rowList.forEachIndexed { index, tableRow ->
            if (index != selectedRow && index != (selectedRow + 1)) {
                tableRow.root.isVisible = false
            } else {
                tableRow.root.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )
            }
        }

        expandTable(
            activity = requireActivity(),
            tableLayout = binding.currencyTableLayout,
            expandQuantifier = selectedRow,
            expandedList = expandedList,
            listener = this
        )
    }

    private fun onRowCollapse() {
        binding.currencyTableLayout.performHapticContextClick()
        expandedList.forEach {
            binding.currencyTableLayout.removeView(it.root)
        }

        rowList.forEach {
            it.root.isVisible = true
            it.root.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_cell_dark)
        }
    }

    /**
     * RowListener
     */
    override fun onSwipeLeft(multiplier: Double) {
        if (!isExpanded) {
            if (SharedPreferenceHelper.isPersistentMultiplierEnabled) {
                SharedPreferenceHelper.multiplier = multiplier.toInt()
            }

            binding.currencyTableLayout.performHapticContextClick()
            updateTable(rowList)
        }
    }

    override fun onSwipeRight(multiplier: Double) {
        if (!isExpanded) {
            if (SharedPreferenceHelper.isPersistentMultiplierEnabled) {
                SharedPreferenceHelper.multiplier = multiplier.toInt()
            }

            binding.currencyTableLayout.performHapticContextClick()
            updateTable(rowList)
        }
    }

    override fun onRowClicked(position: Int) {
        if (isExpanded) {
            onRowCollapse()
        } else {
            onRowExpand(position)
        }

        isExpanded = !isExpanded
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupTable(
        activity: Activity,
        tableLayout: TableLayout,
        rowList: ArrayList<ItemRowBinding>,
        listener: RowListener
    ) = with(viewModel) {
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

    private fun updateTable(
        rowList: ArrayList<ItemRowBinding>
    ) = with(viewModel) {
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

    private fun expandTable(
        activity: Activity,
        tableLayout: TableLayout,
        expandQuantifier: Int,
        expandedList: ArrayList<ItemRowBinding>,
        listener: RowListener
    ) = with(viewModel) {
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

    private class SingleTapConfirm : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent) = true
    }
}