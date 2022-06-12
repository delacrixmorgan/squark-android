package com.delacrixmorgan.squark.ui.preference.credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.launchWebsite
import com.delacrixmorgan.squark.databinding.FragmentCreditListBinding

class CreditListFragment : DialogFragment(R.layout.fragment_credit_list) {
    companion object {
        fun create() = CreditListFragment()
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentCreditListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreditListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spartanImageView.setOnClickListener {
            launchWebsite("https://github.com/theleagueof/league-spartan")
        }

        binding.currencyLayerImageView.setOnClickListener {
            launchWebsite("https://github.com/apilayer/currencylayer-API")
        }

        binding.fastScrollImageView.setOnClickListener {
            launchWebsite("https://github.com/zhanghai/AndroidFastScroll")
        }

        binding.dateTimeWhartonImageView.setOnClickListener {
            launchWebsite("https://github.com/JakeWharton/ThreeTenABP")
        }

        binding.serializationWhartonImageView.setOnClickListener {
            launchWebsite("https://github.com/JakeWharton/retrofit2-kotlinx-serialization-converter")
        }

        binding.doneButton.setOnClickListener {
            activity?.finish()
        }
    }
}