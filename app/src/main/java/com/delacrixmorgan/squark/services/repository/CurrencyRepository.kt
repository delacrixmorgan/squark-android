package com.delacrixmorgan.squark.services.repository

import android.content.Context
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.services.api.CurrencyApi
import com.delacrixmorgan.squark.services.network.NetworkRequestManager

class CurrencyRepository(
    private val api: CurrencyApi,
    private val networkRequestManager: NetworkRequestManager
) {
    suspend fun fetchCurrencies(context: Context) = networkRequestManager.apiRequest {
        context.getString(R.string.currency_layer_api_key)
        api.getCurrencies()
    }
}