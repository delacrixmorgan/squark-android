package com.delacrixmorgan.squark.service.repository

import android.util.Log
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.data.dao.CurrencyDao
import com.delacrixmorgan.squark.model.currency.Currency
import com.delacrixmorgan.squark.model.currency.CurrencyDtoToModelMapper
import com.delacrixmorgan.squark.service.api.CurrencyApi
import com.delacrixmorgan.squark.service.network.Result
import com.delacrixmorgan.squark.service.network.apiRequest
import com.delacrixmorgan.squark.service.network.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDateTime
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: CurrencyApi,
    private val currencyDao: CurrencyDao,
    private val currencyDtoToModelMapper: CurrencyDtoToModelMapper,
) {
    fun getCurrencies(): Flow<Result<List<Currency>, Exception>> {
        return flow {
            apiRequest { api.getCurrencies() }.map {
                currencyDtoToModelMapper.invoke(it)
            }.fold(
                success = { currencies ->
                    currencyDao.insertCurrencies(currencies)
                    SharedPreferenceHelper.lastUpdatedDate = LocalDateTime.now()
                    emit(Result.success(currencies))
                },
                failure = {
                    Log.e("CurrencyRepository", "getCurrencies: ${it.message}")
                    emit(Result.error(it))
                }
            )
        }.flowOn(Dispatchers.Default)
    }
}