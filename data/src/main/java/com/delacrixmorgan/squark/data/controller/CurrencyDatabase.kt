package com.delacrixmorgan.squark.data.controller

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.delacrixmorgan.squark.data.model.Currency

/**
 * CurrencyDatabase
 * squark-android
 *
 * Created by Delacrix Morgan on 23/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

@Database(entities = arrayOf(Currency::class), version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDataDao(): CurrencyDataDao

    companion object {
        private var currencyDatabaseInstance: CurrencyDatabase? = null

        fun getInstance(context: Context): CurrencyDatabase? {
            if (this.currencyDatabaseInstance == null) {
                synchronized(CurrencyDatabase::class) {
                    this.currencyDatabaseInstance = Room.databaseBuilder(context.applicationContext,
                            CurrencyDatabase::class.java,
                            "currency.db")
                            .build()
                }
            }
            return this.currencyDatabaseInstance
        }

        fun destroyInstance() {
            this.currencyDatabaseInstance = null
        }
    }
}