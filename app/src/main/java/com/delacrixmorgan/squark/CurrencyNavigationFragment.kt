package com.delacrixmorgan.squark

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import com.delacrixmorgan.squark.common.PreferenceHelper
import com.delacrixmorgan.squark.common.PreferenceHelper.set
import com.delacrixmorgan.squark.common.RowListener
import com.delacrixmorgan.squark.common.getPreferenceCountry
import com.delacrixmorgan.squark.country.CountryActivity
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.model.Country
import kotlinx.android.synthetic.main.fragment_launch.*
import java.util.*

/**
 * CurrencyNavigationFragment
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CurrencyNavigationFragment : Fragment(), RowListener {

    companion object {
        private const val REQUEST_BASE_COUNTRY: Int = 1
        private const val REQUEST_QUOTE_COUNTRY: Int = 2

        const val EXTRA_COUNTRY_CODE = "CurrencyNavigationFragment.countryCode"

        fun newInstance(): CurrencyNavigationFragment = CurrencyNavigationFragment()
    }

    private var baseCountry: Country? = null
    private var quoteCountry: Country? = null

    private var rowList: ArrayList<TableRow> = ArrayList()
    private var expandedList: ArrayList<TableRow> = ArrayList()

    private var isExpanded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_launch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SquarkEngine.setupTable(
                activity = requireActivity(),
                tableLayout = currencyTableLayout,
                rowList = this.rowList,
                listener = this
        )

        setupListeners()
        updateTable()
    }

    private fun setupListeners() {
        this.baseCurrencyTextView.setOnClickListener {
            val currencyIntent = CountryActivity.newLaunchIntent(requireContext(), countryCode = this.baseCountry?.code)
            startActivityForResult(currencyIntent, REQUEST_BASE_COUNTRY)
        }

        this.quoteCurrencyTextView.setOnClickListener {
            val currencyIntent = CountryActivity.newLaunchIntent(requireContext(), countryCode = this.quoteCountry?.code)
            startActivityForResult(currencyIntent, REQUEST_QUOTE_COUNTRY)
        }

        this.swapButton.setOnClickListener {
            val preference = PreferenceHelper.getPreference(requireContext())

            preference[PreferenceHelper.BASE_CURRENCY_CODE] = this.quoteCountry?.code
            preference[PreferenceHelper.QUOTE_CURRENCY_CODE] = this.baseCountry?.code

            updateTable()
        }
    }

    private fun updateTable() {
        this.baseCountry = CountryDataController.getPreferenceCountry(requireContext(), preferenceCurrency = PreferenceHelper.BASE_CURRENCY_CODE)
        this.quoteCountry = CountryDataController.getPreferenceCountry(requireContext(), preferenceCurrency = PreferenceHelper.QUOTE_CURRENCY_CODE)

        this.baseCurrencyTextView.text = this.baseCountry?.code
        this.quoteCurrencyTextView.text = this.quoteCountry?.code

        if (this.baseCountry?.rate != 0.0 && this.quoteCountry?.rate != 0.0) {
            SquarkEngine.updateConversionRate(this.baseCountry?.rate, this.quoteCountry?.rate)
            SquarkEngine.updateTable(this.rowList)
        } else {
            Snackbar.make(this.view!!, "Unable to get Base and Quote Currencies.", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val preference = PreferenceHelper.getPreference(requireContext())

        when (requestCode) {
            REQUEST_BASE_COUNTRY -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.getStringExtra(EXTRA_COUNTRY_CODE)?.let {
                        preference[PreferenceHelper.BASE_CURRENCY_CODE] = it
                        updateTable()
                    }
                }
            }

            REQUEST_QUOTE_COUNTRY -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.getStringExtra(EXTRA_COUNTRY_CODE)?.let {
                        preference[PreferenceHelper.QUOTE_CURRENCY_CODE] = it
                        updateTable()
                    }
                }
            }
        }
    }

    private fun onRowExpand(selectedRow: Int) {
        this.rowList.forEachIndexed { index, tableRow ->
            if (index != selectedRow && index != (selectedRow + 1)) {
                tableRow.visibility = View.GONE
            } else {
                tableRow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
        }

        SquarkEngine.expandTable(
                activity = requireActivity(),
                tableLayout = currencyTableLayout,
                expandQuantifier = selectedRow,
                expandedList = expandedList,
                listener = this
        )
    }

    private fun onRowCollapse() {
        this.expandedList.forEach {
            currencyTableLayout.removeView(it)
        }

        this.rowList.forEach {
            it.visibility = View.VISIBLE
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        }
    }

    override fun onSwipeLeft() {
        if (!this.isExpanded) {
            SquarkEngine.updateTable(this.rowList)
        }
    }

    override fun onSwipeRight() {
        if (!this.isExpanded) {
            SquarkEngine.updateTable(this.rowList)
        }
    }

    override fun onClick(position: Int) {
        if (this.isExpanded) {
            onRowCollapse()
        } else {
            onRowExpand(position)
        }

        this.isExpanded = !this.isExpanded
    }
}