package com.delacrixmorgan.squark.di

import android.content.Context
import com.delacrixmorgan.squark.BuildConfig
import com.delacrixmorgan.squark.services.api.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RemoteModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BaseURL

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorOkHttpClient

    @Singleton
    @BaseURL
    @Provides
    fun provideBaseURL(): String = BuildConfig.BASE_URL

    @AuthInterceptorOkHttpClient
    @Provides
    fun provideOkHttpBaseClient(
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val connectTimeout: Long = 60
        val readTimeout: Long = 60

        // Set Logging Interceptors
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        // Set Timeouts
        okHttpClientBuilder
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)

        // Set HeaderInterceptor
        okHttpClientBuilder.addInterceptor(headerInterceptor)

        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideHeaderInterceptor(
        @ApplicationContext appContext: Context
    ): HeaderInterceptor = HeaderInterceptor(appContext)

    @Singleton
    @Provides
    fun provideRetrofit(
        @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient,
        @BaseURL baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}