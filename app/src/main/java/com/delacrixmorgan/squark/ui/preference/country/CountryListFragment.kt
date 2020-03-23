package com.delacrixmorgan.squark.ui.preference.country

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.*
import com.delacrixmorgan.squark.data.api.SquarkResult
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.dao.CountryDatabase
import com.delacrixmorgan.squark.data.model.Country
import com.delacrixmorgan.squark.data.model.Currency
import com.delacrixmorgan.squark.data.service.SquarkService
import com.delacrixmorgan.squark.ui.currency.CurrencyNavigationFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_country_list.*
import kotlinx.coroutines.launch
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import java.util.*

class CountryListFragment : Fragment(), CountryListListener, MenuItem.OnActionExpandListener {
    companion object {
        private const val MILLISECONDS_IN_A_DAY = 86400000
        private const val DEFAULT_COUNTRY_CODE = "USD"

        fun create(countryCode: String? = null) = CountryListFragment().apply {
            arguments = bundleOf(Keys.Country.Code.name to countryCode)
        }
    }

    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null
    private var database: CountryDatabase? = null

    private lateinit var countryCode: String

    private val countryAdapter: CountryRecyclerViewAdapter by lazy {
        CountryRecyclerViewAdapter(listener = this)
    }

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        database = CountryDatabase.getInstance(requireContext())
        countryCode = arguments?.getString(Keys.Country.Code.name)
            ?: DEFAULT_COUNTRY_CODE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_country_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity

        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = ""
        }

        countryRecyclerView.adapter = countryAdapter

        FastScrollerBuilder(countryRecyclerView)
            .build()

        swipeRefreshLayout.setColorSchemeColors(
            R.color.colorAccent.compatColor(context),
            R.color.colorPrimary.compatColor(context)
        )

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.performHapticContextClick()
            checkIsDataUpdated()
        }

        updateDataSet()
    }

    private fun checkIsDataUpdated() {
        val timeStamp = SharedPreferenceHelper.lastUpdatedDate.time
        val currentTimeStamp = Date().time

        if (currentTimeStamp - timeStamp > MILLISECONDS_IN_A_DAY) {
            updateCurrencyRates()
        } else {
            Snackbar.make(
                mainContainer,
                getString(R.string.fragment_country_list_title_everything_already_updated),
                Snackbar.LENGTH_SHORT
            ).show()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateCurrencyRates() {
        lifecycleScope.launch {
            when (val result = SquarkService.getCurrencies()) {
                is SquarkResult.Success -> {
                    swipeRefreshLayout.isRefreshing = false
                    updateCurrencies(result.value.currencies)
                }
                is SquarkResult.Failure -> {
                    swipeRefreshLayout.isRefreshing = false
                    Snackbar.make(
                        mainContainer,
                        getString(R.string.error_api_countries),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateCurrencies(currencies: List<Currency>) {
        lifecycleScope.launch {
            val countries = database?.countryDataDao()?.getCountries()
            countries?.forEach { country ->
                if (country.code != DEFAULT_COUNTRY_CODE) {
                    val updateCurrency = currencies.find {
                        it.code.contains(country.code)
                    }

                    updateCurrency?.let {
                        country.rate = it.rate
                        database?.countryDataDao()?.updateCountry(country)
                    }
                }
            }
            countries?.let {
                CountryDataController.updateDataSet(it)
                SharedPreferenceHelper.lastUpdatedDate = Date()
            }

            if (isVisible) {
                Snackbar.make(
                    mainContainer,
                    getString(R.string.fragment_country_list_title_updated),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateDataSet(searchText: String? = null, searchMode: Boolean = false) {
        val filterCountries =
            CountryDataController.getFilteredCountries(searchText) as MutableList<Country>
        val selectedCountry = filterCountries.firstOrNull { it.code == this.countryCode }

        selectedCountry?.let { country ->
            filterCountries.remove(country)
            filterCountries.sortBy { it.code }
            filterCountries.add(0, country)
        }

        countryAdapter.updateDataSet(filterCountries, searchMode)

        if (isVisible) {
            emptyStateViewGroup.isVisible = filterCountries.isEmpty()
        }
    }

    override fun onCountrySelected(country: Country) {
        val activity = requireActivity()
        val intent = activity.intent

        intent.putExtra(CurrencyNavigationFragment.EXTRA_COUNTRY_CODE, country.code)

        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        searchMenuItem = menu.findItem(R.id.actionSearch)
        searchMenuItem?.setOnActionExpandListener(this)

        searchView = searchMenuItem?.actionView as? SearchView
        searchView?.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                updateDataSet(query, true)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                updateDataSet(newText, true)
                return true
            }
        })
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        searchView?.setQuery("", false)
        updateDataSet()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val activity = requireActivity()

        return when (item.itemId) {
            android.R.id.home -> {
                if (activity.supportFragmentManager.backStackEntryCount > 1) {
                    activity.supportFragmentManager.popBackStack()
                } else {
                    activity.finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        CountryDatabase.destroyInstance()
        super.onDestroy()
    }
}