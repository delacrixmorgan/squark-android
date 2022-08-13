package com.delacrixmorgan.squark.data.shared

import com.delacrixmorgan.squark.service.network.Result
import kotlinx.coroutines.flow.Flow

interface NoParamsFlowUseCase<out Type, out Error : Exception> :
    FlowUseCase<NoParams, Type, Error> {

    operator fun invoke(): Flow<Result<Type, Error>> = invoke(NoParams)
}

internal typealias NoParams = Unit