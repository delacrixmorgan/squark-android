package com.delacrixmorgan.squark.model.currency

import com.delacrixmorgan.squark.data.shared.Mapper
import com.delacrixmorgan.squark.data.usecase.GetCurrencyUnitsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CurrencyDtoToModelMapper @Inject constructor(
    private val getCurrencyUnitsUseCase: GetCurrencyUnitsUseCase
) : Mapper<CurrencyDto, List<Currency>> {
    override suspend fun invoke(input: CurrencyDto): List<Currency> {
        val currencyUnits = getCurrencyUnitsUseCase().first().get()
        val currencies = input.quotes.mapNotNull { dto ->
            currencyUnits
                .firstOrNull { it.code == dto.key.removePrefix("USD") }
                ?.let { currencyUnit ->
                    Currency(
                        code = currencyUnit.code,
                        name = currencyUnit.unit,
                        rate = dto.value,
                    )
                }
        }.toMutableList()
        if (currencies.firstOrNull { it.code == "USD" } == null) {
            currencies.add(
                Currency(
                    rate = 1.0,
                    code = "USD",
                    name = "United States Dollar"
                )
            )
        }
        return currencies
    }
}