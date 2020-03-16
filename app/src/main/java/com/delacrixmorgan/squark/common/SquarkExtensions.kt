package com.delacrixmorgan.squark.common

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.DEFAULT_BASE_CURRENCY_CODE
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.DEFAULT_QUOTE_CURRENCY_CODE
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.baseCurrencyCode
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.model.Country
import java.math.BigDecimal
import kotlin.math.roundToInt


fun View.performHapticContextClick() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    } else {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
}

fun Float.roundUp() = (this * 10F).roundToInt() / 10F

fun Int.compatColor(context: Context?): Int {
    return if (context == null) {
        0
    } else {
        ContextCompat.getColor(context, this)
    }
}

fun Fragment.launchWebsite(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)

    intent.data = Uri.parse(url)
    startActivity(intent)
}

//region Context
val Context.isNetworkAvailable: Boolean
    get() {
        val connectivityManager = getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(
                network
            ) ?: return false

            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            )
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected == true
        }
    }

fun Context.launchPlayStore(packageName: String) {
    val url = "https://play.google.com/store/apps/details?id=$packageName"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

fun Context.shareAppIntent(message: String) {
    val intent = Intent(Intent.ACTION_SEND)

    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, message)

    startActivity(
        Intent.createChooser(
            intent,
            getString(R.string.fragment_support_settings_list_share)
        )
    )
}
//endregion

//region CountryDataController
fun CountryDataController.getPreferenceCountry(
    context: Context,
    preferenceCurrency: String
): Country? {
    val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
    val fallbackCurrency =
        if (preferenceCurrency == baseCurrencyCode) DEFAULT_BASE_CURRENCY_CODE else DEFAULT_QUOTE_CURRENCY_CODE

    return getCountries().firstOrNull {
        it.code == preferenceManager.getString(preferenceCurrency, fallbackCurrency)
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

fun calculateExpandResult(
    expandQuantifier: Int,
    multiplier: Double,
    position: Int,
    conversionRate: Double
): String {
    val quantifier = (expandQuantifier + 1) * multiplier + (multiplier / 10 * position)
    val result = quantifier * conversionRate
    val bigDecimal = BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP)

    return getNumberFormatType(bigDecimal)
}

fun getNumberFormatType(bigDecimal: BigDecimal): String {
    val roundedBigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).precision()
    return when {
        roundedBigDecimal >= 16 -> NumberFormatTypes.QUADRILLIONTH.decimal.format(
            bigDecimal.movePointLeft(
                15
            )
        )
        roundedBigDecimal >= 13 -> NumberFormatTypes.TRILLIONTH.decimal.format(
            bigDecimal.movePointLeft(
                12
            )
        )
        roundedBigDecimal >= 10 -> NumberFormatTypes.BILLIONTH.decimal.format(
            bigDecimal.movePointLeft(
                9
            )
        )
        roundedBigDecimal >= 7 -> NumberFormatTypes.MILLIONTH.decimal.format(
            bigDecimal.movePointLeft(
                6
            )
        )
        else -> NumberFormatTypes.HUNDREDTH.decimal.format(bigDecimal)
    }
}
//endregion