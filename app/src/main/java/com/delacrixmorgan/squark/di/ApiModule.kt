package com.delacrixmorgan.squark.di

import com.delacrixmorgan.squark.service.api.CurrencyApi
import com.delacrixmorgan.squark.service.api.UnsplashApi
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
        @RemoteModule.RetrofitClient
        retrofit: Retrofit
    ): CurrencyApi = retrofit.create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideUnsplashApi(
        @RemoteUnsplashModule.RetrofitUnsplashClient
        retrofit: Retrofit
    ): UnsplashApi = retrofit.create(UnsplashApi::class.java)
}