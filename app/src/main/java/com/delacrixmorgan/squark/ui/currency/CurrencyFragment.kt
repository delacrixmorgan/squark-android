package com.delacrixmorgan.squark.ui.currency

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.RowListener
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.common.getPreferenceCurrency
import com.delacrixmorgan.squark.common.performHapticContextClick
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.databinding.FragmentCurrencyBinding
import com.delacrixmorgan.squark.databinding.ItemRowBinding
import com.delacrixmorgan.squark.models.Country
import com.delacrixmorgan.squark.ui.preference.PreferenceNavigationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyFragment : Fragment(R.layout.fragment_currency), RowListener {

    companion object {
        const val EXTRA_COUNTRY_CODE = "CurrencyNavigationFragment.countryCode"
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentCurrencyBinding? = null

    private var isExpanded = false
    private var baseCountry: Country? = null

    private var quoteCountry: Country? = null

    private var rowList = arrayListOf<ItemRowBinding>()
    private var expandedList = arrayListOf<ItemRowBinding>()

    private val viewModel: CurrencyViewModel by viewModels()

    private val requestBaseCountryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val countryCode = result.data?.getStringExtra(EXTRA_COUNTRY_CODE)
                SharedPreferenceHelper.baseCurrency = countryCode

                if (isExpanded) onRowCollapse()
                updateTable()
            }
        }

    private val requestQuoteCountryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val countryCode = result.data?.getStringExtra(EXTRA_COUNTRY_CODE)
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
            viewModel.updateMultiplier(SharedPreferenceHelper.multiplier)
        }

        viewModel.setupTable(
            activity = requireActivity(),
            tableLayout = binding.currencyTableLayout,
            rowList = rowList,
            listener = this
        )

        binding.baseCurrencyTextView.setOnClickListener {
            val currencyIntent = PreferenceNavigationActivity.newLaunchIntent(
                view.context,
                countryCode = baseCountry?.code
            )
            requestBaseCountryLauncher.launch(currencyIntent)
        }

        binding.quoteCurrencyTextView.setOnClickListener {
            val currencyIntent = PreferenceNavigationActivity.newLaunchIntent(
                view.context,
                countryCode = quoteCountry?.code
            )
            requestQuoteCountryLauncher.launch(currencyIntent)
        }

        binding.swapButton.setOnClickListener {
            SharedPreferenceHelper.apply {
                baseCurrency = quoteCountry?.code
                quoteCurrency = baseCountry?.code
            }

            binding.swapButton.performHapticContextClick()
            updateTable()
        }

        updateTable()
    }

    private fun updateTable() {
        baseCountry = CountryDataController.getPreferenceCurrency(
            SharedPreferenceHelper.baseCurrency
        )
        quoteCountry = CountryDataController.getPreferenceCurrency(
            SharedPreferenceHelper.quoteCurrency
        )

        binding.baseCurrencyTextView.text = baseCountry?.code
        binding.quoteCurrencyTextView.text = quoteCountry?.code

        if (baseCountry?.rate != 0.0 && quoteCountry?.rate != 0.0) {
            viewModel.updateConversionRate(baseCountry?.rate, quoteCountry?.rate)
            viewModel.updateTable(rowList)
        }
    }

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

        viewModel.expandTable(
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
            viewModel.updateTable(rowList)
        }
    }

    override fun onSwipeRight(multiplier: Double) {
        if (!isExpanded) {
            if (SharedPreferenceHelper.isPersistentMultiplierEnabled) {
                SharedPreferenceHelper.multiplier = multiplier.toInt()
            }

            binding.currencyTableLayout.performHapticContextClick()
            viewModel.updateTable(rowList)
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
}