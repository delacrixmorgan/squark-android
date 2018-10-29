package com.delacrixmorgan.squark

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.delacrixmorgan.squark.common.PreferenceHelper
import com.delacrixmorgan.squark.common.PreferenceHelper.set
import com.delacrixmorgan.squark.common.startFragment
import com.delacrixmorgan.squark.data.SquarkWorkerThread
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

/**
 * MainActivity
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class MainActivity : AppCompatActivity() {

    private var countryDatabase: CountryDatabase? = null
    private var disposable: Disposable? = null

    private var countries: List<Country> = ArrayList()
    private var currencies: List<Currency> = ArrayList()

    private lateinit var workerThread: SquarkWorkerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAnalytics.getInstance(this)

        setContentView(R.layout.activity_main)

        this.workerThread = SquarkWorkerThread(SquarkWorkerThread::class.java.simpleName)
        this.workerThread.start()

        this.countryDatabase = CountryDatabase.getInstance(this)

        fetchCurrencyData()
    }

    private fun fetchCurrencyData() {
        this.workerThread.postTask(Runnable {
            val countryData = this.countryDatabase?.countryDataDao()?.getCountries()

            Handler().post {
                if (countryData == null || countryData.isEmpty()) {
                    initCountries(completion = { error ->
                        if (error != null) {
                            Snackbar.make(this.mainContainer, getString(R.string.error_api_countries), Snackbar.LENGTH_SHORT).show()
                        }

                        initCurrencies()
                    })
                } else {
                    startFragment(CurrencyNavigationFragment.newInstance())
                    CountryDataController.updateDataSet(countryData)
                }
            }
        })
    }

    private fun initCountries(completion: (error: Throwable?) -> Unit) {
        this.disposable = SquarkApiService
                .create(this)
                .getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    this.countries = result.quotes?.map { Country(code = it.key, name = it.value.capitalize(), rate = 0.0) } ?: arrayListOf()
                    completion.invoke(null)
                }, { error ->
                    completion.invoke(error)
                })
    }

    private fun initCurrencies() {
        this.disposable = SquarkApiService
                .create(this)
                .getCurrencies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    this.currencies = result.quotes.map { Currency(code = it.key, rate = it.value) }
                    if (this.countries.isNotEmpty() && this.currencies.isNotEmpty()) {
                        insertCountries()
                    }
                }, {
                    Snackbar.make(this.mainContainer, getString(R.string.error_api_countries), Snackbar.LENGTH_SHORT).show()
                })
    }

    private fun insertCountries() {
        val database = this.countryDatabase ?: return
        this.countries.forEachIndexed { index, country ->
            this.currencies[index].let {
                country.rate = it.rate
                AsyncTask.execute {
                    database.countryDataDao().insertCountry(country)
                }
            }
        }

        PreferenceHelper.getPreference(this)[PreferenceHelper.UPDATED_TIME_STAMP] = Date().time
        CountryDataController.updateDataSet(this.countries)
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
        this.workerThread.quit()

        super.onDestroy()
    }
}