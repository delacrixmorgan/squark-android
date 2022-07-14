package com.delacrixmorgan.squark.ui.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.delacrixmorgan.squark.databinding.FragmentCurrencyBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyBottomSheetDialogFragment : BottomSheetDialogFragment() {
    companion object {
        fun show(supportFragmentManager: FragmentManager) = CurrencyBottomSheetDialogFragment().apply {
            show(supportFragmentManager, CurrencyBottomSheetDialogFragment::class.java.simpleName)
        }
    }

    private var _binding: FragmentCurrencyBottomSheetDialogBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PeaksBottomSheetStyle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCurrencyBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.changeCurrencyLayout.setOnClickListener {
//            val currencyIntent = PreferenceNavigationActivity.newLaunchIntent(
//                view.context, requireNotNull(viewModel.quoteCurrency)
//            )
//            requestQuoteCountryLauncher.launch(currencyIntent)
            dismiss()
        }
        binding.setWallpaperLayout.setOnClickListener {
            dismiss()
        }
        binding.unlockFeaturesLayout.setOnClickListener {
            dismiss()
        }
    }
}