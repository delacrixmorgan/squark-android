package com.delacrixmorgan.squark.services.api

import android.content.Context
import com.delacrixmorgan.squark.R
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.header("apikey", "")
        return chain.proceed(requestBuilder.build())
    }
}