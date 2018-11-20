package com.delacrixmorgan.squark.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.launchPlayStore
import kotlinx.android.synthetic.main.fragment_support_list.*

/**
 * SupportListFragment
 * squark-android
 *
 * Created by Delacrix Morgan on 17/11/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class SupportListFragment : Fragment() {
    companion object {
        private const val KINGS_CUP_PACKAGE_NAME = "com.delacrixmorgan.kingscup"

        fun newInstance() = SupportListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_support_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        val context = this.context ?: return

        this.starImageView.setOnClickListener {
            this.personImageView.setImageResource(R.drawable.ic_human_happy)
            this.starImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent))

            context.launchPlayStore(context.packageName)
        }

        this.rateButton.setOnClickListener {
            this.personImageView.setImageResource(R.drawable.ic_human_happy)
            context.launchPlayStore(context.packageName)
        }

        this.kingscupViewGroup.setOnClickListener {
            context.launchPlayStore(KINGS_CUP_PACKAGE_NAME)
        }
    }
}