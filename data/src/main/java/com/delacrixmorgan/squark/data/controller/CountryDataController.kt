package com.delacrixmorgan.squark.data.controller

import android.content.Context
import com.delacrixmorgan.squark.data.R
import com.delacrixmorgan.squark.data.getJsonMap
import com.delacrixmorgan.squark.data.model.Country

/**
 * CountryDataController
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

object CountryDataController {
    private var countries: List<Country> = ArrayList()

    var countryMap = mapOf<String, String>()
    var currencyMap = mapOf<String, String>()

    fun getCountries() = countries

    fun updateDataSet(countries: List<Country>) {
        this.countries = countries
    }

    fun populateMaps(context: Context) {
        this.countryMap = context.getJsonMap(R.raw.data_country, "currencies")
        this.currencyMap = context.getJsonMap(R.raw.data_currency, "quotes")
    }
}