package com.delacrixmorgan.squark.di

import com.delacrixmorgan.squark.BuildConfig
import com.delacrixmorgan.squark.services.api.CurrencyApi
import com.delacrixmorgan.squark.services.repository.CountryRepository
import com.delacrixmorgan.squark.services.repository.CurrencyRepository
import com.delacrixmorgan.squark.services.network.NetworkRequestManager
import com.delacrixmorgan.squark.ui.LaunchViewModel
import com.delacrixmorgan.squark.ui.preference.country.CountryViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val viewModelModule = module {
    viewModel { LaunchViewModel(get()) }
    viewModel { CountryViewModel(get()) }
}

val repositoryModule = module {
    single { CurrencyRepository(get(), get()) }
    single { CountryRepository() }
}

val apiModule = module {
    fun provideCurrencyApi(retrofit: Retrofit) = retrofit.create(CurrencyApi::class.java)
    single { provideCurrencyApi(get()) }
}

val networkModule = module {
    fun provideHttpClient(): OkHttpClient {
        val connectTimeout: Long = 60
        val readTimeout: Long = 60

        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun provideRetrofitResponseParser(): NetworkRequestManager {
        return NetworkRequestManager()
    }

    single {
        val baseUrl = BuildConfig.BASE_URL
        provideRetrofit(provideHttpClient(), baseUrl)
    }

    single { provideRetrofitResponseParser() }
}