package com.delacrixmorgan.squark.common

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * PreferenceHelper
 * squark-android
 *
 * Created by Delacrix Morgan on 23/06/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

object PreferenceHelper {

    const val BASE_CURRENCY_CODE = "Preference.BaseCurrencyCode"
    const val QUOTE_CURRENCY_CODE = "Preference.QuoteCurrencyCode"
    const val UPDATED_TIME_STAMP = "Preference.UpdatedTimeStamp"

    const val DEFAULT_BASE_CURRENCY_CODE = "USD"
    const val DEFAULT_UPDATED_TIME_STAMP = 0L

    fun getPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit({ it.putString(key, value) })
            is Int -> edit({ it.putInt(key, value) })
            is Boolean -> edit({ it.putBoolean(key, value) })
            is Float -> edit({ it.putFloat(key, value) })
            is Long -> edit({ it.putLong(key, value) })
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    operator inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}