package com.delacrixmorgan.squark

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.delacrixmorgan.squark.common.PreferenceHelper
import com.delacrixmorgan.squark.common.PreferenceHelper.set
import com.delacrixmorgan.squark.common.startFragment
import com.delacrixmorgan.squark.data.api.SquarkApiService
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.controller.CountryDatabase
import com.delacrixmorgan.squark.data.model.Country
import com.delacrixmorgan.squark.data.model.Currency
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * MainActivity
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    private var countryDatabase: CountryDatabase? = null
    private var currencies: List<Currency> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_main)

        this.countryDatabase = CountryDatabase.getInstance(this)
        CountryDataController.populateMaps(this)
        fetchCurrencyData()
    }

    private fun fetchCurrencyData() {
        AsyncTask.execute {
            val countryData = this.countryDatabase?.countryDataDao()?.getCountries() ?: listOf()

            if (countryData.isEmpty()) {
                initCurrencies()
            } else {
                launchCurrencyNavigationFragment(countryData)
            }
        }
    }

    private fun initCurrencies() {
        this.disposable = SquarkApiService
                .create(this)
                .getCurrencies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    this.currencies = result.quotes.map { Currency(code = it.key, rate = it.value) }
                    if (this.currencies.isNotEmpty()) {
                        insertCountries()
                    }
                }, {
                    Snackbar.make(this.mainContainer, getString(R.string.error_api_countries), Snackbar.LENGTH_SHORT).show()
                })
    }

    private fun insertCountries() {
        val database = this.countryDatabase ?: return
        val countries = arrayListOf<Country>()

        CountryDataController.countryMap.forEach { country ->
            val currency = this.currencies.firstOrNull {
                it.code == "USD${country.key}"
            }

            if (currency != null) {
                countries.add(Country(code = country.key, name = country.value, rate = currency.rate))
            }
        }

        countries.forEach {
            AsyncTask.execute {
                database.countryDataDao().insertCountry(it)
            }
        }

        PreferenceHelper.getPreference(this)[PreferenceHelper.UPDATED_TIME_STAMP] = Date().time
        launchCurrencyNavigationFragment(countries)
    }

    private fun launchCurrencyNavigationFragment(countries: List<Country>) {
        CountryDataController.updateDataSet(countries)
        startFragment(CurrencyNavigationFragment.newInstance())
    }

    override fun onBackPressed() {
        if (this.supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            this.supportFragmentManager.popBackStack()
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