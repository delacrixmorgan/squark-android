package com.delacrixmorgan.squark.service.network

import retrofit2.Response

suspend inline fun <reified T> apiRequest(crossinline apiCall: suspend () -> Response<T>): Result<T, Exception> {
    return try {
        val response = apiCall.invoke()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.Failure(Exception(response.message()))
            }
        } else {
            Result.Failure(Exception(response.message()))
        }
    } catch (exception: Exception) {
        return Result.error(exception)
    }
}
