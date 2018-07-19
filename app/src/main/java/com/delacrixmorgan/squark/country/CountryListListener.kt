package com.delacrixmorgan.squark.country

import com.delacrixmorgan.squark.data.model.Country

/**
 * CountryListListener
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

interface CountryListListener {
    fun onCountrySelected(country: Country)
}