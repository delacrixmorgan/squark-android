package com.delacrixmorgan.squark.di

import com.delacrixmorgan.squark.services.repository.CurrencyRepository
import com.delacrixmorgan.squark.ui.LaunchViewModel
import com.delacrixmorgan.squark.ui.currency.CurrencyViewModel
import com.delacrixmorgan.squark.ui.preference.country.CountryViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//internal class ViewModelModule {
//    @Singleton
//    @Provides
//    fun provideLaunchViewModel(
//        currencyRepository: CurrencyRepository
//    ) = LaunchViewModel(currencyRepository)
//
//    @Singleton
//    @Provides
//    fun provideCurrencyViewModel(
//    ) = CurrencyViewModel()
//
//    @Singleton
//    @Provides
//    fun provideCountryViewModel(
//        currencyRepository: CurrencyRepository
//    ) = CountryViewModel(currencyRepository)
//}