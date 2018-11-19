package com.delacrixmorgan.squark.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.delacrixmorgan.squark.databinding.FragmentSettingsListBinding
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
        fun newInstance() = SettingsListFragment()
    }

    private lateinit var dataBinding: FragmentSettingsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.dataBinding = FragmentSettingsListBinding.inflate(inflater, container, false)
        return this.dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        this.creditsViewGroup.setOnClickListener {

        }

        this.shareViewGroup.setOnClickListener {

        }

        this.sourceCodeViewGroup.setOnClickListener {

        }
    }
}