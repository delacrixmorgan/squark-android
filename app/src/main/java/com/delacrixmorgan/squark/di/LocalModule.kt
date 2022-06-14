package com.delacrixmorgan.squark.di

import android.content.Context
import com.delacrixmorgan.squark.data.dao.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class LocalModule {
    @Qualifier
    annotation class DatabaseName

    @Singleton
    @Provides
    @DatabaseName
    fun provideDatabaseName() = "squark"

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context,
        @DatabaseName databaseName: String
    ): AppDatabase = AppDatabase.getDatabase(applicationContext, databaseName)

    @Singleton
    @Provides
    fun provideCountryDao(appDatabase: AppDatabase) = appDatabase.countryDataDao()
}