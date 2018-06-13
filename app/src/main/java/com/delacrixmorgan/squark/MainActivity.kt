package com.delacrixmorgan.squark

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.delacrixmorgan.squark.common.changeAppOverview
import com.delacrixmorgan.squark.common.showFragment
import com.delacrixmorgan.squark.data.api.SquarkApiService
import com.delacrixmorgan.squark.data.controller.CurrencyDatabase
import com.delacrixmorgan.squark.data.controller.CurrencyWorkerThread
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

    private var database: CurrencyDatabase? = null
    private var disposable: Disposable? = null

    private lateinit var workerThread: CurrencyWorkerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        changeAppOverview(this, theme)

        this.workerThread = CurrencyWorkerThread(CurrencyWorkerThread::class.java.simpleName)
        this.workerThread.start()

        this.database = CurrencyDatabase.getInstance(this)
        fetchCurrencyData()
    }

    override fun onPause() {
        super.onPause()
        this.disposable?.dispose()
    }

    override fun onDestroy() {
        CurrencyDatabase.destroyInstance()
        this.workerThread.quit()

        super.onDestroy()
    }

    private fun startLaunchFragment() {
        showFragment(this, LaunchFragment.newInstance())
    }

    private fun fetchCurrencyData() {
        this.workerThread.postTask(Runnable {
            val currencyData = this.database?.currencyDataDao()?.getCurrencies()

            Handler().post {
                if (currencyData == null || currencyData.isEmpty()) {
                    initCurrencies()
                } else {
                    Snackbar.make(this.mainContainer, "Populate Currencies", Snackbar.LENGTH_SHORT).show()
                    startLaunchFragment()
                }
            }
        })
    }

    private fun initCurrencies() {
        this.disposable = SquarkApiService
                .create(this)
                .updateRate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val currencies = result.quotes?.map {
                        Currency(
                                code = it.key,
                                rate = it.value
                        )
                    }

                    if (currencies != null) {
                        insertCurrencies(currencies)
                    } else {
                        Snackbar.make(this.mainContainer, "Empty API Currencies", Snackbar.LENGTH_SHORT).show()
                    }
                }, { error ->
                    Snackbar.make(this.mainContainer, "Error API Currencies", Snackbar.LENGTH_SHORT).show()
                    Log.e("Error", "$error")
                })
    }

    private fun insertCurrencies(currencies: List<Currency>) {
        this.workerThread.postTask(Runnable {
            this.database?.apply {
                currencies.map {
                    currencyDataDao().insertCurrency(it)
                }
            }
            Snackbar.make(this.mainContainer, "Request API", Snackbar.LENGTH_SHORT).show()
            startLaunchFragment()
        })
    }
}