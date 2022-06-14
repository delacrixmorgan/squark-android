package com.delacrixmorgan.squark.services.repository

import com.delacrixmorgan.squark.services.api.CurrencyApi
import com.delacrixmorgan.squark.services.network.NetworkRequestManager
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: CurrencyApi,
    private val networkRequestManager: NetworkRequestManager
) {
    suspend fun fetchCurrencies() = networkRequestManager.apiRequest { api.getCurrencies() }
}