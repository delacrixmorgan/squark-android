package com.delacrixmorgan.squark.data.usecase

import android.content.Context
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.getJsonMap
import com.delacrixmorgan.squark.data.shared.NoParams
import com.delacrixmorgan.squark.data.shared.NoParamsFlowUseCase
import com.delacrixmorgan.squark.models.CurrencyUnit
import com.delacrixmorgan.squark.services.network.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrencyUnitsUseCase @Inject constructor(
    private val appContext: Context
) : NoParamsFlowUseCase<List<CurrencyUnit>, Exception> {
    override suspend fun invoke(params: NoParams): Flow<Result<List<CurrencyUnit>, Exception>> = flow {
        val currencyUnits = appContext.getJsonMap(R.raw.data_currency_unit, "currencies").map {
            CurrencyUnit(code = it.key.removePrefix("USD"), unit = it.value)
        }
        emit(Result.success(currencyUnits))
    }
}