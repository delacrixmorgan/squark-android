package com.delacrixmorgan.squark

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.delacrixmorgan.squark.common.RowListener
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.baseCurrencyCode
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.multiplier
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.isMultiplierEnabled
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.quoteCurrencyCode
import com.delacrixmorgan.squark.common.getPreferenceCountry
import com.delacrixmorgan.squark.common.performHapticContextClick
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.model.Country
import com.delacrixmorgan.squark.preference.PreferenceNavigationActivity
import kotlinx.android.synthetic.main.fragment_currency_navigation.*
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
        private const val REQUEST_BASE_COUNTRY = 1
        private const val REQUEST_QUOTE_COUNTRY = 2

        const val EXTRA_COUNTRY_CODE = "CurrencyNavigationFragment.countryCode"
    }

    private var isExpanded = false
    private var baseCountry: Country? = null

    private var quoteCountry: Country? = null
    private var rowList: ArrayList<TableRow> = ArrayList()
    private var expandedList: ArrayList<TableRow> = ArrayList()

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    private val isPersistentMultiplierEnabled: Boolean
        get() {
            return this.sharedPreferences.getBoolean(isMultiplierEnabled, true)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_currency_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this.isPersistentMultiplierEnabled) {
            SquarkEngine.updateMultiplier(this.sharedPreferences.getInt(multiplier, 1))
        }

        SquarkEngine.setupTable(
                activity = requireActivity(),
                tableLayout = currencyTableLayout,
                rowList = this.rowList,
                listener = this
        )

        this.baseCurrencyTextView.setOnClickListener {
            val currencyIntent = PreferenceNavigationActivity.newLaunchIntent(view.context, countryCode = this.baseCountry?.code)
            startActivityForResult(currencyIntent, REQUEST_BASE_COUNTRY)
        }

        this.quoteCurrencyTextView.setOnClickListener {
            val currencyIntent = PreferenceNavigationActivity.newLaunchIntent(view.context, countryCode = this.quoteCountry?.code)
            startActivityForResult(currencyIntent, REQUEST_QUOTE_COUNTRY)
        }

        this.swapButton.setOnClickListener {
            this.sharedPreferences.edit {
                putString(baseCurrencyCode, this@CurrencyNavigationFragment.quoteCountry?.code)
                putString(quoteCurrencyCode, this@CurrencyNavigationFragment.baseCountry?.code)
            }

            this.swapButton.performHapticContextClick()
            updateTable()
        }

        updateTable()
    }

    private fun updateTable() {
        val context = this.context ?: return

        this.baseCountry = CountryDataController.getPreferenceCountry(context, preferenceCurrency = baseCurrencyCode)
        this.quoteCountry = CountryDataController.getPreferenceCountry(context, preferenceCurrency = quoteCurrencyCode)

        this.baseCurrencyTextView.text = this.baseCountry?.code
        this.quoteCurrencyTextView.text = this.quoteCountry?.code

        if (this.baseCountry?.rate != 0.0 && this.quoteCountry?.rate != 0.0) {
            SquarkEngine.updateConversionRate(this.baseCountry?.rate, this.quoteCountry?.rate)
            SquarkEngine.updateTable(this.rowList)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_BASE_COUNTRY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val countryCode = data?.getStringExtra(EXTRA_COUNTRY_CODE)
                    this.sharedPreferences.edit { putString(baseCurrencyCode, countryCode) }

                    if (this.isExpanded) onRowCollapse()
                    updateTable()
                }
            }

            REQUEST_QUOTE_COUNTRY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val countryCode = data?.getStringExtra(EXTRA_COUNTRY_CODE)
                    this.sharedPreferences.edit { putString(quoteCurrencyCode, countryCode) }

                    if (this.isExpanded) onRowCollapse()
                    updateTable()
                }
            }
        }
    }

    private fun onRowExpand(selectedRow: Int) {
        this.currencyTableLayout.performHapticContextClick()
        this.rowList.forEachIndexed { index, tableRow ->
            if (index != selectedRow && index != (selectedRow + 1)) {
                tableRow.isVisible = false
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
        val context = this.context ?: return
        this.currencyTableLayout.performHapticContextClick()
        this.expandedList.forEach {
            currencyTableLayout.removeView(it)
        }

        this.rowList.forEach {
            it.isVisible = true
            it.background = ContextCompat.getDrawable(context, R.drawable.shape_cell_dark)
        }
    }

    //region RowListener
    override fun onSwipeLeft(multiplier: Double) {
        if (!this.isExpanded) {
            if (this.isPersistentMultiplierEnabled) {
                this.sharedPreferences.edit {
                    putInt(SharedPreferenceHelper.multiplier, multiplier.toInt())
                }
            }

            this.currencyTableLayout.performHapticContextClick()
            SquarkEngine.updateTable(this.rowList)
        }
    }

    override fun onSwipeRight(multiplier: Double) {
        if (!this.isExpanded) {
            if (this.isPersistentMultiplierEnabled) {
                this.sharedPreferences.edit {
                    putInt(SharedPreferenceHelper.multiplier, multiplier.toInt())
                }
            }

            this.currencyTableLayout.performHapticContextClick()
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
    //endregion
}