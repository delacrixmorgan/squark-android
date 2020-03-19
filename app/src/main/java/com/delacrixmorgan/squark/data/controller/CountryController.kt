package com.delacrixmorgan.squark.data.controller

import com.delacrixmorgan.squark.App
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.getJsonMap
import com.delacrixmorgan.squark.data.dao.CountryDatabase
import com.delacrixmorgan.squark.data.model.Country

object CountryDataController {
    private var countries = listOf<Country>()

    var countryMap = mapOf<String, String>()
    var currencyMap = mapOf<String, String>()

    init {
        CountryDatabase.getInstance(App.appContext)?.countryDataDao()?.getCountries()?.let {
            countries = it
        }

        countryMap = App.appContext.getJsonMap(R.raw.data_country, "currencies")
        currencyMap = App.appContext.getJsonMap(R.raw.data_currency, "quotes")
    }

    fun getCountries() = countries

    fun updateDataSet(countries: List<Country>) {
        this.countries = countries
    }
}