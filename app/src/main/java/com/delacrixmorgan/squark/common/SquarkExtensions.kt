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

fun CountryDataController.getPreferenceCountry(context: Context, preferenceCurrency: String): Country? {
    return when (preferenceCurrency) {
        PreferenceHelper.BASE_CURRENCY_CODE -> {
            CountryDataController.getCountries().firstOrNull {
                it.code == PreferenceHelper.getPreference(context)[PreferenceHelper.BASE_CURRENCY_CODE, PreferenceHelper.DEFAULT_BASE_CURRENCY_CODE]
            }
        }
        else -> {
            CountryDataController.getCountries().firstOrNull {
                it.code == PreferenceHelper.getPreference(context)[PreferenceHelper.QUOTE_CURRENCY_CODE, PreferenceHelper.DEFAULT_QUOTE_CURRENCY_CODE]
            }
        }
    }
}