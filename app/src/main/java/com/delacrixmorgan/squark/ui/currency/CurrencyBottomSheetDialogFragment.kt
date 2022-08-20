package com.delacrixmorgan.squark.ui.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.delacrixmorgan.squark.databinding.FragmentCurrencyBottomSheetDialogBinding
import com.delacrixmorgan.squark.model.ConvertType
import com.delacrixmorgan.squark.ui.wallpaper.WallpaperPreviewFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyBottomSheetDialogFragment : BottomSheetDialogFragment() {
    companion object {
        fun show(
            supportFragmentManager: FragmentManager,
            listener: Listener,
            convertType: ConvertType
        ) = CurrencyBottomSheetDialogFragment().apply {
            this.listener = listener
            this.convertType = convertType
            show(supportFragmentManager, CurrencyBottomSheetDialogFragment::class.java.simpleName)
        }
    }

    interface Listener {
        fun onChangeCurrency(convertType: ConvertType)
        fun onChangeWallpaper()
        fun onUnlockFeatures()
    }

    private var _binding: FragmentCurrencyBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var listener: Listener
    private lateinit var convertType: ConvertType

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
            listener.onChangeCurrency(convertType)
            dismiss()
        }
        binding.setWallpaperLayout.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace(android.R.id.content, WallpaperPreviewFragment.create())
            }
            dismiss()
        }
        binding.unlockFeaturesLayout.setOnClickListener {
            dismiss()
        }
    }
}