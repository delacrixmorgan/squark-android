package com.delacrixmorgan.squark.data.usecase

import com.delacrixmorgan.squark.common.fromJson
import com.delacrixmorgan.squark.data.shared.NoParams
import com.delacrixmorgan.squark.data.shared.NoParamsFlowUseCase
import com.delacrixmorgan.squark.model.currency.Currency
import com.delacrixmorgan.squark.model.currency.CurrencyDtoToModelMapper
import com.delacrixmorgan.squark.service.network.Result
import com.delacrixmorgan.squark.service.remoteconfig.FirebaseRemoteConfigManager
import com.delacrixmorgan.squark.service.remoteconfig.RemoteConfigKeys
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrenciesFallbackUseCase @Inject constructor(
    private val gson: Gson,
    private val remoteConfigManager: FirebaseRemoteConfigManager,
    private val currencyDtoToModelMapper: CurrencyDtoToModelMapper
) : NoParamsFlowUseCase<List<Currency>, Exception> {
    override fun invoke(params: NoParams): Flow<Result<List<Currency>, Exception>> = flow {
        val currencies = currencyDtoToModelMapper.invoke(
            gson.fromJson(remoteConfigManager.getString(RemoteConfigKeys.DataCurrency.key))
        )
        emit(Result.success(currencies))
    }
}