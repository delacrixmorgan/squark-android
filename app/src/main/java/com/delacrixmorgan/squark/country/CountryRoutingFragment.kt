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
        private const val ARG_BASE_CURRENCY_CODE = "Currency.baseCode"
        private const val ARG_QUOTE_CURRENCY_CODE = "Currency.quoteCode"

        fun newInstance(
                baseCurrencyCode: String? = null,
                quoteCurrencyCode: String? = null
        ): CountryRoutingFragment {
            val fragment = CountryRoutingFragment()
            val args = Bundle()

            baseCurrencyCode?.let {
                args.putString(ARG_BASE_CURRENCY_CODE, it)
            }

            quoteCurrencyCode?.let {
                args.putString(ARG_QUOTE_CURRENCY_CODE, it)
            }

            fragment.arguments = args
            return fragment
        }
    }

    private var baseCurrencyCode: String? = null
    private var quoteCurrencyCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.baseCurrencyCode = this.arguments?.getString(ARG_BASE_CURRENCY_CODE)
        this.quoteCurrencyCode = this.arguments?.getString(ARG_QUOTE_CURRENCY_CODE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_routing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment = when {
            this.baseCurrencyCode != null -> CountryListFragment.newInstance(baseCurrencyCode = this.baseCurrencyCode)
            this.quoteCurrencyCode != null -> CountryListFragment.newInstance(quoteCurrencyCode = this.quoteCurrencyCode)
            else -> CountryListFragment()
        }

        requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(this.routingContainer.id, fragment, fragment.javaClass.simpleName)
                .commit()
    }
}