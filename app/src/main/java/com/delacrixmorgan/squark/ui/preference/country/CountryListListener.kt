package com.delacrixmorgan.squark.ui.preference.country

import com.delacrixmorgan.squark.data.model.Country

interface CountryListListener {
    fun onCountrySelected(country: Country)
}