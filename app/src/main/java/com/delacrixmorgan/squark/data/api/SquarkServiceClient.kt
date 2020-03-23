package com.delacrixmorgan.squark.data.api

import com.delacrixmorgan.squark.data.model.CurrencyModel
import com.delacrixmorgan.squark.data.service.SquarkService
import com.delacrixmorgan.squark.data.service.SquarkService.sourceCurrency
import kotlinx.serialization.UnstableDefault
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@UnstableDefault
interface SquarkServiceClient {
    @GET("live")
    suspend fun getCurrencies(
        @Query("access_key") accessKey: String? = SquarkService.accessKey,
        @Query("source") source: String? = sourceCurrency
    ): Response<CurrencyModel>
}