package com.delacrixmorgan.squark.service.api

import com.delacrixmorgan.squark.model.currency.CurrencyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("currency_data/live")
    suspend fun getCurrencies(
        @Query("source") source: String = "usd"
    ): Response<CurrencyDto>
}