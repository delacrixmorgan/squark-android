package com.delacrixmorgan.squark.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.launchWebsite
import kotlinx.android.synthetic.main.fragment_credit_list.*

/**
 * CreditListFragment
 * squark-android
 *
 * Created by Delacrix Morgan on 13/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CreditListFragment : DialogFragment() {
    companion object {
        fun newInstance() = CreditListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_credit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.spartanImageView.setOnClickListener {
            launchWebsite("https://github.com/theleagueof/league-spartan")
        }

        this.currencyLayerImageView.setOnClickListener {
            launchWebsite("https://github.com/apilayer/currencylayer-API")
        }

        this.doneButton.setOnClickListener {
            this.activity?.finish()
        }
    }
}