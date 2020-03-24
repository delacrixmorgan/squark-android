package com.delacrixmorgan.squark

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen

class App : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        AndroidThreeTen.init(this)
    }
}