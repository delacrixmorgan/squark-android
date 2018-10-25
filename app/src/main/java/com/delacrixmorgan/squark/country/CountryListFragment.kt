package com.delacrixmorgan.squark.country

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delacrixmorgan.squark.CurrencyNavigationFragment
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.PreferenceHelper
import com.delacrixmorgan.squark.common.PreferenceHelper.get
import com.delacrixmorgan.squark.common.PreferenceHelper.set
import com.delacrixmorgan.squark.common.getFilteredCountries
import com.delacrixmorgan.squark.data.SquarkWorkerThread
import com.delacrixmorgan.squark.data.api.SquarkApiService
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.controller.CountryDatabase
import com.delacrixmorgan.squark.data.model.Country
import com.delacrixmorgan.squark.data.model.Currency
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_country_list.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * CountryListFragment
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CountryListFragment : Fragment(), CountryListListener, MenuItem.OnActionExpandListener {
    companion object {
        private const val ARG_BELONGS_TO_COUNTRY_CODE = "Country.countryCode"
        private const val MILLISECONDS_IN_A_WEEK = 604800000
        private const val DEFAULT_COUNTRY_CODE = "USD"

        fun newInstance(
                countryCode: String? = null
        ): CountryListFragment {
            val fragment = CountryListFragment()
            val args = Bundle()

            args.putString(ARG_BELONGS_TO_COUNTRY_CODE, countryCode)
            fragment.arguments = args

            return fragment
        }
    }

    private var database: CountryDatabase? = null
    private var disposable: Disposable? = null

    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null

    private lateinit var countryCode: String
    private lateinit var workerThread: SquarkWorkerThread
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var countryAdapter: CountryRecyclerViewAdapter

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        this.workerThread = SquarkWorkerThread(SquarkWorkerThread::class.java.simpleName)
        this.workerThread.start()

        this.database = CountryDatabase.getInstance(requireContext())
        this.simpleDateFormat = SimpleDateFormat("'Last Updated:' dd/MM/yyyy 'at' hh:mm a")
        this.countryCode = this.arguments?.getString(ARG_BELONGS_TO_COUNTRY_CODE) ?: DEFAULT_COUNTRY_CODE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_country_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.countryAdapter = CountryRecyclerViewAdapter(listener = this)
        this.countryAdapter.updateDataSet(CountryDataController.getCountries(), false)
        this.layoutManager = LinearLayoutManager(this.activity, RecyclerView.VERTICAL, false)

        this.countryRecyclerView.layoutManager = this.layoutManager
        this.countryRecyclerView.adapter = this.countryAdapter
        this.countryRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount - visibleItemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    this@CountryListFragment.updateViewGroup.visibility = if (pastVisibleItems + visibleItemCount >= totalItemCount && countryAdapter.itemCount > 0) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                }
            }
        })

        this.updateViewGroup.setOnClickListener { checkIsDataUpdated() }

        checkIsDataUpdated()
        updateDataSet(null, false)
    }

    private fun checkIsDataUpdated() {
        val timeStamp = PreferenceHelper.getPreference(requireContext())[PreferenceHelper.UPDATED_TIME_STAMP, PreferenceHelper.DEFAULT_UPDATED_TIME_STAMP]
        val currentTimeStamp = Date().time

        this.updateImageView.visibility = View.GONE

        if (currentTimeStamp - timeStamp > MILLISECONDS_IN_A_WEEK) {
            this.updateTextView.text = getString(R.string.fragment_country_list_title_updating)
            updateCurrencyRates()
        } else {
            this.updateTextView.text = getString(R.string.fragment_country_list_title_updated)
            Handler().postDelayed({
                if (this.isVisible) {
                    this.updateImageView.visibility = View.VISIBLE
                    this.updateTextView.text = simpleDateFormat.format(Date(currentTimeStamp))
                }
            }, 3000)
        }
    }

    private fun updateCurrencyRates() {
        this.workerThread.postTask(Runnable {
            val countries = this.database?.countryDataDao()?.getCountries() ?: arrayListOf()

            Handler().post {
                if (countries.isNotEmpty()) {
                    this.disposable = SquarkApiService
                            .create(requireActivity())
                            .getCurrencies()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ result ->
                                val currencies = result.quotes.map {
                                    Currency(
                                            code = it.key,
                                            rate = it.value
                                    )
                                }

                                if (currencies.isNotEmpty()) {
                                    updateCurrencies(
                                            countries = countries,
                                            currencies = currencies
                                    )
                                }
                            }, {
                                Snackbar.make(this.mainContainer, getString(R.string.error_api_countries), Snackbar.LENGTH_SHORT).show()
                            })
                }
            }
        })
    }

    private fun updateCurrencies(countries: List<Country>, currencies: List<Currency>) {
        this.workerThread.postTask(Runnable {
            countries.forEach { country ->
                if (country.code != DEFAULT_COUNTRY_CODE) {
                    val updateCurrency = currencies.find {
                        it.code.contains(country.code)
                    }

                    updateCurrency?.let {
                        country.rate = it.rate
                        this.database?.countryDataDao()?.updateCountry(country)
                    }
                }
            }

            CountryDataController.updateDataSet(countries)
            this@CountryListFragment.activity?.runOnUiThread {
                if (this.isVisible) {
                    val currentTimeStamp = Date().time
                    PreferenceHelper.getPreference(requireContext())[PreferenceHelper.UPDATED_TIME_STAMP] = currentTimeStamp

                    this.updateTextView.text = simpleDateFormat.format(Date(currentTimeStamp))
                    this.updateImageView.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun updateDataSet(searchText: String? = null, searchMode: Boolean) {
        val filterCountries = CountryDataController.getFilteredCountries(searchText) as MutableList<Country>
        val selectedCountry = filterCountries.firstOrNull { it.code == this.countryCode }

        selectedCountry?.let {
            filterCountries.remove(it)
            filterCountries.sortBy { it.code }
            filterCountries.add(0, it)
        }

        this.emptyStateViewGroup.visibility = if (filterCountries.isEmpty()) View.VISIBLE else View.GONE
        this.countryAdapter.updateDataSet(filterCountries, searchMode)
    }

    override fun onCountrySelected(country: Country) {
        val activity = this.activity ?: return
        val intent = activity.intent

        intent.putExtra(CurrencyNavigationFragment.EXTRA_COUNTRY_CODE, country.code)

        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_search, menu)

        this.searchMenuItem = menu?.findItem(R.id.actionSearch)
        this.searchMenuItem?.setOnActionExpandListener(this)
        this.searchView = this.searchMenuItem?.actionView as? SearchView
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        this.updateViewGroup.visibility = View.GONE
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
        this.updateViewGroup.visibility = View.VISIBLE

        updateDataSet("", false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.activity?.onBackPressed()
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
        this.workerThread.quit()

        super.onDestroy()
    }
}