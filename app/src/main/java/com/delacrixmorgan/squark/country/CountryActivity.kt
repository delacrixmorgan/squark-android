package com.delacrixmorgan.squark.country

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.delacrixmorgan.squark.R
import kotlinx.android.synthetic.main.activity_currency.*

/**
 * CountryActivity
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CountryActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_BASE_CURRENCY_CODE = "Currency.baseCode"
        private const val EXTRA_QUOTE_CURRENCY_CODE = "Currency.quoteCode"

        fun newLaunchIntent(
                context: Context,
                baseCurrencyCode: String? = null,
                quoteCurrencyCode: String? = null
        ): Intent {
            val launchIntent = Intent(context, CountryActivity::class.java)

            baseCurrencyCode?.let {
                launchIntent.putExtra(EXTRA_BASE_CURRENCY_CODE, it)
            }

            quoteCurrencyCode?.let {
                launchIntent.putExtra(EXTRA_QUOTE_CURRENCY_CODE, it)
            }

            return launchIntent
        }
    }

    private var baseCurrencyCode: String? = null
    private var quoteCurrencyCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_currency)

        if (this.intent.extras != null) {
            this.baseCurrencyCode = this.intent.extras.getString(EXTRA_BASE_CURRENCY_CODE)
            this.quoteCurrencyCode = this.intent.extras.getString(EXTRA_QUOTE_CURRENCY_CODE)
        }

        val routingFragment = CountryRoutingFragment.newInstance(
                baseCurrencyCode = this.baseCurrencyCode,
                quoteCurrencyCode = this.quoteCurrencyCode
        )

        if (savedInstanceState == null) {
            this.supportFragmentManager
                    .beginTransaction()
                    .replace(this.contentContainer.id, routingFragment, routingFragment::class.java.simpleName)
                    .commit()
        }
    }
}