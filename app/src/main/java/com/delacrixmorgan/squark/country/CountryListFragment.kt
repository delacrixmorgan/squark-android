package com.delacrixmorgan.squark.country

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NavUtils
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.data.SquarkWorkerThread
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.controller.CountryDatabase
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_country_list.*

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

    private var database: CountryDatabase? = null
    private var disposable: Disposable? = null

    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null
    private var baseCurrencyCode: String? = null
    private var quoteCurrencyCode: String? = null

    private lateinit var workerThread: SquarkWorkerThread
    private lateinit var countryAdapter: CountryRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        this.baseCurrencyCode = this.arguments?.getString(ARG_BELONGS_TO_BASE_CURRENCY_CODE)
        this.quoteCurrencyCode = this.arguments?.getString(ARG_BELONGS_TO_QUOTE_CURRENCY_CODE)

//        this.database = CountryDatabase.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_country_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.countryAdapter = CountryRecyclerViewAdapter()
        this.countryAdapter.updateDataSet(CountryDataController.getCountries())

        this.countryRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        this.countryRecyclerView.adapter = this.countryAdapter

//        fetchCurrencyData()
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
                this@CountryListFragment.searchMenuItem = searchMenuItem

                searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                        this@CountryListFragment.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String): Boolean {
                                return true
                            }

                            override fun onQueryTextChange(newText: String): Boolean {
                                return true
                            }
                        })
                        return true
                    }

                    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                        return true
                    }
                })
            }
        }
    }

//    override fun onPause() {
//        super.onPause()
//        this.disposable?.dispose()
//    }
//
//    override fun onDestroy() {
//        CountryDatabase.destroyInstance()
//        this.workerThread.quit()
//
//        super.onDestroy()
//    }

//    private fun fetchCurrencyData() {
//        this.workerThread = SquarkWorkerThread(SquarkWorkerThread::class.java.simpleName)
//        this.workerThread.start()
//        this.workerThread.postTask(Runnable {
//            val countryData = this.database?.countryDataDao()?.getCountries()
//
//            if (countryData != null) {
//                this.countryAdapter.updateDataSet(countries = countryData)
//            }
//        })
//    }
}