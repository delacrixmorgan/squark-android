package com.delacrixmorgan.squark.services.api

import com.delacrixmorgan.squark.models.CurrencyResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("currency_data/live")
    suspend fun getCurrencies(
        @Query("source") source: String = "usd"
    ): Response<CurrencyResult>
}