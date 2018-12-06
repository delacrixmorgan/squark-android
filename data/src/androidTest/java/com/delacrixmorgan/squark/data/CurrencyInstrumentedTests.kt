package com.delacrixmorgan.squark.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.delacrixmorgan.squark.data.model.Country
import org.junit.Test
import org.junit.runner.RunWith

/**
 * CurrencyInstrumentedTests
 * squark-android
 *
 * Created by Delacrix Morgan on 06/12/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

@RunWith(AndroidJUnit4::class)
class CurrencyInstrumentedTests {
    private val appContext = InstrumentationRegistry.getInstrumentation().context

    @Test
    fun populateCountryJson() {
        val countries = arrayListOf<Country>()
        val countryMap = appContext.getJsonMap(R.raw.data_country, "currencies")
        val currencyMap = appContext.getJsonMap(R.raw.data_currency, "quotes")

        org.junit.Assert.assertTrue("Should have at least 1 country", countryMap.isNotEmpty())
        org.junit.Assert.assertTrue("Should have at least 1 currency", currencyMap.isNotEmpty())

        countryMap.forEach {
            countries.add(
                    Country(
                            code = it.key,
                            name = it.value,
                            rate = currencyMap["USD${it.key}"]?.toDouble() ?: 0.0
                    )
            )
        }

        org.junit.Assert.assertTrue("Should have at least 1 countryList", countries.isNotEmpty())
    }
}