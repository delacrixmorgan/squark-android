package com.delacrixmorgan.squark

import android.app.Application
import android.content.Context
import com.delacrixmorgan.squark.di.apiModule
import com.delacrixmorgan.squark.di.networkModule
import com.delacrixmorgan.squark.di.repositoryModule
import com.delacrixmorgan.squark.di.viewModelModule
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        AndroidThreeTen.init(this)

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(viewModelModule, repositoryModule, apiModule, networkModule)
        }
    }
}