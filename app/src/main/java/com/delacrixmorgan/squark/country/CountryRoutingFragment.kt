package com.delacrixmorgan.squark.country

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.delacrixmorgan.squark.R
import kotlinx.android.synthetic.main.fragment_base_routing.*

/**
 * CountryRoutingFragment
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CountryRoutingFragment : Fragment() {
    companion object {
        private const val ARG_COUNTRY_CODE = "Country.countryCode"

        fun newInstance(
                countryCode: String? = null
        ): CountryRoutingFragment {
            val fragment = CountryRoutingFragment()
            val args = Bundle()

            countryCode?.let {
                args.putString(ARG_COUNTRY_CODE, it)
            }

            fragment.arguments = args
            return fragment
        }
    }

    private var countryCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.countryCode = this.arguments?.getString(ARG_COUNTRY_CODE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_routing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment = when {
            this.countryCode != null -> CountryListFragment.newInstance(countryCode = this.countryCode)
            else -> CountryListFragment()
        }

        requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(this.routingContainer.id, fragment, fragment.javaClass.simpleName)
                .commit()
    }
}