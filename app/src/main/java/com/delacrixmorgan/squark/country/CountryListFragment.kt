package com.delacrixmorgan.squark.country

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.delacrixmorgan.squark.R

/**
 * CountryListFragment
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CountryListFragment : Fragment() {

    companion object {
        private const val ARG_BELONGS_TO_BASE_CURRENCY_CODE = "Currency.baseCode"
        private const val ARG_BELONGS_TO_QUOTE_CURRENCY_CODE = "Currency.quoteCode"

        fun newInstance(
                baseCurrencyCode: String? = null,
                quoteCurrencyCode: String? = null
        ): CountryListFragment {
            val fragment = CountryListFragment()
            val args = Bundle()

            args.putString(ARG_BELONGS_TO_BASE_CURRENCY_CODE, baseCurrencyCode)
            args.putString(ARG_BELONGS_TO_QUOTE_CURRENCY_CODE, quoteCurrencyCode)

            return fragment
        }
    }

    private var baseCurrencyCode: String? = null
    private var quoteCurrencyCode: String? = null

    private lateinit var countryAdapter: CountryRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.baseCurrencyCode = this.arguments?.getString(ARG_BELONGS_TO_BASE_CURRENCY_CODE)
        this.quoteCurrencyCode = this.arguments?.getString(ARG_BELONGS_TO_QUOTE_CURRENCY_CODE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_currency_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.countryAdapter = CountryRecyclerViewAdapter()
    }
}