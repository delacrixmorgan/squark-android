package com.delacrixmorgan.squark.common

import com.delacrixmorgan.squark.data.controller.CountryDataController
import java.util.*

/**
 * SharedPreferenceHelper
 * squark-android
 *
 * Created by Delacrix Morgan on 12/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

object SharedPreferenceHelper {
    const val BASE_CURRENCY_CODE = "Preference.BaseCurrencyCode"
    const val QUOTE_CURRENCY_CODE = "Preference.QuoteCurrencyCode"
    const val UPDATED_TIME_STAMP = "Preference.UpdatedTimeStamp"
    const val MULTIPLIER_ENABLED = "Preference.MultiplierEnabled"
    const val MULTIPLIER = "Preference.Multiplier"

    const val DEFAULT_UPDATED_TIME_STAMP = 0L
    const val DEFAULT_BASE_CURRENCY_CODE = "USD"
    val DEFAULT_QUOTE_CURRENCY_CODE: String
        get() {
            val localeCountryCode = Currency.getInstance(Locale.getDefault()).currencyCode
            val localeCountry = CountryDataController.getCountries().firstOrNull { it.code == localeCountryCode }
            val fallbackCountryCode = "MYR"

            return if (localeCountryCode != DEFAULT_BASE_CURRENCY_CODE && localeCountry != null) {
                localeCountryCode
            } else {
                fallbackCountryCode
            }
        }
}