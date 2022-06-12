package com.delacrixmorgan.squark.services.network

import retrofit2.Response

class NetworkRequestManager {
    suspend inline fun <reified T> apiRequest(crossinline apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Failure(Exception(response.message()))
                }
            } else {
                Result.Failure(Exception(response.message()))
            }
        } catch (exception: Exception) {
            return Result.Failure(exception)
        }
    }
}