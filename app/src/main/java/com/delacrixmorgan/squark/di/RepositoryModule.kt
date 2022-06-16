package com.delacrixmorgan.squark.di

import com.delacrixmorgan.squark.data.dao.CurrencyDao
import com.delacrixmorgan.squark.services.api.CurrencyApi
import com.delacrixmorgan.squark.services.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//class RepositoryModule {
//    @Singleton
//    @Provides
//    fun provideCurrencyRepository(
//        currencyApi: CurrencyApi,
//        currencyDao: CurrencyDao
//    ) = CurrencyRepository(currencyApi, currencyDao)
//
//    @Singleton
//    @Provides
//    fun provide
//}