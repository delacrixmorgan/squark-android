package com.delacrixmorgan.squark.ui

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.RowListener
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.baseCurrencyCode
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.isMultiplierEnabled
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.multiplier
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.quoteCurrencyCode
import com.delacrixmorgan.squark.common.getPreferenceCountry
import com.delacrixmorgan.squark.common.performHapticContextClick
import com.delacrixmorgan.squark.data.SquarkEngine
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.model.Country
import com.delacrixmorgan.squark.ui.preference.PreferenceNavigationActivity
import kotlinx.android.synthetic.main.fragment_currency_navigation.*
import java.util.*

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
        get() = sharedPreferences.getBoolean(isMultiplierEnabled, true)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isPersistentMultiplierEnabled) {
            SquarkEngine.updateMultiplier(sharedPreferences.getInt(multiplier, 1))
        }

        SquarkEngine.setupTable(
            activity = requireActivity(),
            tableLayout = currencyTableLayout,
            rowList = this.rowList,
            listener = this
        )

        baseCurrencyTextView.setOnClickListener {
            val currencyIntent = PreferenceNavigationActivity.newLaunchIntent(
                view.context,
                countryCode = baseCountry?.code
            )
            startActivityForResult(currencyIntent,
                REQUEST_BASE_COUNTRY
            )
        }

        quoteCurrencyTextView.setOnClickListener {
            val currencyIntent = PreferenceNavigationActivity.newLaunchIntent(
                view.context,
                countryCode = quoteCountry?.code
            )
            startActivityForResult(currencyIntent,
                REQUEST_QUOTE_COUNTRY
            )
        }

        swapButton.setOnClickListener {
            sharedPreferences.edit {
                putString(baseCurrencyCode, quoteCountry?.code)
                putString(quoteCurrencyCode, baseCountry?.code)
            }

            swapButton.performHapticContextClick()
            updateTable()
        }

        updateTable()
    }

    private fun updateTable() {
        val context = requireContext()

        baseCountry = CountryDataController.getPreferenceCountry(
            context,
            preferenceCurrency = baseCurrencyCode
        )
        quoteCountry = CountryDataController.getPreferenceCountry(
            context,
            preferenceCurrency = quoteCurrencyCode
        )

        baseCurrencyTextView.text = baseCountry?.code
        quoteCurrencyTextView.text = quoteCountry?.code

        if (baseCountry?.rate != 0.0 && quoteCountry?.rate != 0.0) {
            SquarkEngine.updateConversionRate(baseCountry?.rate, quoteCountry?.rate)
            SquarkEngine.updateTable(rowList)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_BASE_COUNTRY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val countryCode = data?.getStringExtra(EXTRA_COUNTRY_CODE)
                    sharedPreferences.edit { putString(baseCurrencyCode, countryCode) }

                    if (isExpanded) onRowCollapse()
                    updateTable()
                }
            }

            REQUEST_QUOTE_COUNTRY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val countryCode = data?.getStringExtra(EXTRA_COUNTRY_CODE)
                    sharedPreferences.edit { putString(quoteCurrencyCode, countryCode) }

                    if (isExpanded) onRowCollapse()
                    updateTable()
                }
            }
        }
    }

    private fun onRowExpand(selectedRow: Int) {
        currencyTableLayout.performHapticContextClick()
        rowList.forEachIndexed { index, tableRow ->
            if (index != selectedRow && index != (selectedRow + 1)) {
                tableRow.isVisible = false
            } else {
                tableRow.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )
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
        val context = requireContext()
        currencyTableLayout.performHapticContextClick()
        expandedList.forEach {
            currencyTableLayout.removeView(it)
        }

        rowList.forEach {
            it.isVisible = true
            it.background = ContextCompat.getDrawable(context,
                R.drawable.shape_cell_dark
            )
        }
    }

    /**
     * RowListener
     */
    override fun onSwipeLeft(multiplier: Double) {
        if (!isExpanded) {
            if (isPersistentMultiplierEnabled) {
                sharedPreferences.edit {
                    putInt(SharedPreferenceHelper.multiplier, multiplier.toInt())
                }
            }

            currencyTableLayout.performHapticContextClick()
            SquarkEngine.updateTable(rowList)
        }
    }

    override fun onSwipeRight(multiplier: Double) {
        if (!isExpanded) {
            if (isPersistentMultiplierEnabled) {
                sharedPreferences.edit {
                    putInt(SharedPreferenceHelper.multiplier, multiplier.toInt())
                }
            }

            currencyTableLayout.performHapticContextClick()
            SquarkEngine.updateTable(rowList)
        }
    }

    override fun onClick(position: Int) {
        if (isExpanded) {
            onRowCollapse()
        } else {
            onRowExpand(position)
        }

        isExpanded = !isExpanded
    }
}