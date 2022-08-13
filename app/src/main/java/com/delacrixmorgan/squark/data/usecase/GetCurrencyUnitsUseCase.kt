package com.delacrixmorgan.squark.data.usecase

import com.delacrixmorgan.squark.common.fromJson
import com.delacrixmorgan.squark.data.shared.NoParams
import com.delacrixmorgan.squark.data.shared.NoParamsFlowUseCase
import com.delacrixmorgan.squark.model.currencyunit.CurrencyUnit
import com.delacrixmorgan.squark.model.currencyunit.CurrencyUnitDtoToModelMapper
import com.delacrixmorgan.squark.service.network.Result
import com.delacrixmorgan.squark.service.remoteconfig.FirebaseRemoteConfigManager
import com.delacrixmorgan.squark.service.remoteconfig.RemoteConfigKeys
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrencyUnitsUseCase @Inject constructor(
    private val gson: Gson,
    private val remoteConfigManager: FirebaseRemoteConfigManager,
    private val currencyUnitDtoToModelMapper: CurrencyUnitDtoToModelMapper
) : NoParamsFlowUseCase<List<CurrencyUnit>, Exception> {
    override fun invoke(params: NoParams): Flow<Result<List<CurrencyUnit>, Exception>> = flow {
        val currencyUnits = currencyUnitDtoToModelMapper.invoke(
            gson.fromJson(remoteConfigManager.getString(RemoteConfigKeys.DataCurrencyUnit.key))
        )
        emit(Result.success(currencyUnits))
    }
}