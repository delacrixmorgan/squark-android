package com.delacrixmorgan.squark.ui.currency

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.RowListener
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.common.performHapticContextClick
import com.delacrixmorgan.squark.databinding.FragmentCurrencyBinding
import com.delacrixmorgan.squark.ui.preference.PreferenceNavigationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyFragment : Fragment(R.layout.fragment_currency), RowListener {

    companion object {
        const val EXTRA_CURRENCY = "CurrencyFragment.currency"
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentCurrencyBinding? = null

    private var isExpanded = false

    private val viewModel: CurrencyViewModel by viewModels()

    private val requestBaseCountryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val countryCode = result.data?.getStringExtra(EXTRA_CURRENCY)
                SharedPreferenceHelper.baseCurrency = countryCode

                if (isExpanded) binding.currencyTableLayout.onRowCollapse()
                updateTable()
            }
        }

    private val requestQuoteCountryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val countryCode = result.data?.getStringExtra(EXTRA_CURRENCY)
                SharedPreferenceHelper.quoteCurrency = countryCode

                if (isExpanded) binding.currencyTableLayout.onRowCollapse()
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

        binding.currencyTableLayout.setupTable(
            requireActivity(), this, viewModel.expandablePanningViewConfig
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
//            CurrencyBottomSheetDialogFragment.show(requireActivity().supportFragmentManager)
        }

        // TODO (Remove When Ready)
//        requireActivity().supportFragmentManager.commit {
//            replace(android.R.id.content, WallpaperFragment.create())
//        }

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
            binding.currencyTableLayout.updateTable(viewModel.expandablePanningViewConfig)
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
            binding.currencyTableLayout.updateTable(viewModel.expandablePanningViewConfig)
        }
    }

    override fun onSwipeRight(multiplier: Double) {
        if (!isExpanded) {
            if (SharedPreferenceHelper.isPersistentMultiplierEnabled) {
                SharedPreferenceHelper.multiplier = multiplier.toInt()
            }
            binding.currencyTableLayout.updateTable(viewModel.expandablePanningViewConfig)
        }
    }

    override fun onRowClicked(position: Int) {
        if (isExpanded) {
            binding.currencyTableLayout.onRowCollapse()
        } else {
            binding.currencyTableLayout.onRowExpand(
                activity = requireActivity(),
                selectedRow = position,
                listener = this,
                config = viewModel.expandablePanningViewConfig
            )
        }
        isExpanded = !isExpanded
    }
}