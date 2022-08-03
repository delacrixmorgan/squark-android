package com.delacrixmorgan.squark.ui.wallpaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.RowListener
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.databinding.FragmentWallpaperBinding
import com.delacrixmorgan.squark.ui.currency.CurrencyUiState
import com.delacrixmorgan.squark.ui.currency.CurrencyViewModel
import kotlinx.coroutines.launch

class WallpaperFragment : Fragment(R.layout.fragment_wallpaper), RowListener {
    companion object {
        fun create() = WallpaperFragment()
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentWallpaperBinding? = null

    private val viewModel: CurrencyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperBinding.inflate(inflater, container, false)
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

        binding.wallpaperImageView.load("https://images.unsplash.com/photo-1576675466969-38eeae4b41f6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80")

        binding.changeWallpaperButton.setOnClickListener {

        }

        binding.setWallpaperButton.setOnClickListener {

        }

        binding.currencyTableLayout.setupTable(
            thresholdTranslationWidth = requireActivity().resources.displayMetrics.widthPixels / 6F,
            listener = this,
            config = viewModel.expandablePanningViewConfig
        )

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
}