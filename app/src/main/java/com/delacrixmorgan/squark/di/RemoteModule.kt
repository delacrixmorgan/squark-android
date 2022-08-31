package com.delacrixmorgan.squark.di

import android.content.Context
import com.delacrixmorgan.squark.BuildConfig
import com.delacrixmorgan.squark.service.api.HeaderCurrencyLayerInterceptor
import com.delacrixmorgan.squark.service.remoteconfig.FirebaseRemoteConfigManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
    @Retention(AnnotationRetention.BINARY)
    annotation class RetrofitClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BaseURL

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OkHttpBaseClient

    @Singleton
    @BaseURL
    @Provides
    fun provideBaseURL(): String = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun provideHeaderCurrencyLayerInterceptor(
        @ApplicationContext appContext: Context
    ): HeaderCurrencyLayerInterceptor = HeaderCurrencyLayerInterceptor(appContext)

    @OkHttpBaseClient
    @Provides
    fun provideOkHttpCurrencyLayerClient(
        interceptor: HeaderCurrencyLayerInterceptor
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
    @RetrofitClient
    fun provideRetrofit(
        @OkHttpBaseClient okHttpClient: OkHttpClient,
        @BaseURL baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfigManager(): FirebaseRemoteConfigManager {
        return FirebaseRemoteConfigManager()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }
}