package com.delacrixmorgan.squark.services.api

import com.delacrixmorgan.squark.models.CurrencyResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("api/live")
    suspend fun getCurrencies(
        @Query("access_key") accessKey: String,
        @Query("source") source: String = "usd"
    ): Response<CurrencyResult>
}