package com.delacrixmorgan.squark.api

import com.delacrixmorgan.squark.App
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.model.Currency
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object SquarkService {
    private const val BASE_URL = "http://apilayer.net/api/"
    const val sourceCurrency = "usd"
    val accessKey: String
        get() {
            return App.appContext.getString(R.string.currency_layer_api_key)
        }

    private val client: SquarkServiceClient

    init {
        client = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(SquarkServiceClient::class.java)
    }

    suspend fun getCurrencies(): List<Currency> {
        val result = client.getCurrencies()
        return result.quotes.map { Currency(code = it.key, rate = it.value) }
    }
}