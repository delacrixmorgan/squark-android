package com.delacrixmorgan.squark.data.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * SquarkApiService
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

interface SquarkApiService {

    companion object {
        private const val BASE_URL = "http://apilayer.net/api/"
        private const val ACCESS_KEY = ""
        private const val LIST_END_POINT = "list"
        private const val LIVE_END_POINT = "live"

        fun create(): SquarkApiService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()

            return retrofit.create(SquarkApiService::class.java)
        }
    }
}