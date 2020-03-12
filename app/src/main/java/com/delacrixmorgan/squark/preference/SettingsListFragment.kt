package com.delacrixmorgan.squark.preference

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.library.BuildConfig
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.multiplier
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.isMultiplierEnabled
import com.delacrixmorgan.squark.common.launchWebsite
import com.delacrixmorgan.squark.common.shareAppIntent
import com.delacrixmorgan.squark.databinding.FragmentSettingsListBinding
import com.delacrixmorgan.squark.preference.credit.CreditActivity
import kotlinx.android.synthetic.main.cell_settings.view.*
import kotlinx.android.synthetic.main.fragment_settings_list.*

class SettingsListFragment : Fragment() {

    companion object {
        private const val SOURCE_CODE_URL = "https://github.com/delacrixmorgan/squark-android"

        fun newInstance() = SettingsListFragment()
    }

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentSettingsListBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.buildNumberTextView.text = getString(R.string.message_build_version_name, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)

        this.multiplierViewGroup.switchCompat.isChecked = this.sharedPreferences.getBoolean(isMultiplierEnabled, true)

        this.multiplierViewGroup.setOnClickListener {
            val updatedSwitchCheck = !this.multiplierViewGroup.switchCompat.isChecked
            this.multiplierViewGroup.switchCompat.isChecked = updatedSwitchCheck

            this.sharedPreferences.edit {
                putInt(multiplier, 1)
                putBoolean(isMultiplierEnabled, updatedSwitchCheck)
            }
        }

        this.creditsViewGroup.setOnClickListener {
            val launchIntent = CreditActivity.newLaunchIntent(view.context)
            startActivity(launchIntent)
        }

        this.shareViewGroup.setOnClickListener {
            val shareMessage = getString(R.string.fragment_settings_list_share_message)
            view.context.shareAppIntent(shareMessage)
        }

        this.sourceCodeViewGroup.setOnClickListener {
            launchWebsite(SOURCE_CODE_URL)
        }
    }
}