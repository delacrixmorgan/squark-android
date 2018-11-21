package com.delacrixmorgan.squark.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.delacrixmorgan.squark.data.model.Country
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().context
        assertEquals("com.delacrixmorgan.squark.data", appContext.packageName)
    }

    @Test
    fun populateCountryJson() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val countries = arrayListOf<Country>()
        val countryMap = context.getJsonMap(R.raw.data_country, "currencies")
        val currencyMap = context.getJsonMap(R.raw.data_currency, "quotes")

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
