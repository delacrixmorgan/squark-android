package com.delacrixmorgan.squark.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.DEFAULT_BASE_CURRENCY_CODE
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.DEFAULT_QUOTE_CURRENCY_CODE
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.models.Country
import com.delacrixmorgan.squark.models.Currency
import org.json.JSONObject
import java.io.BufferedReader
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

/**
 * Common
 */

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

/**
 * Date
 */

fun Date.toStringFormat(): String {
    val localDateTime = LocalDateTime.ofInstant(
        this.toInstant(),
        ZoneOffset.UTC
    )
    return DateTimeFormatter.ISO_INSTANT.format(localDateTime)
}

fun String.toDateFormat(): Date {
    val localDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_INSTANT)
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
}

/**
 * Context
 */
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

fun Context.getJsonMap(rawFile: Int, key: String): Map<String, String> {
    val inputStream = resources.openRawResource(rawFile)
    val responseObject = inputStream.bufferedReader().use(BufferedReader::readText)

    val map = HashMap<String, String>()
    val jsonObject = JSONObject(responseObject).optJSONObject(key)

    jsonObject?.keys()?.forEach {
        map[it] = "${jsonObject[it]}"
    }

    return map
}

/**
 * CountryDataController
 */
fun CountryDataController.getPreferenceCurrency(
    preferenceCurrency: String?
): Country? {
    val fallbackCurrency = if (preferenceCurrency == SharedPreferenceHelper.baseCurrency) {
        DEFAULT_BASE_CURRENCY_CODE
    } else {
        DEFAULT_QUOTE_CURRENCY_CODE
    }

    return getCountries().firstOrNull {
        it.code == (preferenceCurrency ?: fallbackCurrency)
    }
}

fun CountryDataController.getFilteredCountries(
    searchText: String?
) = if (searchText.isNullOrBlank()) {
    getCountries()
} else {
    val text: String = searchText.lowercase()
    getCountries().filter {
        it.name.lowercase().contains(text) || it.code.lowercase().contains(text)
    }
}

/**
 * CalculationQuantifier, CalculationResult
 */
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