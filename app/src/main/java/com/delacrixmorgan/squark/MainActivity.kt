package com.delacrixmorgan.squark

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.delacrixmorgan.squark.common.changeAppOverview
import com.delacrixmorgan.squark.common.showFragment
import com.delacrixmorgan.squark.data.api.SquarkApiService
import com.delacrixmorgan.squark.data.SquarkWorkerThread
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.controller.CountryDatabase
import com.delacrixmorgan.squark.data.model.Country
import com.delacrixmorgan.squark.data.model.Currency
import com.delacrixmorgan.squark.launch.LaunchFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

/**
 * MainActivity
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class MainActivity : AppCompatActivity() {

    private var database: CountryDatabase? = null
    private var disposable: Disposable? = null

    private var countries: List<Country>? = ArrayList()
    private var currencies: List<Currency>? = ArrayList()

    private lateinit var workerThread: SquarkWorkerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        changeAppOverview(this, theme)

        this.workerThread = SquarkWorkerThread(SquarkWorkerThread::class.java.simpleName)
        this.workerThread.start()

        this.database = CountryDatabase.getInstance(this)

        fetchCurrencyData()
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

    private fun startLaunchFragment() {
        showFragment(this, LaunchFragment.newInstance())
    }

    private fun fetchCurrencyData() {
        this.workerThread.postTask(Runnable {
            val countryData = this.database?.countryDataDao()?.getCountries()

            Handler().post {
                if (countryData == null || countryData.isEmpty()) {
                    initCountries()
                } else {
                    startLaunchFragment()
                    CountryDataController.updateDataSet(countryData)
                    Snackbar.make(this.mainContainer, "Populate Countries", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initCountries() {
        this.disposable = SquarkApiService
                .create(this)
                .getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    this.countries = result.quotes?.map {
                        Country(
                                code = it.key,
                                name = it.value
                        )
                    }

                    initCurrencies()
                }, { error ->
                    Snackbar.make(this.mainContainer, "Error API Countries", Snackbar.LENGTH_SHORT).show()
                    Log.e("Error", "$error")
                })


    }

    private fun initCurrencies() {
        this.disposable = SquarkApiService
                .create(this)
                .getCurrencies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    this.currencies = result.quotes?.map {
                        Currency(
                                code = it.key,
                                rate = it.value
                        )
                    }

                    if (this.countries != null && this.currencies != null) {
                        insertCountries()
                    } else {
                        Snackbar.make(this.mainContainer, "Empty API Countries", Snackbar.LENGTH_SHORT).show()
                    }
                }, { error ->
                    Snackbar.make(this.mainContainer, "Error API Countries", Snackbar.LENGTH_SHORT).show()
                    Log.e("Error", "$error")
                })
    }

    private fun insertCountries() {
        this.workerThread.postTask(Runnable {
            this.database?.let { database ->
                this.countries?.forEachIndexed { index, country ->
                    this.currencies?.get(index).let {
                        country.currency = it
                        database.countryDataDao().insertCountry(country)
                    }
                }
            }

            this.countries?.let {
                CountryDataController.updateDataSet(it)
                startLaunchFragment()
                Snackbar.make(this.mainContainer, "Request API", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}