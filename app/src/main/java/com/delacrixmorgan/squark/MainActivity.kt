package com.delacrixmorgan.squark

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.delacrixmorgan.squark.common.changeAppOverview
import com.delacrixmorgan.squark.common.showFragment
import com.delacrixmorgan.squark.data.api.SquarkApiService
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

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeAppOverview(this, theme)

        this.disposable = SquarkApiService
                .create(this)
                .updateRate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Log.i("MainActivity", "Result: ${result.success}")
                            showFragment(this, LaunchFragment.newInstance())
                        },
                        { error ->
                            Log.i("MainActivity", "Error: $error")
                        }
                )
    }

    override fun onPause() {
        super.onPause()
        this.disposable?.dispose()
    }
}