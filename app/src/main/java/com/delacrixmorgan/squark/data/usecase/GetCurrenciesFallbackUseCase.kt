package com.delacrixmorgan.squark.data.usecase

import com.delacrixmorgan.squark.App
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.getJsonMap
import com.delacrixmorgan.squark.data.shared.NoParams
import com.delacrixmorgan.squark.data.shared.NoParamsFlowUseCase
import com.delacrixmorgan.squark.model.Currency
import com.delacrixmorgan.squark.service.network.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrenciesFallbackUseCase @Inject constructor(
    private val getCurrencyUnitsUseCase: GetCurrencyUnitsUseCase
) : NoParamsFlowUseCase<List<Currency>, Exception> {
    override fun invoke(params: NoParams): Flow<Result<List<Currency>, Exception>> = flow {
        val currencyUnits = getCurrencyUnitsUseCase().first().get()

        // TODO (Change to Use Firebase Remote Config)
        val currencies =
            App.appContext.getJsonMap(R.raw.data_currency, "quotes").mapNotNull { dto ->
                currencyUnits
                    .firstOrNull { it.code == dto.key.removePrefix("USD") }
                    ?.let { currencyUnit ->
                        Currency(
                            code = currencyUnit.code,
                            name = currencyUnit.unit,
                            rate = dto.value.toDouble(),
                        )
                    }
            }
        emit(Result.success(currencies))
    }
}