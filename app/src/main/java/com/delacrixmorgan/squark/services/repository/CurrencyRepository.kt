package com.delacrixmorgan.squark.services.repository

import android.util.Log
import com.delacrixmorgan.squark.data.dao.CurrencyDao
import com.delacrixmorgan.squark.models.Currency
import com.delacrixmorgan.squark.models.CurrencyDtoToModelMapper
import com.delacrixmorgan.squark.services.api.CurrencyApi
import com.delacrixmorgan.squark.services.network.Result
import com.delacrixmorgan.squark.services.network.apiRequest
import com.delacrixmorgan.squark.services.network.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
                success = {
                    currencyDao.insertCurrencies(it)
                    emit(Result.success(it))
                },
                failure = {
                    Log.e("CurrencyRepository", "getCurrencies: ${it.message}")
                    emit(Result.error(it))
                }
            )
        }.flowOn(Dispatchers.Default)
    }
}