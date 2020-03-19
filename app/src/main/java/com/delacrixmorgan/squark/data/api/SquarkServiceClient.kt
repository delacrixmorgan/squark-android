package com.delacrixmorgan.squark.data.api

import com.delacrixmorgan.squark.data.api.SquarkService.sourceCurrency
import com.delacrixmorgan.squark.data.model.CurrencyModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SquarkServiceClient {
    @GET("live")
    suspend fun getCurrencies(
        @Query("access_key") accessKey: String? = SquarkService.accessKey,
        @Query("source") source: String? = sourceCurrency
    ): Response<CurrencyModel.Result>
}