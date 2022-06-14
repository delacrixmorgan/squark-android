package com.delacrixmorgan.squark.di

import com.delacrixmorgan.squark.services.api.CurrencyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Singleton
    @Provides
    fun provideCurrencyApi(
        retrofit: Retrofit
    ): CurrencyApi = retrofit.create(CurrencyApi::class.java)
}