package com.delacrixmorgan.squark.ui.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.delacrixmorgan.squark.BuildConfig
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.common.launchWebsite
import com.delacrixmorgan.squark.common.shareAppIntent
import com.delacrixmorgan.squark.databinding.FragmentSettingsListBinding
import com.delacrixmorgan.squark.ui.preference.credit.CreditActivity

class SettingsListFragment : Fragment(R.layout.fragment_settings_list) {

    companion object {
        private const val SOURCE_CODE_URL = "https://github.com/delacrixmorgan/squark-android"

        fun create() = SettingsListFragment()
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentSettingsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buildNumberTextView.text = getString(
            R.string.message_build_version_name,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        )

        binding.multiplierViewGroup.switchCompat.isChecked =
            SharedPreferenceHelper.isPersistentMultiplierEnabled

        binding.multiplierViewGroup.root.setOnClickListener {
            val updatedSwitchCheck = !binding.multiplierViewGroup.switchCompat.isChecked
            binding.multiplierViewGroup.switchCompat.isChecked = updatedSwitchCheck

            SharedPreferenceHelper.apply {
                multiplier = 1
                isPersistentMultiplierEnabled = updatedSwitchCheck
            }
        }

        binding.creditsViewGroup.root.setOnClickListener {
            val launchIntent = CreditActivity.newLaunchIntent(view.context)
            startActivity(launchIntent)
        }

        binding.shareViewGroup.root.setOnClickListener {
            val shareMessage = getString(R.string.fragment_settings_list_share_message)
            view.context.shareAppIntent(shareMessage)
        }

        binding.sourceCodeViewGroup.root.setOnClickListener {
            launchWebsite(SOURCE_CODE_URL)
        }
    }
}