package com.delacrixmorgan.squark.data.service

import com.delacrixmorgan.squark.App
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.data.api.NetworkController
import com.delacrixmorgan.squark.data.api.SquarkResult
import com.delacrixmorgan.squark.data.api.SquarkServiceClient
import com.delacrixmorgan.squark.data.model.CurrencyModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@UnstableDefault
object SquarkService {
    private const val BASE_URL = "http://apilayer.net/api/"
    private val client: SquarkServiceClient

    const val sourceCurrency = "usd"
    val accessKey: String
        get() {
            return App.appContext.getString(R.string.currency_layer_api_key)
        }

    init {
        val contentType = "application/json".toMediaType()
        client = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(SquarkServiceClient::class.java)
    }

    suspend fun getCurrencies(): SquarkResult<CurrencyModel> {
        return NetworkController.apiRequest {
            client.getCurrencies()
        }
    }
}