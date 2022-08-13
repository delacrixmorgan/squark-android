package com.delacrixmorgan.squark.common

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.core.os.ConfigurationCompat
import androidx.preference.PreferenceManager
import com.delacrixmorgan.squark.App
import java.time.LocalDateTime
import java.util.Currency

object SharedPreferenceHelper {

    enum class Keys {
        BaseCurrency,
        QuoteCurrency,
        LastUpdatedDate,
        IsPersistentMultipleEnabled,
        Multiplier
    }

    private const val DEFAULT_BASE_CURRENCY_CODE = "USD"
    private const val DEFAULT_QUOTE_FALLBACK_CURRENCY_CODE = "GBP"

    private val DEFAULT_QUOTE_CURRENCY_CODE: String
        get() {
            val currentLocale = ConfigurationCompat.getLocales(
                App.appContext.resources.configuration
            )[0]
            val localeCountryCode = Currency.getInstance(currentLocale).currencyCode

            return if (localeCountryCode != DEFAULT_BASE_CURRENCY_CODE) {
                localeCountryCode
            } else {
                DEFAULT_QUOTE_FALLBACK_CURRENCY_CODE
            }
        }

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(App.appContext)

    var baseCurrency: String?
        get() = sharedPreferences.getString(Keys.BaseCurrency.name, DEFAULT_BASE_CURRENCY_CODE)
        set(value) = sharedPreferences.edit { putString(Keys.BaseCurrency.name, value) }

    var quoteCurrency: String?
        get() = sharedPreferences.getString(Keys.QuoteCurrency.name, DEFAULT_QUOTE_CURRENCY_CODE)
        set(value) = sharedPreferences.edit { putString(Keys.QuoteCurrency.name, value) }

    var lastUpdatedDate: LocalDateTime?
        get() {
            return sharedPreferences.getString(Keys.LastUpdatedDate.name, null)?.toLocalDateTime()
        }
        set(value) = sharedPreferences.edit {
            putString(
                Keys.LastUpdatedDate.name,
                value?.toStringFormat()
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