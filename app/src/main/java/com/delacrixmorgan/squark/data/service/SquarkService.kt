package com.delacrixmorgan.squark.data.service

import android.content.Context
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.data.api.NetworkController
import com.delacrixmorgan.squark.data.api.SquarkResult
import com.delacrixmorgan.squark.data.api.SquarkServiceClient
import com.delacrixmorgan.squark.data.model.CurrencyResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SquarkService {
    private const val BASE_URL = "http://apilayer.net/api/"
    private val client: SquarkServiceClient = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SquarkServiceClient::class.java)

    suspend fun getCurrencies(context: Context): SquarkResult<CurrencyResult> {
        return NetworkController.apiRequest {
            client.getCurrencies(
                accessKey = context.getString(R.string.currency_layer_api_key)
            )
        }
    }
}