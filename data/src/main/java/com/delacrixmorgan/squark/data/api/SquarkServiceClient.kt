package com.delacrixmorgan.squark.data.api

import android.content.Context
import com.delacrixmorgan.squark.data.R
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface SquarkServiceClient {
    @GET("list")
    fun getCountries(
        @Query("access_key") accessKey: String? = "ACCESS_KEY",
        @Query("source") source: String? = "SOURCE"
    ): Observable<CountryModel.Result>

    @GET("live")
    fun getCurrencies(
        @Query("access_key") accessKey: String? = "ACCESS_KEY",
        @Query("source") source: String? = "SOURCE"
    ): CurrencyModel.Result
}