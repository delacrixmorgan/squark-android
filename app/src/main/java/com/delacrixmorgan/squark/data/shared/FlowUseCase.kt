package com.delacrixmorgan.squark.data.shared

import com.delacrixmorgan.squark.service.network.Result
import kotlinx.coroutines.flow.Flow

interface FlowUseCase<in Params, out Type, out Error : Exception> {
    operator fun invoke(params: Params): Flow<Result<Type, Error>>
}
