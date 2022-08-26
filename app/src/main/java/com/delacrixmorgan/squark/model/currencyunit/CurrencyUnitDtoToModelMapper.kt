package com.delacrixmorgan.squark.model.currencyunit

import com.delacrixmorgan.squark.data.shared.Mapper
import javax.inject.Inject

class CurrencyUnitDtoToModelMapper @Inject constructor() : Mapper<CurrencyUnitDto, List<CurrencyUnit>> {
    override suspend fun invoke(input: CurrencyUnitDto): List<CurrencyUnit> {
        return input.currencies.mapNotNull {
            CurrencyUnit(code = it.key, unit = it.value)
        }
    }
}