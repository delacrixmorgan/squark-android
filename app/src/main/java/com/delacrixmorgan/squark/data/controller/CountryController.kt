package com.delacrixmorgan.squark.data.controller

import com.delacrixmorgan.squark.App
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.getJsonMap
import com.delacrixmorgan.squark.model.Country

// TODO (Move to Country Repository)
@Deprecated("To be removed.")
object CountryDataController {
    private var countries = listOf<Country>()

    var countryMap = mapOf<String, String>()
    var currencyMap = mapOf<String, String>()

    init {
        countryMap = App.appContext.getJsonMap(R.raw.data_currency_unit, "currencies")
        currencyMap = App.appContext.getJsonMap(R.raw.data_currency, "quotes")
    }

    fun getCountries() = countries
}