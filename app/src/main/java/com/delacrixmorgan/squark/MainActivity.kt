package com.delacrixmorgan.squark

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.delacrixmorgan.squark.common.changeAppOverview
import com.delacrixmorgan.squark.common.showFragment
import com.delacrixmorgan.squark.data.api.SquarkApiService
import com.delacrixmorgan.squark.data.controller.CurrencyDatabase
import com.delacrixmorgan.squark.data.model.Currency
import com.delacrixmorgan.squark.launch.LaunchFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        changeAppOverview(this, theme)

        startLaunchFragment()

//        this.database = CurrencyDatabase.getInstance(this)
//        val currencySize = database?.currencyDataDao()?.getCurrencies()?.size ?: 0
//
//        if (currencySize < 0) {
//            initCurrencies()
//        } else {
//            startLaunchFragment()
//        }
    }

    override fun onPause() {
        super.onPause()
        this.disposable?.dispose()
    }

    private fun startLaunchFragment() {
        showFragment(this, LaunchFragment.newInstance())
    }

    private fun initCurrencies() {
        this.disposable = SquarkApiService
                .create(this)
                .updateRate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    result.quotes?.map {
                        val currency = Currency(
                                code = it.key,
                                rate = it.value
                        )
                        database?.apply {
                            currencyDataDao().insertCurrency(currency)
                        }
                    }
                    startLaunchFragment()
                }, { error ->
                    // TODO - Handle Error
                    Log.e("Error", "$error")
                })
    }
}