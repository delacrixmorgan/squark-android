package com.delacrixmorgan.squark

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.delacrixmorgan.squark.common.changeAppOverview
import com.delacrixmorgan.squark.common.showFragment
import com.delacrixmorgan.squark.launch.LaunchFragment

/**
 * MainActivity
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeAppOverview(this, theme)
        showFragment(this, LaunchFragment.newInstance())
    }
}
