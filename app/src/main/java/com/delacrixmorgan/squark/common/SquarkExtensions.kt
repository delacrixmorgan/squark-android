package com.delacrixmorgan.squark.common

import android.app.ActivityManager
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.PreferenceHelper.get
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.model.Country
import java.math.BigDecimal

fun startFragment(context: Context, fragment: Fragment) {
    val activity = context as FragmentActivity

    activity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
}

fun changeAppOverview(activity: AppCompatActivity, theme: Resources.Theme) {
    val typedValue = TypedValue()
    val colour = typedValue.data
    val bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.squark_logo_coin)

    activity.setTaskDescription(ActivityManager.TaskDescription(null, bitmap, colour))
    theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
    bitmap.recycle()
}

fun Float.roundUp() = Math.round(this * 10F) / 10F


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
    return when (bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).precision()) {
        13, 12, 11, 10 -> NumberFormatTypes.BILLIONTH.decimal.format(bigDecimal.movePointLeft(9))
        9, 8, 7 -> NumberFormatTypes.MILLIONTH.decimal.format(bigDecimal.movePointLeft(6))
        6, 5, 4 -> NumberFormatTypes.THOUSANDTH.decimal.format(bigDecimal.movePointLeft(3))
        else -> NumberFormatTypes.HUNDREDTH.decimal.format(bigDecimal)
    }
}

fun CountryDataController.getPreferenceCountry(context: Context, preferenceCurrency: String): Country? {
    return when (preferenceCurrency) {
        PreferenceHelper.BASE_CURRENCY_CODE -> getCountries().firstOrNull {
            it.code == PreferenceHelper.getPreference(context)[PreferenceHelper.BASE_CURRENCY_CODE, PreferenceHelper.DEFAULT_BASE_CURRENCY_CODE]
        }
        else -> getCountries().firstOrNull {
            it.code == PreferenceHelper.getPreference(context)[PreferenceHelper.QUOTE_CURRENCY_CODE, PreferenceHelper.DEFAULT_QUOTE_CURRENCY_CODE]
        }
    }
}

fun CountryDataController.getFilteredCountries(
        searchText: String?
) = if (searchText.isNullOrBlank()) {
    getCountries()
} else {
    val text: String = searchText?.toLowerCase() ?: ""
    getCountries().filter {
        it.name.toLowerCase().contains(text) || it.code.toLowerCase().contains(text)
    }
}