package com.delacrixmorgan.squark.service.api

import android.content.Context
import com.delacrixmorgan.squark.R
import okhttp3.Interceptor
import okhttp3.Response

class HeaderCurrencyLayerInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.header("apikey", context.getString(R.string.currency_layer_api_key))
        return chain.proceed(requestBuilder.build())
    }
}