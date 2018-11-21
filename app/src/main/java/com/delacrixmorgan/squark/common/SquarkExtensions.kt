package com.delacrixmorgan.squark.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.PreferenceHelper.get
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.model.Country
import java.math.BigDecimal
import java.util.*

/**
 * SquarkExtensions
 * squark-android
 *
 * Created by Delacrix Morgan on 21/11/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

val defaultQuoteCurrencyCode: String
    get() {
        val localeCountryCode = Currency.getInstance(Locale.getDefault()).currencyCode
        val fallbackCountryCode = "MYR"

        return when {
            localeCountryCode == "USD" -> {
                fallbackCountryCode
            }

            CountryDataController.getCountries().find { it.code == localeCountryCode } != null -> {
                localeCountryCode
            }

            else -> {
                fallbackCountryCode
            }
        }
    }

fun AppCompatActivity.startFragment(fragment: Fragment) {
    supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
}

fun View.performHapticContextClick() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    } else {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
}

fun Float.roundUp() = Math.round(this * 10F) / 10F

//region Context
fun Context.getCompatColor(colorResource: Int): Int {
    return ContextCompat.getColor(this, colorResource)
}

fun Context.launchPlayStore(packageName: String) {
    val url = "https://play.google.com/store/apps/details?id=$packageName"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

fun Context.launchWebsite(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)

    intent.data = Uri.parse(url)
    startActivity(intent)
}

fun Context.shareAppIntent(message: String) {
    val intent = Intent(Intent.ACTION_SEND)

    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, message)

    startActivity(Intent.createChooser(intent, getString(R.string.fragment_support_settings_list_share)))
}

//endregion

//region CountryDataController
fun CountryDataController.getPreferenceCountry(context: Context, preferenceCurrency: String): Country? {
    return when (preferenceCurrency) {
        PreferenceHelper.BASE_CURRENCY_CODE -> getCountries().firstOrNull {
            it.code == PreferenceHelper.getPreference(context)[PreferenceHelper.BASE_CURRENCY_CODE, PreferenceHelper.DEFAULT_BASE_CURRENCY_CODE]
        }
        else -> getCountries().firstOrNull {
            it.code == PreferenceHelper.getPreference(context)[PreferenceHelper.QUOTE_CURRENCY_CODE, defaultQuoteCurrencyCode]
        }
    }
}

fun CountryDataController.getFilteredCountries(
        searchText: String?
) = if (searchText.isNullOrBlank()) {
    getCountries()
} else {
    val text: String = searchText.toLowerCase()
    getCountries().filter {
        it.name.toLowerCase().contains(text) || it.code.toLowerCase().contains(text)
    }
}
//endregion

//region CalculationQuantifier, CalculationResult
fun calculateRowQuantifier(multiplier: Double, position: Int): String {
    val quantifier = (multiplier * (position + 1))
    val bigDecimal = BigDecimal(quantifier).setScale(2, BigDecimal.ROUND_HALF_UP)

    return getNumberFormatType(bigDecimal)
}

fun calculateRowResult(multiplier: Double, position: Int, conversionRate: Double): String {
    val quantifier = (multiplier * (position + 1))
    val result = quantifier * conversionRate
    val bigDecimal = BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP)

    return getNumberFormatType(bigDecimal)
}

fun calculateExpandQuantifier(expandQuantifier: Int, multiplier: Double, position: Int): String {
    val quantifier = (expandQuantifier + 1) * multiplier + (multiplier / 10 * position)
    val bigDecimal = BigDecimal(quantifier).setScale(2, BigDecimal.ROUND_HALF_UP)

    return getNumberFormatType(bigDecimal)
}

fun calculateExpandResult(expandQuantifier: Int, multiplier: Double, position: Int, conversionRate: Double): String {
    val quantifier = (expandQuantifier + 1) * multiplier + (multiplier / 10 * position)
    val result = quantifier * conversionRate
    val bigDecimal = BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP)

    return getNumberFormatType(bigDecimal)
}

fun getNumberFormatType(bigDecimal: BigDecimal): String {
    val roundedBigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).precision()
    return when {
        roundedBigDecimal >= 16 -> NumberFormatTypes.QUADRILLIONTH.decimal.format(bigDecimal.movePointLeft(15))
        roundedBigDecimal >= 13 -> NumberFormatTypes.TRILLIONTH.decimal.format(bigDecimal.movePointLeft(12))
        roundedBigDecimal >= 10 -> NumberFormatTypes.BILLIONTH.decimal.format(bigDecimal.movePointLeft(9))
        roundedBigDecimal >= 7 -> NumberFormatTypes.MILLIONTH.decimal.format(bigDecimal.movePointLeft(6))
        else -> NumberFormatTypes.HUNDREDTH.decimal.format(bigDecimal)
    }
}
//endregion