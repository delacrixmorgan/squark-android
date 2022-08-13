package com.delacrixmorgan.squark.ui.currency

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.RowListener
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.common.performHapticContextClick
import com.delacrixmorgan.squark.databinding.FragmentCurrencyBinding
import com.delacrixmorgan.squark.model.ConvertType
import com.delacrixmorgan.squark.ui.preference.PreferenceNavigationActivity
import com.delacrixmorgan.squark.ui.wallpaper.WallpaperFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyFragment : Fragment(R.layout.fragment_currency), RowListener, CurrencyBottomSheetDialogFragment.Listener {
    companion object {
        const val EXTRA_CURRENCY = "CurrencyFragment.currency"
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentCurrencyBinding? = null

    private val viewModel: CurrencyViewModel by activityViewModels()

    private val requestBaseCountryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val countryCode = result.data?.getStringExtra(EXTRA_CURRENCY)
                SharedPreferenceHelper.baseCurrency = countryCode

                binding.currencyTableLayout.onRowCollapse()
                updateTable()
            }
        }

    private val requestQuoteCountryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val countryCode = result.data?.getStringExtra(EXTRA_CURRENCY)
                SharedPreferenceHelper.quoteCurrency = countryCode

                binding.currencyTableLayout.onRowCollapse()
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
            thresholdTranslationWidth = requireActivity().resources.displayMetrics.widthPixels / 6F,
            listener = this,
            config = viewModel.expandablePanningViewConfig
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
//            CurrencyBottomSheetDialogFragment.show(
//                requireActivity().supportFragmentManager,
//                listener = this,
//                convertType = ConvertType.Quote
//            )
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

    override fun onSwiped(multiplier: Double) {
        viewModel.multiplier = multiplier
    }

    override fun onChangeCurrency(convertType: ConvertType) {
        val currency = when (convertType) {
            ConvertType.Base -> viewModel.baseCurrency
            ConvertType.Quote -> viewModel.quoteCurrency
        }
        val currencyIntent = PreferenceNavigationActivity.newLaunchIntent(requireContext(), requireNotNull(currency))
        requestBaseCountryLauncher.launch(currencyIntent)
    }

    override fun onChangeWallpaper() {
        requireActivity().supportFragmentManager.commit {
            replace(android.R.id.content, WallpaperFragment.create())
        }
    }

    override fun onUnlockFeatures() {
        Toast.makeText(requireContext(), "Unlock Features", Toast.LENGTH_SHORT).show()
    }
}