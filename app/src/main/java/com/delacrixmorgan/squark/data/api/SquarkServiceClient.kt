package com.delacrixmorgan.squark.data.api

import com.delacrixmorgan.squark.data.model.CurrencyResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SquarkServiceClient {
    @GET("live")
    suspend fun getCurrencies(
        @Query("source") source: String = "usd",
        @Query("access_key") accessKey: String
    ): Response<CurrencyResult>
}