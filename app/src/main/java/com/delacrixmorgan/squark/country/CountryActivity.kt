package com.delacrixmorgan.squark.country

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.delacrixmorgan.squark.R
import kotlinx.android.synthetic.main.activity_country.*

/**
 * CountryActivity
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CountryActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_COUNTRY_CODE = "Country.countryCode"

        fun newLaunchIntent(
                context: Context,
                countryCode: String? = null
        ): Intent {
            val launchIntent = Intent(context, CountryActivity::class.java)

            countryCode?.let {
                launchIntent.putExtra(EXTRA_COUNTRY_CODE, it)
            }

            return launchIntent
        }
    }

    private var countryCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (this.intent.extras != null) {
            this.countryCode = this.intent.extras.getString(EXTRA_COUNTRY_CODE)
        }

        this.setContentView(R.layout.activity_country)
        setSupportActionBar(this.toolbar)
        this.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            title = ""
        }

        val routingFragment = CountryRoutingFragment.newInstance(
                countryCode = this.countryCode
        )

        if (savedInstanceState == null) {
            this.supportFragmentManager
                    .beginTransaction()
                    .replace(this.contentContainer.id, routingFragment, routingFragment::class.java.simpleName)
                    .commit()
        }
    }
}