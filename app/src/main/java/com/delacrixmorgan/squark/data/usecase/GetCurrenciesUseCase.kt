package com.delacrixmorgan.squark.data.usecase

import com.delacrixmorgan.squark.data.shared.NoParams
import com.delacrixmorgan.squark.data.shared.NoParamsFlowUseCase
import com.delacrixmorgan.squark.models.Currency
import com.delacrixmorgan.squark.services.network.Result
import com.delacrixmorgan.squark.services.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val getCurrenciesFallbackUseCase: GetCurrenciesFallbackUseCase
) : NoParamsFlowUseCase<List<Currency>, Exception> {
    override fun invoke(params: NoParams): Flow<Result<List<Currency>, Exception>> {
        // TODO (Use Fallback Until Overhaul is Completed)
        return flow {
            val fallbackCurrencies = getCurrenciesFallbackUseCase().first().get().sortedBy { it.name }
            emit(Result.success(fallbackCurrencies))
        }
//        return currencyRepository.getCurrencies()
//            .flatMapLatest { result: Result<List<Currency>, Exception> ->
//                result.fold(
//                    success = {
//                        flow {
//                            emit(result)
//                        }
//                    },
//                    failure = {
//                        flow {
//                            val fallbackCurrencies = getCurrenciesFallbackUseCase().first().get()
//                            emit(Result.success(fallbackCurrencies))
//                        }
//                    }
//                )
//            }
    }
}