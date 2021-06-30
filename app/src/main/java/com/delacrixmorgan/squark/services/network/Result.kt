package com.delacrixmorgan.squark.services.network

sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class Failure(val error: Throwable) : Result<Nothing>()
}