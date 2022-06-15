package com.delacrixmorgan.squark.services.repository

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
    private val currencyDtoToModelMapper: CurrencyDtoToModelMapper
) {
    suspend fun getCurrencies(): Flow<Result<List<Currency>, Exception>> {
        return flow {
            apiRequest { api.getCurrencies() }.map {
                currencyDtoToModelMapper.invoke(it)
            }.fold(
                success = {
                    // TODO (Save to DAO)
                    emit(Result.success(it))
                },
                failure = {
                    emit(Result.error(it))
                    // TODO (Fallback to Firebase Remote Config)
                }
            )
        }.flowOn(Dispatchers.Default)
    }
}