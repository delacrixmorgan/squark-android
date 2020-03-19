package com.delacrixmorgan.squark.preference.country

import com.delacrixmorgan.squark.data.model.Country

interface CountryListListener {
    fun onCountrySelected(country: Country)
}