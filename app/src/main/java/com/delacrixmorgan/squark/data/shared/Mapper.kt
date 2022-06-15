package com.delacrixmorgan.squark.data.shared

interface Mapper<in Input, out Output> {
    suspend operator fun invoke(input: Input): Output
}