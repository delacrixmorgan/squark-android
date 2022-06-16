package com.delacrixmorgan.squark.models

import com.delacrixmorgan.squark.data.shared.Mapper
import com.delacrixmorgan.squark.data.usecase.GetCurrencyUnitsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CurrencyDtoToModelMapper @Inject constructor(
    private val getCurrencyUnitsUseCase: GetCurrencyUnitsUseCase
) : Mapper<CurrencyDto, List<Currency>> {
    override suspend fun invoke(input: CurrencyDto): List<Currency> {
        val currencyUnits = getCurrencyUnitsUseCase().first().get()

        return input.quotes.map { dto ->
            val currencyUnit = currencyUnits.first { it.code == dto.key }
            Currency(
                code = currencyUnit.code,
                name = currencyUnit.unit,
                rate = dto.value,
            )
        }
    }
}