package com.delacrixmorgan.squark.api

import com.delacrixmorgan.squark.api.SquarkService.sourceCurrency
import com.delacrixmorgan.squark.data.api.CountryModel
import com.delacrixmorgan.squark.data.api.CurrencyModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface SquarkServiceClient {
    @GET("list")
    suspend fun getCountries(
        @Query("access_key") accessKey: String? = SquarkService.accessKey,
        @Query("source") source: String? = sourceCurrency
    ): Observable<CountryModel.Result>

    @GET("live")
    suspend fun getCurrencies(
        @Query("access_key") accessKey: String? = SquarkService.accessKey,
        @Query("source") source: String? = sourceCurrency
    ): CurrencyModel.Result
}