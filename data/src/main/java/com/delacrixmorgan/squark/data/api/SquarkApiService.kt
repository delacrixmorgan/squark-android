package com.delacrixmorgan.squark.data.api

import android.content.Context
import com.delacrixmorgan.squark.data.R
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * SquarkApiService
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

interface SquarkApiService {

    @GET("live")
    fun updateRate(
            @Query("access_key") accessKey: String? = ACCESS_KEY,
            @Query("source") source: String? = SOURCE
    ): Observable<SquarkModel.Result>

    companion object {
        private const val BASE_URL = "http://apilayer.net/api/"
        private const val SOURCE = "usd"

        private lateinit var ACCESS_KEY: String

        fun create(context: Context): SquarkApiService {
            ACCESS_KEY = context.getString(R.string.currency_layer_api_key)

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()

            return retrofit.create(SquarkApiService::class.java)
        }
    }
}