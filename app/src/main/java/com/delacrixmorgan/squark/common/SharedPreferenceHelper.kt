package com.delacrixmorgan.squark.common

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.delacrixmorgan.squark.App
import com.delacrixmorgan.squark.data.controller.CountryDataController
import java.util.*

object SharedPreferenceHelper {

    enum class Keys {
        BaseCurrency,
        QuoteCurrency,
        LastUpdatedDate,
        IsPersistentMultipleEnabled,
        Multiplier
    }

    const val DEFAULT_BASE_CURRENCY_CODE = "USD"

    val DEFAULT_QUOTE_CURRENCY_CODE: String
        get() {
            val localeCountryCode = Currency.getInstance(Locale.getDefault()).currencyCode
            val localeCountry = CountryDataController.getCountries().firstOrNull {
                it.code == localeCountryCode
            }
            val fallbackCountryCode = "MYR"

            return if (localeCountryCode != DEFAULT_BASE_CURRENCY_CODE && localeCountry != null) {
                localeCountryCode
            } else {
                fallbackCountryCode
            }
        }

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(App.appContext)

    var baseCurrency: String?
        get() = sharedPreferences.getString(Keys.BaseCurrency.name, DEFAULT_BASE_CURRENCY_CODE)
            ?: DEFAULT_BASE_CURRENCY_CODE
        set(value) = sharedPreferences.edit { putString(Keys.BaseCurrency.name, value) }

    var quoteCurrency: String?
        get() = sharedPreferences.getString(Keys.QuoteCurrency.name, DEFAULT_QUOTE_CURRENCY_CODE)
            ?: DEFAULT_QUOTE_CURRENCY_CODE
        set(value) = sharedPreferences.edit { putString(Keys.QuoteCurrency.name, value) }

    var lastUpdatedDate: Date
        get() = sharedPreferences.getString(Keys.LastUpdatedDate.name, "")?.toDateFormat()
            ?: Date()
        set(value) = sharedPreferences.edit {
            putString(
                Keys.LastUpdatedDate.name,
                value.toStringFormat()
            )
        }

    var isPersistentMultiplierEnabled: Boolean
        get() = sharedPreferences.getBoolean(Keys.IsPersistentMultipleEnabled.name, true)
        set(value) = sharedPreferences.edit {
            putBoolean(
                Keys.IsPersistentMultipleEnabled.name,
                value
            )
        }

    var multiplier: Int
        get() = sharedPreferences.getInt(Keys.Multiplier.name, 1)
        set(value) = sharedPreferences.edit { putInt(Keys.Multiplier.name, value) }
}