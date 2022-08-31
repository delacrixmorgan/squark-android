package com.delacrixmorgan.squark.service.api

import android.content.Context
import com.delacrixmorgan.squark.R
import okhttp3.Interceptor
import okhttp3.Response

class HeaderUnsplashInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.header("Authorization", "Client-ID ${context.getString(R.string.unsplash_api_key)}")
        return chain.proceed(requestBuilder.build())
    }
}