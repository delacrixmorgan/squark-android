package com.delacrixmorgan.squark.data.usecase

import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.common.isMoreThanADay
import com.delacrixmorgan.squark.data.dao.CurrencyDao
import com.delacrixmorgan.squark.data.shared.NoParams
import com.delacrixmorgan.squark.data.shared.NoParamsFlowUseCase
import com.delacrixmorgan.squark.model.currency.Currency
import com.delacrixmorgan.squark.service.network.Result
import com.delacrixmorgan.squark.service.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val currencyRepository: CurrencyRepository,
    private val getCurrenciesFallbackUseCase: GetCurrenciesFallbackUseCase
) : NoParamsFlowUseCase<List<Currency>, Exception> {
    override fun invoke(params: NoParams): Flow<Result<List<Currency>, Exception>> {
        return if (SharedPreferenceHelper.lastUpdatedDate == null || SharedPreferenceHelper.lastUpdatedDate?.isMoreThanADay(LocalDateTime.now()) == true) {
            fetchRemotely()
        } else {
            fetchLocally()
        }
    }

    private fun fetchRemotely(): Flow<Result<List<Currency>, Exception>> {
        return currencyRepository.getCurrencies()
            .flatMapLatest { result: Result<List<Currency>, Exception> ->
                result.fold(
                    success = {
                        flow {
                            emit(result)
                        }
                    },
                    failure = {
                        flow {
                            val fallbackCurrencies = getCurrenciesFallbackUseCase().first().get()
                            emit(Result.success(fallbackCurrencies))
                        }
                    }
                )
            }
    }

    private fun fetchLocally(): Flow<Result.Success<List<Currency>, Nothing>> {
        return flow {
            val daoCurrencies = currencyDao.getCurrencies()
            val currencies = daoCurrencies.first()
                .ifEmpty { getCurrenciesFallbackUseCase().first().get() }
                .sortedBy { it.name }
            emit(Result.success(currencies))
        }
    }
}