package com.delacrixmorgan.squark.data.api

import retrofit2.Response

object NetworkController {
    suspend inline fun <reified T> apiRequest(crossinline apiCall: suspend () -> Response<T>): SquarkResult<T> {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    SquarkResult.Success(body)
                } else {
                    SquarkResult.Failure(Exception(response.message()))
                }
            } else {
                SquarkResult.Failure(Exception(response.message()))
            }
        } catch (exception: Exception) {
            return SquarkResult.Failure(exception)
        }
    }
}