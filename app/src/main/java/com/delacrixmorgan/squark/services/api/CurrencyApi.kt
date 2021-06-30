package com.delacrixmorgan.squark.services.api

import com.delacrixmorgan.squark.models.CurrencyResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("live")
    suspend fun getCurrencies(
        @Query("source") source: String = "usd",
        @Query("access_key") accessKey: String
    ): Response<CurrencyResult>
}