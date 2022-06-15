package com.delacrixmorgan.squark.data.usecase

import com.delacrixmorgan.squark.data.shared.NoParams
import com.delacrixmorgan.squark.data.shared.NoParamsFlowUseCase
import com.delacrixmorgan.squark.models.Currency
import com.delacrixmorgan.squark.services.network.Result
import com.delacrixmorgan.squark.services.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : NoParamsFlowUseCase<List<Currency>, Exception> {
    override suspend fun invoke(params: NoParams): Flow<Result<List<Currency>, Exception>> {
        return currencyRepository.getCurrencies()
    }
}