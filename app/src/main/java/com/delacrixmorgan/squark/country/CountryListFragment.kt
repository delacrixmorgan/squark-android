package com.delacrixmorgan.squark.country

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.data.SquarkWorkerThread
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.controller.CountryDatabase
import com.delacrixmorgan.squark.data.model.Country
import com.delacrixmorgan.squark.CurrencyNavigationFragment
import com.delacrixmorgan.squark.common.PreferenceHelper
import com.delacrixmorgan.squark.common.PreferenceHelper.get
import com.delacrixmorgan.squark.common.PreferenceHelper.set
import com.delacrixmorgan.squark.data.api.SquarkApiService
import com.delacrixmorgan.squark.data.model.Currency
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_country_list.*
import java.math.RoundingMode
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

/**
 * CountryListFragment
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CountryListFragment : Fragment(), CountryListListener {
    companion object {
        private const val ARG_BELONGS_TO_COUNTRY_CODE = "Country.countryCode"

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
    private val simpleDateFormat = SimpleDateFormat("'Last Updated:' dd/MM/yyyy 'at' hh:mm a")

    private var database: CountryDatabase? = null
    private var disposable: Disposable? = null

    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null

    private lateinit var countryCode: String
    private lateinit var workerThread: SquarkWorkerThread
    private lateinit var countryAdapter: CountryRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        this.workerThread = SquarkWorkerThread(SquarkWorkerThread::class.java.simpleName)
        this.workerThread.start()

        this.database = CountryDatabase.getInstance(requireContext())
        this.countryCode = this.arguments?.getString(ARG_BELONGS_TO_COUNTRY_CODE) ?: "USD"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_country_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.countryAdapter = CountryRecyclerViewAdapter(listener = this, countryCode = this.countryCode)
        this.countryAdapter.updateDataSet(CountryDataController.getCountries())

        this.countryRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        this.countryRecyclerView.adapter = this.countryAdapter

        this.updateViewGroup.setOnClickListener {
            this.updateTextView.text = "Everything is up to date.."
            Handler().postDelayed({
                this.updateTextView.text = simpleDateFormat.format(Date(Date().time))
            }, 3000)
        }

        checkIsDataUpdated()
        updateDataSet(null)
    }

    private fun checkIsDataUpdated() {
        val weekInMilliseconds = 604800000

        val timeStamp = PreferenceHelper.getPreference(requireContext())[PreferenceHelper.UPDATED_TIME_STAMP, PreferenceHelper.DEFAULT_UPDATED_TIME_STAMP]
        val currentTimeStamp = Date().time

        if (currentTimeStamp - timeStamp > weekInMilliseconds) {
            this.updateTextView.text = "Updating.."
            updateCurrencyRates()
        } else {
            this.updateTextView.text = "Everything is already up to date.."

            Handler().postDelayed({
                this.updateTextView.text = simpleDateFormat.format(Date(currentTimeStamp))
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
                                val currencies = result.quotes?.map {
                                    Currency(
                                            code = it.key,
                                            rate = it.value.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                                    )
                                } ?: arrayListOf()

                                if (currencies.isNotEmpty()) {
                                    updateCurrencies(
                                            countries = countries,
                                            currencies = currencies
                                    )
                                }
                            }, { error ->
                                Snackbar.make(this.view!!, "Error API Countries", Snackbar.LENGTH_SHORT).show()
                                Log.e("Error", "$error")
                            })
                }
            }
        })
    }

    private fun updateCurrencies(countries: List<Country>, currencies: List<Currency>) {
        this.workerThread.postTask(Runnable {
            countries.forEach { country ->
                if (country.code != "USD") {
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
                val currentTimeStamp = Date().time

                PreferenceHelper.getPreference(requireContext())[PreferenceHelper.UPDATED_TIME_STAMP] = currentTimeStamp
                this.updateTextView.text = simpleDateFormat.format(Date(currentTimeStamp))
            }
        })
    }

    private fun updateDataSet(searchText: String? = null) {
        val filterCountries: MutableList<Country> = if (searchText.isNullOrBlank()) {
            CountryDataController.getCountries()
        } else {
            val text: String = searchText?.toLowerCase() ?: ""
            CountryDataController.getCountries().filter {
                it.name.toLowerCase().contains(text) || it.code.toLowerCase().contains(text)
            }
        } as MutableList<Country>

        val selectedCountry = filterCountries.firstOrNull {
            it.code == this.countryCode
        }

        selectedCountry?.let {
            filterCountries.remove(it)
            filterCountries.sortBy {
                it.code
            }
            filterCountries.add(0, it)
        }

        this.emptyStateViewGroup.visibility = if (filterCountries.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }

        this.countryAdapter.updateDataSet(filterCountries)
    }

    override fun onCountrySelected(country: Country) {
        requireActivity().let {
            val intent = it.intent
            intent.putExtra(CurrencyNavigationFragment.EXTRA_COUNTRY_CODE, country.code)

            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.activity?.onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_search, menu)

        menu?.findItem(R.id.actionSearch)?.let { searchMenuItem ->
            (searchMenuItem.actionView as? SearchView)?.let {
                this@CountryListFragment.searchView = it
                this@CountryListFragment.searchMenuItem = searchMenuItem

                searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                        this@CountryListFragment.updateViewGroup.visibility = View.GONE
                        this@CountryListFragment.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String): Boolean {
                                updateDataSet(query)
                                return true
                            }

                            override fun onQueryTextChange(newText: String): Boolean {
                                updateDataSet(newText)
                                return true
                            }
                        })
                        return true
                    }

                    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                        this@CountryListFragment.searchView?.setQuery("", false)
                        this@CountryListFragment.updateViewGroup.visibility = View.VISIBLE
                        return true
                    }
                })
            }
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