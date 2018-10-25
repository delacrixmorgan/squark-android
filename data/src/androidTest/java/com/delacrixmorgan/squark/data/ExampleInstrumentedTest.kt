package com.delacrixmorgan.squark.data

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.delacrixmorgan.squark.data.model.Country
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedReader

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

    private fun Context.getJsonMap(rawFile: Int, key: String): Map<String, String> {
        val inputStream = resources.openRawResource(rawFile)
        val responseObject = inputStream.bufferedReader().use(BufferedReader::readText)

        val map = HashMap<String, String>()
        val jsonObject = JSONObject(responseObject).optJSONObject(key)

        jsonObject.keys().forEach {
            map[it] = "${jsonObject[it]}"
        }

        return map
    }
}
