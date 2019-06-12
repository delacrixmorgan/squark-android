package com.delacrixmorgan.squark.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.delacrixmorgan.squark.BuildConfig
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.launchWebsite
import com.delacrixmorgan.squark.common.shareAppIntent
import com.delacrixmorgan.squark.databinding.FragmentSettingsListBinding
import kotlinx.android.synthetic.main.cell_settings.view.*
import kotlinx.android.synthetic.main.fragment_settings_list.*

/**
 * SettingsListFragment
 * squark-android
 *
 * Created by Delacrix Morgan on 17/11/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class SettingsListFragment : Fragment() {

    companion object {
        private const val SOURCE_CODE_URL = "https://github.com/delacrixmorgan/squark-android"

        fun newInstance() = SettingsListFragment()
    }

    private lateinit var dataBinding: FragmentSettingsListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.dataBinding = FragmentSettingsListBinding.inflate(inflater, container, false)
        return this.dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.buildNumberTextView.text = getString(R.string.message_build_version_name, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)

        this.multiplierViewGroup.setOnClickListener {
            this.multiplierViewGroup.switchCompat.isChecked = !this.multiplierViewGroup.switchCompat.isChecked
        }

        this.multiplierViewGroup.switchCompat.setOnCheckedChangeListener { _, isChecked ->

        }

        this.creditsViewGroup.setOnClickListener {

        }

        this.shareViewGroup.setOnClickListener {
            val shareMessage = getString(R.string.fragment_settings_list_share_message)
            view.context.shareAppIntent(shareMessage)
        }

        this.sourceCodeViewGroup.setOnClickListener {
            view.context.launchWebsite(SOURCE_CODE_URL)
        }
    }
}