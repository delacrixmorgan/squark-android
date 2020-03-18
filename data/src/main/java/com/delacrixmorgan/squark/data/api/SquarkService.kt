package com.delacrixmorgan.squark.data.api

import android.content.Context
import com.delacrixmorgan.squark.data.R
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object SquarkService {
    private const val BASE_URL = "http://apilayer.net/api/"
    private const val SOURCE = "usd"
    private lateinit var ACCESS_KEY: String

    fun create(context: Context): SquarkServiceClient {
        ACCESS_KEY = context.getString(R.string.currency_layer_api_key)

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(SquarkServiceClient::class.java)
    }

//    suspend fun getCards(context: Context): CurrencyModel.Result {
//
//    }
}