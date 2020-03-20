package com.delacrixmorgan.squark.preference.country

import android.app.Activity
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.delacrixmorgan.squark.CurrencyNavigationFragment
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.DEFAULT_UPDATED_TIME_STAMP
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.updatedTimeStamp
import com.delacrixmorgan.squark.common.compatColor
import com.delacrixmorgan.squark.common.getFilteredCountries
import com.delacrixmorgan.squark.common.performHapticContextClick
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.dao.CountryDatabase
import com.delacrixmorgan.squark.data.model.Country
import com.delacrixmorgan.squark.data.model.Currency
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_country_list.*
import kotlinx.coroutines.launch
import java.util.*

class CountryListFragment : Fragment(), CountryListListener, MenuItem.OnActionExpandListener {
    companion object {
        private const val ARG_BELONGS_TO_COUNTRY_CODE = "Country.countryCode"
        private const val MILLISECONDS_IN_A_DAY = 86400000
        private const val DEFAULT_COUNTRY_CODE = "USD"

        fun newInstance(countryCode: String? = null): CountryListFragment {
            return CountryListFragment().apply {
                this.arguments = bundleOf(ARG_BELONGS_TO_COUNTRY_CODE to countryCode)
            }
        }
    }

    private var database: CountryDatabase? = null
    private var disposable: Disposable? = null

    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null

    private lateinit var countryCode: String
    private lateinit var countryAdapter: CountryRecyclerViewAdapter

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        this.database = CountryDatabase.getInstance(requireContext())
        this.countryCode = this.arguments?.getString(ARG_BELONGS_TO_COUNTRY_CODE)
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
        val activity = this.activity as AppCompatActivity

        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = ""
        }

        this.countryAdapter = CountryRecyclerViewAdapter(listener = this)
        this.countryRecyclerView.adapter = this.countryAdapter

        this.swipeRefreshLayout.setColorSchemeColors(
            R.color.colorAccent.compatColor(this.context),
            R.color.colorPrimary.compatColor(this.context)
        )

        this.swipeRefreshLayout.setOnRefreshListener {
            this.swipeRefreshLayout.performHapticContextClick()
            checkIsDataUpdated()
        }

        updateDataSet()
    }

    private fun checkIsDataUpdated() {
        val timeStamp = this.sharedPreferences.getLong(updatedTimeStamp, DEFAULT_UPDATED_TIME_STAMP)
        val currentTimeStamp = Date().time

        if (currentTimeStamp - timeStamp > MILLISECONDS_IN_A_DAY) {
            updateCurrencyRates()
        } else {
            Snackbar.make(
                this.mainContainer,
                getString(R.string.fragment_country_list_title_everything_already_updated),
                Snackbar.LENGTH_SHORT
            ).show()
            this.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateCurrencyRates() {
//        AsyncTask.execute {
//            val countries = this.database?.countryDataDao()?.getCountries() ?: arrayListOf()
//            if (countries.isNotEmpty()) {
//                this.disposable = SquarkServiceClient
//                        .create(requireActivity())
//                        .getCurrencies()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe({ result ->
//                            val currencies = result.quotes.map {
//                                Currency(code = it.key, rate = it.value)
//                            }
//
//                            if (currencies.isNotEmpty()) {
//                                updateCurrencies(countries = countries, currencies = currencies)
//                            }
//                            this.swipeRefreshLayout.isRefreshing = false
//                        }, {
//                            Snackbar.make(this.mainContainer, getString(R.string.error_api_countries), Snackbar.LENGTH_SHORT).show()
//                            this.swipeRefreshLayout.isRefreshing = false
//                        })
//            }
//        }
    }

    private fun updateCurrencies(countries: List<Country>, currencies: List<Currency>) {
        AsyncTask.execute {
            countries.forEach { country ->
                if (country.code != DEFAULT_COUNTRY_CODE) {
                    val updateCurrency = currencies.find {
                        it.code.contains(country.code)
                    }

                    lifecycleScope.launch {
                        updateCurrency?.let {
                            country.rate = it.rate
                            database?.countryDataDao()?.updateCountry(country)
                        }
                    }
                }
            }

            CountryDataController.updateDataSet(countries)
            this.activity?.runOnUiThread {
                if (this.isVisible) {
                    val currentTimeStamp = Date().time
                    this.sharedPreferences.edit { putLong(updatedTimeStamp, currentTimeStamp) }
                    Snackbar.make(
                        this.mainContainer,
                        getString(R.string.fragment_country_list_title_updated),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
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

        this.countryAdapter.updateDataSet(filterCountries, searchMode)

        if (this.isVisible) {
            this.emptyStateViewGroup.visibility =
                if (filterCountries.isEmpty()) View.VISIBLE else View.GONE
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

        this.searchMenuItem = menu.findItem(R.id.actionSearch)
        this.searchMenuItem?.setOnActionExpandListener(this)

        this.searchView = this.searchMenuItem?.actionView as? SearchView
        this.searchView?.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        this.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        this.searchView?.setQuery("", false)
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

    override fun onPause() {
        super.onPause()
        this.disposable?.dispose()
    }

    override fun onDestroy() {
        CountryDatabase.destroyInstance()
        super.onDestroy()
    }
}