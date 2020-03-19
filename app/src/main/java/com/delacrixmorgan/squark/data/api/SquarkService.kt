package com.delacrixmorgan.squark.data.api

import com.delacrixmorgan.squark.App
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.data.model.CurrencyModel
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object SquarkService {
    private const val BASE_URL = "http://apilayer.net/api/"
    private val client: SquarkServiceClient

    const val sourceCurrency = "usd"
    val accessKey: String
        get() {
            return App.appContext.getString(R.string.currency_layer_api_key)
        }

    init {
        client = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(SquarkServiceClient::class.java)
    }

    suspend fun getCurrencies(): SquarkResult<CurrencyModel.Result> {
        return NetworkController.apiRequest {
            client.getCurrencies()
        }
    }
}