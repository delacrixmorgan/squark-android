package com.delacrixmorgan.squark.di

import android.content.Context
import com.delacrixmorgan.squark.BuildConfig
import com.delacrixmorgan.squark.service.api.HeaderUnsplashInterceptor
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
internal class RemoteUnsplashModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RetrofitUnsplashClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BaseUnsplashURL

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OkHttpUnsplashClient

    @Singleton
    @BaseUnsplashURL
    @Provides
    fun provideBaseUnsplashURL(): String = BuildConfig.BASE_UNSPLASH_URL

    @Singleton
    @Provides
    fun provideHeaderUnsplashInterceptor(
        @ApplicationContext appContext: Context
    ): HeaderUnsplashInterceptor = HeaderUnsplashInterceptor(appContext)

    @OkHttpUnsplashClient
    @Provides
    fun provideOkHttpUnsplashClient(
        interceptor: HeaderUnsplashInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val connectTimeout: Long = 60
        val readTimeout: Long = 60

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        okHttpClientBuilder
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)

        okHttpClientBuilder.addInterceptor(interceptor)
        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    @RetrofitUnsplashClient
    fun provideUnsplashRetrofit(
        @OkHttpUnsplashClient okHttpClient: OkHttpClient,
        @BaseUnsplashURL baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}