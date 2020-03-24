package com.delacrixmorgan.squark.data.api

sealed class SquarkResult<out T> {
    data class Success<out T>(val value: T) : SquarkResult<T>()
    data class Failure(val error: Throwable) : SquarkResult<Nothing>()
}