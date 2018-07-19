package com.delacrixmorgan.squark.data.controller

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

    fun getCountries() = countries

    fun updateDataSet(countries: List<Country>) {
        this.countries = countries
    }
}