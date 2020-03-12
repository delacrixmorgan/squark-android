package com.delacrixmorgan.squark.common

import com.delacrixmorgan.squark.data.controller.CountryDataController
import java.util.*

object SharedPreferenceHelper {
    const val baseCurrencyCode = "Preference.BaseCurrencyCode"
    const val quoteCurrencyCode = "Preference.QuoteCurrencyCode"
    const val updatedTimeStamp = "Preference.UpdatedTimeStamp"
    const val isMultiplierEnabled = "Preference.MultiplierEnabled"
    const val multiplier = "Preference.Multiplier"

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